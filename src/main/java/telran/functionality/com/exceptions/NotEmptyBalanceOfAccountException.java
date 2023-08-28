package telran.functionality.com.exceptions;

public class NotEmptyBalanceOfAccountException extends RuntimeException{

    public NotEmptyBalanceOfAccountException(String message) {
        super(message);
    }
}
