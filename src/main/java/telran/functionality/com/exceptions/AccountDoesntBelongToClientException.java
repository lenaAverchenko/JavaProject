package telran.functionality.com.exceptions;

public class AccountDoesntBelongToClientException extends RuntimeException {
    public AccountDoesntBelongToClientException(String message) {
        super(message);
    }
}
