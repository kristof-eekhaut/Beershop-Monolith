package be.ordina.beershop.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandingAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandingAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException businessException) {

        LOGGER.error(businessException.getErrorCode(), businessException);

        HttpStatus httpStatus;
        ResponseStatus responseStatusAnnotation = AnnotationUtils.findAnnotation(businessException.getClass(), ResponseStatus.class);
        if (responseStatusAnnotation != null) {
            httpStatus = responseStatusAnnotation.code();
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
}
