package telran.functionality.com.exceptions;

public class ForbiddenDeleteAttemptException extends RuntimeException{
    public ForbiddenDeleteAttemptException(String message) {
        super(message);
    }
}
