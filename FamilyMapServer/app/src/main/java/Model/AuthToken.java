package Model;

/** AuthToken Class */
public class AuthToken {
    /** username associated with the authToken */
    private String associatedUsername;
    /** the authToken itself */
    private String authToken;

    /** AuthToken constructor
     * @param username associated username for the authToken
     * @param authToken the authToken itself
     */
    public AuthToken(String username, String authToken) {
        this.associatedUsername = username;
        this.authToken = authToken;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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
        AuthToken a = (AuthToken) o;
        if (!a.getAssociatedUsername().equals(this.associatedUsername)) return false;
        if (!a.getAuthToken().equals(this.authToken)) return false;

        return true;
    }
}
