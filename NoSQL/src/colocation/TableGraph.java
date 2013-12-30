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

class Edge {
    // table being linked to
    private Table table;
    // cost assigned
    private double cost;
    
    public Edge(Table table, double cost) {
        this.table = table;
        this.cost = cost;
    }
    
    public Table getTable() {
        return table;
    }
    
    public double getCost() {
        return cost;
    }
}

class TableNode {
    // parent table being represented
    private Table table;
    // list of edges to children
    private ArrayList<Edge> outEdges;
    
    public TableNode(Table table) {
        this.table = table;
        outEdges = new ArrayList<Edge>();
    }
    
    public Table getTable() {
        return table;
    }
    
    public ArrayList<Edge> getOutEdges() {
        return outEdges;
    }
    
    public double maxEdge() {
        double max = 0;
        for (Edge edge : outEdges) {
            if (max < edge.getCost()) {
                max = edge.getCost();
            }
        }
        return max;
    }
    
    public double getCostTo(Table table) {
        for (Edge edge : outEdges) {
            if (edge.getTable().equals(table)) {
                return edge.getCost();
            }
        }
        return Double.POSITIVE_INFINITY;
    }
}

class ValueComparator implements Comparator<Table> {
    private Map<Table, TableNode> base;
    
    public ValueComparator(Map<Table, TableNode> base) {
        this.base = base;
    }
    
    public int compare(Table a, Table b) {
        return (int)(base.get(a).maxEdge() - base.get(b).maxEdge());
    }
}

public class TableGraph {	
	// mapping from table to the table's node in the graph 
	private HashMap<Table, TableNode> tableToNode;
	
	public TableGraph(List<Table> tables, HashMap<TablePair, Double> costMap) {
		tableToNode = Util518.newHashMap();
		
		for (TablePair pair : costMap.keySet()) {
			Table table = pair.getPrimaryTable();
			TableNode node;

			if (tableToNode.containsKey(table)) {
				node = tableToNode.get(table);
			} else {
				node = new TableNode(table);
				tableToNode.put(table, node);
			}
			node.getOutEdges().add(new Edge(pair.getForeignTable(), costMap.get(pair)));
		}
	}
	
	public HashMap<Table, ArrayList<Table>> produceEntities(int k) {
	    // entities as mapping from parent table to children tables
		HashMap<Table, ArrayList<Table>> entities = new HashMap<Table, ArrayList<Table>>();
		for (TableNode node : getHighestOutDegreeNodes(k)) {
		    // put parent tables
			entities.put(node.getTable(), new ArrayList<Table>());
		}
		
		// add every table to a partition
		for (Table table : tableToNode.keySet()) {
			double bestCost = Double.MAX_VALUE;
			Table bestCenter = null;
			boolean isACenter = false;
			for (Table center : entities.keySet()) {
				if (table.equals(center)) {
					isACenter = true;
					break;
				}
				
				// find best center to join with table
				double cost = tableToNode.get(center).getCostTo(table);
				if (cost < bestCost) {
					bestCost = cost;
					bestCenter = center;
				}
			}
			
			if (!isACenter) {
				if (bestCenter != null && bestCost != Double.POSITIVE_INFINITY) {
					entities.get(bestCenter).add(table);
				} else {
				    // create new entity with only center
					entities.put(table, new ArrayList<Table>());
				}
			}
		}
		return entities;
	}
	
	public List<TableNode> getHighestOutDegreeNodes(int k) {
		ValueComparator comparator = new ValueComparator(tableToNode);
		TreeMap<Table, TableNode> sortedMap = new TreeMap<Table, TableNode>(comparator);
		sortedMap.putAll(tableToNode);
		
		ArrayList<TableNode> entityCenters = new ArrayList<TableNode>();
		int i = 0;
		for (Table table : sortedMap.keySet()) {
			entityCenters.add(tableToNode.get(table));
			if (++i == k) break;
		}
		return entityCenters;
	}
}