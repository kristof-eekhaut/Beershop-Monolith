package be.ordina.beershop.exception.rest;

import be.ordina.beershop.exception.BusinessErrorCode;
import be.ordina.beershop.exception.ErrorCode;

public class ErrorDTO {

    private String message;
    private ErrorCode errorCode;

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(String message, BusinessErrorCode errorCode) {
        this(message);
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
