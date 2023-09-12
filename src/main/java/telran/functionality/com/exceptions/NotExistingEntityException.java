package telran.functionality.com.exceptions;

/**
 * Class of Exception, extending RuntimeException - to be thrown, when user is trying to get access to some account,
 * that doesn't exist
 *
 * @author Olena Averchenko
 */
public class NotExistingEntityException extends RuntimeException {
    public NotExistingEntityException(String message) {
        super(message);
    }
}
