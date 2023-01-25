package Dao;

/** DataAccessException exception class */
public class DataAccessException extends Exception {
    /** returns the exception
     * @param message explains the exception
     */
    DataAccessException(String message) {
        super(message);
    }

    /** returns the exception */
    DataAccessException() {
        super();
    }
}
