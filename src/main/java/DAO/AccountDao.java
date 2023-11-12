package DAO;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {
    //it's for creating a new user
    public Account createAccount(Account account){
        Connection con=ConnectionUtil.getConnection();
        try{
            String sql="INSERT INTO account (username,password) VALUES (?,?)";
            PreparedStatement ps=con.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,account.getUsername());
            ps.setString(2,account.getPassword());
            ps.executeUpdate();
            ResultSet keyrs=ps.getGeneratedKeys();
                  if(keyrs.next()){
                int generated_account_key=(int) keyrs.getInt("account_id");
                return new Account(generated_account_key,account.getUsername(),account.getPassword());
                 }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    } 


//verify user login
    public Account verifyAccount(Account account){
        Connection con=ConnectionUtil.getConnection();
        try {
            String sql="select * from account where username=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,account.getUsername());
            ResultSet  rs=ps.executeQuery();
            while(rs.next())
            {
                    Account acc=new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
                    return acc;
           }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
          return null;
}
//retrieve account by user name
public Account getAccountByUsername(String name){
    Connection con=ConnectionUtil.getConnection();
    try {
        String sql="select * from account where username=(?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,name);
        ResultSet rs=ps.executeQuery();
        while(rs.next())
        {
           Account account=new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
            return account;
        }
        
    } catch (Exception e) {
        System.out.println(e.getMessage()); 
   }
    return null;
}


//retrieve account by id
public Account getAccountByUserId(int id){
    Connection con=ConnectionUtil.getConnection();
    try {
        String sql="select * from account where username=(?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs=ps.executeQuery();
        while(rs.next())
        {
           Account account=new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
            return account;
        }
        
    } catch (Exception e) {
        System.out.println(e.getMessage()); 
   }
    return null;
}


}