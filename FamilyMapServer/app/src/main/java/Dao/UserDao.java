package Dao;

import android.annotation.TargetApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.User;

/** UserDao class */
public class UserDao {
    /** connection object conn */
    private Connection conn;
    private Database db;

    /** Constructor for UserDao */
    public UserDao() {
        db = new Database();
    }

    /** Inserts a user into the user table
     * @param user user to be inserted
     * @return true if the insert was successful
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean insert(User user) throws DataAccessException {
        conn = db.openConnection();
        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        db.closeConnection(true);
        return true;
    }

    /** finds and returns a user given a username
     * @param username username matching the user to be found
     * @return the found event
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public User find(String username) throws DataAccessException {
        conn = db.openConnection();
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                db.closeConnection(false);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding user");
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

    /** clears the User table
     * @return true if the table was cleared
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean clear() throws DataAccessException {
        conn = db.openConnection();
        String sql = "DELETE FROM Users;";
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing users table");
        }
        db.closeConnection(true);
        return true;
    }
}
