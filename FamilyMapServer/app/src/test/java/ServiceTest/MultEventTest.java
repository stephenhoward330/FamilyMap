package ServiceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Dao.AuthTokenDao;
import Dao.DataAccessException;
import Dao.Database;
import Dao.EventDao;
import Model.AuthToken;
import Model.Event;
import Response.Response;
import Response.ErrorResponse;
import Response.EventMultResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MultEventTest {
    private Database db;
    private Event bestEvent;
    private Event anotherEvent;
    private AuthToken bestAuthToken;
    private AuthToken badAuthToken;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale50", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        anotherEvent = new Event("Fun_123A", "Gale50", "Jake123A",
                11.2f, 9.5f, "China", "Nagasaki",
                "Hiking_Around", 2018);
        bestAuthToken = new AuthToken("Gale50", "AUTH321d");
        badAuthToken = new AuthToken("John2", "1234AUTH");

        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void multEventPass() throws Exception {
        Response compareTest = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            eDao.insert(anotherEvent);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            Services services = new Services();
            compareTest = services.event(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), EventMultResponse.class);
        EventMultResponse eventTest = (EventMultResponse) compareTest;
        assertEquals(bestEvent, eventTest.getData()[0]);
        assertEquals(anotherEvent, eventTest.getData()[1]);
    }

    @Test
    public void multEventFail() throws Exception {
        Response compareTest = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            eDao.insert(anotherEvent);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            aDao.insert(badAuthToken);
            Services services = new Services();
            compareTest = services.event("a1b2c3d4");
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        ErrorResponse eventTest = (ErrorResponse) compareTest;
        assertEquals(eventTest.getMessage(), "error: Invalid authToken");
    }
}
