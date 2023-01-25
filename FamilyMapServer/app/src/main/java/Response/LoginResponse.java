package Response;

/** LoginResponse class for conversion to JSON */
public class LoginResponse extends Response {
    /** authToken for the response */
    private String authToken;
    /** username for the response */
    private String userName;
    /** personID for the response */
    private String personID;

    /** constructor for LoginResponse
     * @param authToken authToken for the response
     * @param userName username for the response
     * @param personID personID for the response
     */
    public LoginResponse(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }
}
