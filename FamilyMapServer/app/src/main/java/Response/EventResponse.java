package Response;

/** EventResponse class for conversion to JSON */
public class EventResponse extends Response {
    /** associated username for the response */
    private String associatedUsername;
    /** eventID for the response */
    private String eventID;
    /** personID for the response */
    private String personID;
    /** latitude for the response */
    private float latitude;
    /** longitude for the response */
    private float longitude;
    /** country for the response */
    private String country;
    /** city for the response */
    private String city;
    /** eventType for the response */
    private String eventType;
    /** year for the response */
    private int year;

    /** constructor for EventResponse
     * @param associatedUsername associated username for the response
     * @param eventID eventID for the response
     * @param personID personID for the response
     * @param latitude latitude for the response
     * @param longitude longitude for the response
     * @param country country for the response
     * @param city city for the response
     * @param eventType eventType for the response
     * @param year year for the response
     */
    public EventResponse(String associatedUsername, String eventID, String personID,
                         float latitude, float longitude, String country,
                         String city, String eventType, int year) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }
}
