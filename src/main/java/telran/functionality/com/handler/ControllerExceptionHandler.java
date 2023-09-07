package telran.functionality.com.handler;
/**
 * Class to handle different Exceptions, which could occur in different places of any Controller
 *
 * @author Olena Averchenko
 */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
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
        return new ResponseEntity("Provided data is not equivalent to the expected one", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleInvalidStatusException(InvalidStatusException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    @ExceptionHandler
    public ResponseEntity handleWrongValueException(WrongValueException exception, HttpServletRequest request) {
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
        return new ResponseEntity("Wrong type of provided data. ", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        return new ResponseEntity("Wrong type of provided data. ", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleUnsupportedConvertingTypesOfCurrencyException(UnsupportedConvertingTypesOfCurrencyException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity handleNotEmptyBalanceOfAccountException(NotEmptyBalanceOfAccountException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handleConflictIdException(ConflictIdException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler
    public ResponseEntity handleForbiddenDeleteAttemptException(ForbiddenDeleteAttemptException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handleUsernameNotFoundException(UsernameNotFoundException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity handleBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
        return new ResponseEntity("Неверный логин или пароль", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity handleForbiddenAccessException(ForbiddenAccessException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity handleForbiddenLoginNameException(ForbiddenLoginNameException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

}
