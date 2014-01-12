package sqlfire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                .getTablesFromModel("data_models/tpc-h.model");
        ArrayList<Query> queries = QueryLib.getQueryList("query_logs/onefilequeries.sql");
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            double distributedCost = 0;
            for (Query query : queries) {
                try {
                    dbManager.sendQuery(query);
                } catch (SQLException e) {
                    distributedCost += dbManager.handleError(query, e);
                }
            }
            System.out.println("Total Distributed Cost: " + distributedCost);
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
    }
}