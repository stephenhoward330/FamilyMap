package DaoTest;

import Dao.AuthTokenDao;
import Dao.DataAccessException;
import Dao.Database;
import Model.AuthToken;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenTest {
    private Database db;
    private AuthToken bestAuthToken;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestAuthToken = new AuthToken("Gale123A", "AUTH321d");
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {
        AuthToken compareTest = null;

        try {
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            compareTest = aDao.find(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(bestAuthToken);
            aDao.insert(bestAuthToken);
        } catch (DataAccessException e) {
            didItWork = false;
        }
        assertFalse(didItWork);

        db.clearTables();

        AuthToken compareTest = bestAuthToken;
        try {
            AuthTokenDao aDao = new AuthTokenDao();
            compareTest = aDao.find(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        AuthToken compareTest = null;

        try {
            AuthTokenDao pDao = new AuthTokenDao();
            pDao.insert(bestAuthToken);
            compareTest = pDao.find(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestAuthToken, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        AuthToken compareTest = bestAuthToken;
        try {
            AuthTokenDao pDao = new AuthTokenDao();
            compareTest = pDao.find(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws Exception {
        AuthToken compareTest = null;

        try {
            AuthTokenDao pDao = new AuthTokenDao();
            pDao.insert(bestAuthToken);
            pDao.clear();
            compareTest = pDao.find(bestAuthToken.getAuthToken());
        } catch (DataAccessException e) {
            //db.closeConnection(false);
        }
        assertNull(compareTest);
    }
}
