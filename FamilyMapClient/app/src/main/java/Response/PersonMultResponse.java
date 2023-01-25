package Response;

import Model.Person;

/** PersonMultResponse class for conversion to JSON */
public class PersonMultResponse extends Response {
    /** person array for the response */
    private Person[] data;

    /** constructor for PersonMultResponse
     * @param data person array for the response
     */
    public PersonMultResponse(Person[] data) {
        this.data = data;
    }

    public Person[] getData() {
        return data;
    }
}