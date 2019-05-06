package be.ordina.beershop.exception;

import static java.util.Objects.requireNonNull;

public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode) {
        super();
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessException(String errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = requireNonNull(errorCode);
    }

    public BusinessException(String errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = requireNonNull(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
