import bank.*;

/**
 * Main class serves as test class for the PrivateBank
 *
 * @author moussaka-crypto: Hristomir Dimov
 * Date: 16.11.22
 */
public class Main {
    public static void main(String[] args) throws Exception{

        // Tests for deleteAccount(deleteFile) & getAllAccounts - später in PrivateBankTest schreiben
        Transfer transfer = new Transfer("28.11.2022", 70, "wallet", "sender", "emp");
        Payment payment = new Payment("28.11.2022", 60, "wallet");
        IncomingTransfer incomingTransfer = new IncomingTransfer("28.11.2022", 50, "wallet", "sender", "emp");
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer("28.11.2022", 40, "wallet");

        PrivateBank pb1 = new PrivateBank("bank1", 0.2, 0.2);
        pb1.setName("bank2");

        pb1.createAccount("acc1");
        pb1.addTransaction("acc1", transfer);
        pb1.addTransaction("acc1", payment);
        pb1.addTransaction("acc1", incomingTransfer);
        pb1.addTransaction("acc1", outgoingTransfer);

        pb1.createAccount("acc2");
        pb1.addTransaction("acc2", transfer);
        pb1.addTransaction("acc2", payment);
        pb1.addTransaction("acc2", incomingTransfer);
        pb1.addTransaction("acc2", outgoingTransfer);

        System.out.println(pb1.getAllAccounts());
        pb1.deleteAccount("acc2");
        System.out.println(pb1.getAllAccounts());
        pb1.deleteAccount("acc1");
        System.out.println(pb1.getAllAccounts());
    }
}