package ServiceTest;

import Dao.*;
import Model.*;
import Service.Services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {
    private Database db;
    private User bestUser;
    private AuthToken bestAuthToken;
    private Person bestPerson;
    private Event bestEvent;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new User("Gale50", "Taxes", "Gale@gmail.com",
                "Gale", "Smith", "f", "Gale123A");
        bestAuthToken = new AuthToken("Gale123A", "AUTH321d");
        bestPerson = new Person("Gale123A", "Gale50", "Gale",
                "Smith", "f", "John123A", "Mary123A",
                "Mark123A");
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void clearPass() throws Exception {
        User compareTestUser = null;
        Person compareTestPerson = null;
        Event compareTestEvent = null;
        AuthToken compareTestAuthToken = null;

        Services services = new Services();

        try {
            UserDao uDao = new UserDao();
            uDao.insert(bestUser);
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            EventDao eDao = new EventDao();
            eDao.insert(bestEvent);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);

            services.clear();

            compareTestUser = uDao.find(bestUser.getUserName());
            compareTestPerson = pDao.find(bestPerson.getPersonID());
            compareTestEvent = eDao.find(bestEvent.getEventID());
            compareTestAuthToken = aDao.find(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //PROBLEM
        }
        assertNull(compareTestUser);
        assertNull(compareTestPerson);
        assertNull(compareTestEvent);
        assertNull(compareTestAuthToken);
    }
}