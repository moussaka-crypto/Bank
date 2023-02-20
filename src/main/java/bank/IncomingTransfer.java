package bank;
import bank.exceptions.TransactionAttributeException;

/**
 * class for Incoming transfers
 */
public class IncomingTransfer extends bank.Transfer {
    /**
     * Constructor for an incoming transfer
     * @param date date of transfer
     * @param amount amount of transfer
     * @param description transfer description
     * @throws TransactionAttributeException during attribute check
     */
    public IncomingTransfer(String date, double amount, String description)
            throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     * Constructor for incoming transfer with sender & recipient
     * @param date date of transfer
     * @param amount amount of transfer
     * @param description transfer description
     * @param sender sender details
     * @param recipient recipient details
     * @throws TransactionAttributeException during attribute check
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient)
            throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }

    /**
     * calculates incoming interest
     * @return double value with inc interest
     */
    @Override
    public double calculate() {
        return super.calculate();
    }
}
