package Response;

/** ClearResponse class for conversion to JSON */
public class ClearResponse extends Response {
    /** message for the response */
    private String message;

    /** constructor for ClearResponse
     * @param message message for the response
     */
    public ClearResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
