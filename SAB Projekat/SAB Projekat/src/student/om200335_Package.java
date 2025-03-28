package student;

import Connecting.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.PackageOperations;

public class om200335_Package implements PackageOperations {
    
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public int insertPackage(int opstinaOd, int opstinaDo, String korisnickoIme, int tip, BigDecimal tezina) { //insert zahtev_prevoz,insert paket
        int IdP=-1;
        
        try (PreparedStatement ps=connection.prepareStatement("insert into PAKET(Status) values(0)",PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                IdP=rs.getInt(1);
            }else{
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        } 
        
        try (PreparedStatement ps=connection.prepareStatement("insert into ZAHTEV_PREVOZ(Posiljalac,OpstinaOd,OpstinaDo,Tezina,Tip,IdP) values(?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, korisnickoIme);
            ps.setInt(2, opstinaOd);
            ps.setInt(3, opstinaDo);
            ps.setBigDecimal(4, tezina);
            ps.setInt(5,tip);
            ps.setInt(6, IdP);
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                return IdP;
            }else{
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        } 
    }

    @Override
    public int insertTransportOffer(String korisnickoIme, int IdP, BigDecimal procenat) { //insert ponuda
        String posiljalac;
        
        try(PreparedStatement ps=connection.prepareStatement("select Posiljalac from ZAHTEV_PREVOZ where IdP=?");){
            ps.setInt(1,IdP);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    posiljalac=rs.getString(1);
                }else{
                    return -1;
                }
            }catch(SQLException e){
                return -1;
            }  
        }catch(SQLException e){
            return -1;
        }  
        
        try (PreparedStatement ps=connection.prepareStatement("insert into PONUDA(Procenat,Kurir,IdP,Posiljalac) values(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setBigDecimal(1, procenat);
            ps.setString(2,korisnickoIme);
            ps.setInt(3, IdP);
            ps.setString(4,posiljalac);
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        } 
    }

    @Override
    public boolean acceptAnOffer(int IdPon) { //delete ponuda,delete zahtev_prevoz, update paket ,update korisnik       
        int IdP;
        BigDecimal procenat;
        String posiljalac;
        String kurir;
        try (PreparedStatement ps=connection.prepareStatement("select IdP,Procenat,Kurir,Posiljalac from PONUDA where IdPon=? ");){
            ps.setInt(1, IdPon);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    IdP=rs.getInt(1);
                    procenat=rs.getBigDecimal(2);
                    kurir=rs.getString(3);
                    posiljalac=rs.getString(4);
                }else{
                    return false;
                }
            }catch(SQLException e){
                return false;
            } 
        } catch (SQLException e) {
            return false;
        } 
        
        //treba triger umesto ovoga
        try(PreparedStatement ps=connection.prepareStatement("delete from PONUDA where IdP=?");){
            ps.setInt(1, IdP);
            ps.executeUpdate();
        }catch(SQLException e){
            return false;
        }
        
