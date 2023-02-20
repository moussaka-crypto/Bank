package bank.exceptions;

/**
 * Exception class for when transaction does not exist
 */
public class TransactionDoesNotExistException extends Exception{
    /**
     * class Constructor
     * @param message Error message which is different according to use case
     */
    public TransactionDoesNotExistException(final String message) {super(message);}
}
