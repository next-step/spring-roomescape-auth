package roomescape.exception;

public class ErrorCodeResponse {

    private int status;
    private ErrorCode errorCode;
    private String message;

    public ErrorCodeResponse() {
    }

    public ErrorCodeResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.status = errorCode.getStatus().value();
    }

    public int getStatus() {
        return status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
