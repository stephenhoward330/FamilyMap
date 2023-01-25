package Dao;

import android.annotation.TargetApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Person;

/** PersonDao class */
public class PersonDao {
    /** connection object conn */
    private Connection conn;
    private Database db;

    /** Constructor for PersonDao */
    public PersonDao() {
        db = new Database();
    }

    /** Inserts a person into the person table
     * @param person person to be inserted
     * @return true if the insert was successful
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean insert(Person person) throws DataAccessException {
        conn = db.openConnection();
        String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        db.closeConnection(true);
        return true;
    }

    /** finds and returns a person given a personID
     * @param personID ID matching the event to be found
     * @return the found person
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public Person find(String personID) throws DataAccessException {
        conn = db.openConnection();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                db.closeConnection(false);
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        db.closeConnection(false);
        return null;
    }

    /** return all persons associated with the specified user
     * @param username the username for the specified user
     * @return an array of all the persons associated with the person
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public Person[] findAll(String username) throws DataAccessException {
        conn = db.openConnection();
        ArrayList<Person> persons = new ArrayList<>();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                persons.add(person);
            }
            if (persons.size() > 0) {
                db.closeConnection(false);
                Person[] personArray = new Person[persons.size()];
                for (int i = 0; i < persons.size(); i++) {
                    personArray[i] = persons.get(i);
                }
                return personArray;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding persons");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        db.closeConnection(false);
        return null;
    }

    /** clears the Person table
     * @return true if the table was cleared
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean clear() throws DataAccessException {
        conn = db.openConnection();
        String sql = "DELETE FROM Persons;";
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing persons table");
        }
        db.closeConnection(true);
        return true;
    }

    @TargetApi(19)
    public void clearUserData(String username) throws DataAccessException {
        conn = db.openConnection();
        String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing user data from persons table");
        }
        db.closeConnection(true);
    }
}
