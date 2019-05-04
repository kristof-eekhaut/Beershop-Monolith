package be.ordina.beershop.exception;

import static java.util.Objects.requireNonNull;

public abstract class BusinessException extends RuntimeException {

    private final BusinessErrorCode errorCode;

    public BusinessException(BusinessErrorCode errorCode) {
        super();
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessException(BusinessErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessException(BusinessErrorCode errorCode, String message) {
        super(message);
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessException(BusinessErrorCode errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessErrorCode getErrorCode() {
        return errorCode;
    }
}
