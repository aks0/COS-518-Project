import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
            if (query.referencesColumns(subset.getColumns())) {
                totalCost += normalizedCost(query);
            }
        }
        
        return totalCost;
    }
    
    /**
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
     * Computes the size of all the tables corresponding to the given columns
     * Used in normalizedWeightedCost.
     * 
     * @param columns
     * @return size of all tables
     */
    private static double computeColumnsSize(Set<Column> columns) {
        double size = 0;
        for (Column column : columns) {
            size += column.getTable().getSize();
        }
        return size;
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
            //if query contains all the columns of the table subset
            if (query.referencesColumns(subset.getColumns())) {
                // size of tables in query
                double totalTablesSize =
                        computeColumnsSize(query.getReferencedColumns());
                // size of tables in the table subset
                double subsetTablesSize = computeColumnsSize(subset.getColumns());

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
        double columns = 0;
        double rows = 0;
        HashMap<Table, ArrayList<Column>> tablesToQueryColumns = Util518.newHashMap();
        
        // find all primary columns and tentative number of columns;
        // also fill in tablesToQueryColumns mapping
        for (Column column : query.getReferencedColumns()) {
            if (subset == null || !subset.getColumns().contains(column)) {
                columns++;
                
                if (!tablesToQueryColumns.containsKey(column.getTable())) {
                    tablesToQueryColumns.put(column.getTable(), new ArrayList<Column>());
                }
                tablesToQueryColumns.get(column.getTable()).add(column);
            }
        }
        
        // get number of cells in join of referenced tables with projections
        for (Table table : tablesToQueryColumns.keySet()) {
            for (Column column : tablesToQueryColumns.get(table)) {
                // find foreign columns that join with primary columns in query
                if (column.isForeignReference()
                    && query.getJoinedColumns().contains(new Pair<Column, Column>(column.getForeignKeyReference(), column))) {
                        columns--;
                    }
            }
            if (rows == 0) {
                // first table to fetch
                rows = table.getSize();
            } else {
                // scan through table for joins
                rows *= table.getSize();
            }
        }
        // one unit of cost per cell
        return rows * columns;
    }
    
    // test main
    public static void main(String[] args) {
		List<Table> tables = Table.getTablesFromModel("./data_models/data1.model");
        ArrayList<Query> queryList = QueryLib.getQueryList("query_logs/queries.sql");
        for (Query query : queryList) {
            System.out.println(CostEstimator.normalizedCost(query));
        }
		/*
        double baseline = 1;
        int maxSize = 10;
        ArrayList<TableSubset> subsets = TableSubsetProducer.produce(queryList, baseline, maxSize);
        HashSet<TableSubset> selectedSubsets = new HashSet<TableSubset>();
        for (Query query : queryList) {
            ArrayList<TableSubset> candidateSubsets = new ArrayList<TableSubset>();
            for (TableSubset subset : subsets) {
                if (query.referencesColumns(subset.getColumns())) {
                    candidateSubsets.add(subset);
                }
            }
            TableSubset bestSubset = CostEstimator.findBestSubset(query, candidateSubsets);
            selectedSubsets.add(bestSubset);
        }*/
    }
}
