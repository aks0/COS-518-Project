import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CostEstimator {
    public static double STORAGE_PENALTY = 1.0;
    public static double NOT_IN_SUBSET_PENALTY = 1.0;
    public static double FINAL_JOIN_PENALTY = 1.0; 
    
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
            // filter subsets that you can get from one table
            HashSet<Table> subsetTables = new HashSet<Table>();
            for (Column column : subset.getColumns()) {
                subsetTables.add(column.getTable());
            }
            if (subsetTables.size() == 1) continue;
            double cost = denormalizedCost(query, subset);
            System.out.println(subset + " " + cost);
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
    public static double denormalizedCost(Query query, TableSubset subset) {
        HashMap<Table, HashSet<Column>> tablesToQueryColumnsInSubset = Util518.newHashMap();
        HashMap<Table, HashSet<Column>> tablesToQueryColumnsNotInSubset = Util518.newHashMap();
        
        // fill in tablesToQueryColumns mappings
        for (Column column : query.getReferencedColumns()) {
            HashMap<Table, HashSet<Column>> tablesToQueryColumns;
            // if query column is not in subset
            if (subset == null || !subset.getColumns().contains(column)) {
                tablesToQueryColumns = tablesToQueryColumnsNotInSubset;
            } else {
                // query column is in subset
                tablesToQueryColumns = tablesToQueryColumnsInSubset;
            }
            // add to mapping
            if (!tablesToQueryColumns.containsKey(column.getTable())) {
                tablesToQueryColumns.put(column.getTable(), new HashSet<Column>());
            }
            tablesToQueryColumns.get(column.getTable()).add(column);
        }
        
        HashSet<Column> columnsInSubset = new HashSet<Column>();
        for (HashSet<Column> value : tablesToQueryColumnsInSubset.values()) {
            columnsInSubset.addAll(value);
        }
        HashSet<Column> columnsNotInSubset = new HashSet<Column>();
        for (HashSet<Column> value : tablesToQueryColumnsNotInSubset.values()) {
            columnsNotInSubset.addAll(value);
        }
        Column joinedForeignColumn = searchForEquijoin(columnsInSubset, columnsNotInSubset, query);
        
        Pair<Double, Double> inSubsetDimensions = calculateJoinDimensions(tablesToQueryColumnsInSubset, query); 
        Pair<Double, Double> notInSubsetDimensions = calculateJoinDimensions(tablesToQueryColumnsNotInSubset, query); 
        
        double rowsInSubsetJoin = inSubsetDimensions.getFirst();
        double columnsInSubsetJoin = inSubsetDimensions.getSecond();
        double rowsNotInSubsetJoin = notInSubsetDimensions.getFirst();
        double columnsNotInSubsetJoin = notInSubsetDimensions.getSecond();
        double cost = STORAGE_PENALTY * (rowsInSubsetJoin * columnsInSubsetJoin);
        cost += NOT_IN_SUBSET_PENALTY * (rowsNotInSubsetJoin * columnsNotInSubsetJoin);
        if (joinedForeignColumn == null) {
            cost += FINAL_JOIN_PENALTY * (rowsInSubsetJoin * rowsNotInSubsetJoin);
        } else if (columnsInSubset.contains(joinedForeignColumn)) {
            cost += FINAL_JOIN_PENALTY * rowsInSubsetJoin;
        } else if (columnsNotInSubset.contains(joinedForeignColumn)){
            cost += FINAL_JOIN_PENALTY * rowsNotInSubsetJoin;
        }
        return cost;
    }
   
    /**
     * Determine if the two sets of columns provided can equijoin. If so
     * return the foreign column in the join.  Otherwise return null
     * @param tablesToColumns
     * @param otherTablesToColumns
     * @param query
     * @return Column or null
     */
    private static Column searchForEquijoin(HashSet<Column> columns,
            HashSet<Column> otherColumns, Query query) {
        
        Column foreignColumn = null;
        for (Column column : columns) {
            if (foreignColumn != null) break;
            // find foreign columns that join with primary columns in other list
            if (column.isForeignReference()
                    && query.getEquijoinedColumns().contains(column)
                    && query.getEquijoinedColumns().contains(column.getForeignKeyReference())
                    && otherColumns.contains(column.getForeignKeyReference())) {
                foreignColumn = column;
            }
        }
        // other way around
        for (Column column : otherColumns) {
            if (foreignColumn != null) break;
            if (column.isForeignReference()
                    && query.getEquijoinedColumns().contains(column)
                    && query.getEquijoinedColumns().contains(column.getForeignKeyReference())
                    && columns.contains(column.getForeignKeyReference())) {
                foreignColumn = column;
            }
        }
        return foreignColumn;
    }
    
    /**
     * Calculates the number of rows and columns in the join of the columns
     * in tablesToColumns based on query.
     * @param tablesToColumns
     * @param query
     * @return rows, columns pair
     */
    private static Pair<Double, Double> calculateJoinDimensions(
            HashMap<Table, HashSet<Column>> tablesToColumns, Query query) {
        double columns = 0.0;
        double rows = 0.0;
        
        HashSet<Column> allColumns = new HashSet<Column>();
        for (HashSet<Column> value : tablesToColumns.values()) {
            allColumns.addAll(value);
        }
        
        // find primary columns that join with foreign columns in query
        HashSet<Column> joinedPrimaryColumns = new HashSet<Column>();
        for (Column column : allColumns) {
            if (column.isForeignReference()
                    && query.getEquijoinedColumns().contains(column)
                    && query.getEquijoinedColumns().contains(column.getForeignKeyReference())
                    && allColumns.contains(column.getForeignKeyReference())) {
                joinedPrimaryColumns.add(column.getForeignKeyReference());
            }
        }
        
        // get number of rows and columns in join of referenced tables with projections
        for (Table table : tablesToColumns.keySet()) {
            boolean skipCost = false;
            for (Column column : tablesToColumns.get(table)) {
                if (column.isPrimary() && joinedPrimaryColumns.contains(column)) {
                    skipCost = true;
                } 
                columns++;
            }
            
            // increase cost with foreign columns and cross joined columns
            if (!skipCost) {
                if (rows == 0.0) {
                    // first table to fetch
                    rows = table.getSize();
                } else {
                    // scan through table for joins
                    rows *= table.getSize();
                }
            } else {
                columns--;
            }
        }
        return new Pair<Double, Double>(rows, columns);
    }
    
    // test main
    public static void main(String[] args) {
        List<Table> tables = Table.getTablesFromModel("./data_models/data1.model");
        ArrayList<Query> queryList = QueryLib.getQueryList("query_logs/queries.sql");
        for (Query query : queryList) {
            System.out.println(CostEstimator.normalizedCost(query));
        }
    }
}
