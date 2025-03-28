package student;

import Connecting.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.CourierOperations;


public class om200335_Courier implements CourierOperations {
    
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public boolean insertCourier(String korisnickoIme, String registracija) {
        try(PreparedStatement ps=connection.prepareStatement("insert into KURIR(KorisnickoIme,Registracija,BrojPaketa,Profit,Status) values (?,?,0,0,0)");){
            ps.setString(1, korisnickoIme);
            ps.setString(2, registracija);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){return false;}
    }

    @Override
    public boolean deleteCourier(String korisnickoIme) {
        try(PreparedStatement ps=connection.prepareStatement("delete from KURIR where KorisnickoIme=?");){
            ps.setString(1, korisnickoIme);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){return false;}
    }

    @Override
    public List<String> getCouriersWithStatus(int arg0) {
        List<String> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select KorisnickoIme from KURIR where Status=?");){
            ps.setInt(1,arg0);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    res.add(rs.getString(1));
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
    public List<String> getAllCouriers() {
         List<String> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select KorisnickoIme from KURIR");){
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    res.add(rs.getString(1));
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
    public BigDecimal getAverageCourierProfit(int arg0) {
       try(PreparedStatement ps=connection.prepareStatement("select AVG(Profit) from KURIR where BrojPaketa>=?");){
           ps.setInt(1,arg0); 
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
    
}
