package Response;

/** FillResponse class for conversion to JSON */
public class FillResponse extends Response {
    /** message for the response */
    private String message;

    /** constructor for FillResponse
     * @param message message for the response
     */
    public FillResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
