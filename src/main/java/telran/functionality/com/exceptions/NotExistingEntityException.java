package telran.functionality.com.exceptions;

public class NotExistingEntityException extends RuntimeException {
    public NotExistingEntityException(String message) {
        super(message);
    }
}
