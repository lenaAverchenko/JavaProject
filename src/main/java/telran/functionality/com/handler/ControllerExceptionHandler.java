package telran.functionality.com.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import telran.functionality.com.exceptions.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity handleNotExistingEntityException(NotExistingEntityException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity handleEmptyRequiredListException(EmptyRequiredListException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided data is not equivalent to the expected one");
    }

    @ExceptionHandler
    public ResponseEntity handleInvalidStatusException(InvalidStatusException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    @ExceptionHandler
    public ResponseEntity handleWrongValueOfStatusException(WrongValueException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handle(AccountDoesntBelongToClientException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handleNotEnoughMoneyException(NotEnoughMoneyException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity ProductDoesntBelongToManagerException(ProductDoesntBelongToManagerException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

}
