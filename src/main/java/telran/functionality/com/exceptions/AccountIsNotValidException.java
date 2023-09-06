package telran.functionality.com.exceptions;
/**
 * Class of Exception, extending RuntimeException - to be thrown, when the account hasn't been activated
 * by creating an agreement
 *
 * @author Olena Averchenko
 * */
public class AccountIsNotValidException extends RuntimeException{
    public AccountIsNotValidException(String message) {
        super(message);
    }
}
