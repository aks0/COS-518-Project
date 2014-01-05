package common;

import java.util.ArrayList;
import java.util.List;

import materializedViews.Table;

import com.sun.org.glassfish.external.statistics.AverageRangeStatistic;

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
		partitionedEntityGroup.addReplicatedTable(table);
		while (true) {
			double benefit = partitionedEntityGroup.getPartitionedSize(servers.size()) - 
					partitionedEntityGroup.getPartitionedSize(servers.size()+1);
			double cost = partitionedEntityGroup.getReplicatedSize() * (servers.size() + 1);
			
			if (benefit - cost < 0)
				break;
			
			addServer();
		}
	}
}
