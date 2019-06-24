package be.ordina.beershop.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandingAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandingAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException businessException) {

        LOGGER.error(businessException.getErrorCode(), businessException);

        HttpStatus httpStatus;
        if (businessException instanceof  EntityNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(new ErrorDTO(businessException.getMessage(), businessException.getErrorCode()), httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleUnexpectedException(RuntimeException exception) {

        LOGGER.error(exception);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        LOGGER.warn(exception);

        StringBuilder errors = new StringBuilder();
        errors.append("Validation errors: \n");
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            errors.append(error.getObjectName()).append(": ").append(error.getDefaultMessage()).append("\n");
        }
        LOGGER.warn(errors);

        return handleExceptionInternal(exception, errors.toString(), headers, HttpStatus.BAD_REQUEST, request);
    }
}
