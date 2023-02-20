package bank.exceptions;

/**
 * Exception class for when Transaction already exists
 */
public class TransactionAlreadyExistException extends Exception{
    /**
     * Constructor for exception class
     * @param message Error message which is different according to use case
     */
    public TransactionAlreadyExistException(final String message) {super(message);}
}
