package telran.functionality.com.exceptions;

public class ForbiddenLoginNameException extends RuntimeException{

    public ForbiddenLoginNameException(String message) {
        super(message);
    }
}
