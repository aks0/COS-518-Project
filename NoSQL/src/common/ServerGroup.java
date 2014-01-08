package common;

import java.util.ArrayList;
import java.util.List;

import materializedViews.Table;

import colocation.EntityGroup;

class Server {
	private MemorySize size;
	
	public Server(MemorySize size) {
		this.size = size;
	}
	
	public MemorySize getSize() {
		return size;
	}
}

public class ServerGroup {
	private List<Server> servers;
	private EntityGroup partitionedEntityGroup;
	
	public ServerGroup(int n, MemorySize avgServerSize, EntityGroup group) {
		if (n <= 0) {
			throw new RuntimeException("Need to add at least one server"); 
		}
		
		servers = new ArrayList<Server>();
		partitionedEntityGroup = group;
		
		for (int i = 0; i < n; ++i) {
			servers.add(new Server(avgServerSize));
		}
	}
	
	public void addServer() {
		if (servers.size() == 0) {
			throw new RuntimeException("Need average server size"); 
		}
		
		MemorySize existingSize = servers.get(0).getSize();
		servers.add(new Server(existingSize));
	}
	
	public int getNumServers() {
		return servers.size();
	}
	
	public EntityGroup getEntityGroup() {
		return partitionedEntityGroup;
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
	public void addReplicatedTable(Table table) {
		boolean success = partitionedEntityGroup.addReplicatedTable(table);
		if (!success)
			return;
		
		// Keep adding servers till replicated table will fit in memory at each node
		System.out.println("Avg: " + partitionedEntityGroup.getPartitionedSize(servers.size()));
		System.out.println("Server Avg: " + servers.get(0).getSize().getBytes());
		while (partitionedEntityGroup.getPartitionedSize(servers.size())  > servers.get(0).getSize().getBytes()) {
			addServer();
		}
		
		// See if adding servers past minimum necessary is useful
		// TODO: is this really useful?
		/*while (true) {
			double benefit = partitionedEntityGroup.getPartitionedSize(servers.size()) - 
					partitionedEntityGroup.getPartitionedSize(servers.size()+1);
			double cost = partitionedEntityGroup.getReplicatedSize();
			
			if (benefit - cost < 0)
				break;
			
			addServer();
		}*/
	}
	
	public String toString() {
		String str = "";
		str += "\n-------------------------------\n";
		str += partitionedEntityGroup.toString();
		str += "Num Servers: " + servers.size() + "; AvgSize: " + servers.get(0).getSize() + "\n";
		str += "Partition Size: " + new MemorySize(partitionedEntityGroup.getPartitionedSize(servers.size())).toString() 
				+ "; Total Size: " + new MemorySize(partitionedEntityGroup.getSize()).toString() + "\n";
		str += "-------------------------------\n";
		
		return str;
	}
}
