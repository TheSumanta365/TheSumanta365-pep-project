package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.MessageDao;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
  public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register",this::newUserHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/messages",this::newMessageHandler);
        app.get("/messages",this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getMessagesByMessageIdHandler);
        app.delete("/messages/{message_id}",this::deleteBymessageId);
        app.patch("/messages/{message_id}",this::updateByMessageId);
        app.get("/accounts/{account_id}/messages",this::getAllMessagesByUserIdHandler);
        return app;
    }
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService=new AccountService();
        this.messageService=new MessageService();
       }
   /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
      private void newUserHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
      ObjectMapper om=new ObjectMapper();
      Account acc=om.readValue(ctx.body(),Account.class);
      System.out.println(acc); 
      if(acc.getUsername().isEmpty()){
           ctx.status(400);
           return;
         }
      else if(acc.getPassword().length() <4){
           ctx.status(400);
           return;
         }else if (accountService.getUserByUserName(acc)!=null)
         {
           ctx.status(400);
           return;
         }
   Account addedUser=accountService.addAccount(acc);
        ctx.json(om.writeValueAsString(addedUser));
        ctx.status(200); //OK
        System.out.println((addedUser));
         }

        
    private void userLoginHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
      ObjectMapper om=new ObjectMapper();
      Account acc=om.readValue(ctx.body(),Account.class);
      Account verifyUser=accountService.getUserByUserName(acc);
      if(verifyUser==null)
      {
        ctx.status(401);
        return;
      }
      else if(!verifyUser.getPassword().contains(acc.getPassword())){
        ctx.status(401);
        return;
      }
      else{
        ctx.status(200);
        ctx.json(om.writeValueAsString(verifyUser));
        return;
      }
   }
     private void newMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
         ObjectMapper om=new ObjectMapper();
      Message msg=om.readValue(ctx.body(),Message.class);
      MessageDao md=new MessageDao();
      if(md.isExist(msg.posted_by) && !msg.message_text.isBlank() && msg.message_text.length()< 255)
      {
             Message addedMsg=messageService.addMessage(msg);
             ctx.json(addedMsg);
             ctx.status(200);
             }
           else{
                ctx.status(400);
                 return;
               }
    }

      private void getAllMessagesHandler(Context ctx){
        List<Message> msg=new ArrayList<Message>();
        msg.addAll(messageService.getAllmessages());
          ctx.json(msg);
         ctx.status(200);
         System.out.println(msg);
    }

 private void getMessagesByMessageIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg2=messageService.getMessageById(msg_id);
        if(msg2==null)
        {
                  ctx.status(200);//OK
                  return;
        }else
        ctx.json(msg2);
        ctx.status(200);//OK
       
  }
      // private void deleteByMessageId(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException,MismatchedInputException{
      //   // try{ 
      //    ObjectMapper om=new ObjectMapper();
      //     Message msg=om.readValue(ctx.body(),Message.class);
      //     int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
      //   Message toBeDeleted_message=messageService.getMessageById(msg_id);
      //   System.out.println(toBeDeleted_message);
      //   MessageDao md=new MessageDao();
      //   if(md.isMessageExist(msg_id) && toBeDeleted_message!=null)
      //   {
      //         messageService.deleteMessageById(msg);
      //         ctx.json(toBeDeleted_message);
      //         ctx.status(200);
 //         return;
      //   }else{
      //      ctx.status(200); //msg_not exist //ok
      //      return;
      //   }
     
        //  ctx.json(messageService.getMessageById(msg.getMessage_id()));
        // messageService.deleteMessageById(msg);

        //       ctx.status(200);
        //        return;
        // messageService.deleteMessageById(msg);
        // ctx.json(msg.getMessage_id());
        // ctx.status(200); //ok
        // return;


 private void deleteBymessageId(Context ctx) {
          int messageId=Integer.parseInt(ctx.pathParam("message_id"));
          Message to_be_deleted_message=messageService.deleteMessageById(messageId);
          if(to_be_deleted_message !=null)
          {
              ctx.status(200);
              ctx.json(to_be_deleted_message);
             }
         else
         {
  ctx.status(200);
         }
 }

    private void updateByMessageId(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper om=new ObjectMapper();
        Message msg=om.readValue(ctx.body(),Message.class);
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage=messageService.updateMessage(msg_id,msg);
        System.out.println(updatedMessage);
        if(updatedMessage==null)
        {
          ctx.status(400);
        }else if(msg.message_text.length()>255){
          ctx.status(400);
        }else if(msg.message_text.isBlank()){
          ctx.status(400);
        }
        else
        {
          ctx.json(om.writeValueAsString(updatedMessage));
        ctx.status(200);//OK
           }
    }
      private void getAllMessagesByUserIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
         List<Message> messages=new ArrayList<>();
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
         messages.addAll(messageService.getMessagesByUserId(account_id));
         ctx.status(200);
          ctx.json(messages);
         System.out.println(messages);
      }

}
