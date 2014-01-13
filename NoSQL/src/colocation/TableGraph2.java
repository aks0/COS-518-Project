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
    
    public double getCost(double normCost) {
    	if (!table.getClaimed())
    		return cost;
    	else 
    		return normCost;
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
     * The benefit of a node for partitioning is the total savings it offers 
     * For each existing edge of node, we calculate the savings by subtracting
     * from the normalized cost, the denormalized cost assuming that edge exists
     * 
     * @param totalNormalizedCost
     * @return
     */
    public double getPartitionBenefit(double totalNormalizedCost) {
    	double benefit = 0;
    	
    	// If no outgoing edges, no partition benefit
    	if (outEdges.size() == 0)
    		return 0;
    	
    	for (Edge edge : outEdges) {
    		benefit += totalNormalizedCost - edge.getCost(totalNormalizedCost);
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
    public double getReplicationBenefit(ServerGroup serverGroup, HashMap<Table, TableNode> tableToNode, double totalNormalizedCost) {
        double benefit = 0;
        
        
        EntityGroup group = serverGroup.getEntityGroup(0);
        
        // If group already contains table, no benefit in replicating it
        if (group.contains(this.getTable()))
        	return 0;
        
        // See if any joins exist from entity group to replicated table candidate
        for (Edge edge : outEdges) {
        	//System.out.println("Edge: " + edge.getTable().getName());
            if (group.contains(edge.getTable())) {
                benefit += totalNormalizedCost - edge.getCost(totalNormalizedCost);
            }
        }
        
        // See if any joins exist from replicated table candidate to table in entity group
        for (Table entityTable : group.getEntities()) {
        	TableNode node = tableToNode.get(entityTable);
        	for (Edge edge : node.outEdges) {
            	//System.out.println("Edge: " + edge.getTable().getName());
                if (edge.getTable().equals(this.table)) {
                    benefit += totalNormalizedCost - edge.getCost(totalNormalizedCost);
                }
            }
        }
        
        benefit -= table.getSize() * serverGroup.getNumServers();
        benefit -= table.getUpdateRate() * table.getSize() * serverGroup.getNumServers();
        return benefit;
    }
    
    /**
     * Get cost of edge to specific table (infinity if it doesn't exist)
     * @param table
     * @return
     */
    public double getCostTo(Table table, double normCost) {
        for (Edge edge : outEdges) {
            if (edge.getTable().equals(table)) {
                return edge.getCost(normCost);
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
    private HashMap<Table, TableNode> base;
    private double totalNormalizedCost;
    
    public ReplicationValueComparator(HashMap<Table, TableNode> base, double totalNormalizedCost) {
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

	@Override
	public int compare(Pair<Table, ServerGroup> a, Pair<Table, ServerGroup> b) {
		TableNode tNodeA = base.get(a.getFirst());
        TableNode tNodeB = base.get(b.getFirst());
        
		double scoreA =  tNodeA.getReplicationBenefit(a.getSecond(), base, totalNormalizedCost)
				- a.getFirst().getSize() * a.getSecond().getNumServers() 
				- a.getFirst().getSize() * a.getSecond().getNumServers() * a.getFirst().getUpdateRate();
		double scoreB =  tNodeB.getReplicationBenefit(b.getSecond(), base, totalNormalizedCost) 
				- b.getFirst().getSize() * b.getSecond().getNumServers()
				- b.getFirst().getSize() * b.getSecond().getNumServers() * b.getFirst().getUpdateRate();
		
		return (int)(scoreB - scoreA);
	}
}

public class TableGraph2 {	
	// mapping from table to the table's node in the graph 
	private HashMap<Table, TableNode> tableToNode;
	private double totalNormalizedCost;
	
	public TableGraph2(List<Table> tables, HashMap<TablePair, Double> costMap, double totalNormalizedCost) {
		tableToNode = Util518.newHashMap();
		this.totalNormalizedCost = totalNormalizedCost;
		
		System.out.println("totalNormalizedCost: " + totalNormalizedCost);
		
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
			System.out.println("Node name: " + node.getTable().getName());
			System.out.println("Partition Benefit: " + node.getPartitionBenefit(totalNormalizedCost));
		}
		
		// Make node with no edges for tables not existing in any pair in above costMap
		for (Table table : tables) {
			if (!tableToNode.containsKey(table)) {
				TableNode node = new TableNode(table);
				tableToNode.put(table, node);
				
				System.out.println("Node name: " + node.getTable().getName());
				System.out.println("Partition Benefit: " + node.getPartitionBenefit(totalNormalizedCost));
			}
		}
	}
	
	public HashMap<Table, TableNode> getTableToNode() {
	    return tableToNode;
	}
	
	public HashMap<Table, EntityGroup> produceEntityGroups(int k) {
		int count = 0;
		
		// mapping from entity center to entity group
		HashMap<Table, EntityGroup> entityMap = new HashMap<Table, EntityGroup>();
		HashMap<Table, EntityGroup> oneCenterMap = new HashMap<Table, EntityGroup>();
				
		while (count < k) {
			TableNode node = getHighestPartitionScore();
			
			// Don't add as center if benefit is all based on size
			if (!entityMap.containsKey(node.getTable()) && node.getPartitionBenefit(totalNormalizedCost) > node.getTable().getSize())
				entityMap.put(node.getTable(), new EntityGroup(node.getTable()));
		
			for (Edge edge : node.getOutEdges()) {
				edge.getTable().setClaimed(true);
			}
			
			count++;
		}
		
		// Reset edges
		for (TableNode node : tableToNode.values()) {
			for (Edge edge : node.getOutEdges()) {
				edge.getTable().setClaimed(false);
			}
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
				double cost = tableToNode.get(center).getCostTo(table, this.totalNormalizedCost);
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
					EntityGroup group = new EntityGroup(table);
					group.unused = true;
        			oneCenterMap.put(table, group);
        			
				}
			}
		}
		
		entityMap.putAll(oneCenterMap);
		return entityMap;
	}
	
	/**
	 * Get the best k nodes from the graph according to the comparator defined between two tables
	 * Currently : this score involves the min edge for a table
	 * 
	 * @param k
	 * @return
	 */
	public TableNode getHighestPartitionScore() {
		// Sort nodes by adding to treeMap
        Comparator<Table> comparator = new PartitionValueComparator(tableToNode, totalNormalizedCost);
		TreeMap<Table, TableNode> sortedMap = new TreeMap<Table, TableNode>(comparator);
		sortedMap.putAll(tableToNode);
		
		
		// Get first item from Sorted Map
		Iterator<Table> it = sortedMap.keySet().iterator();
		Table table = it.next();
		System.out.println("Table picked: " + table.getName());
		System.out.println("Partition score: " + tableToNode.get(table).getPartitionBenefit(totalNormalizedCost));
		return tableToNode.get(table);
		
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
	    
	    if (tables.size() == 0)
	        return new ArrayList<Pair<Table, ServerGroup>>();
	    
		// Sort nodes by adding to treeMap
        Comparator<Pair<Table, ServerGroup>> comparator = new ReplicationValueComparator(tableToNode, totalNormalizedCost);
		PriorityQueue<Pair<Table, ServerGroup>> pqueue = 
		        new PriorityQueue<Pair<Table, ServerGroup>>(tables.size() * groups.size(), comparator);
		for (Table table : tables) {
		    for (ServerGroup group : groups) {
		    	Pair<Table, ServerGroup> a = new Pair<Table, ServerGroup>(table, group);
		        pqueue.add(a);
		        System.out.println("Table: " + table.getName());
		        System.out.println("Entity Group: " + group.getEntityGroup(0).toString());
		        double scoreA = tableToNode.get(table).getReplicationBenefit(a.getSecond(), tableToNode, totalNormalizedCost)
				- a.getFirst().getSize() * a.getSecond().getNumServers() 
				- a.getFirst().getSize() * a.getSecond().getNumServers() * a.getFirst().getUpdateRate();
		        System.out.println("Replication benefit: " + scoreA);
		        System.out.println("*************");
		    }
		}
		
		// Iterate 
		ArrayList<Pair<Table, ServerGroup>> pairs = new ArrayList<Pair<Table, ServerGroup>>();
		int count = 0;
		while (pqueue.size() > 0) {
			pairs.add(pqueue.remove());
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
