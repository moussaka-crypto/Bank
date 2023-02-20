package bank.exceptions;

/**
 * Exception class for when account already exists
 *
 */
public class AccountAlreadyExistsException extends Exception{

    /**
     * Constructor for the Exception class
     * @param message Error message which is different according to use case
     */
    public AccountAlreadyExistsException(final String message)
    {
        super(message);
    }
}
