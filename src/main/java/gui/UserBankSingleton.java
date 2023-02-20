package gui;
import bank.PrivateBank;

/**
 * Global Object for accessing Bank and User
 *
 */
public class UserBankSingleton {
	
    private PrivateBank pb;
    private String username = "";
    private static final UserBankSingleton instance = new UserBankSingleton();

    private UserBankSingleton() {}

    public static UserBankSingleton getInstance() {return instance;}
    
    public String getUser() {return this.username;}
    public PrivateBank getBank() {return this.pb;}
    public void setUser(String user) {this.username = user;}
    public void setBank(PrivateBank pb) {this.pb = pb;}
}
