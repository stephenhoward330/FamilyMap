package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.DataModel;
import Model.Event;
import Model.Person;

import static org.junit.jupiter.api.Assertions.*;

public class FilterEventTest {

    private DataModel dataModel;

    @BeforeEach
    public void setUp() {
        dataModel = new DataModel();
        Person root = new Person("rootPersonID", "username", "child", "jones", "m", "fatherID", "motherID", null);
        Person father = new Person("fatherID", "username", "father", "jones", "m", null, null, "motherID");
        Person mother = new Person("motherID", "username", "mother", "jones", "f", null, null, "fatherID");
        Person[] family = {root, father, mother};
        dataModel.setPersons(family);

        Event rootEvent = new Event("rootEID", "username", "rootPersonID", (float)5.0, (float)5.0, "Spain", "Malaga", "Birth", 1523);
        Event fatherEvent = new Event("fatherEID", "username", "fatherID", (float)10.0, (float)10.0, "Spain", "Chiclana", "Birth", 1492);
        Event motherEvent = new Event("motherEID", "username", "motherID", (float)15.0, (float)15.0, "Spain", "San Vicente", "Birth", 1500);
        Event[] famEvents = {rootEvent, fatherEvent, motherEvent};
        dataModel.setEvents(famEvents);

        dataModel.setRootPersonID("rootPersonID");
    }

    @Test
    public void filterEventPass() { // some settings on
        Event[] testEvents = dataModel.getFilteredEvents();
        assertEquals(3, testEvents.length);

        dataModel.setShowFemales(false);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(2, testEvents.length);

        dataModel.setShowMales(false);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(0, testEvents.length);

        dataModel.setShowFemales(true);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(1, testEvents.length);
        assertEquals("motherID", testEvents[0].getPersonID());

        dataModel.setShowMales(true);

        dataModel.setShowMotherSide(false);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(2, testEvents.length);

        dataModel.setShowFatherSide(false);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(1, testEvents.length);
        assertEquals("rootPersonID", testEvents[0].getPersonID());

        dataModel.setShowMotherSide(true);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(2, testEvents.length);
    }

    @Test
    public void filterEventFail() { // all settings off
        Event[] testEvents = dataModel.getFilteredEvents();
        assertEquals(3, testEvents.length);

        dataModel.setShowFatherSide(false);
        dataModel.setShowMotherSide(false);
        dataModel.setShowFemales(false);
        dataModel.setShowMales(false);

        dataModel.setShowMotherSide(false);
        testEvents = dataModel.getFilteredEvents();
        assertEquals(0, testEvents.length);
    }
}