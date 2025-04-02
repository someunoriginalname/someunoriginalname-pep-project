package DAO;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;


public class AccountDAO {
    public Account loginAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Boolean accountExists(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT account_id FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                return true;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Account insertAccount(Account account){
        if(account.getPassword().length() < 4 || account.getUsername().length() <= 0)
            return null;
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet pKeyResultSet = ps.getGeneratedKeys();
            if(pKeyResultSet.next()){
                int id = (int) pKeyResultSet.getLong(1);
                return new Account(id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
