package Model;

/** Event class */
public class Event {
    /** eventID of the event */
    private String eventID;
    /** associated username of the event */
    private String associatedUsername;
    /** personID of the event */
    private String personID;
    /** latitude of the event */
    private float latitude;
    /** longitude of the event */
    private float longitude;
    /** country of the event */
    private String country;
    /** city of the event */
    private String city;
    /** eventType of the event */
    private String eventType;
    /** year of the event */
    private int year;

    /** Event constructor
     * @param eventID eventID of the event
     * @param username associated username of the event
     * @param personID personID of the event
     * @param latitude latitude of the event
     * @param longitude longitude of the event
     * @param country country of the event
     * @param city city of the event
     * @param eventType eventType of the event
     * @param year year of the event
     */
    public Event(String eventID, String username, String personID, float latitude,
                 float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
        Event e = (Event) o;
        if (!e.getAssociatedUsername().equals(this.associatedUsername)) return false;
        if (!e.getCity().equals(this.city)) return false;
        if (!e.getCountry().equals(this.country)) return false;
        if (!e.getEventID().equals(this.eventID)) return false;
        if (e.getLatitude() != this.latitude) return false;
        if (e.getLongitude() != this.longitude) return false;
        if (!e.getEventType().equals(this.eventType)) return false;
        if (!e.getPersonID().equals(this.personID)) return false;
        if (e.getYear() != this.year) return false;

        return true;
    }
}
