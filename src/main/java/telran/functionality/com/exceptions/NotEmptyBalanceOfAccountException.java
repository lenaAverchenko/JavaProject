package telran.functionality.com.exceptions;

/**
 * Class of Exception, extending RuntimeException - to be thrown, when user is trying to make fatal changes (like deleting)
 * while there is not empty balance of an account
 *
 * @author Olena Averchenko
 */
public class NotEmptyBalanceOfAccountException extends RuntimeException {

    public NotEmptyBalanceOfAccountException(String message) {
        super(message);
    }
}
