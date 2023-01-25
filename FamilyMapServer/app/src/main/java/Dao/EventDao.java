package Dao;

import android.annotation.TargetApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Event;

/** EventDao Class */
public class EventDao {
    /** connection object conn */
    private Connection conn;
    private Database db;

    /** Constructor for EventDao */
    public EventDao() {
        db = new Database();
    }

    /** Inserts an event into the event table
     * @param event event to be inserted
     * @return true if the insert was successful
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean insert(Event event) throws DataAccessException {
        conn = db.openConnection();
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        db.closeConnection(true);
        return true;
    }

    /** finds and returns an event given an eventID
     * @param eventID ID matching the event to be found
     * @return the found event
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public Event find(String eventID) throws DataAccessException {
        conn = db.openConnection();
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                db.closeConnection(false);
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding event");
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

    /** return all events associated with the specified person
     * @param username the username for the specified user
     * @return an array of all the events for the person
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public Event[] findAll(String username) throws DataAccessException {
        conn = db.openConnection();
        ArrayList<Event> events = new ArrayList<>();
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                events.add(event);
            }
            if (events.size() > 0) {
                db.closeConnection(false);
                Event[] eventArray = new Event[events.size()];
                for (int i = 0; i < events.size(); i++) {
                    eventArray[i] = events.get(i);
                }
                return eventArray;
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

    /** clears the Event table
     * @return true if the table was cleared
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean clear() throws DataAccessException {
        conn = db.openConnection();
        String sql = "DELETE FROM Events;";
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing events table");
        }
        db.closeConnection(true);
        return true;
    }

    @TargetApi(19)
    public void clearUserData(String username) throws DataAccessException {
        conn = db.openConnection();
        String sql = "DELETE FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing user data from events table");
        }
        db.closeConnection(true);
    }
}
