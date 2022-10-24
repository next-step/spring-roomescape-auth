package nextstep.support;

public class ErrorResponse {
    private String message;

    protected ErrorResponse() {
    }

    ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}