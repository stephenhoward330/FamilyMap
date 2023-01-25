package DaoTest;

import Dao.DataAccessException;
import Dao.Database;
import Dao.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private Person anotherPerson;
    private Person thirdPerson;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestPerson = new Person("Gale123A", "Gale50", "Gale",
                "Smith", "f", "Jake123A", "Mary123A",
                "John123A");
        anotherPerson = new Person("John123A", "Gale50", "John",
                "Smith", "m", "Tomm123A", "Mart123A",
                "Gale123A");
        thirdPerson = new Person("Tom1234", "Tom50", "Jax",
                "Smith", "m", "To123A", "Mar123A",
                "Ge123A");
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {
        Person compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            pDao.insert(bestPerson);
        } catch (DataAccessException e) {
            didItWork = false;
        }
        assertFalse(didItWork);

        db.clearTables();

        Person compareTest = bestPerson;
        try {
            PersonDao pDao = new PersonDao();
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        Person compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        Person compareTest = bestPerson;
        try {
            PersonDao pDao = new PersonDao();
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findAllPass() throws Exception {
        Person[] compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            pDao.insert(anotherPerson);
            compareTest = pDao.findAll(bestPerson.getAssociatedUsername());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest[0]);
        assertEquals(anotherPerson, compareTest[1]);
    }

    @Test
    public void findAllFail() throws Exception {
        Person[] compareTest = new Person[1];
        compareTest[0] = bestPerson;

        try {
            PersonDao pDao = new PersonDao();
            compareTest = pDao.findAll(bestPerson.getAssociatedUsername());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws Exception {
        Person compareTest = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            pDao.clear();
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearFail() throws Exception {
        Person compareTest = bestPerson;
        try {
            PersonDao pDao = new PersonDao();
            pDao.clear();
            pDao.insert(bestPerson);
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void clearUserDataPass() throws Exception {
        Person compareTest = null;
        Person compareTest2 = null;
        Person compareTest3 = null;

        try {
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);
            pDao.insert(anotherPerson);
            pDao.insert(thirdPerson);
            pDao.clearUserData(bestPerson.getAssociatedUsername());
            compareTest = pDao.find(bestPerson.getPersonID());
            compareTest2 = pDao.find(anotherPerson.getPersonID());
            compareTest3 = pDao.find(thirdPerson.getPersonID());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
        assertNull(compareTest2);
        assertNotNull(compareTest3);
        assertEquals(compareTest3, thirdPerson);
    }
}
