package be.ordina.beershop.exception;

public class ErrorDTO {

    private String message;
    private String errorCode;

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(String message, String errorCode) {
        this(message);
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
