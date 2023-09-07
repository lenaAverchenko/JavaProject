package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, when returns the empty list of data, while
 * it's expected to have some information
 *
 * @author Olena Averchenko
 */
public class EmptyRequiredListException extends RuntimeException {
    public EmptyRequiredListException(String message) {
        super(message);
    }
}
