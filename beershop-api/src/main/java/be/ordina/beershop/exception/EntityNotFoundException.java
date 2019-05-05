package be.ordina.beershop.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(BusinessErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(BusinessErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    public EntityNotFoundException(BusinessErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public EntityNotFoundException(BusinessErrorCode errorCode, String message, Throwable throwable) {
        super(errorCode, message, throwable);
    }
}
