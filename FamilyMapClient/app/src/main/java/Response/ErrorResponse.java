package Response;

/** ErrorResponse class for conversion to JSON */
public class ErrorResponse extends Response {
    /** message from the error response */
    private String message;

    /** constructor for errorResponse
     * @param message message from the error response
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}