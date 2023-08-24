package telran.functionality.com.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import telran.functionality.com.exceptions.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

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
    public ResponseEntity handleAccountDoesntBelongToClientException(AccountDoesntBelongToClientException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handleNotEnoughMoneyException(NotEnoughMoneyException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity handleProductDoesntBelongToManagerException(ProductDoesntBelongToManagerException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity handleAccountIsNotValidException(AccountIsNotValidException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception, HttpServletRequest request) {
        return new ResponseEntity("Unable to make changes to Entity, linked to another Entity. " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        return new ResponseEntity("Wrong type of provided data. " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return new ResponseEntity("Wrong type of provided data. " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}