package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, when the attempt to get access to a certain method
 * is impossible, because of the lack of access rights
 *
 * @author Olena Averchenko
 */
public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
