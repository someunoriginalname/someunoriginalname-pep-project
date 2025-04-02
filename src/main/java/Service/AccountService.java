package Service;

import Model.Account;
import DAO.AccountDAO;;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /** 
     *  Constructor for an account. 
     *  @param accountDAO 
    */
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    /**
     * 
     * @param account an account object
     * @return The persisted account if persistence is successful.
     */
    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }
    /**
     * 
     * @param account an account object
     * @return The account if login is successful.
     */
    public Account login(Account account){
        return accountDAO.loginAccount(account);
    }
}
