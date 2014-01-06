package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import colocation.EntityGroup;

public class MemoryManager {
	
	public static ArrayList<ServerGroup> assignEntityGroups(ArrayList<EntityGroup> entityGroups, MemorySize avgServerSize) {
		ArrayList<ServerGroup> serverGroups = new ArrayList<ServerGroup>();
		
		for (EntityGroup entityGroup : entityGroups) {
			long entityGroupSize = entityGroup.getSize();
			
			int numServers = (int)Math.ceil((double)entityGroupSize/avgServerSize.getBytes());
			ServerGroup serverGroup = new ServerGroup(numServers, avgServerSize, entityGroup);
			serverGroups.add(serverGroup);
		}
		
		return serverGroups;
	}
	
}
