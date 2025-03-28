/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import rs.etf.sab.operations.GeneralOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Connecting.*;

/**
 *
 * @author f1
 */
public class om200335_General implements GeneralOperations{

    private Connection connection=DB.getInstance().getConnection();
    
    @Override
    public void eraseAll() {
        try(PreparedStatement ps=connection.prepareStatement("delete from PREVOZ");){//1
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase PREVOZ");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from VOZNJA");){//2
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase VOZNJA");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from PONUDA");){//3
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase PONUDA");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from ZAHTEV_PREVOZ");){//4
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase ZAHTEV_PREVOZ");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from PAKET");){//5
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase PAKET");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from ZAHTEV_KURIR");){//6
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase ZAHTEV_KURIR");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from KURIR");){//7
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase KURIR");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from VOZILO");){//8
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase VOZILO");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from KORISNIK");){//9
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase KORISNIK");
        } 
        
        try(PreparedStatement ps=connection.prepareStatement("delete from OPSTINA");){//10
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase OPSTINA");
        } 
         
        try(PreparedStatement ps=connection.prepareStatement("delete from GRAD");){//11
            ps.execute();
        }catch(SQLException e){
            System.out.print("Greska: General erase GRAD");
        }
   
    }
    
}
