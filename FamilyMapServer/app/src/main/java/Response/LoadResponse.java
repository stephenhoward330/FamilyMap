package Response;

/** LoadResponse class for conversion to JSON */
public class LoadResponse extends Response {
    /** message for the response */
    private String message;

    /** constructor for LoadResponse
     * @param message message for the response
     */
    public LoadResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
