package sqlfire;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.Joinable;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JoinRowSetImpl;

import materializedViews.Column;
import materializedViews.Pair;
import materializedViews.Query;
import materializedViews.Table;
import materializedViews.Util518;

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
       statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

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
    public ResultSet sendQuery(Query query) throws SQLException {
        return statement.executeQuery(query.getStatement()); 
    }
    
    /**
     * @throws SQLException
     */
    public ResultSet sendQuery(String query) throws SQLException {
        return statement.executeQuery(query); 
    }
    
    /**
     * @throws SQLException
     */
    public ResultSet sendQuery(String query, Statement additionalStatement) throws SQLException {
        return additionalStatement.executeQuery(query); 
    }
    
    public double handleErrorWithCost(Query query) {
        double totalCost = 0.0;
        for (Pair<Column, Column> join : query.getEquijoinedColumns()) {
            Column column1 = join.getFirst();
            Column column2 = join.getSecond();
            String joinQuery = constructJoinQuery(column1, column2);
			double cost = 0.0;
            try {
                sendQuery(joinQuery);
            } catch (SQLException error) {
                Table table1 = Table.getInstance(column1.getTable().getName().toLowerCase());
                Table table2 = Table.getInstance(column2.getTable().getName().toLowerCase());

                // I/O's for fetching rows of both tables
                cost = (double)table1.getSize() + (double)table2.getSize();
                // I/O's for join, assume no index?
                cost += (double)table1.getSize() * (double)table2.getSize();
                System.out.println(table1.getName() + "X" + table2.getName() + " Cost: " + cost);
            }
			totalCost += cost;
        }
        return totalCost;
    }
    
    public void handleErrorWithTime(Query query) {
        for (Pair<Column, Column> join : query.getEquijoinedColumns()) {
            Column column1 = join.getFirst();
            Column column2 = join.getSecond();
            String joinQuery = constructJoinQuery(column1, column2);
            try {
                ResultSet results = sendQuery(joinQuery);
                results.close();
            } catch (SQLException error) {
                Table table1 = Table.getInstance(column1.getTable().getName().toLowerCase());
                Table table2 = Table.getInstance(column2.getTable().getName().toLowerCase());
                System.out.println(table1.getName() + "X" + table2.getName() + " joined");
                
                // fetch both tables
                String selectQuery1 = constructSelectQuery(table1, query.getTableToColumns().get(table1));
                String selectQuery2 = constructSelectQuery(table2, query.getTableToColumns().get(table2));
                try {
                    ResultSet result1 = sendQuery(selectQuery1);
                    // execute join
                    Statement additionalStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet result2 = sendQuery(selectQuery2, additionalStatement);
                    while (result1.next()) {
                        int key1 = result1.getInt(column1.getName());
                        while (result2.next()) {
                            int key2 = result2.getInt(column2.getName());
                            if (key1 == key2) {
                                //match
                                ;
                            }
                        }
                        result2.beforeFirst();
                    }
                    result1.close();
                    result2.close();
                    additionalStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println();
    }
    
    private String constructJoinQuery(Column column1, Column column2) {
        String query = "select * from " + column1.getTable() + " , " + column2.getTable() 
                + " where " + column1.getTable() + "." + column1.getName() + " = "
                + column2.getTable() + "." + column2.getName() + ";";
        return query;
    }
    
    private String constructSelectQuery(Table table, HashSet<Column> columns) {
        String query = "select ";
        int columnNum = 1;
        for (Column column : columns) {
            query += column.getName();
            if (columnNum++ != columns.size()) {
                query += ", ";
            }
        }
        query += " from " + table.getName() + ";"; 
        return query;
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Must supply 1 or 2");
        } else {
            new DatabaseClient().run(Integer.parseInt(args[0]));
        }
    }
}
