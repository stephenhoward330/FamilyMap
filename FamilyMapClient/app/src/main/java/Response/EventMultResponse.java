package Response;

import Model.Event;

/** EventMultResponse class for conversion to JSON */
public class EventMultResponse extends Response {
    /** event array for the response */
    private Event[] data;

    /** constructor for EventMultResponse
     * @param data event array for the response
     */
    public EventMultResponse(Event[] data) {
        this.data = data;
    }

    public Event[] getData() {
        return data;
    }
}