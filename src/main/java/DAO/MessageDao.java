package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao{
    
    //it's for creating a new message
    public Message createMessage(Message msg){
        Connection con=ConnectionUtil.getConnection();
        try{
            String sql="insert into message(posted_by,message_text,time_posted_epoch) values(?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);  
            ps.setInt(1,msg.getPosted_by());
            ps.setString(2,msg.getMessage_text());
            ps.setLong(3,msg.getTime_posted_epoch());
            int rowsAffected=ps.executeUpdate();
            if(rowsAffected>0){
            ResultSet keyrs=ps.getGeneratedKeys();
            if(keyrs.next()){
                int generated_message_key=(int) keyrs.getInt("message_id");
                return new Message(generated_message_key,msg.getPosted_by(),msg.getMessage_text(),msg.getTime_posted_epoch());
            }
        }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
//for retrieving all messages
    public List<Message> getAllMessage(){
        Connection con=ConnectionUtil.getConnection();
    List<Message> list=new ArrayList<Message>();
    try{
     String sql="select * from message";
     PreparedStatement ps=con.prepareStatement(sql);
     ResultSet rs=ps.executeQuery();
     while(rs.next())
     {
        Message msg=new Message( rs.getInt("message_id"), rs.getInt("posted_by"),
        rs.getString("message_text"),rs.getLong("time_posted_epoch"));
        list.add(msg);
     }
    }catch(SQLException e)
    {
    System.out.println(e.getMessage());
    }
    return list;
}
//retrieve message by id
public Message getMessageById(int id){
    Connection con=ConnectionUtil.getConnection();
    try {
        String sql="select * from message where message_id=(?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet  rs=ps.executeQuery();
        while(rs.next())
        {
         Message msg=new Message( rs.getInt("message_id"), rs.getInt("posted_by"),
        rs.getString("message_text"),rs.getLong("time_posted_epoch"));
        return msg;
        }
        
    } catch (Exception e) {
        System.out.println(e.getMessage()); 
   }
    return null;
}

//delete by msg id

    public Message deleteBymessageId(int messageId){
        try(Connection con = ConnectionUtil.getConnection()){
            Message message = getMessageById(messageId);
            String sql="delete from Message where message_id=?";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,messageId);
            int rowsAffected=ps.executeUpdate();
            if(rowsAffected>0){
                return message;
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
 }


//update by message id
public void updateMessageById(int id,Message msg){
    Connection con=ConnectionUtil.getConnection();
    try {
        String sql="update message SET message_text=? WHERE message_id=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,msg.getMessage_text());
        ps.setInt(2,id);
        ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage()); 
   }
}
 //retrieve all message by a user
 public List<Message> getMessagesByUserId(int id){
   List<Message> messages=new ArrayList<>();
    Connection con=ConnectionUtil.getConnection();
    try {
        String sql="select * from message m inner join account a on m.posted_by=a.account_id where a.account_id=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs=ps.executeQuery();
        while(rs.next())
        {
        Message msg=new Message( rs.getInt("message_id"), rs.getInt("posted_by"),
        rs.getString("message_text"),rs.getLong("time_posted_epoch"));
        messages.add(msg);
        }
        
    } catch (SQLException e) {
        System.out.println(e.getMessage()); 
   }
    return messages;
}
 
//user exist or not
public boolean isExist(int posted_by)
{
    boolean b=false;
      Connection con=ConnectionUtil.getConnection();
    try {
        String sql="select * FROM message WHERE posted_by=(?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,posted_by);
        ResultSet rs=ps.executeQuery();
        while(rs.next())
        {
            b= true;
        }

    } catch (Exception e) {
        System.out.println(e.getMessage()); 
   }
    return b;
}

//message exist or not
public boolean isMessageExist(int message_id)
{
    int rowsAffected=0;
      Connection con=ConnectionUtil.getConnection();
    try {
        String sql="select * FROM message WHERE message_id=(?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,message_id);
        ps.executeQuery();
        rowsAffected=ps.executeUpdate();

    } catch (Exception e) {
        System.out.println(e.getMessage()); 
   }
    return rowsAffected >0;
}
}