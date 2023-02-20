package bank.exceptions;

/**
 * Exception class used for attribute check
 */
public class TransactionAttributeException extends Exception{
    /**
     * class Constructor
     * @param message Error message which is different according to use case
     */
    public TransactionAttributeException(final String message) {super(message);}
}
