package DaoTest;

import Dao.DataAccessException;
import Dao.Database;
import Dao.UserDao;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private Database db;
    private User bestUser;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new User("Gale50", "Taxes", "Gale@gmail.com",
                "Gale", "Smith", "f", "Gale123A");
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {
        User compareTest = null;

        try {
            UserDao pDao = new UserDao();
            pDao.insert(bestUser);
            compareTest = pDao.find(bestUser.getUserName());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            UserDao pDao = new UserDao();
            pDao.insert(bestUser);
            pDao.insert(bestUser);
        } catch (DataAccessException e) {
            //db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        db.clearTables();

        User compareTest = bestUser;
        try {
            UserDao pDao = new UserDao();
            compareTest = pDao.find(bestUser.getUserName());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        User compareTest = null;

        try {
            UserDao pDao = new UserDao();
            pDao.insert(bestUser);
            compareTest = pDao.find(bestUser.getUserName());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        User compareTest = bestUser;
        try {
            UserDao pDao = new UserDao();
            compareTest = pDao.find(bestUser.getUserName());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws Exception {
        User compareTest = null;

        try {
            UserDao pDao = new UserDao();
            pDao.insert(bestUser);
            pDao.clear();
            compareTest = pDao.find(bestUser.getUserName());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearFail() throws Exception {
        User compareTest = bestUser;
        try {
            UserDao pDao = new UserDao();
            pDao.clear();
            pDao.insert(bestUser);
            compareTest = pDao.find(bestUser.getUserName());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }
}
