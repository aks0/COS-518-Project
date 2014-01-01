package common;

import java.util.ArrayList;
import java.util.HashMap;

import colocation.EntityGroup;

public class MemoryManager {
	
	public static HashMap<EntityGroup, ServerGroup> assignEntityGroups(ArrayList<EntityGroup> entityGroups, MemorySize avgServerSize) {
		HashMap<EntityGroup, ServerGroup> entityToServerGroupMap = new HashMap<EntityGroup, ServerGroup>();
		
		for (EntityGroup entityGroup : entityGroups) {
			long entityGroupSize = entityGroup.getSize();
			
			int numServers = (int) (entityGroupSize/avgServerSize.getBytes());
			ServerGroup serverGroup = new ServerGroup(numServers, avgServerSize);
			entityToServerGroupMap.put(entityGroup, serverGroup);
		}
		
		return entityToServerGroupMap;
	}
	
}
