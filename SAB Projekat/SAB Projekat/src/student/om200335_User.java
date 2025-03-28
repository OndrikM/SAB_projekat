package student;

import Connecting.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import rs.etf.sab.operations.UserOperations;


public class om200335_User implements UserOperations {
    
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public boolean insertUser(String arg0, String arg1, String arg2, String arg3) {
        try (PreparedStatement ps=connection.prepareStatement("insert into KORISNIK(KorisnickoIme,Ime,Prezime,Sifra,BrojPoslatihPaketa,Admin) values(?,?,?,?,?,?)");){
            if(!Pattern.compile("^[A-Z][a-zA-Z]*$").matcher(arg1).matches()){
                return false;
            }
            if(!Pattern.compile("^[A-Z][a-zA-Z]*$").matcher(arg2).matches()){
                return false;
            }
            if(!Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$").matcher(arg3).matches()){
                return false;
            }
            ps.setString(1, arg0);
            ps.setString(2, arg1);
            ps.setString(3, arg2);
            ps.setString(4, arg3);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
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
    public int declareAdmin(String arg0) {
        try(PreparedStatement ps=connection.prepareStatement("select Admin from KORISNIK where KorisnickoIme=?");){
            ps.setString(1,arg0);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    if(rs.getInt(1)==1){
                        return 1;
                    }else{
                        try(PreparedStatement ps2=connection.prepareStatement("update KORISNIK set Admin=1 where KorisnickoIme=?");){
                            ps2.setString(1, arg0);
                            if(ps2.executeUpdate()==1){
                                return 0;
                            }else{
                                return 2;
                            }
                        }catch(SQLException e){
                            return 2;
                        }
                    } 
                }else{
                    return 2;
                }
            }catch(SQLException e){
                return 2;
            }
        }catch(SQLException e){
            return 2;
        }
    }

    @Override
    public Integer getSentPackages(String... arg0) {
        int res=0;
        
        for(int i=0;i<arg0.length;i++){
            try (PreparedStatement ps=connection.prepareStatement("select BrojPoslatihPaketa from KORISNIK where KorisnickoIme=?");){
                ps.setString(1,arg0[i]);
                try(ResultSet rs=ps.executeQuery();){
                    if(rs.next()){
                        res+=rs.getInt(1);
                    }else if(arg0.length==1){
                        return null;
                    }
                }catch(SQLException e){
                    return -1;
                }
            } catch (SQLException ex) {
                return -1;
            }
        }
        
        return res;
    }

    @Override
    public int deleteUsers(String... arg0) {
        int res=0;
        
        for(int i=0;i<arg0.length;i++){
            try (PreparedStatement ps=connection.prepareStatement("delete from KORISNIK where KorisnickoIme=?");){
                ps.setString(1,arg0[i]);
                res+=ps.executeUpdate();
            } catch (SQLException ex) {
                return -1;
            }
        }
        
        return res;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select KorisnickoIme from KORISNIK");){
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
    
}
