package telran.functionality.com.exceptions;

public class ProductDoesntBelongToManagerException extends RuntimeException {
    public ProductDoesntBelongToManagerException(String message) {
        super(message);
    }
}
