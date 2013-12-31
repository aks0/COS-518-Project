package colocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import materializedViews.Column;
import materializedViews.CostEstimator;
import materializedViews.Query;
import materializedViews.QueryLib;
import materializedViews.Table;
import materializedViews.TableSubset;
import materializedViews.Util518;


public class EntityProducer {
    
	public static void produce(List<Table> tables, List<Query> queries) {
		HashMap<TablePair, Double> costMap = Util518.newHashMap();
		
		// Iterate through every possible pair of tables
		for (int i = 0; i < tables.size(); ++i) {
			Table table1 = tables.get(i);
			for (int j = i+1; j < tables.size(); ++j) {
				Table table2 = tables.get(j);
				
				Table parentTable;
				// If a natural join exists between the pair of tables...
				if ((parentTable = Table.findParentTable(table1, table2)) != null) {
					// Create the join in terms of table subset
					TableSubset subset = new TableSubset();
					for (Column column : table1.getColumns()) {
						subset.addColumn(column);
					}
					
					for (Column column : table2.getColumns()) {
						subset.addColumn(column);
					}
					
					// Compute the denormalized cost of query workload with this subset
					double cost = 0.0;
					for (Query query : queries) {
						cost += CostEstimator.denormalizedCost(query, subset);
					}
					
					// Record cost of this pair
					if (parentTable.equals(table1)) {
						costMap.put(new TablePair(table1, table2), cost);
						System.out.println(table1 + "-" + table2);
					} else {
						costMap.put(new TablePair(table2, table1), cost);
						System.out.println(table2 + "-" + table1);
					}
					
					System.out.println("cost: " + cost);
				}
			}
		}
		
		// Calculate normalized cost of query workload
		double normalizedCost = 0;
		for (Query query : queries) {
			normalizedCost += CostEstimator.normalizedCost(query);
		}
		
		System.out.println("Printing graph: ***********************");
		TableGraph graph = new TableGraph(tables, costMap, normalizedCost);
		System.out.println(graph);
		
		HashMap<Table, ArrayList<Table>> entityMap = graph.produceEntities(2);
		System.out.println("Printing entities: ********************");
		for (Table table : entityMap.keySet()) {
			System.out.println("Entity Center: " + table);
			for (Table member : entityMap.get(table)) {
				System.out.println("Entity Member: " + member);
			}
			System.out.println("-------------------");
		}
	}
	
	public static void main(String args[]) {
		List<Table> tables = Table
				.getTablesFromModel("../data_models/data2.model");
		ArrayList<Query> queryList = QueryLib
				.getQueryList("../query_logs/queries_sqlfire.sql");
		/*for (Query query : queryList) {
			for (Column column : query.getReferencedColumns()) {
				System.out.println(column);
			}
			System.out.println("--------");
		}*/
		produce(tables, queryList);
	}
}
