package student;

import Connecting.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;


public class om200335_Vehicle implements VehicleOperations{
    
    private Connection connection=DB.getInstance().getConnection();

    @Override
    public boolean insertVehicle(String arg0, int arg1, BigDecimal arg2) {
        try (PreparedStatement ps=connection.prepareStatement("insert into VOZILO(Registracija,TipGoriva,Potrosnja) values(?,?,?)");){
            ps.setString(1, arg0);
            ps.setInt(2, arg1);
            ps.setBigDecimal(3, arg2);
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
    public int deleteVehicles(String... arg0) {
        int res=0;
        
        for(int i=0;i<arg0.length;i++){
            try (PreparedStatement ps=connection.prepareStatement("delete from VOZILO where Registracija=?");){
                ps.setString(1,arg0[i]);
                res+=ps.executeUpdate();
            } catch (SQLException ex) {
                return -1;
            }
        }
        
        return res;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> res=new ArrayList<>();
        
        try(PreparedStatement ps=connection.prepareStatement("select Registracija from VOZILO");){
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
    public boolean changeFuelType(String arg0, int arg1) {
        try(PreparedStatement ps=connection.prepareStatement("update VOZILO set TipGoriva=? where Registracija=?");){
            ps.setString(2, arg0);
            ps.setInt(1,arg1);
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
    public boolean changeConsumption(String arg0, BigDecimal arg1) {
        try(PreparedStatement ps=connection.prepareStatement("update VOZILO set Potrosnja=? where Registracija=?");){
            ps.setString(2, arg0);
            ps.setBigDecimal(1,arg1);
            if(ps.executeUpdate()>0){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            return false;
        }
    }
    
}
