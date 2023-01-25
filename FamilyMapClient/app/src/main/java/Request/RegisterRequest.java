package Request;

/** RegisterRequest class for conversion from JSON */
public class RegisterRequest {
    /** userName of the new user */
    private String userName;
    /** password of the new user */
    private String password;
    /** email of the new user */
    private String email;
    /** first name of the new user */
    private String firstName;
    /** last name of the new user */
    private String lastName;
    /** gender of the new user */
    private String gender;

    /** constructor for RegisterRequest class
     * @param userName userName of the new user
     * @param password password of the new user
     * @param email email of the new user
     * @param firstName first name of the new user
     * @param lastName last name of the new user
     * @param gender gender of the new user
     */
    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}