package common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import materializedViews.Column;
import materializedViews.CostEstimator;
import materializedViews.Query;
import materializedViews.Table;
import materializedViews.TableSubset;

import colocation.EntityGroup;

public class ServerGroup {
    private String name;
	private List<Server> servers;
	private List<EntityGroup> partitionedEntityGroups;
	private MemorySize avgServerSize;
	private static int NUM_GROUPS = 0;
	
	public ServerGroup(int n, MemorySize avgServerSize, EntityGroup group) {
		if (n <= 0) {
			throw new RuntimeException("Need to add at least one server"); 
		}
		
		NUM_GROUPS++;
		name = "GROUP" + NUM_GROUPS;
		
		this.avgServerSize = avgServerSize;
		servers = new ArrayList<Server>();
		partitionedEntityGroups = new ArrayList<EntityGroup>();
		partitionedEntityGroups.add(group);
		
		for (int i = 0; i < n; ++i) {
			servers.add(new Server(avgServerSize));
		}
	}
	
	public String getName() {
	    return name;
	}
	
	public void addServer() {
		if (servers.size() == 0) {
			throw new RuntimeException("Need average server size"); 
		}
		
		servers.add(new Server(avgServerSize));
	}
	
	public int getNumServers() {
		return servers.size();
	}
	
	public List<Server> getServers() {
	    return servers;
	}
	
	public EntityGroup getEntityGroup(int index) {
		return partitionedEntityGroups.get(index);
	}
	
	public List<EntityGroup> getEntityGroups() {
		return partitionedEntityGroups;
	}
	
	/**
	 * Given a table, adds it to entity group as replicated table.
	 * Then, decides how many servers to add because of replicated table
	 * When adding a server consider if benefit - cost > 0
	 * where benefit = better partitioning because of one more server 
	 * where cost = more space usage because of replicated table being stored in more server
	 * 
	 * @param table
	 */
	public boolean addReplicatedTable(Table table, int index, int maxServers) {
		EntityGroup partitionedEntityGroup = partitionedEntityGroups.get(index);
		boolean success = partitionedEntityGroup.addReplicatedTable(table);
		if (!success)
			return false;
		
		// Keep adding servers till replicated table will fit in memory at each node
		System.out.println("Avg: " + partitionedEntityGroup.getPartitionedSize(servers.size()));
		System.out.println("Server Avg: " + avgServerSize.getBytes());
		
		int originalSize = servers.size();
		while (partitionedEntityGroup.getPartitionedSize(servers.size())  > avgServerSize.getBytes()) {
			addServer();
			
			// Sometimes not possible to add replicated table because not enough size
			// Try adding servers and if you go past max, then that means replicated table cannot be added
			if (servers.size() > maxServers) {
				servers = servers.subList(0, originalSize);
				partitionedEntityGroup.removeReplicatedTable(table);
				return false;
			}
		}
		
		while (true) {
			if (servers.size() == maxServers)
				return true;
			
			double benefit = partitionedEntityGroup.getPartitionedSize(servers.size()) - 
					partitionedEntityGroup.getPartitionedSize(servers.size()+1);
			double cost = partitionedEntityGroup.getReplicatedSize();
			
			System.out.println("Benefit of adding server: " + benefit);
			System.out.println("Cost of adding server: " + cost);
			
			if (benefit - cost < 0)
				break;
			
			addServer();
		}
		return true;
	}
	
	/**
	 * The benefit of merging is the ability to do new joins with replicated table(s)
	 * The cost is the number of extra servers necessary to allow merge
	 * 
	 * @param server
	 */
	public boolean merge(ServerGroup group, List<Query> queries) {
		double benefit = 0;
		
		for (EntityGroup existingGroup : partitionedEntityGroups) {
			HashSet<Table> partitionTables = existingGroup.getEntities();
			HashSet<Table> replicatedTables = new HashSet<Table>();
			for (EntityGroup eGroup : group.getEntityGroups()) {
				replicatedTables.addAll(eGroup.getReplicatedTables());
			}
			
			TableSubset subsetWith = new TableSubset();
			TableSubset subsetWithout = new TableSubset();
			for (Table table : partitionTables) {
				for (Column column : table.getColumns()) {
					subsetWith.addColumn(column);
					subsetWithout.addColumn(column);
				}
			}
			for (Table table : replicatedTables) {
				for (Column column : table.getColumns()) {
					subsetWith.addColumn(column);
				}
			}
			
			// Compute the denormalized cost of query workload with this subset
			for (Query query : queries) {
				benefit += CostEstimator.denormalizedCost(query, subsetWithout) - CostEstimator.denormalizedCost(query, subsetWith);
			}
		}
		
		System.out.println("Benefit: " + benefit);
		
		double totalPartitionSize = 0, totalReplicatedSize = 0;
		int numServersNecessary = servers.size();
		for (EntityGroup eGroup : partitionedEntityGroups) {
			totalPartitionSize += eGroup.getSize();
			totalReplicatedSize += eGroup.getReplicatedSize();
		}
		for (EntityGroup eGroup : group.getEntityGroups()) {
			totalPartitionSize += eGroup.getSize();
			totalReplicatedSize += eGroup.getReplicatedSize();
		}
		while (totalPartitionSize/numServersNecessary + totalReplicatedSize  > avgServerSize.getBytes()) {
			numServersNecessary++;
		}
		
		double cost = numServersNecessary * avgServerSize.getBytes();
		
		System.out.println("Num servers necessary: " + numServersNecessary);
		System.out.println("Cost: " + new MemorySize((long)cost).toString());
		
		if (benefit - cost > 0) {
			while (servers.size() < numServersNecessary)
				addServer();
			
			partitionedEntityGroups.addAll(group.getEntityGroups());
			
			return true;
		}
		
		return false;
			
			
	}
	
	public String toString() {
		String str = "";
		str += "\n-------------------------------\n";
		str += "\n-------------------------------\n";
		for (EntityGroup partitionedEntityGroup : partitionedEntityGroups) {
			str += partitionedEntityGroup.toString();
			str += "Partition Size: " + new MemorySize(partitionedEntityGroup.getPartitionedSize(servers.size())).toString();
			str += "; Total Size: " + new MemorySize(partitionedEntityGroup.getSize()).toString() + "\n";
		}
		str += "Num Servers: " + servers.size() + "; AvgSize: " + avgServerSize + "\n";
		str += "-------------------------------\n";
		str += "\n-------------------------------\n";
		
		return str;
	}
}
