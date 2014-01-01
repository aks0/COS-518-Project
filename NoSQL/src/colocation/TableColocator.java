package colocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            int numServers, double averageMemory, double dataSetSize) {
        
        // decide colocation via partitioning
        TableGraph graph = GraphBuilder.build(tables, queries);
        HashMap<Table, EntityGroup> entityMap = graph.produceEntityGroups(2);
        
        // add colocation via replication
        
        ArrayList<Table> replicationCandidates = Util518.newArrayList();
        for (Table entityCenter : entityMap.keySet()) {
            if (entityMap.get(entityCenter).onlyContainsCenter()) {
                replicationCandidates.add(entityCenter);
            }
        }
        ArrayList<EntityGroup> entityGroups = Util518.newArrayList();
        for (Table center : entityMap.keySet()) {
            entityGroups.add(entityMap.get(center));
        }
        graph.getHighestReplicationScorePairs(2, replicationCandidates, entityGroups);
    }
}
