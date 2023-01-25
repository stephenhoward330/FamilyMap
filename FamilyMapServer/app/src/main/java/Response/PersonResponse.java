package Response;

/** PersonResponse class for conversion to JSON */
public class PersonResponse extends Response {
    /** associated username for the response */
    private String associatedUsername;
    /** personID for the response */
    private String personID;
    /** first name for the response */
    private String firstName;
    /** last name for the response */
    private String lastName;
    /** gender for the response */
    private String gender;
    /** fatherID for the response */
    private String fatherID;
    /** motherID for the response */
    private String motherID;
    /** spouseID for the response */
    private String spouseID;

    /** constructor for personResponse
     * @param associatedUsername associated username for the response
     * @param personID personID for the response
     * @param firstName first name for the response
     * @param lastName last name for the response
     * @param gender gender for the response
     * @param fatherID fatherID for the response
     * @param motherID motherID for the response
     * @param spouseID spouseID for the response
     */
    public PersonResponse(String associatedUsername, String personID, String firstName,
                          String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}
