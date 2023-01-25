package ServiceTest;

import Dao.*;
import JsonData.Location;
import JsonData.LocationNameProcessor;
import Model.*;
import Response.Response;
import Response.FillResponse;
import Response.ErrorResponse;
import Service.Services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FillTest {
    Database db;
    User bestUser;
    Person bestPerson;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new User("Gale50", "Taxes", "Gale@gmail.com",
                "Gale", "Smith", "f", "Gale123A");
        bestPerson = new Person("Gale123A", "Gale50", "Gale",
                "Smith", "f", "John123A", "Mary123A",
                "Mark123A");
        db.createTables();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void locationNamePass() throws Exception {
        LocationNameProcessor locationNameProcessor = new LocationNameProcessor();
        String name = locationNameProcessor.getRandFName();
        System.out.println("female name: " + name);
        name = locationNameProcessor.getRandMName();
        System.out.println("male name: " + name);
        name = locationNameProcessor.getRandSName();
        System.out.println("last name: " + name);
        Location location = locationNameProcessor.getRandLocation();
        System.out.println("city: " + location.getCity());
        System.out.println("country: " + location.getCountry());
        System.out.println("latitude: " + location.getLatitude());
        System.out.println("longitude: " + location.getLongitude());
    }

    @Test
    public void fillPass() throws Exception {
        Person comparePerson = bestPerson;
        Services services = new Services();
        Response response;
        FillResponse testResponse;

        try {
            UserDao uDao = new UserDao();
            uDao.insert(bestUser);
            PersonDao pDao = new PersonDao();
            pDao.insert(bestPerson);

            response = services.fill(bestUser.getUserName(), 0);
            comparePerson = pDao.find(bestPerson.getPersonID());

            assertNotEquals(comparePerson, bestPerson);
            assertEquals(response.getClass(), FillResponse.class);
            testResponse = (FillResponse) response;
            assertEquals(testResponse.getMessage(), "Successfully added 1 persons and 1 events to the database");
        } catch (DataAccessException d) {
            //problem
        }

        response = services.fill(bestUser.getUserName(), 1);
        assertEquals(response.getClass(), FillResponse.class);
        testResponse = (FillResponse) response;
        assertEquals(testResponse.getMessage(), "Successfully added 3 persons and 7 events to the database");

        response = services.fill(bestUser.getUserName(), 2);
        assertEquals(response.getClass(), FillResponse.class);
        testResponse = (FillResponse) response;
        assertEquals(testResponse.getMessage(), "Successfully added 7 persons and 19 events to the database");
    }

    @Test
    public void fillFail() throws Exception {
        try {
            UserDao uDao = new UserDao();
            uDao.insert(bestUser);
        } catch (DataAccessException d) {
            //problem
        }

        Services services = new Services();
        Response response = services.fill("aa11bb22", 0);

        assertEquals(response.getClass(), ErrorResponse.class);
        ErrorResponse testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: invalid username");

        response = services.fill(bestUser.getUserName(), -1);

        assertEquals(response.getClass(), ErrorResponse.class);
        testResponse = (ErrorResponse) response;
        assertEquals(testResponse.getMessage(), "error: invalid number of generations");
    }
}
