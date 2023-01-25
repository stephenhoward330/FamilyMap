package com.example.familymapclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.DataModel;
import Model.Event;
import Model.Person;
import Model.Singleton;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {

    private DataModel dataModel;
    private SearchActivity searchActivity;

    @BeforeEach
    public void setUp() {
        dataModel = Singleton.getInstance();

        Person root = new Person("rootPersonID", "username", "child", "jones", "m", "fatherID", "motherID", null);
        Person father = new Person("fatherID", "username", "fathir", "jones", "m", null, null, "motherID");
        Person mother = new Person("motherID", "username", "mother", "jones", "f", null, null, "fatherID");
        Person[] family = {root, father, mother};
        dataModel.setPersons(family);

        Event rootEvent = new Event("rootEID", "username", "rootPersonID", (float)5.0, (float)5.0, "Spain", "Malaga", "Birth", 1523);
        Event fatherEvent = new Event("fatherEID", "username", "fatherID", (float)10.0, (float)10.0, "Spain", "Chiclana", "Birth", 1492);
        Event motherEvent = new Event("motherEID", "username", "motherID", (float)15.0, (float)15.0, "Spain", "San Vicente", "Birth", 1500);
        Event[] famEvents = {rootEvent, fatherEvent, motherEvent};
        dataModel.setEvents(famEvents);

        searchActivity = new SearchActivity();
        searchActivity.setPersonsAndEvents();
    }

    @Test
    public void searchPass() { // results with multiple persons and events
        Person[] foundPersons = searchActivity.searchPeople("jones");
        assertEquals(3, foundPersons.length);
        foundPersons = searchActivity.searchPeople("hi");
        assertEquals(2, foundPersons.length);
        foundPersons = searchActivity.searchPeople("mother");
        assertEquals(1, foundPersons.length);

        Event[] foundEvents = searchActivity.searchEvents("in");
        assertEquals(3, foundEvents.length);
        foundEvents = searchActivity.searchEvents("an");
        assertEquals(2, foundEvents.length);
        foundEvents = searchActivity.searchEvents("mal");
        assertEquals(1, foundEvents.length);
    }

    @Test
    public void searchFail() { // results with no persons or events
        Person[] foundPersons = searchActivity.searchPeople("");
        assertEquals(0, foundPersons.length);
        foundPersons = searchActivity.searchPeople("father");
        assertEquals(0, foundPersons.length);

        Event[] foundEvents = searchActivity.searchEvents("");
        assertEquals(0, foundEvents.length);
        foundEvents = searchActivity.searchEvents("Sant");
        assertEquals(0, foundEvents.length);
    }
}