package Dao;

import android.annotation.TargetApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.AuthToken;

/** AuthTokenDao class */
public class AuthTokenDao {
    /** connection object conn */
    private Connection conn;
    private Database db;

    /** Constructor for AuthTokenDao */
    public AuthTokenDao() {
        db = new Database();
    }

    /** Inserts an authToken into the Auth table
     * @param authToken authtoken to be inserted
     * @return true if the insert was successful
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean insert(AuthToken authToken) throws DataAccessException {
        conn = db.openConnection();
        String sql = "INSERT INTO AuthTokens (AssociatedUsername, AuthToken) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAssociatedUsername());
            stmt.setString(2, authToken.getAuthToken());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        db.closeConnection(true);
        return true;
    }

    /** finds and returns an event given an eventID
     * @param aToken authtoken matching the AuthToken to be found
     * @return the found AuthToken
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public AuthToken find(String aToken) throws DataAccessException {
        conn = db.openConnection();
        AuthToken authToken;
        ResultSet rs = null;

        String sql = "SELECT * FROM AuthTokens WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aToken);
            rs = stmt.executeQuery();

            if (rs.next()) {
                authToken = new AuthToken(rs.getString("AssociatedUsername"),
                        rs.getString("AuthToken"));
                db.closeConnection(false);
                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new DataAccessException("Error encountered while finding authToken");
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

    /** clears the authToken table
     * @return true if the table was cleared
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public boolean clear() throws DataAccessException {
        conn = db.openConnection();

        String sql = "DELETE FROM AuthTokens;";
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
