import java.util.ArrayList;
import java.util.HashMap;


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
				subsetRelevant = subsetRelevant && query.getSelectColumns().contains(column);
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
				subsetRelevant = subsetRelevant && query.getSelectColumns().contains(column);
			}
			
			if (subsetRelevant) {
				HashMap<Table, Boolean> tables = new HashMap<Table, Boolean>();
				
				// Get Common tables in query
				for (Column column : query.getSelectColumns()) {
					if (!tables.containsKey(column.getTable())) {
						tables.put(column.getTable(), true);
					}
				}
				
				double totalTablesSize = 0.0;
				// Add up size of tables
				for (Table table : tables.keySet()) {
					totalTablesSize += table.getSize();
				}
				
				tables.clear();
				
				// Get Common tables in table subset
				for (Column column : subset.getColumns()) {
					if (!tables.containsKey(column.getTable())) {
						tables.put(column.getTable(), true);
					}
				}
				
				double subsetTablesSize = 0.0;
				// Add up size of tables
				for (Table table : tables.keySet()) {
					subsetTablesSize += table.getSize();
				}
				
				totalCost += (subsetTablesSize/totalTablesSize) * normalizedCost(query);
			}
		}
		
		return totalCost;
	}
}
