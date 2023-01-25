package Model;

/** Person class */
public class Person {
    /** personID of the person */
    private String personID;
    /** username of the person */
    private String associatedUsername;
    /** first name of the person */
    private String firstName;
    /** last name of the person */
    private String lastName;
    /** gender of the person */
    private String gender;
    /** ID of the father of the person */
    private String fatherID;
    /** ID of the mother of the person */
    private String motherID;
    /** ID of the spouse of the person */
    private String spouseID;

    /** Person constructor
     * @param personID personID of the person
     * @param username username of the person
     * @param firstName first name of the person
     * @param lastName last name of the person
     * @param gender gender of the person
     * @param fatherID ID of the father of the person
     * @param motherID ID of the mother of the person
     * @param spouseID ID of the spouse of the person
     */
    public Person(String personID, String username, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
        Person p = (Person) o;
        if (!p.getAssociatedUsername().equals(this.associatedUsername)) return false;
        if (!p.getFatherID().equals(this.fatherID)) return false;
        if (!p.getFirstName().equals(this.firstName)) return false;
        if (!p.getGender().equals(this.gender)) return false;
        if (!p.getLastName().equals(this.lastName)) return false;
        if (!p.getPersonID().equals(this.personID)) return false;
        if (!p.getMotherID().equals(this.motherID)) return false;
        if (!p.getSpouseID().equals(this.spouseID)) return false;

        return true;
    }
}
