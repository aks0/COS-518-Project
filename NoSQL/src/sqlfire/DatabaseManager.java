package sqlfire;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import materializedViews.Query;

/**
 * Class for managing the database.
 * @author kevil
 */
public class DatabaseManager {
    // Driver for database, this is for psql, but you can replace it with any database driver
    private static final String JDBC_DRIVER = "com.vmware.sqlfire.jdbc.ClientDriver";
    // URL for postgresql
    private static final String URL = "jdbc:sqlfire://10.1.1.2:1527/";
    
    private Connection connection;
    private Statement statement;
    
    /**
     * Constructor
     * Initiates connection to database and sets up
     * tables for orders and items if necessary.
     * @throws SQLException
     */
    public DatabaseManager() throws SQLException {
        try {
           // register driver
           Class.forName(JDBC_DRIVER);
           Properties properties = new Properties();
           
           // setup
           initConnection(properties);
        } catch(SQLException e) {
           // errors for JDBC
           throw e;
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
    
    /**
     * Initiates the connection to the database using login specified by
     * passed in properties.
     * @param properties - Properties object with username and password set
     * @throws SQLException
     */
    private void initConnection(Properties properties) throws SQLException {
       // open connection
       connection = DriverManager.getConnection(URL, properties);
       // keep around a statement for interacting with the database with
       statement = connection.createStatement();

    }
    
    /**
     * Close the DatabaseManager by closing all interactions with the database.
     */
    public void close() {
        try {
            connection.close();
            statement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @throws SQLException
     */
    public void sendQuery(Query query) throws SQLException {
        statement.executeQuery(query.getStatement()); 
    }
    
    public void handleError(Query query, SQLException e) {
        System.out.println(e);
    }
    
    public static void main(String[] args) {
        
    }
}
