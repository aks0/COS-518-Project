package colocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import materializedViews.Pair;
import materializedViews.Table;
import materializedViews.Util518;

public class TableGraph {
	class Edge {
		public Table table;
		public double cost;
		
		public Edge(Table table, double cost) {
			this.table = table;
			this.cost = cost;
		}
	}
	
	class Entry {
		public List<Edge> outEdges;
		
		public Entry() {
			outEdges = new ArrayList<Edge>();
		}
		
		public double maxEdge() {
			double max = 0;
			for (Edge edge : outEdges) {
				if (max < edge.cost)
					max = edge.cost;
			}
			
			return max;
		}
		
		public double getCost(Table table) {
			for (Edge edge : outEdges) {
				if (edge.table.equals(table)) {
					return edge.cost;
				}
			}
			
			return Double.POSITIVE_INFINITY;
		}
		
	}
	
	class ValueComparator implements Comparator<Table> {

	    Map<Table, Entry> base;
	    public ValueComparator(Map<Table, Entry> base) {
	        this.base = base;
	    }
	    
	    public int compare(Table a, Table b) {
	    	return (int)(base.get(a).maxEdge() - base.get(b).maxEdge());
	    }
	}
	
	private HashMap<Table, Entry> adjMap;
	
	public TableGraph(List<Table> tables, HashMap<Pair<String, String>, Double> costMap) {
		adjMap = Util518.newHashMap();
		
		for (Pair<String, String> pair : costMap.keySet()) {
			Table foreignKeyTable = Table.getInstance(pair.getFirst());
			double cost = costMap.get(pair);
			
			Entry entry;
			if (adjMap.containsKey(foreignKeyTable)) {
				entry = adjMap.get(foreignKeyTable);
			}
			else {
				entry = new Entry();
			}
			
			entry.outEdges.add(new Edge(Table.getInstance(pair.getSecond()), cost));
			adjMap.put(foreignKeyTable, entry);
				
		}
	}
	
	public List<ArrayList<Table> >produceEntities(int k) {
		List<ArrayList<Table>> entities = new ArrayList<ArrayList<Table> >();
		List<Table> entityCenters = getHighestOutDegree(k);
		
		for (Table table : entityCenters) {
			ArrayList<Table> entity = new ArrayList<Table>();
			entity.add(table);
			entities.add(entity);
		}
		
		for (Table table : adjMap.keySet()) {
			int bestTableIndex = -1;
			double bestCost = Double.MAX_VALUE;
			boolean exists = false;
			for (int i = 0; i < entityCenters.size(); ++i) {
				Table center = entityCenters.get(i);
				if (table.equals(center)) {
					exists = true;
					break;
				}
				
				double cost = adjMap.get(center).getCost(table);
				if (cost < bestCost) {
					bestCost = cost;
					bestTableIndex = i;
				}
			}
			
			if (!exists) {
				if (bestTableIndex != -1)
					entities.get(bestTableIndex).add(table);
				else {
					ArrayList<Table> entity = new ArrayList<Table>();
					entity.add(table);
					entities.add(entity);
				}
			}
		}
		
		return entities;
	}
	
	public List<Table> getHighestOutDegree(int k) {
		ValueComparator bvc = new ValueComparator(adjMap);
		TreeMap<Table, Entry> sortedMap = new TreeMap<Table, Entry>(bvc);
		sortedMap.putAll(adjMap);
		
		ArrayList<Table> entityCenters = new ArrayList<Table>();
		int i = 0;
		for (Table table : sortedMap.keySet()) {
			entityCenters.add(table);
			
			i++;
			if (i == k)
				break;
		}
		
		return entityCenters;
	}
	
}
