package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.DataModel;
import Model.Event;
import Model.Person;

import static org.junit.jupiter.api.Assertions.*;

public class SortPersonEventsTest {

    private DataModel dataModel;
    private Person root;
    private Person eventlessPerson;

    @BeforeEach
    public void setUp() {
        dataModel = new DataModel();

        root = new Person("rootPersonID", "username", "root", "jones", "m", "fatherID", "motherID", "spouseID");
        eventlessPerson = new Person("aloneID", "username", "alone", "jones", "f", null, null, null);
        Person[] family = {root};
        dataModel.setPersons(family);

        Event birthEvent = new Event("birthEID", "username", "rootPersonID", (float)5.0, (float)5.0, "Spain", "Malaga", "Birth", 1523);
        Event party1Event = new Event("party1EID", "username", "rootPersonID", (float)10.0, (float)10.0, "Spain", "Chiclana", "Party", 1569);
        Event party2Event = new Event("party2EID", "username", "rootPersonID", (float)12.0, (float)12.0, "Spain", "Alicante", "Party", 1573);
        Event danceEvent = new Event("danceEID", "username", "rootPersonID", (float)14.0, (float)14.0, "Spain", "Molina", "Dance", 1573);
        Event deathEvent = new Event("deathEID", "username", "rootPersonID", (float)15.0, (float)15.0, "Spain", "San Vicente", "Death", 1600);

        Event[] personEvents = {deathEvent, birthEvent, danceEvent, party2Event, party1Event}; // random order
        dataModel.setEvents(personEvents);
    }

    @Test
    public void sortPersonEventsPass() { // returns 5 events
        Event[] testEvents = dataModel.getPersonalEvents(root);

        assertEquals(5, testEvents.length);
        // should be: birth -> party1 -> dance -> party2 -> death
        assertEquals("birthEID", testEvents[0].getEventID());
        assertEquals("party1EID", testEvents[1].getEventID());
        assertEquals("danceEID", testEvents[2].getEventID());
        assertEquals("party2EID", testEvents[3].getEventID());
        assertEquals("deathEID", testEvents[4].getEventID());
        // parties have the same type, party2 and dance have the same year
    }

    @Test
    public void sortPersonEventsFail() { // returns no events
        Event[] testEvents = dataModel.getPersonalEvents(eventlessPerson);

        assertEquals(0, testEvents.length);
    }
}