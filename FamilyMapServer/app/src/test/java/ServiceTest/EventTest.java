package ServiceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Dao.AuthTokenDao;
import Dao.DataAccessException;
import Dao.Database;
import Dao.EventDao;
import Dao.PersonDao;
import Model.AuthToken;
import Model.Event;
import Response.Response;
import Response.EventResponse;
import Response.ErrorResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventTest {
    private Database db;
    private Event bestEvent;
    private AuthToken bestAuthToken;
    private AuthToken badAuthToken;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale50", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        bestAuthToken = new AuthToken("Gale50", "AUTH321d");
        badAuthToken = new AuthToken("John2", "1234AUTH");

        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void eventPass() throws Exception {
        Response compareTest = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            Services services = new Services();
            compareTest = services.event(bestEvent.getEventID(), bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(EventResponse.class, compareTest.getClass());
        EventResponse eventTest = (EventResponse) compareTest;
        assertEquals(bestEvent.getPersonID(), eventTest.getPersonID());
    }

    @Test
    public void eventFail() throws Exception {
        Response compareTest = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            aDao.insert(badAuthToken);
            Services services = new Services();
            compareTest = services.event(bestEvent.getEventID(),"a1b2c3d4");
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        ErrorResponse eventTest = (ErrorResponse) compareTest;
        assertEquals(eventTest.getMessage(), "error: Invalid authToken");


        Services services = new Services();
        compareTest = services.event("a1b2c3d4", bestAuthToken.getAuthToken());
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        eventTest = (ErrorResponse) compareTest;
        assertEquals(eventTest.getMessage(), "error: Invalid eventID");

        compareTest = services.event(bestEvent.getEventID(), badAuthToken.getAuthToken());
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        eventTest = (ErrorResponse) compareTest;
        assertEquals(eventTest.getMessage(), "error: Requested event does not belong to this user");
    }
}
