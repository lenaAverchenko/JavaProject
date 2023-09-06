package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, if provided data is not expected in the certain place
 *
 * @author Olena Averchenko
 * */
public class WrongValueException extends RuntimeException {
    public WrongValueException(String message) {
        super(message);
    }
}
