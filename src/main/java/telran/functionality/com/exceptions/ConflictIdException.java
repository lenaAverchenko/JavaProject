package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, when the provided ids in different places
 * are expected to be the same, but they are not
 *
 * @author Olena Averchenko
 */
public class ConflictIdException extends RuntimeException {
    public ConflictIdException(String message) {
        super(message);
    }
}
