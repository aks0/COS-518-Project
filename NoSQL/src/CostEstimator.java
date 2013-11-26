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
			}
			
			if (subsetRelevant) {
				totalCost += normalizedCost(query);
			}
		}
		
		return totalCost;
	}
	
	/**
	 * TODO: Write this - fully normalized cost of query (assuming no indices, denormalization, etc)
	 * 
	 * @param query
	 * @return
	 */
	public static double normalizedCost(Query query) {
	    return 0.0;
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
}
