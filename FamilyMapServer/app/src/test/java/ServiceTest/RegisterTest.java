package ServiceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Dao.DataAccessException;
import Dao.Database;
import Dao.UserDao;
import Model.User;
import Request.RegisterRequest;
import Response.Response;
import Response.RegisterResponse;
import Response.ErrorResponse;
import Service.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterTest {
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
    public void registerPass() throws Exception {
        RegisterRequest request = new RegisterRequest("Gale50", "Taxes", "Gale@gmail.com",
                "Gale", "Smith", "f");
        Services services = new Services();
        Response response = services.register(request);

        assertEquals(RegisterResponse.class, response.getClass());
        RegisterResponse testResponse = (RegisterResponse) response;
        assertEquals(bestUser.getUserName(), testResponse.getUserName());
    }

    @Test
    public void registerFail() throws Exception {
        try {
            UserDao uDao = new UserDao();
            uDao.insert(bestUser);
        } catch (DataAccessException d) {
            //fail
        }

        RegisterRequest request = new RegisterRequest("Gale50", "Taxes", "Gale@gmail.com",
                "Gale", "Smith", "f");
        Services services = new Services();
        Response response = services.register(request);

        assertEquals(ErrorResponse.class, response.getClass());
        ErrorResponse testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: username taken");

        services = new Services();
        response = services.register(null);

        assertEquals(ErrorResponse.class, response.getClass());
        testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: request missing");
    }
}
