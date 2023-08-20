package telran.functionality.com.exceptions;

public class AccountIsNotValidException extends RuntimeException{
    public AccountIsNotValidException(String message) {
        super(message);
    }
}
