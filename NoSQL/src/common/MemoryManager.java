package common;

import java.util.ArrayList;

import colocation.EntityGroup;

public class MemoryManager {
	
	public static ArrayList<ServerGroup> assignEntityGroups(ArrayList<EntityGroup> entityGroups, MemorySize avgServerSize, int maxServers) {
		ArrayList<ServerGroup> serverGroups = new ArrayList<ServerGroup>();
		
		for (EntityGroup entityGroup : entityGroups) {
			long entityGroupSize = entityGroup.getSize();
			
			int numServers = (int)Math.ceil((double)entityGroupSize/avgServerSize.getBytes());
			
			if (numServers > maxServers)
				throw new RuntimeException("Number of servers necessary to store partition is greater than max!");
			
			ServerGroup serverGroup = new ServerGroup(numServers, avgServerSize, entityGroup);
			serverGroups.add(serverGroup);
		}
		
		return serverGroups;
	}
	
}
