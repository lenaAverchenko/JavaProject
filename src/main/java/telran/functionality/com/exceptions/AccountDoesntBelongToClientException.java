package telran.functionality.com.exceptions;

/**
 * Class of Exception, extending RuntimeException - to be thrown, when the claimed account doesn't belong to the clamed client
 *
 * @author Olena Averchenko
 */
public class AccountDoesntBelongToClientException extends RuntimeException {
    public AccountDoesntBelongToClientException(String message) {
        super(message);
    }
}
