package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, when user is trying to make a transaction, required
 * more money then there is on the account
 *
 * @author Olena Averchenko
 */
public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
