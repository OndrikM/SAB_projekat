/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import Connecting.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;
import java.sql.Connection;
import java.util.ArrayList;
/**
 *
 * @author f1
 */
public class om200335_District implements DistrictOperations {
    
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public int insertDistrict(String arg0, int arg1, int arg2, int arg3) {
        try (PreparedStatement ps=connection.prepareStatement("insert into OPSTINA(Naziv,IdG,X,Y) values(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, arg0);
            ps.setInt(2, arg1);
            ps.setInt(3, arg2);
            ps.setInt(4, arg3);
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
    public int deleteDistricts(String... arg0) {
        int res=0;
        
        for(int i=0;i<arg0.length;i++){
            try (PreparedStatement ps=connection.prepareStatement("delete from OPSTINA where Naziv=?");){
                ps.setString(1,arg0[i]);
                res+=ps.executeUpdate();
            } catch (SQLException ex) {
                return -1;
            }
        }
        
        return res;
    }

    @Override
    public boolean deleteDistrict(int arg0) {
        try (PreparedStatement ps=connection.prepareStatement("delete from OPSTINA where IdO=?");){
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
    public int deleteAllDistrictsFromCity(String arg0) {
        int IdG;
        try(PreparedStatement ps=connection.prepareStatement("Select IdG from GRAD where Naziv=?");){
            ps.setString(1,arg0);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    IdG=rs.getInt(1);
                }else{
                    return -1;
                }
            }catch(SQLException e){
            return -1;
            }
        }catch(SQLException e){
            return -1;
        }
          
        try (PreparedStatement ps=connection.prepareStatement("delete from OPSTINA where IdG=?");){
            ps.setInt(1,IdG);
            return ps.executeUpdate();
               
        } catch (SQLException ex) {
            return -1;
        }
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int arg0) {
        List<Integer> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select IdO from OPSTINA where IdG=?");){
            ps.setInt(1, arg0);
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
    public List<Integer> getAllDistricts() {
        List<Integer> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select IdO from OPSTINA");){
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
