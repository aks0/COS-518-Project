package sqlfire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.mail.*;

import materializedViews.Query;
import materializedViews.QueryLib;
import materializedViews.Table;

/**
 * Client thread.
 * @author kevil
 */
public class DatabaseClient extends Thread {
    
    @Override
    public void run() {
        List<Table> tables = Table
                .getTablesFromModel("./data_models/tpc-h.model");
        ArrayList<Query> queries = QueryLib.getQueryList("query_logs/onefilequeries.sql");
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            // Insert each datum into the database
            for (Query query : queries) {
                try {
                    dbManager.sendQuery(query);
                } catch (SQLException e) {
                    dbManager.handleError(query, e);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }
}