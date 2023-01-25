package ServiceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Dao.AuthTokenDao;
import Dao.DataAccessException;
import Dao.Database;
import Dao.PersonDao;
import Model.AuthToken;
import Model.Person;
import Response.Response;
import Response.PersonMultResponse;
import Response.ErrorResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MultPersonTest {
    private Database db;
    private Person bestPerson;
    private Person anotherPerson;
    private AuthToken bestAuthToken;
    private AuthToken badAuthToken;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestPerson = new Person("Gale123A", "Gale50", "Gale",
                "Smith", "f", "John123A", "Mary123A",
                "Mark123A");
        anotherPerson = new Person("John123A", "Gale50", "John",
                "Smith", "m", "Tomm123A", "Mart123A",
                "Gale123A");
        bestAuthToken = new AuthToken("Gale50", "AUTH321d");
        badAuthToken = new AuthToken("John2", "1234AUTH");

        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void multPersonPass() throws Exception {
        Response compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            pDao.insert(anotherPerson);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            Services services = new Services();
            compareTest = services.person(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), PersonMultResponse.class);
        PersonMultResponse personTest = (PersonMultResponse) compareTest;
        assertEquals(bestPerson, personTest.getData()[0]);
        assertEquals(anotherPerson, personTest.getData()[1]);
    }

    @Test
    public void multPersonFail() throws Exception {
        Response compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            pDao.insert(anotherPerson);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            aDao.insert(badAuthToken);
            Services services = new Services();
            compareTest = services.person("a1b2c3d4");
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        ErrorResponse personTest = (ErrorResponse) compareTest;
        assertEquals(personTest.getMessage(), "error: Invalid authToken");
    }
}
