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
import Response.PersonResponse;
import Response.ErrorResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PersonTest {
    private Database db;
    private Person bestPerson;
    private AuthToken bestAuthToken;
    private AuthToken badAuthToken;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestPerson = new Person("Gale123A", "Gale50", "Gale",
                "Smith", "f", "John123A", "Mary123A",
                "Mark123A");
        bestAuthToken = new AuthToken("Gale50", "AUTH321d");
        badAuthToken = new AuthToken("John2", "1234AUTH");

        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void personPass() throws Exception {
        Response compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            Services services = new Services();
            compareTest = services.person(bestPerson.getPersonID(),bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), PersonResponse.class);
        PersonResponse personTest = (PersonResponse) compareTest;
        assertEquals(bestPerson.getFirstName(), personTest.getFirstName());
    }

    @Test
    public void personFail() throws Exception {
        Response compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            aDao.insert(badAuthToken);
            Services services = new Services();
            compareTest = services.person(bestPerson.getPersonID(),"a1b2c3d4");
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        ErrorResponse personTest = (ErrorResponse) compareTest;
        assertEquals(personTest.getMessage(), "error: Invalid authToken");


        Services services = new Services();
        compareTest = services.person("a1b2c3d4", bestAuthToken.getAuthToken());
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        personTest = (ErrorResponse) compareTest;
        assertEquals(personTest.getMessage(), "error: Invalid personID");

        compareTest = services.person(bestPerson.getPersonID(), badAuthToken.getAuthToken());
        assertEquals(compareTest.getClass(), ErrorResponse.class);
        personTest = (ErrorResponse) compareTest;
        assertEquals(personTest.getMessage(), "error: Requested person does not belong to this user");
    }
}
