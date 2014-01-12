package sqlfire;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    /**
     * @throws SQLException
     */
    public void sendQuery(String query) throws SQLException {
        statement.executeQuery(query); 
    }
    
    public double handleError(Query query, SQLException e) {
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
        System.out.println();
        return totalCost;
        /*String error = e.getMessage();
        ArrayList<String> tables = extractBetweenDelimiters(error, "(.*?)", " 'APP\\.", "(\\[|')");
        if (tables.size() == 2) {
            // distributed join
            Table table1 = Table.getInstance(tables.get(0).toLowerCase());
            Table table2 = Table.getInstance(tables.get(1).toLowerCase());

            // I/O's for fetching rows of both tables
            long cost = (long)table1.getSize() + (long)table2.getSize();
            // I/O's for join, assume no index?
            cost += (long)table1.getSize() * (long)table2.getSize();
            System.out.println(table1.getName() + "X" + table2.getName() + " Cost: " + cost);
            return cost;
        }*/
    }
    
    private String constructJoinQuery(Column column1, Column column2) {
        String query = "select * from " + column1.getTable() + " , " + column2.getTable() 
                + " where " + column1.getTable() + "." + column1.getName() + " = "
                + column2.getTable() + "." + column2.getName() + ";";
        return query;
    }
    
    /**
     * Extracts a substring of a specified format that is between a specified
     * start substring delimiter and end substring delimiter.
     * @param content - String to extract substring from
     * @param formatOfExtract - String describing regex format of the substring to extract
     * @param start - start substring delimiter in regex format
     * @param end - end substring delimiter in regex format
     * @return String for extracted substring
     */
    public static ArrayList<String> extractBetweenDelimiters(String content, String formatOfExtract,
            String start, String end) {
        ArrayList<String> matches = Util518.newArrayList();
        // Find the number of groups in start
        Pattern pattern = Pattern.compile(start);
        Matcher matcher = pattern.matcher("");
        // the group number of the extract is 1 plus the number of groups in start
        int groupNumOfExtract = matcher.groupCount() + 1;
        
        pattern = Pattern.compile(start + formatOfExtract  + "\\s*" + end);
        matcher = pattern.matcher(content);
        while (matcher.find()) {
            // return what matches between the parentheses of pattern
            matches.add(matcher.group(groupNumOfExtract));
        }
        return matches;
    }

    
    public static void main(String[] args) {
        new DatabaseClient().run();
    }
}
