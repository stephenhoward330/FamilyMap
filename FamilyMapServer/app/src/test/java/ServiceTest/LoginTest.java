package ServiceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Dao.DataAccessException;
import Dao.Database;
import Dao.UserDao;
import Model.User;
import Request.LoginRequest;
import Response.Response;
import Response.LoginResponse;
import Response.ErrorResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTest {
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
    public void loginPass() throws Exception {
        try {
            UserDao uDao = new UserDao();
            uDao.insert(bestUser);
        }
        catch (DataAccessException d) {
            //prob
        }

        Services services = new Services();
        LoginRequest request = new LoginRequest(bestUser.getUserName(), bestUser.getPassword());
        Response response = services.login(request);

        assertNotNull(response);
        assertEquals(response.getClass(), LoginResponse.class);
        LoginResponse testResponse = (LoginResponse) response;
        assertEquals(testResponse.getPersonID(), bestUser.getPersonID());
        assertEquals(testResponse.getUserName(), bestUser.getUserName());
    }

    @Test
    public void loginFail() throws Exception {
        try {
            UserDao uDao = new UserDao();
            uDao.insert(bestUser);
        }
        catch (DataAccessException d) {
            //prob
        }

        Services services = new Services();
        LoginRequest request = new LoginRequest("wrong", bestUser.getPassword());
        Response response = services.login(request);

        assertNotNull(response);
        assertEquals(response.getClass(), ErrorResponse.class);
        ErrorResponse testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: incorrect username");

        request = new LoginRequest(bestUser.getUserName(), "wrong");
        response = services.login(request);

        assertNotNull(response);
        assertEquals(response.getClass(), ErrorResponse.class);
        testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: incorrect password");

        response = services.login(null);

        assertNotNull(response);
        assertEquals(response.getClass(), ErrorResponse.class);
        testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: request missing");
    }
}
