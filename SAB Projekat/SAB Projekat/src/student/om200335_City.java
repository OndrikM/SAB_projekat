package student;

import java.sql.Connection;
import Connecting.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.CityOperations;

public class om200335_City implements CityOperations {

    private Connection connection=DB.getInstance().getConnection();
    
    @Override
    public int insertCity(String arg0, String arg1) {
        try (PreparedStatement ps=connection.prepareStatement("insert into GRAD(Naziv,PostanskiBroj) values(?,?)",PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, arg0);
            ps.setString(2, arg1);
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
    public int deleteCity(String... arg0) {
        int res=0;
        
        for(int i=0;i<arg0.length;i++){
            try (PreparedStatement ps=connection.prepareStatement("delete from GRAD where Naziv=?");){
                ps.setString(1,arg0[i]);
                res+=ps.executeUpdate();
            } catch (SQLException ex) {
                return -1;
            }
        }
        
        return res;
    }

    @Override
    public boolean deleteCity(int arg0) {    
        try (PreparedStatement ps=connection.prepareStatement("delete from GRAD where IdG=?");){
                ps.setInt(1,arg0);
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
    public List<Integer> getAllCities() {
        List<Integer> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select IdG from GRAD");){
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
    
    
    
    
    
}
