package ServiceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Dao.*;
import Model.*;
import Request.LoadRequest;
import Response.Response;
import Response.ErrorResponse;
import Response.LoadResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoadTest {
    private Database db;
    private User bestUser;
    private User anotherUser;
    private Person bestPerson;
    private Person anotherPerson;
    private Event bestEvent;
    private Event anotherEvent;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new User("Gale50", "Taxes", "Gale@gmail.com",
                "Gale", "Smith", "f", "Gale123A");
        anotherUser = new User("Gale87", "Tax", "Bob@gmail.com",
                "Bob", "Smiths", "m", "Bobs123A");
        bestPerson = new Person("Gale123A", "Gale50", "Gale",
                "Smith", "f", "John123A", "Mary123A",
                "Mark123A");
        anotherPerson = new Person("John123A", "Gale50", "John",
                "Smith", "m", "Tomm123A", "Mart123A",
                "Gale123A");
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        anotherEvent = new Event("Fun_123A", "Gale50", "Jake123A",
                11.2f, 9.5f, "China", "Nagasaki",
                "Hiking_Around", 2018);
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void loadPass() throws Exception {
        User[] userArray = new User[2];
        Person[] personArray = new Person[2];
        Event[] eventArray = new Event[2];

        userArray[0] = bestUser;
        userArray[1] = anotherUser;
        personArray[0] = bestPerson;
        personArray[1] = anotherPerson;
        eventArray[0] = bestEvent;
        eventArray[1] = anotherEvent;

        LoadRequest request = new LoadRequest(userArray, personArray, eventArray);

        Services services = new Services();
        Response response = services.load(request);

        User compareTestUser = null;
        Person compareTestPerson = null;
        Event compareTestEvent = null;

        try {
            UserDao uDao = new UserDao();
            compareTestUser = uDao.find(bestUser.getUserName());
            PersonDao pDao = new PersonDao();
            compareTestPerson = pDao.find(bestPerson.getPersonID());
            EventDao eDao = new EventDao();
            compareTestEvent = eDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {
            //PROBLEM
        }
        assertEquals(response.getClass(), LoadResponse.class);
        assertNotNull(compareTestEvent);
        assertNotNull(compareTestPerson);
        assertNotNull(compareTestUser);
        assertEquals(compareTestEvent, bestEvent);
        assertEquals(compareTestPerson, bestPerson);
        assertEquals(compareTestUser, bestUser);
    }

    // Load can't fail, should never return an ErrorResponse
}
