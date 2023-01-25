package Request;

import java.util.List;

import Model.Event;
import Model.Person;
import Model.User;

/** LoadRequest class for conversion from JSON */
public class LoadRequest {
    /** array of users to be loaded in */
    private User[] users;
    /** array of persons to be loaded in */
    private Person[] persons;
    /** array of events to be loaded in */
    private Event[] events;

    /** constructor for LoadRequest
     * @param users array of users to be loaded in
     * @param persons array of persons to be loaded in
     * @param events array of events to be loaded in
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }
}
