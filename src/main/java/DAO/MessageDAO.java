package DAO;
import Model.Account;
import DAO.AccountDAO;
import Model.Message;
import Util.ConnectionUtil;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.*;
import java.util.*;

public class MessageDAO {
    public Message newMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            AccountDAO accountDAO = new AccountDAO();
            if(message.getMessage_text().length() <= 0 ||
                message.getMessage_text().length() > 255 ||
                !accountDAO.accountExists(message.getPosted_by()))
                {
                    return null;
                }
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet pKeyResultSet = ps.getGeneratedKeys();
            if(pKeyResultSet.next()){
                int message_id =  (int) pKeyResultSet.getLong(1);
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "Select * from message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs. getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public Message getMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs. getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message deleteMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            Message message = getMessage(message_id);
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ps.execute();
            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message updateMessage(Message message){
        if(message.getMessage_text().length() > 255 || message.getMessage_text().length() <= 0)
            return null;
        Connection connection = ConnectionUtil.getConnection();
        try{
            String newText = message.getMessage_text();
            message = getMessage(message.getMessage_id());
            if(message == null)
                return null;
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newText);
            ps.setInt(2, message.getMessage_id());
            ps.execute();
            message.setMessage_text(newText);
            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> getAllMessagesByUser(int posted_by){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "Select * from message where posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs. getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
