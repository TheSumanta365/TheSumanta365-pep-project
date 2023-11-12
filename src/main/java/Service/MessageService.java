package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService {
    MessageDao msgdao;
    //no arg constructor
    public MessageService()
    {
        msgdao=new MessageDao();
    }
     //single arg cons
      public MessageService(MessageDao msg)
    {
        this.msgdao=new MessageDao();
    }
     //get all messages
      public List<Message> getAllmessages()
    {
         return msgdao.getAllMessage();
    }
    //create new msg
    public Message addMessage(Message msg){
        return msgdao.createMessage(msg);
    }

    //retrieve message by id
     public Message getMessageById(int id)
     {
        return msgdao.getMessageById(id);
     }

     //delete by message  id
     public Message deleteMessageById(int id)
     {
        return msgdao.deleteBymessageId(id);
     }

     //update by msg  id
     public Message updateMessage(int id,Message msg)
     {
        if(msgdao.getMessageById(id) != null){
            msgdao.updateMessageById(id, msg);
            return msgdao.getMessageById(id);
        }
        else return null;
        
     }

     //retrieve all message by a user
 public List<Message> getMessagesByUserId(int id){
    return msgdao.getMessagesByUserId(id);
 }


}