package student;

import Connecting.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.CourierRequestOperation;


public class om200335_CourierRequest implements CourierRequestOperation {

    private Connection connection=DB.getInstance().getConnection();
    
    @Override
    public boolean insertCourierRequest(String korisnickoIme, String registracija) {
        try (PreparedStatement ps=connection.prepareStatement("insert into ZAHTEV_KURIR(KorisnickoIme,Registracija) values(?,?)");){
            ps.setString(1, korisnickoIme);
            ps.setString(2, registracija);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        } catch (SQLException ex) {
            return false;
        } 
    }

    @Override
    public boolean deleteCourierRequest(String korisnickoIme) {
        try (PreparedStatement ps=connection.prepareStatement("delete from ZAHTEV_KURIR where KorisnickoIme=?");){
                ps.setString(1,korisnickoIme);
                if(ps.executeUpdate()>0){
                    return true;
                }else{
                    return false;
                }
            } catch (SQLException ex) {
                return false;
            }
    }

    @Override
    public boolean changeVehicleInCourierRequest(String korisnickoIme, String registracija) {
        try(PreparedStatement ps=connection.prepareStatement("update ZAHTEV_KURIR Registracija=? where KorisnickoIme=?");){
            ps.setString(1, registracija);
            ps.setString(2,korisnickoIme);
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
    public List<String> getAllCourierRequests() {
        List<String> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select KorisnickoIme from ZAHTEV_KURIR");){
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
    public boolean grantRequest(String korisnickoIme) {//ovo treba kroz proceduru
        String query = "{ call spGrantRequest (?) }";
        try(CallableStatement cs = connection.prepareCall(query)) {
            cs.setString(1, korisnickoIme);
            cs.execute();
            return true;
        }catch (SQLException ex) {
           return false;
        }
         
        /*
        String registracija;
        
        try(PreparedStatement ps=connection.prepareStatement("select Registracija from ZAHTEV_KURIR where KorisnickoIme=?");){
            ps.setString(1,korisnickoIme);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    registracija=rs.getString(1);
                }else{
                    return false;
                }
            }catch(SQLException e){return false;}
        }catch(SQLException e){return false;}
        
        try(PreparedStatement ps=connection.prepareStatement("delete from ZAHTEV_KURIR where KorisnickoIme=?");){
            ps.setString(1,korisnickoIme);
            if(ps.executeUpdate()==0){
                return false;
            }
        }catch(SQLException e){return false;}
        
        try(PreparedStatement ps=connection.prepareStatement("insert into KURIR(KorisnickoIme,Registracija,BrojPaketa,Profit,Status) values (?,?,0,0,0)");){
            ps.setString(1, korisnickoIme);
            ps.setString(2, registracija);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){return false;}
        */
    }
    
}
