package colocation;

import java.util.HashMap;
import java.util.List;

import materializedViews.Column;
import materializedViews.CostEstimator;
import materializedViews.Pair;
import materializedViews.Query;
import materializedViews.Table;
import materializedViews.TableSubset;
import materializedViews.Util518;


public class EntityProducer {
	public static void produce(List<Table> tables, List<Query> queries, double computationMemoryRatio) {
		HashMap<Pair<String, String>, Double> costMap = Util518.newHashMap();
		
		for (Table table1 : tables) {
			for (Table table2 : tables) {
				Table foreignKeyTable;
				if (!table1.equals(table2) && (foreignKeyTable = Table.equiJoined(table1, table2)) != null) {
					TableSubset subset = new TableSubset();
					for (Column column : table1.getColumns()) {
						subset.addColumn(column);
					}
					
					for (Column column : table2.getColumns()) {
						subset.addColumn(column);
					}
					
					double cost = 0.0;
					for (Query query : queries) {
						cost += CostEstimator.denormalizedCost(query, subset);
					}
					
					if (foreignKeyTable.equals(table1))
						costMap.put(new Pair<String, String>(table1.getName(), table2.getName()), cost);
					else
						costMap.put(new Pair<String, String>(table2.getName(), table1.getName()), cost);
						
				}
			}
		}
		
		
	}
	
}
