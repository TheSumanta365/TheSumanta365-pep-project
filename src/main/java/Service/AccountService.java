package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    AccountDao accdao;
    // no arg cons
    public AccountService()
    {
        accdao=new AccountDao();
    }
    //arg cons
    public AccountService(AccountDao accdao)
    {
      this.accdao=accdao;
    }
    
         // create new account
     public Account addAccount(Account acc)
    {
         return accdao.createAccount(acc);
    }

    //verify login
    public Account verifyLogin(Account account)
    {
      return accdao.verifyAccount(account);
    }

    //get account by user name for verifying
    public Account getUserByUserName(Account account)
    {
      return accdao.getAccountByUsername(account.getUsername());
    }
     //get account by user id for verifying
    public Account getUserByUserId(Account account)
    {
      return accdao.getAccountByUserId(account.getAccount_id());
    }


}