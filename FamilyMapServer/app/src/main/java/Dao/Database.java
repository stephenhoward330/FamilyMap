package Dao;

import android.annotation.TargetApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Database class */
public class Database {
    /** connection object conn */
    private Connection conn;

    /** constructor for the database */
    public Database() {
    }

    /** opens the connection with the SQL database and returns the connection object
     * @return the connection
     * @throws DataAccessException if error occurs during data access
     */
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:database.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    /** gets the connection object
     * @return the connection
     * @throws DataAccessException if error occurs during data access
     */
    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    /** closes the connection with the SQL database
     * @param commit if we have changes to commit
     * @throws DataAccessException if error occurs during data access
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /** creates the 4 tables in the database
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public void createTables() throws DataAccessException {
        openConnection();
        try (Statement stmt = conn.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS Events " +
                    "(" +
                    "EventID text not null unique, " +
                    "AssociatedUsername text not null, " +
                    "PersonID text not null, " +
                    "Latitude float not null, " +
                    "Longitude float not null, " +
                    "Country text not null, " +
                    "City text not null, " +
                    "EventType text not null, " +
                    "Year int not null, " +
                    "primary key (EventID), " +
                    "foreign key (AssociatedUsername) references Users(Username), " +
                    "foreign key (PersonID) references Persons(PersonID)" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS Persons " +
                    "(" +
                    "PersonID text not null unique, " +
                    "AssociatedUsername text not null, " +
                    "FirstName text not null, " +
                    "LastName text not null, " +
                    "Gender text not null, " +
                    "FatherID text, " +
                    "MotherID text, " +
                    "SpouseID text, " +
                    "primary key (PersonID), " +
                    "foreign key (AssociatedUsername) references Users(Username) " +
                    ")";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS Users " +
                    "(" +
                    "Username text not null unique, " +
                    "Password text not null, " +
                    "Email text not null, " +
                    "FirstName text not null, " +
                    "LastName text not null, " +
                    "Gender text not null, " +
                    "PersonID text not null unique, " +
                    "primary key (Username), " +
                    "foreign key (PersonID) references Persons(PersonID) " +
                    ")";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS AuthTokens" +
                    "(" +
                    "AssociatedUsername text not null, " +
                    "AuthToken text not null unique, " +
                    "primary key (AuthToken), " +
                    "foreign key (AssociatedUsername) references Users(Username) " +
                    ")";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
        closeConnection(true);
    }

    /** deletes the 4 tables from the database
     * @throws DataAccessException if error occurs during data access
     */
    @TargetApi(19)
    public void clearTables() throws DataAccessException {
        openConnection();
        try (Statement stmt = conn.createStatement()){
            System.out.println("trying to delete...");
            String sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Users";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM AuthTokens";
            stmt.executeUpdate(sql);
            System.out.println("all deleted");
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
        closeConnection(true);
    }
}
