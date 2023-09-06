package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, if the claimed product doesn't belong to
 * the claimed manager
 *
 * @author Olena Averchenko
 * */
public class ProductDoesntBelongToManagerException extends RuntimeException {
    public ProductDoesntBelongToManagerException(String message) {
        super(message);
    }
}
