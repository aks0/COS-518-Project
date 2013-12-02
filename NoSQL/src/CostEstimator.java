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
	 * Fully normalized cost of query (assuming no indices, denormalization, etc)
	 * 
	 * Cost model:
	 * Charges one unit of cost per cell in the cross join (worst case) of the
	 * tables referenced by a query where projected columns are those 
	 * referenced by the query.
	 * 
	 * @param query
	 * @return cost
	 */
	public static double normalizedCost(Query query) {
        HashSet<Table> seenTables = new HashSet<Table>();
        
        // get number of cells in cross join of referenced tables with projections
        int columns = query.getReferencedColumns().size();
        int rows = 0;
        for (Column column : query.getReferencedColumns()) {
            Table referencedTable = column.getTable();
            if (!seenTables.contains(referencedTable)) {
                seenTables.add(referencedTable);
                if (rows == 0) {
                    // first table to fetch
                    rows = referencedTable.getSize();
                } else {
                    // scan through next table for joins
                    rows *= referencedTable.getSize();
                }
            }
        }
        // one unit of cost per cell
        return rows * columns;
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
	 * TODO: Discuss a cost model
	 * Fully normalized cost of query (assuming no indices, denormalization, etc)
	 * 
	 * Cost model:
	 * Charges one unit of cost per cell in the cross join (worst case) of the
	 * tables referenced by a query where projected column are those that 
	 * are not in the provided table subset.
	 * 
	 * @param query
	 * @param subset
	 * @return cost
	 */
	public static double denormalizedCost(Query query, TableSubset subset) {
        HashSet<Table> seenTables = new HashSet<Table>();

        // get number of cells in cross join of referenced tables with projections
        // omit projection of columns that are in subset
        int columns = 0;
        int rows = 0;
        for (Column column : query.getReferencedColumns()) {
            // project column if it is not in subset
            if (!subset.getColumns().contains(column)) {
                columns++;
                Table referencedTable = column.getTable();
                if (!seenTables.contains(referencedTable)) {
                    seenTables.add(referencedTable);
                    if (rows == 0) {
                        // first table to fetch
                        rows = referencedTable.getSize();
                    } else {
                        // scan through next table for joins
                        rows *= referencedTable.getSize();
                    }
                }
            }
        }
        // one unit of cost per cell
        // may also subtract a multiple of size of subset (as maintenance/storage cost) here
        return rows * columns;
	}
}
