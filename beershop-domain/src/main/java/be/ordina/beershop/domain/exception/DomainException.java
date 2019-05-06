package be.ordina.beershop.domain.exception;

import static java.util.Objects.requireNonNull;

public abstract class DomainException extends RuntimeException {

    private final DomainErrorCode errorCode;

    public DomainException(DomainErrorCode errorCode) {
        super();
        this.errorCode = requireNonNull(errorCode);
    }

    public DomainException(DomainErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = requireNonNull(errorCode);
    }

    public DomainException(DomainErrorCode errorCode, String message) {
        super(message);
        this.errorCode = requireNonNull(errorCode);
    }

    public DomainException(DomainErrorCode errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = requireNonNull(errorCode);
    }

    public DomainErrorCode getErrorCode() {
        return errorCode;
    }
}
