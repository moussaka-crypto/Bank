package bank.exceptions;

/**
 * Excepetion class for when account does not exist
 */
public class AccountDoesNotExistException extends Exception{
    /**
     * Constructor der Klasse
     * @param message Error message which is different according to use case
     */
    public AccountDoesNotExistException(final String message)
    {
        super(message);
    }
}
