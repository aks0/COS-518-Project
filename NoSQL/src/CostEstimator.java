import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class CostEstimator {
    /**
     * Normalized cost of all queries which involve subset
     * 
     * @param subset
     * @param queries
     * @return
     */
    public static double normalizedCost(TableSubset subset, ArrayList<Query> queries) {
        double totalCost = 0.0;
        
        for (Query query : queries) {
            boolean subsetRelevant = true;
            for (Column column : subset.getColumns()) {
                subsetRelevant = subsetRelevant && query.getReferencedColumns().contains(column);
                if (!subsetRelevant) {
                    break;
                }
            }
            
            if (subsetRelevant) {
                totalCost += normalizedCost(query);
            }
        }
        
        return totalCost;
    }
    
    /**
     * TODO: Discuss a cost model
     * Find normalized cost of query (assuming no indices, denormalization, etc)
     * 
     * Cost model:
     * Charges one unit of cost per cell in the join of the
     * tables referenced by a query where projected columns 
     * are those referenced by the query.
     * 
     * @param query
     * @return cost
     */
    public static double normalizedCost(Query query) {
        // normalized cost is denormalized cost without table subset
        return denormalizedCost(query, null);
    }
    
    /**
     * Weighted normalized cost of all queries which involve subset
     * 
     * @param subset
     * @param queries
     * @return
     */
    public static double normalizedWeightedCost(TableSubset subset, ArrayList<Query> queries) {
        double totalCost = 0.0;
        
        for (Query query : queries) {
            boolean subsetRelevant = true;
            for (Column column : subset.getColumns()) {
                subsetRelevant = subsetRelevant && query.getReferencedColumns().contains(column);
                if (!subsetRelevant) {
                    break;
                }
            }
            
            if (subsetRelevant) {
                HashSet<Table> tables = new HashSet<Table>();
                
                // Get Common tables in query
                for (Column column : query.getReferencedColumns()) {
                    tables.add(column.getTable());
                }
                
                double totalTablesSize = 0.0;
                // Add up size of tables
                for (Table table : tables) {
                    totalTablesSize += table.getSize();
                }
                
                tables.clear();
                
                // Get Common tables in table subset
                for (Column column : subset.getColumns()) {
                    tables.add(column.getTable());
                }
                
                double subsetTablesSize = 0.0;
                // Add up size of tables
                for (Table table : tables) {
                    subsetTablesSize += table.getSize();
                }
                
                totalCost += (subsetTablesSize/totalTablesSize) * normalizedCost(query);
            }
        }
        
        return totalCost;
    }
    
    /**
     * Cost based pruner
     * Selects the best table subset from a provided list of subsets proposed for the given query
     * based on minimum denormalized cost
     * 
     * @param query
     * @param proposedSubsets
     * @return best subset
     */
    public static TableSubset findBestSubset(Query query, ArrayList<TableSubset> proposedSubsets) {
        double minCost = Double.POSITIVE_INFINITY;
        TableSubset bestSubset = null;
        for (TableSubset subset : proposedSubsets) {
            double cost = denormalizedCost(query, subset);
            if (cost < minCost) {
                minCost = cost;
                bestSubset = subset;
            }
        }
        return bestSubset;
    }
    
    /**
     * TODO: Discuss a cost model
     * Helper function to findBestSubset()
     * Find denormalized cost of query with respect to provided table subset
     * 
     * Cost model:
     * Charges one unit of cost per cell in the join of the
     * tables referenced by a query where projected column are those that 
     * are not in the provided table subset.
     * 
     * @param query
     * @param subset
     * @return cost
     */
    private static double denormalizedCost(Query query, TableSubset subset) {
        int columns = 0;
        int rows = 0;
        HashMap<Table, ArrayList<Column>> tablesToQueryColumns = new HashMap<Table, ArrayList<Column>>();
        HashSet<Column> primaryColumns = new HashSet<Column>();
        
        // find all primary columns and tentative number of columns;
        // also fill in tablesToQueryColumns mapping
        for (Column column : query.getReferencedColumns()) {
            if (subset == null || !subset.getColumns().contains(column)) {
                if (column.isPrimary()) {
                    primaryColumns.add(column);
                }
                columns++;
                
                if (!tablesToQueryColumns.containsKey(column.getTable())) {
                    tablesToQueryColumns.put(column.getTable(), new ArrayList<Column>());
                }
                tablesToQueryColumns.get(column.getTable()).add(column);
            }
        }
        
        // get number of cells in join of referenced tables with projections
        for (Table table : tablesToQueryColumns.keySet()) {
            boolean mustCrossJoin = true;
            for (Column column : tablesToQueryColumns.get(table)) {
                // find foreign columns that join with primary columns in query
                if (column.isForeignReference() 
                        && primaryColumns.contains(column.getForeignKeyReference())) {
                    // don't have to cross join
                    mustCrossJoin = false;
                    columns--;
                }
            }
            
            // cross join worst case
            if (mustCrossJoin) {
                if (rows == 0) {
                    // first table to fetch
                    rows = table.getSize();
                } else {
                    // scan through table for joins
                    rows *= table.getSize();
                }
            }
        }
        // one unit of cost per cell
        return rows * columns;
    }
}
