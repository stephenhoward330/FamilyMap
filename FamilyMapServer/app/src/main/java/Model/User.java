package Model;

/** User class */
public class User {
    /** username of the user */
    private String userName;
    /** password of the user */
    private String password;
    /** email of the user */
    private String email;
    /** first name of the user */
    private String firstName;
    /** last name of the user */
    private String lastName;
    /** gender of the user */
    private String gender;
    /** personID of the user */
    private String personID;

    /** User constructor
     * @param username username of the user
     * @param password password of the user
     * @param email email of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param gender gender of the user
     * @param personID personID of the user
     */
    public User(String username, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /** determines if the object o equals this object
     * @param o the object to compare this with
     * @return true if o equals this
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        User u = (User) o;
        if (!u.getEmail().equals(this.email)) return false;
        if (!u.getFirstName().equals(this.firstName)) return false;
        if (!u.getGender().equals(this.gender)) return false;
        if (!u.getLastName().equals(this.lastName)) return false;
        if (!u.getPassword().equals(this.password)) return false;
        if (!u.getPersonID().equals(this.personID)) return false;
        if (!u.getUserName().equals(this.userName)) return false;

        return true;
    }
}
