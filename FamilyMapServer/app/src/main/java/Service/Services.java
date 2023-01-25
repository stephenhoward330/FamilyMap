package Service;

import java.util.UUID;

import Dao.*;
import JsonData.Location;
import JsonData.LocationNameProcessor;
import Model.*;
import Response.*;
import Request.*;

/** Services class that performs the services */
public class Services {
    private Database db;

    /** empty constructor for Services class */
    public Services() {
        db = new Database();
    }

    /** Creates a new user account, generates 4 generations of ancestor data for the new user,
     *  logs the user in, and returns an auth token
     * @param request contains all the necessary information for this method
     * @return a response body with the auth token
     */
    public Response register(RegisterRequest request) {
        if (request == null) {
            return new ErrorResponse("error: request missing");
        }

        String newPersonID = makeRandomID();
        try {
            UserDao uDao = new UserDao();
            if (uDao.find(request.getUserName()) != null) {
                return new ErrorResponse("error: username taken");
            }
            uDao.insert(new User(request.getUserName(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender(), newPersonID));
        }
        catch (DataAccessException d) {
            return new ErrorResponse("error: register failed");
        }

        Response response = fill(request.getUserName(), 4);
        if (response.getClass() == ErrorResponse.class) {
            return new ErrorResponse("error: register failed on fill");
        }

        response = login(new LoginRequest(request.getUserName(), request.getPassword()));
        if (response.getClass() == ErrorResponse.class) {
            return new ErrorResponse("error: register failed on login");
        }
        LoginResponse loginResponse = (LoginResponse) response;

        return new RegisterResponse(loginResponse.getAuthToken(), loginResponse.getUserName(),
                loginResponse.getPersonID());
    }

    /** Logs in the user and returns an auth token
     * @param request contains all the necessary information for this method
     * @return a response body with the auth token
     */
    public Response login(LoginRequest request) {
        if (request == null) {
            return new ErrorResponse("error: request missing");
        }

        UserDao uDao = new UserDao();
        User user;
        try {
            user = uDao.find(request.getUserName());
        } catch (DataAccessException d) {
            return new ErrorResponse("error: login failed");
        }

        if (user == null) {
            return new ErrorResponse("error: incorrect username");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return new ErrorResponse("error: incorrect password");
        }

        String newAToken = makeRandomID();

        try {
            AuthTokenDao aDao = new AuthTokenDao();
            aDao.insert(new AuthToken(user.getUserName(), newAToken));
        } catch (DataAccessException d) {
            return new ErrorResponse("error: login failed");
        }

        return new LoginResponse(newAToken, user.getUserName(), user.getPersonID());
    }

    /** Deletes ALL data from the database, including user accounts, auth tokens,
     * and generated person and event data
     * @return a response body
     */
    public Response clear() {
        try {
            db.clearTables();
        }
        catch (DataAccessException d) {
            return new ErrorResponse("error: Clear failed");
        }
        return new ClearResponse("Clear succeeded");
    }

    private int numPersonsAdded;
    private int numEventsAdded;

    /** Populates the server's database with generated data for the specified username
     * @param username username to add data for
     * @param generations number of generations to added
     * @return a response body
     */
    public Response fill(String username, int generations) {
        numEventsAdded = 0;
        numPersonsAdded = 0;

        if (generations < 0) {
            return new ErrorResponse("error: invalid number of generations");
        }

        User user;
        try {
            PersonDao pDao = new PersonDao();
            EventDao eDao = new EventDao();
            UserDao uDao = new UserDao();
            user = uDao.find(username);
            if (user == null) {
                return new ErrorResponse("error: invalid username");
            }

            pDao.clearUserData(username);
            eDao.clearUserData(username);
        } catch (DataAccessException d) {
            return new ErrorResponse("error: fill failed");
        }

        String fatherID;
        String motherID;
        int userBirthYear = 2000;

        if (generations > 0) {
            fatherID = makeRandomID();
            motherID = makeRandomID();
            fillParents(fatherID, motherID, username, generations, userBirthYear);
        }
        else {
            fatherID = null;
            motherID = null;
        }

        LocationNameProcessor locationNameProcessor = new LocationNameProcessor();
        Location randLocation = locationNameProcessor.getRandLocation();
        try {
            //insert person
            PersonDao pDao = new PersonDao();
            pDao.insert(new Person(user.getPersonID(), user.getUserName(), user.getFirstName(),
                    user.getLastName(), user.getGender(), fatherID, motherID, null));
            numPersonsAdded++;

            //insert birth
            EventDao eDao = new EventDao();
            eDao.insert(new Event(makeRandomID(), user.getUserName(), user.getPersonID(),
                    randLocation.getLatitude(), randLocation.getLongitude(), randLocation.getCountry(),
                    randLocation.getCity(), "Birth", userBirthYear));
            numEventsAdded++;
        } catch (DataAccessException d) {
            return new ErrorResponse("error: fill failed");
        }

        //numPersonsAdded = (int) Math.pow(2, generations + 1) - 1;
        //numEventsAdded = (numPersonsAdded * 3) - 2;
        return new FillResponse("Successfully added " + numPersonsAdded +
                " persons and " + numEventsAdded + " events to the database");
    }

    private void fillParents(String maleID, String femaleID, String username, int generations, int birthYear) {
        generations -= 1;
        birthYear -= 30;

        String fatherID;
        String motherID;
        String father2ID;
        String mother2ID;
        if (generations > 0) {
            fatherID = makeRandomID();
            motherID = makeRandomID();
            father2ID = makeRandomID();
            mother2ID = makeRandomID();

            fillParents(fatherID, motherID, username, generations, birthYear);
            fillParents(father2ID, mother2ID, username, generations, birthYear);
        }
        else {
            fatherID = null;
            motherID = null;
            father2ID = null;
            mother2ID = null;
        }

        LocationNameProcessor locationNameProcessor = new LocationNameProcessor();
        String maleName = locationNameProcessor.getRandMName();
        String femaleName = locationNameProcessor.getRandFName();
        String surName = locationNameProcessor.getRandSName();
        Location randLocation;

        try {
            PersonDao pDao = new PersonDao();
            EventDao eDao = new EventDao();

            // insert the couple
            pDao.insert(new Person(maleID, username, maleName, surName, "m",
                    fatherID, motherID, femaleID));
            pDao.insert(new Person(femaleID, username, femaleName, surName, "f",
                    father2ID, mother2ID, maleID));
            numPersonsAdded += 2;

            // insert their birth events
            randLocation = locationNameProcessor.getRandLocation();
            eDao.insert(new Event(makeRandomID(), username, maleID, randLocation.getLatitude(),
                    randLocation.getLongitude(), randLocation.getCountry(), randLocation.getCity(),
                    "Birth", birthYear-1));
            randLocation = locationNameProcessor.getRandLocation();
            eDao.insert(new Event(makeRandomID(), username, femaleID, randLocation.getLatitude(),
                    randLocation.getLongitude(), randLocation.getCountry(), randLocation.getCity(),
                    "Birth", birthYear+1));

            // insert their marriage events (do these need to have the same eventID???)
            randLocation = locationNameProcessor.getRandLocation();
            eDao.insert(new Event(makeRandomID(), username, maleID, randLocation.getLatitude(),
                    randLocation.getLongitude(), randLocation.getCountry(), randLocation.getCity(),
                    "Marriage", birthYear+20));
            eDao.insert(new Event(makeRandomID(), username, femaleID, randLocation.getLatitude(),
                    randLocation.getLongitude(), randLocation.getCountry(), randLocation.getCity(),
                    "Marriage", birthYear+20));

            // insert their death events
            randLocation = locationNameProcessor.getRandLocation();
            eDao.insert(new Event(makeRandomID(), username, maleID, randLocation.getLatitude(),
                    randLocation.getLongitude(), randLocation.getCountry(), randLocation.getCity(),
                    "Death", birthYear+80));
            randLocation = locationNameProcessor.getRandLocation();
            eDao.insert(new Event(makeRandomID(), username, femaleID, randLocation.getLatitude(),
                    randLocation.getLongitude(), randLocation.getCountry(), randLocation.getCity(),
                    "Death", birthYear+85));
            numEventsAdded += 6;
        } catch (DataAccessException d) {
            d.printStackTrace();
            System.out.println("problem in fillParents");
        }
    }

    /** Clears all data from the database and then loads the posted user, person,
     *  and event data into the database
     * @param request contains all the necessary information for this method
     * @return a response body
     */
    public Response load(LoadRequest request) {
        Response clearResponse = clear();
        if (clearResponse.getClass() == ErrorResponse.class) {
            return new ErrorResponse("error: Load failed on Clear");
        }

        Event[] eventArray = request.getEvents();
        Person[] personArray = request.getPersons();
        User[] userArray = request.getUsers();
        int numEvents = 0;
        int numPersons = 0;
        int numUsers = 0;

        try {
            if (eventArray != null) {
                EventDao eDao = new EventDao();
                for (int i = 0; i < eventArray.length; i++) {
                    eDao.insert(eventArray[i]);
                    numEvents++;
                }
            }
            if (personArray != null) {
                PersonDao pDao = new PersonDao();
                for (int i = 0; i < personArray.length; i++) {
                    pDao.insert(personArray[i]);
                    numPersons++;
                }
            }
            if (userArray != null) {
                UserDao uDao = new UserDao();
                for (int i = 0; i < userArray.length; i++) {
                    uDao.insert(userArray[i]);
                    numUsers++;
                }
            }
        }
        catch (DataAccessException d) {
            d.printStackTrace();
            return new ErrorResponse("error: Load failed on load");
        }
        return new LoadResponse("Successfully added " + numUsers + " users, " +
                       numPersons + " persons, and " + numEvents + " events to the database.");
    }

    /** Returns the single Person object with the specified ID
     * @param personID the specified personID
     * @param authToken authToken for verification
     * @return a response body
     */
    public Response person(String personID, String authToken) {
        Person person;

        try {
            AuthTokenDao aDao = new AuthTokenDao();
            AuthToken aToken = aDao.find(authToken);
            if (aToken == null) {
                return new ErrorResponse("error: Invalid authToken");
            }

            PersonDao pDao = new PersonDao();
            person = pDao.find(personID);
            if (person == null) {
                return new ErrorResponse("error: Invalid personID");
            }

            //associated username from person table should match the one in the authToken table
            if (!person.getAssociatedUsername().equals(aToken.getAssociatedUsername())) {
                return new ErrorResponse("error: Requested person does not belong to this user");
            }
        }
        catch (DataAccessException d) {
            return new ErrorResponse("error: Person failed");
        }
        return new PersonResponse(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(),
                person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(),
                person.getSpouseID());
    }

    /** Returns ALL family members of the current user (determined from the authToken)
     * @param authToken authToken for verification
     * @return a response body
     */
    public Response person(String authToken) {
        Person[] persons;

        try {
            AuthTokenDao aDao = new AuthTokenDao();
            AuthToken aToken = aDao.find(authToken);
            if (aToken == null) {
                return new ErrorResponse("error: Invalid authToken");
            }

            PersonDao pDao = new PersonDao();
            persons = pDao.findAll(aToken.getAssociatedUsername());
        }
        catch (DataAccessException d) {
            return new ErrorResponse("error: Person failed");
        }
        return new PersonMultResponse(persons);
    }

    /** Returns the single Event object with the specified ID
     * @param eventID the specified eventID
     * @param authToken authToken for verification
     * @return a response body
     */
    public Response event(String eventID, String authToken) {
        Event event;

        try {
            AuthTokenDao aDao = new AuthTokenDao();
            AuthToken aToken = aDao.find(authToken);
            if (aToken == null) {
                return new ErrorResponse("error: Invalid authToken");
            }

            EventDao eDao = new EventDao();
            event = eDao.find(eventID);
            if (event == null) {
                return new ErrorResponse("error: Invalid eventID");
            }

            //associated username from person table should match the one in the authToken table
            if (!event.getAssociatedUsername().equals(aToken.getAssociatedUsername())) {
                return new ErrorResponse("error: Requested event does not belong to this user");
            }
        }
        catch (DataAccessException d) {
            return new ErrorResponse("error: Event failed");
        }
        return new EventResponse(event.getAssociatedUsername(),event.getEventID(), event.getPersonID(),
                event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(),
                event.getEventType(), event.getYear());
    }

    /** Returns ALL events for ALL family members of the current user (determined from the authToken)
      * @param authToken authToken for verification
     * @return a response body
     */
    public Response event(String authToken) {
        Event[] events;

        try {
            AuthTokenDao aDao = new AuthTokenDao();
            AuthToken aToken = aDao.find(authToken);
            if (aToken == null) {
                return new ErrorResponse("error: Invalid authToken");
            }

            EventDao eDao = new EventDao();
            events = eDao.findAll(aToken.getAssociatedUsername());
        }
        catch (DataAccessException d) {
            return new ErrorResponse("error: Person failed");
        }
        return new EventMultResponse(events);
    }

    private String makeRandomID() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        id = id.substring(0,8);

        return id;
    }
}
