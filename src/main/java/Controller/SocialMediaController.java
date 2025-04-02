package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postUserRegistration);
        app.post("/login", this::postUserLogin);
        app.post("/messages", this::postNewMessage);
        app.get("/messages", this::getListAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteDeleteMessage);
        app.patch("/messages/{message_id}", this::patchUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    /**
     * 
     * @param ctx The body of the account to be registered.
     * @throws JsonProcessingException
     */
    private void postUserRegistration(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        account = accountService.addAccount(account);
        if(account != null){
            ctx.json(mapper.writeValueAsString(account));
        }
        else{
            ctx.status(400);
        }
    }
    /**
     * 
     * @param ctx The body of the account to be logged in.
     * @throws JsonProcessingException
     */
    private void postUserLogin(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        account = accountService.login(account);
        if(account != null){
            ctx.json(mapper.writeValueAsString(account));
        }
        else{
            ctx.status(401);
        }
    }
    /**
     * 
     * @param ctx The body of the new message.
     * @throws JsonProcessingException
     */
    private void postNewMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        message = messageService.insertMessage(message);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
        else{
            ctx.status(400);
        }
    }
    /**
     * 
     * @param ctx
     * @throws JsonProcessingException
     */
    private void getListAllMessages(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List <Message> Messages = new ArrayList<>();
        Messages = messageService.messageList();
        ctx.json(mapper.writeValueAsString(Messages));
    }
    /**
     * 
     * @param ctx The target message id.
     * @throws JsonProcessingException
     */
    private void getMessageByID(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.messageById(message_id);
        if(message != null)
            ctx.json(mapper.writeValueAsString(message));
        else
            ctx.json("");
    }
    /**
     * 
     * @param ctx The id of the message to be deleted.
     * @throws JsonProcessingException
     */
    private void deleteDeleteMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        //int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.removeMessage(message_id);
        if(message != null)
            ctx.json(mapper.writeValueAsString(message));
        else
            ctx.json("");
    }
    /**
     * 
     * @param ctx The contents of the updated message.
     * @throws JsonProcessingException
     */
    private void patchUpdateMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        //int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        //String message_text = ctx.body();
        Message message = mapper.readValue(ctx.body(), Message.class);
        message.setMessage_id(message_id);
        //message.setMessage_text(message_text);
        message = messageService.changeMessage(message);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
        else{
            ctx.status(400);
        }
    }
    private void getMessagesByUser(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> Messages = new ArrayList<>();
        Integer account_id = Integer.parseInt(ctx.pathParam("account_id"));
        // int user_id = Integer.valueOf(ctx.pathParam("account_id"));
        Messages = messageService.messagesByUser(account_id);
        ctx.json(mapper.writeValueAsString(Messages));
    }
}