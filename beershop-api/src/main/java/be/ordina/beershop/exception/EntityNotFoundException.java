package be.ordina.beershop.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(String errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    public EntityNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    public EntityNotFoundException(String errorCode, String message, Throwable throwable) {
        super(errorCode, message, throwable);
    }
}