        int opstinaOd;
        int opstinaDo;
        int tip;
        BigDecimal tezina;
        try (PreparedStatement ps=connection.prepareStatement("select OpstinaOd,OpstinaDo,Tip,Tezina from ZAHTEV_PREVOZ where IdP=?");){
            ps.setInt(1, IdP);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    opstinaOd=rs.getInt(1);
                    opstinaDo=rs.getInt(2);
                    tip=rs.getInt(3);
                    tezina=rs.getBigDecimal(4);
                }else{
                    return false;
                }
            }catch(SQLException e){
                return false;
            } 
        } catch (SQLException ex) {
            return false;
        } 

        try(PreparedStatement ps=connection.prepareStatement("update KORISNIK set BrojPoslatihPaketa=BrojPoslatihPaketa+1 where KorisnickoIme=?");){
            ps.setString(1, posiljalac);
            ps.executeUpdate();
        }catch(SQLException e){
            return false;
        }
        
        int x1,y1;
        try (PreparedStatement ps=connection.prepareStatement("select X,Y from OPSTINA where IdO=?");){
            ps.setInt(1, opstinaOd);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                   x1=rs.getInt(1);
                   y1=rs.getInt(2);
                }else{
                    return false;
                }
            }catch(SQLException e){
                return false;
            } 
        } catch (SQLException ex) {
            return false;
        } 
        
        int x2,y2;
        try (PreparedStatement ps=connection.prepareStatement("select X,Y from OPSTINA where IdO=?");){
            ps.setInt(1, opstinaDo);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                   x2=rs.getInt(1);
                   y2=rs.getInt(2);
                }else{
                    return false;
                }
            }catch(SQLException e){
                return false;
            } 
        } catch (SQLException ex) {
            return false;
        } 
        
        BigDecimal cena=new BigDecimal(0);
        double rastojanje;
        rastojanje=Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
        procenat = procenat.divide(new BigDecimal(100));
        switch (tip) {
            case 0:
                cena=(new BigDecimal(10.0D * rastojanje)).multiply(procenat.add(new BigDecimal(1)));break;
            case 1:
                cena=(new BigDecimal((25.0D + tezina.doubleValue() * 100.0D) * rastojanje)).multiply(procenat.add(new BigDecimal(1)));break;
            case 2:
                cena=(new BigDecimal((75.0D + tezina.doubleValue() * 300.0D) * rastojanje)).multiply(procenat.add(new BigDecimal(1)));break;
        }
        
        try(PreparedStatement ps=connection.prepareStatement("update PAKET set Cena=?,Kurir=?,DatumPrihvatanja=CURRENT_TIMESTAMP,Status=1 where IdP=?");){
            ps.setBigDecimal(1, cena);
            ps.setString(2,kurir);
            ps.setInt(3,IdP);
            ps.executeUpdate();
        }catch(SQLException e){
            return false;
        }
        
        return true;
        
    }

    @Override
    public List<Integer> getAllOffers() { //select ponuda
        List<Integer> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select IdPon from PONUDA");){
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    res.add(rs.getInt(1));
                }
            }catch(SQLException e){
                return null;
            }
            return res;
        }catch(SQLException e){
            return null;
        }
    }

    @Override//obavezno proveri
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int IdP) { //select ponuda
        List<Pair<Integer,BigDecimal>> res=new ArrayList<>();
        try(PreparedStatement ps=connection.prepareStatement("select IdPon,Procenat from PONUDA where IdP=?");){
            ps.setInt(1, IdP);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    res.add(new om200335_Pair(rs.getInt(1),rs.getBigDecimal(2)));
                }
            }
            return res;
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public boolean deletePackage(int IdP) { //delete paket + mozda delete zahtev_prevoz +dodatne specifikacije ako padne javni
        try(PreparedStatement ps=connection.prepareStatement("delete from PONUDA where IdP=?");){
            ps.setInt(1, IdP);
            ps.executeUpdate();
        }catch(SQLException e){
            return false;
        }
        
        try(PreparedStatement ps=connection.prepareStatement("delete from ZAHTEV_PREVOZ where IdP=?");){
            ps.setInt(1,IdP);
            ps.execute();
        }catch(SQLException e){
            return false;
        }
        
        try(PreparedStatement ps=connection.prepareStatement("delete from PAKET where IdP=?");){
            ps.setInt(1,IdP);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }       
        }catch(SQLException e){
            return false;
        }
    }

    @Override
    public boolean changeWeight(int IdP, BigDecimal tezina) { //update zahtev_prevoz
        try(PreparedStatement ps=connection.prepareStatement("update ZAHTEV_PREVOZ set Tezina=? where IdP=?");){
            ps.setBigDecimal(1, tezina);
            ps.setInt(2,IdP);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            return false;
        }
    }

    @Override
    public boolean changeType(int IdP, int tip) { //update zahtev_prevoz
        try(PreparedStatement ps=connection.prepareStatement("update ZAHTEV_PREVOZ set Tip=? where IdP=?");){
            ps.setInt(1, tip);
            ps.setInt(2,IdP);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            return false;
        }
    }

    @Override
    public Integer getDeliveryStatus(int IdP) { //select paket
        try(PreparedStatement ps=connection.prepareStatement("select Status from PAKET where IdP=?");){
            ps.setInt(1,IdP);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    return rs.getInt(1);
                }else{
                    return -1;
                }
            }catch(SQLException e){
                return -1;
            }
        }catch(SQLException e){
            return -1;
        }
    }

    @Override
    public BigDecimal getPriceOfDelivery(int IdP) { //select paket
        try(PreparedStatement ps=connection.prepareStatement("select Cena from PAKET where IdP=?");){
            ps.setInt(1,IdP);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    return rs.getBigDecimal(1);
                }else{
                    return null;
                }
            }catch(SQLException e){
                return null;
            }
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public Date getAcceptanceTime(int IdP) { //select paket
        try(PreparedStatement ps=connection.prepareStatement("select DatumPrihvatanja from PAKET where IdP=?");){
            ps.setInt(1,IdP);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    return rs.getDate(1);
                }else{
                    return null;
                }
            }catch(SQLException e){
                return null;
            }
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int tip) { //select zahtev_prevoz
        List<Integer> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select IdP from ZAHTEV_PREVOZ where Tip=?");){
            ps.setInt(1,tip);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    res.add(rs.getInt(1));
                }
            }catch(SQLException e){
                return null;
            }
            return res;
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackages() { //select zahtev_prevoz
        List<Integer> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select IdP from PAKET");){
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    res.add(rs.getInt(1));
                }
            }catch(SQLException e){
                return null;
            }
            return res;
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public List<Integer> getDrive(String korisnickoIme) {
        List<Integer>res=new ArrayList<>();
        
        int IdV=-1;
        try(PreparedStatement ps=connection.prepareStatement("select IdV from VOZNJA where Kurir=?");){
            ps.setString(1,korisnickoIme);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    IdV=rs.getInt(1);
                }else{
                    return null;
                }
            }catch(SQLException e){
                return null;
            }
        }catch(SQLException e){
            return null;
        }
        
        try(PreparedStatement ps=connection.prepareStatement("select IdP from PREVOZ where IdV=?");){
            ps.setInt(1,IdV);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    res.add(rs.getInt(1));
                }
            }catch(SQLException e){
                return null;
            }
        }catch(SQLException e){
            return null;
        }
        return res;
    }

    @Override
    public int driveNextPackage(String kurir) {
        int status;
        try(PreparedStatement ps=connection.prepareStatement("select Status from KURIR where korisnickoIme=?");){
            ps.setString(1,kurir);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    status=rs.getInt(1);
                }else{
                    return -1;
                }
            }catch(SQLException e){
                return -1;
            }
        }catch(SQLException e){
            return -1;
        }
          
        int IdV;    
        List<Integer> paketi=new ArrayList<>();
        BigDecimal profit=new BigDecimal(0);
        if(status==0){        
            try(PreparedStatement ps=connection.prepareStatement("select IdP from PAKET where Kurir=? and DatumPrihvatanja<CURRENT_TIMESTAMP and Status=1 order by DatumPrihvatanja asc");){
            ps.setString(1,kurir);
                try(ResultSet rs=ps.executeQuery();){
                    while(rs.next()){
                        paketi.add(rs.getInt(1));
                    }
                }catch(SQLException e){
                    return -2;
                }
            }catch(SQLException e){
                return -2;
            }
            
            if(paketi.isEmpty()){
                return-1;
            }
            
            double rastojanje=0;    
            int opstinaOd,opstinaDo;
            int x1,y1;
            int x2,y2;
            for(int i=0;i<paketi.size();i++){
                try(PreparedStatement ps=connection.prepareStatement("select Cena from PAKET where IdP=?");){
                    ps.setInt(1, paketi.get(i));
                    try(ResultSet rs=ps.executeQuery();){
                        if(rs.next()){
                            profit=profit.add(rs.getBigDecimal(1));
                        }else{return -2;}
                    }catch(SQLException e){return -2;}    
                }catch(SQLException e){return -2;}
                
                
                try(PreparedStatement ps=connection.prepareStatement("select OpstinaOd,OpstinaDo from ZAHTEV_PREVOZ where IdP=?");){
                    ps.setInt(1, paketi.get(i));
                    try(ResultSet rs=ps.executeQuery();){
                        if(rs.next()){
                            opstinaOd=rs.getInt(1);
                            opstinaDo=rs.getInt(2);
                        }else{return -2;}
                    }catch(SQLException e){return -2;}    
                }catch(SQLException e){return -2;}
                
                try(PreparedStatement ps=connection.prepareStatement("select X,Y from OPSTINA where IdO=?");){
                    ps.setInt(1, opstinaOd);
                    try(ResultSet rs=ps.executeQuery();){
                        if(rs.next()){
                            x1=rs.getInt(1);
                            y1=rs.getInt(2);
                        }else{return -2;}
                    }catch(SQLException e){return -2;}    
                }catch(SQLException e){return -2;}
                
                try(PreparedStatement ps=connection.prepareStatement("select X,Y from OPSTINA where IdO=?");){
                    ps.setInt(1, opstinaDo);
                    try(ResultSet rs=ps.executeQuery();){
                        if(rs.next()){
                            x2=rs.getInt(1);
                            y2=rs.getInt(2);
                        }else{return -2;}
                    }catch(SQLException e){return -2;}    
                }catch(SQLException e){return -2;}
                
                rastojanje+=Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
                
                if((i+1)<paketi.size()){
                    try(PreparedStatement ps=connection.prepareStatement("select OpstinaOd from ZAHTEV_PREVOZ where IdP=?");){
                        ps.setInt(1, paketi.get(i+1));
                        try(ResultSet rs=ps.executeQuery();){
                            if(rs.next()){
                                opstinaOd=rs.getInt(1);
                            }else{return -2;}
                        }catch(SQLException e){return -2;}    
                    }catch(SQLException e){return -2;}
                    
                    try(PreparedStatement ps=connection.prepareStatement("select X,Y from OPSTINA where IdO=?");){
                        ps.setInt(1, opstinaOd);
                        try(ResultSet rs=ps.executeQuery();){
                            if(rs.next()){
                                x1=rs.getInt(1);
                                y1=rs.getInt(2);
                            }else{return -2;}
                        }catch(SQLException e){return -2;}    
                    }catch(SQLException e){return -2;}
                    
                  rastojanje+=Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));  
                }     
            }
            
            int tipGoriva=-1;
            BigDecimal potrosnja=new BigDecimal(0);
            
            try(PreparedStatement ps=connection.prepareStatement("select TipGoriva,Potrosnja from VOZILO V join KURIR K on V.Registracija=K.Registracija where K.KorisnickoIme=?");){
                ps.setString(1,kurir);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next()){
                        tipGoriva=rs.getInt(1);
                        potrosnja=rs.getBigDecimal(2);
                    }else{return -2;}
                }catch(SQLException e){return -2;}
            }catch(SQLException e){
                return -2;
            }
            
            switch (tipGoriva) {
                case 0:
                  profit=profit.subtract(potrosnja.multiply(new BigDecimal(rastojanje).multiply(new BigDecimal(15))));break;
                case 1:
                  profit=profit.subtract(potrosnja.multiply(new BigDecimal(rastojanje).multiply(new BigDecimal(32))));break;
                case 2:
                  profit=profit.subtract(potrosnja.multiply(new BigDecimal(rastojanje).multiply(new BigDecimal(36))));break;
            }
            
            
            try(PreparedStatement ps=connection.prepareStatement("insert into VOZNJA(Kurir,Profit) values(?,?)",PreparedStatement.RETURN_GENERATED_KEYS);){
                ps.setString(1,kurir);
                ps.setBigDecimal(2,profit);
                ps.executeUpdate();
                ResultSet rs=ps.getGeneratedKeys();
                if(rs.next()){
                    IdV=rs.getInt(1);
                }else{
                    return -2;
                }
            }catch(SQLException e){
                return -2;
            }
            
            try(PreparedStatement ps=connection.prepareStatement("update Kurir set Status=1,BrojPaketa=BrojPaketa+1 where KorisnickoIme=?");){
                ps.setString(1, kurir);
                ps.executeUpdate();   
            }catch(SQLException e){
                return -2;
            }
            
            for(int i=1;i<paketi.size();i++){
                try(PreparedStatement ps=connection.prepareStatement("insert into PREVOZ(IdV,IdP) values(?,?)");){
                    ps.setInt(1, IdV);
                    ps.setInt(2, paketi.get(i));
                    ps.execute();
                }catch(SQLException e){return -2;}
                
                try(PreparedStatement ps=connection.prepareStatement("update PAKET set Status=2 where IdP=?");){
                    ps.setInt(1, paketi.get(i));
                    ps.execute();
                }catch(SQLException e){return -2;}
            }
            
            try(PreparedStatement ps=connection.prepareStatement("update PAKET set Status=3 where IdP=?");){
                    ps.setInt(1, paketi.get(0));
                    if(ps.executeUpdate()>0){
                        return paketi.get(0);
                    }
            }catch(SQLException e){return -2;}
            
        }else{
            try(PreparedStatement ps=connection.prepareStatement("select IdV,Profit from VOZNJA where Kurir=? ")){
                ps.setString(1,kurir);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next()){
                        IdV=rs.getInt(1);
                        profit=rs.getBigDecimal(2);
                    }else{
                        return -2;
                    }
                }catch(SQLException e){
                    return -2;
                }
            }catch(SQLException e){
                return -2;
            }
            
            try(PreparedStatement ps=connection.prepareStatement("select IdP from PREVOZ where IdV=?");){
                ps.setInt(1,IdV);
                try(ResultSet rs=ps.executeQuery();){
                    while(rs.next()){
                        paketi.add(rs.getInt(1));
                    }
                }catch(SQLException e){
                    return -2;
                }
            }catch(SQLException e){
                return -2;
            }
            
            try(PreparedStatement ps=connection.prepareStatement("delete from PREVOZ where IdP=?");){
                    ps.setInt(1, paketi.get(0));
                    ps.executeUpdate();
            }catch(SQLException e){return -2;}
            
            if(paketi.size()==1){
                try(PreparedStatement ps=connection.prepareStatement("delete from VOZNJA where IdV=?");){
                    ps.setInt(1, IdV);
                    ps.executeUpdate();
                }catch(SQLException e){return -2;}
                
                try(PreparedStatement ps=connection.prepareStatement("update Kurir set Status=0,Profit=Profit+? where KorisnickoIme=?");){
                    ps.setString(2, kurir);
                    ps.setBigDecimal(1,profit);
                    ps.executeUpdate();   
                }catch(SQLException e){
                    return -2;
                }
            }
            
            try(PreparedStatement ps=connection.prepareStatement("update Kurir set BrojPaketa=BrojPaketa+1 where KorisnickoIme=?");){
                    ps.setString(1, kurir);
                    ps.executeUpdate();   
                }catch(SQLException e){
                    return -2;
                }
            
            try(PreparedStatement ps=connection.prepareStatement("update PAKET set Status=3 where IdP=?");){
                    ps.setInt(1, paketi.get(0));
                    if(ps.executeUpdate()>0){
                        return paketi.get(0);
                    }
            }catch(SQLException e){return -2;}
        
        }
        
        return -2;        
    }
    
}
