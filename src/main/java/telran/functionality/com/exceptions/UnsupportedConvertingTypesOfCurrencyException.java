package telran.functionality.com.exceptions;

/**
 * Class of Exception, extending RuntimeException - to be thrown, when user is trying to convert money, while
 * this type of converting is not supported
 *
 * @author Olena Averchenko
 */
public class UnsupportedConvertingTypesOfCurrencyException extends RuntimeException {
    public UnsupportedConvertingTypesOfCurrencyException(String message) {
        super(message);
    }
}
