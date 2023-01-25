package DaoTest;

import Dao.DataAccessException;
import Dao.Database;
import Dao.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private Event anotherEvent;
    private Event thirdEvent;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        anotherEvent = new Event("Fun_123A", "Gale", "Jake123A",
                11.2f, 9.5f, "China", "Nagasaki",
                "Hiking_Around", 2018);
        thirdEvent = new Event("F_123A", "Gale50", "Jae123A",
                11.9f, 9.1f, "Korea", "Nagasi",
                "Liking_Around", 2017);
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {
        Event compareTest = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            compareTest = eDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            eDao.insert(bestEvent);
        } catch (DataAccessException e) {
            didItWork = false;
        }
        assertFalse(didItWork);

        db.clearTables();

        Event compareTest = bestEvent;
        try {
            EventDao eDao = new EventDao();
            compareTest = eDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        Event compareTest = null;

        try {
            EventDao pDao = new EventDao();
            pDao.insert(bestEvent);
            compareTest = pDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        Event compareTest = bestEvent;
        try {
            EventDao pDao = new EventDao();
            compareTest = pDao.find(bestEvent.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findAllPass() throws Exception {
        Event[] compareTest = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            eDao.insert(anotherEvent);
            compareTest = eDao.findAll(bestEvent.getAssociatedUsername());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest[0]);
        assertEquals(anotherEvent, compareTest[1]);
    }

    @Test
    public void findAllFail() throws Exception {
        Event[] compareTest = new Event[1];
        compareTest[0] = bestEvent;

        try {
            EventDao eDao = new EventDao();
            compareTest = eDao.findAll(bestEvent.getAssociatedUsername());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws Exception {
        Event compareTest = null;

        try {
            EventDao pDao = new EventDao();
            pDao.insert(bestEvent);
            pDao.clear();
            compareTest = pDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearUserDataPass() throws Exception {
        Event compareTest = null;
        Event compareTest2 = null;
        Event compareTest3 = null;

        try {
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            eDao.insert(anotherEvent);
            eDao.insert(thirdEvent);
            eDao.clearUserData(bestEvent.getAssociatedUsername());
            compareTest = eDao.find(bestEvent.getEventID());
            compareTest2 = eDao.find(anotherEvent.getEventID());
            compareTest3 = eDao.find(thirdEvent.getEventID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
        assertNull(compareTest2);
        assertNotNull(compareTest3);
        assertEquals(compareTest3, thirdEvent);
    }
}