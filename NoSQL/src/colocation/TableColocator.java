package colocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.MemoryManager;
import common.MemorySize;
import common.ServerGroup;
import common.Size;

import materializedViews.Pair;
import materializedViews.Query;
import materializedViews.QueryLib;
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
    public static void colocate(List<Table> tables, List<Query> queries, MemorySize avgServerSize) {
        
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
        ArrayList<ServerGroup> serverGroups = MemoryManager.assignEntityGroups(entityGroups, avgServerSize);
        
        System.out.println(serverGroups.toString());
        
        System.out.println("\n----------------------\n");
        
        // Get Best candidates for replication
        ArrayList<Pair<Table, ServerGroup>> pairs = graph.getHighestReplicationScorePairs(2, replicationCandidates, serverGroups);
    
        // For each pair, decide how many servers to add in light of adding the replicated table
        for (Pair<Table, ServerGroup> pair : pairs) {
        	Table replicatedTable = pair.getFirst();
        	ServerGroup group = pair.getSecond();
        	
        	System.out.println("Replicated table: " + replicatedTable.getName());
        	group.addReplicatedTable(replicatedTable, 0);
        	System.out.println("Replicate to: " + group.toString());
        }
        
        for (int i = 0; i < pairs.size(); ++i) {
        	for (int j = i+1; j < pairs.size(); ++j) {
        		ServerGroup group1 = pairs.get(i).getSecond();
        		ServerGroup group2 = pairs.get(j).getSecond();
        		
        		group1.merge(group2, queries);
        	}
        }
        
    }
    
    public static void main(String[] args) {
    	List<Table> tables = Table
				.getTablesFromModel("../data_models/data2.model");
		ArrayList<Query> queryList = QueryLib
				.getQueryList("../query_logs/queries_sqlfire_test_merge.sql");
		
		colocate(tables, queryList, new MemorySize(1.0, Size.MB));
    }
}
