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
    private static final int COST_MODE = 1;
    private static final int TIME_MODE = 2;
    
    public void run(int mode) {
        List<Table> tables = Table
                .getTablesFromModel("data_models/tpc-h.model");
        ArrayList<Query> queries = QueryLib.getQueryList("query_logs/queries-degree-non-modified.sql");
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            double distributedCost = 0;
            long startTime = System.nanoTime();
            for (Query query : queries) {
                try {
                    ResultSet results = dbManager.sendQuery(query);
                    results.close();
                } catch (SQLException e) {
                    if (mode == COST_MODE) {
                        distributedCost += dbManager.handleErrorWithCost(query);
                    } else if (mode == TIME_MODE) {
                        dbManager.handleErrorWithTime(query);
                    }
                }
            }
            
            if (mode == COST_MODE) {
                System.out.println("Total Distributed Cost: " + distributedCost);
            } else if (mode == TIME_MODE) {
                long endTime = System.nanoTime();
                System.out.println("Total execution time: " + (endTime - startTime) / 1000000000L );
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
