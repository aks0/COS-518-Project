package colocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    
    public String toString() {
    	return "(" + "Table: " + table.getName() + "; Cost: " + cost + ")";
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
    
    /**
     * Get cost of min edge outgoing
     * @return
     */
    public double minEdge() {
        double min = Double.MAX_VALUE;
        for (Edge edge : outEdges) {
            if (min > edge.getCost()) {
                min = edge.getCost();
            }
        }
        return min;
    }
    
    /**
     * Get cost of edge to specific table (infinity if it doesn't exist)
     * @param table
     * @return
     */
    public double getCostTo(Table table) {
        for (Edge edge : outEdges) {
            if (edge.getTable().equals(table)) {
                return edge.getCost();
            }
        }
        return Double.POSITIVE_INFINITY;
    }
    
    public String toString() {
    	String s = "Node: " + table.getName() + "\n";
    	for (Edge edge : outEdges) {
    		s += edge.toString() + "\n";
    	}
    	s += "-----------------";
    			
    	return s;
    }
}

/**
 * Allows one to compare two tables in terms of the graph
 * @author sachin1
 *
 */
class ValueComparator implements Comparator<Table> {
    private Map<Table, TableNode> base;
    
    public ValueComparator(Map<Table, TableNode> base) {
        this.base = base;
    }
    
    /**
     * > 0 : a has min edge of lower cost than b
     * < 0 : a has min edge of higher cost than b
     * == 0 : a has min edge of same cost as b
     * 
     * Doing this because edge cost indicates the amount of saving offered by join represented by edge
     */
    public int compare(Table a, Table b) {
        return (int)(-1*(base.get(a).minEdge() - base.get(b).minEdge()));
    }
}

public class TableGraph {	
	// mapping from table to the table's node in the graph 
	private HashMap<Table, TableNode> tableToNode;
	
	public TableGraph(List<Table> tables, HashMap<TablePair, Double> costMap) {
		tableToNode = Util518.newHashMap();
		
		// For each pair in the costMap, record edge in the graph
		for (TablePair pair : costMap.keySet()) {
			Table table = pair.getParentTable();
			TableNode node;

			if (tableToNode.containsKey(table)) {
				node = tableToNode.get(table);
			} else {
				node = new TableNode(table);
				tableToNode.put(table, node);
			}
			node.getOutEdges().add(new Edge(pair.getChildTable(), costMap.get(pair)));
		}
		
		// Make node with no edges for tables not existing in any pair in above costMap
		for (Table table : tables) {
			if (!tableToNode.containsKey(table)) {
				TableNode node = new TableNode(table);
				tableToNode.put(table, node);
			}
		}
	}
	
	public HashMap<Table, ArrayList<Table>> produceEntities(int k) {
	    // entities as mapping from parent table to children tables
		HashMap<Table, ArrayList<Table>> entities = new HashMap<Table, ArrayList<Table>>();
		for (TableNode node : getHighestScoreNodes(k)) {
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
	
	/**
	 * Get the best k nodes from the graph according to the comparator defined between two tables
	 * Currently : this score involves the min edge for a table
	 * 
	 * @param k
	 * @return
	 */
	public List<TableNode> getHighestScoreNodes(int k) {
		// Sort nodes by adding to treeMap
		ValueComparator comparator = new ValueComparator(tableToNode);
		TreeMap<Table, TableNode> sortedMap = new TreeMap<Table, TableNode>(comparator);
		sortedMap.putAll(tableToNode);
		
		// Iterate through sortedMap
		ArrayList<TableNode> entityCenters = new ArrayList<TableNode>();
		int count = 0;
		Iterator<Table> it = sortedMap.keySet().iterator();
		while (it.hasNext()) {
			Table table = it.next();
			
			// Don't make table a center if it can be joined with existing center
			for (TableNode tableNode : entityCenters) {
				if (Table.findParentTable(table, tableNode.getTable()) != null) continue;
			}
			
			entityCenters.add(tableToNode.get(table));
			if (++count == k) break;
		}
		
		return entityCenters;
	}
	
	public String toString() {
		String s = "";
		for (Table table : tableToNode.keySet()) {
			s += tableToNode.get(table).toString() + "\n";
		}
		
		return s;
	}
}