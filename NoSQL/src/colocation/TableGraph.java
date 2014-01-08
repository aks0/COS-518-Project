package colocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import common.ServerGroup;

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
     * The benefit of a node for partitioning is the total savings it offers 
     * For each existing edge of node, we calculate the savings by subtracting
     * from the normalized cost, the denormalized cost assuming that edge exists
     * 
     * @param totalNormalizedCost
     * @return
     */
    public double getPartitionBenefit(double totalNormalizedCost) {
    	double benefit = 0;
    	for (Edge edge : outEdges) {
    		benefit += totalNormalizedCost - edge.getCost();
    	}
    	
    	benefit += table.getSize();
    	return benefit;
    }
    
    /**
     * The benefit of a node for replication given an entity group is
     * the savings provided by having that node be replicated 
     * + savings in cost by having the edges from replicated node to nodes in entity group
     * - Size of node 
     * - update rate * size of node
     * 
     * @param group
     * @param totalNormalizedCost
     * @return
     */
    public double getReplicationBenefit(EntityGroup group, double totalNormalizedCost) {
        double benefit = 0;
        for (Edge edge : outEdges) {
            if (group.contains(edge.getTable())) {
                benefit += totalNormalizedCost - edge.getCost();
            }
        }
        
        benefit -= table.getSize();
        benefit -= table.getUpdateRate() * table.getSize();
        return benefit;
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
class PartitionValueComparator implements Comparator<Table> {
    private Map<Table, TableNode> base;
    private double totalNormalizedCost;
    
    public PartitionValueComparator(Map<Table, TableNode> base, double totalNormalizedCost) {
        this.base = base;
        this.totalNormalizedCost = totalNormalizedCost;
    }
    
    /**
     * > 0 : a has min edge of lower cost than b
     * < 0 : a has min edge of higher cost than b
     * == 0 : a has min edge of same cost as b
     * 
     * Doing this because edge cost indicates the amount of saving offered by join represented by edge
     */
    public int compare(Table a, Table b) {
        TableNode tNodeA = base.get(a);
        TableNode tNodeB = base.get(b);
    	
        return (int)(tNodeB.getPartitionBenefit(totalNormalizedCost) - tNodeA.getPartitionBenefit(totalNormalizedCost)); 
    }
}

/**
 * Allows one to compare two tables in terms of the graph
 */
class ReplicationValueComparator implements Comparator<Pair<Table, ServerGroup>> {
    private Map<Table, TableNode> base;
    private double totalNormalizedCost;
    
    public ReplicationValueComparator(Map<Table, TableNode> base, double totalNormalizedCost) {
        this.base = base;
        this.totalNormalizedCost = totalNormalizedCost;
    }
    
    /**
     * > 0 : a has min edge of lower cost than b
     * < 0 : a has min edge of higher cost than b
     * == 0 : a has min edge of same cost as b
     * 
     * Doing this because edge cost indicates the amount of saving offered by join represented by edge
     */
    /*public int compare(Pair<Table, EntityGroup> a, Pair<Table, EntityGroup> b) {
        TableNode tNodeA = base.get(a.getFirst());
        TableNode tNodeB = base.get(b.getFirst());
        
        return (int)(tNodeB.getReplicationBenefit(b.getSecond(), totalNormalizedCost) 
                - tNodeA.getReplicationBenefit(a.getSecond(), totalNormalizedCost)); 
    }*/

	@Override
	public int compare(Pair<Table, ServerGroup> a, Pair<Table, ServerGroup> b) {
		TableNode tNodeA = base.get(a.getFirst());
        TableNode tNodeB = base.get(b.getFirst());
        
		double scoreA =  tNodeA.getReplicationBenefit(a.getSecond().getEntityGroup(), totalNormalizedCost) 
				- a.getFirst().getSize() * a.getSecond().getNumServers() * a.getFirst().getUpdateRate();
		double scoreB =  tNodeB.getReplicationBenefit(b.getSecond().getEntityGroup(), totalNormalizedCost) 
				- b.getFirst().getSize() * b.getSecond().getNumServers() * b.getFirst().getUpdateRate();
		
		return (int)(scoreB - scoreA);
	}
}

public class TableGraph {	
	// mapping from table to the table's node in the graph 
	private HashMap<Table, TableNode> tableToNode;
	private double totalNormalizedCost;
	
	public TableGraph(List<Table> tables, HashMap<TablePair, Double> costMap, double totalNormalizedCost) {
		tableToNode = Util518.newHashMap();
		this.totalNormalizedCost = totalNormalizedCost;
		
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
	
	public HashMap<Table, TableNode> getTableToNode() {
	    return tableToNode;
	}
	
	public HashMap<Table, EntityGroup> produceEntityGroups(int k) {
	    // mapping from entity center to entity group
		HashMap<Table, EntityGroup> entityMap = new HashMap<Table, EntityGroup>();
		for (TableNode node : getHighestPartitionScoreNodes(k)) {
		    // put parent tables
			System.out.println("Picked one of k center: " + node.getTable().getName());
			entityMap.put(node.getTable(), new EntityGroup(node.getTable()));
		}
		
		// add every table to a partition
		for (Table table : tableToNode.keySet()) {
			double bestCost = Double.MAX_VALUE;
			Table bestCenter = null;
			boolean isACenter = false;
			for (Table center : entityMap.keySet()) {
				if (table.equals(center)) {
					isACenter = true;
					break;
				}
				
				// find best center to join with table (one where the cost is lowest)
				double cost = tableToNode.get(center).getCostTo(table);
				if (cost < bestCost) {
					bestCost = cost;
					bestCenter = center;
				}
			}
			
			if (!isACenter) {
				if (bestCenter != null && bestCost != Double.POSITIVE_INFINITY) {
					entityMap.get(bestCenter).add(table);
				} else {
				    // create new entity with only center if it doesn't have join with any existing center
        			entityMap.put(table, new EntityGroup(table));
				}
			}
		}
		return entityMap;
	}
	
	/**
	 * Get the best k nodes from the graph according to the comparator defined between two tables
	 * Currently : this score involves the min edge for a table
	 * 
	 * @param k
	 * @return
	 */
	public List<TableNode> getHighestPartitionScoreNodes(int k) {
		// Sort nodes by adding to treeMap
        Comparator<Table> comparator = new PartitionValueComparator(tableToNode, totalNormalizedCost);
		TreeMap<Table, TableNode> sortedMap = new TreeMap<Table, TableNode>(comparator);
		sortedMap.putAll(tableToNode);
		
		// Iterate through sortedMap
		ArrayList<TableNode> nodes = new ArrayList<TableNode>();
		int count = 0;
		Iterator<Table> it = sortedMap.keySet().iterator();
		while (it.hasNext()) {
			Table table = it.next();
			
			// Don't make table a center if it can be joined with existing center
			for (TableNode tableNode : nodes) {
				if (Table.findParentTable(table, tableNode.getTable()) != null) continue;
			}
			
			nodes.add(tableToNode.get(table));
			if (++count == k) break;
		}
		
		return nodes;
	}

	/**
	 * Get the best k pairs of tables and server groups from the graph according to
     * the comparator defined between two tables
	 * @param k
	 * @param tables
	 * @param groups
	 * @return
	 */
	public ArrayList<Pair<Table, ServerGroup>> getHighestReplicationScorePairs(int k, 
	        ArrayList<Table> tables, ArrayList<ServerGroup> groups) {
		// Sort nodes by adding to treeMap
        Comparator<Pair<Table, ServerGroup>> comparator = new ReplicationValueComparator(tableToNode, totalNormalizedCost);
		PriorityQueue<Pair<Table, ServerGroup>> pqueue = 
		        new PriorityQueue<Pair<Table, ServerGroup>>(tables.size() * groups.size(), comparator);
		for (Table table : tables) {
		    for (ServerGroup group : groups) {
		        pqueue.add(new Pair<Table, ServerGroup>(table, group));
		    }
		}
		
		// Iterate 
		ArrayList<Pair<Table, ServerGroup>> pairs = new ArrayList<Pair<Table, ServerGroup>>();
		int count = 0;
		Iterator<Pair<Table, ServerGroup>> it = pqueue.iterator();
		while (it.hasNext()) {
			pairs.add(it.next());
			if (++count == k) break;
		}
		
		return pairs;
	}
	
	public String toString() {
		String s = "";
		for (Table table : tableToNode.keySet()) {
			s += tableToNode.get(table).toString() + "\n";
		}
		
		return s;
	}
}