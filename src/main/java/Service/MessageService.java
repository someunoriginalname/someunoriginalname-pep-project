package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.*;

public class MessageService {
    private MessageDAO messageDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /** 
     *  Constructor for an account. 
     *  @param messageDAO 
    */
    
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    /**
     * 
     * @param message
     * @return The new message, if it is created.
     */
    public Message insertMessage(Message message){
        return messageDAO.newMessage(message);
    }
    /**
     * 
     * @return A list of all messages.
     */
    public List<Message> messageList(){
        return messageDAO.getAllMessages();
    }
    /**
     * 
     * @param id the id of the target message
     * @return returns the message of that id, if it exists.
     */
    public Message messageById(int id){
        return messageDAO.getMessage(id);
    }
    /**
     * 
     * @param id the id of the message to be deleted
     * @return the message contents, if it is deleted
     */
    public Message removeMessage(int id){
        return messageDAO.deleteMessage(id);
    }
    /**
     * 
     * @param text The new message contents
     * @param id The id of the message to update
     * @return The updated message.
     */
    public Message changeMessage(Message message){
        return messageDAO.updateMessage(message);
    }

    public List<Message> messagesByUser(int id){
        return messageDAO.getAllMessagesByUser(id);
    }
}