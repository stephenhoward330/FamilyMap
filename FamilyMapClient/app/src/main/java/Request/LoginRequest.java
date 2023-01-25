package Request;

/** LoginRequest class for conversion from JSON */
public class LoginRequest {
    /** username of the user */
    private String userName;
    /** password of the user */
    private String password;

    /** constructor for LoginRequest
     * @param userName username of the user
     * @param password password of the user
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}