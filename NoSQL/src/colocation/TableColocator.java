package colocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.MemoryManager;
import common.MemorySize;
import common.ServerGroup;

import materializedViews.Query;
import materializedViews.Table;
import materializedViews.Util518;

public class TableColocator {

    /**
     * Decide colocation of tables based on queries
     * @param tables - list of tables
     * @param queries - list of queries
     * @param numServers - number of servers
     * @param averageMemory - average memory on a server in GB
     * @param dataSetSize - size of entire dataset in GB, if using TPC-H it is either 1 or 10
     */
    public static void colocate(List<Table> tables, List<Query> queries, 
            int numServers, MemorySize avgServerSize, double dataSetSize) {
        
        // decide colocation via partitioning
        TableGraph graph = GraphBuilder.build(tables, queries);
        HashMap<Table, EntityGroup> entityMap = graph.produceEntityGroups(2);
        
        // Get replication candidates
        ArrayList<Table> replicationCandidates = Util518.newArrayList();
        for (Table entityCenter : entityMap.keySet()) {
            if (entityMap.get(entityCenter).onlyContainsCenter()) {
                replicationCandidates.add(entityCenter);
            }
        }
        
        // Get entity groups
        ArrayList<EntityGroup> entityGroups = Util518.newArrayList();
        for (Table center : entityMap.keySet()) {
            entityGroups.add(entityMap.get(center));
        }
        
        // Do initial entity to server group mapping
        HashMap<EntityGroup, ServerGroup> entityToServerGroupMap = MemoryManager.assignEntityGroups(entityGroups, avgServerSize);
        
        // Get Best candidates for replication
        graph.getHighestReplicationScorePairs(2, replicationCandidates, entityGroups);
    }
}