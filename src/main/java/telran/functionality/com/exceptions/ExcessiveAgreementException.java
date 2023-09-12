package telran.functionality.com.exceptions;

/**
 * Class of Exception, extending RuntimeException - to be thrown, when the new agreement is going to be created for the account, which is already activated (and has an agreement)
 *
 * @author Olena Averchenko
 */
public class ExcessiveAgreementException extends RuntimeException {
    public ExcessiveAgreementException(String message) {
        super(message);
    }
}
