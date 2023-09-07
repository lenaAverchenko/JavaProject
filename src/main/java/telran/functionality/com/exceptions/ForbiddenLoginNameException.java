package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, when user with provided login already exists
 *
 * @author Olena Averchenko
 */
public class ForbiddenLoginNameException extends RuntimeException{

    public ForbiddenLoginNameException(String message) {
        super(message);
    }
}
