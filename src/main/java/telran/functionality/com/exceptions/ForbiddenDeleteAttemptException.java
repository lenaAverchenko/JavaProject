package telran.functionality.com.exceptions;

/**
 * Class of Exception, extending RuntimeException - to be thrown, when the delete method can't be fulfilled, because
 * this entity must be impossible to delete
 *
 * @author Olena Averchenko
 */
public class ForbiddenDeleteAttemptException extends RuntimeException {
    public ForbiddenDeleteAttemptException(String message) {
        super(message);
    }
}
