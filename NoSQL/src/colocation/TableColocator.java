package colocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.CreateTableStatements;
import common.MemoryManager;
import common.MemorySize;
import common.Server;
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
    public static void colocate(ArrayList<Table> tables, List<Query> queries, MemorySize avgServerSize, int maxServers) {
        
        // decide colocation via partitioning
        TableGraph2 graph = GraphBuilder.build(tables, queries);
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
        	 if (!entityMap.get(center).onlyContainsCenter()) {
        		 entityGroups.add(entityMap.get(center));
             }
        }
        
        // Do initial entity to server group mapping
        ArrayList<ServerGroup> serverGroups = MemoryManager.assignEntityGroups(entityGroups, avgServerSize, maxServers);
        
        System.out.println(serverGroups.toString());
        
        System.out.println("\n----------------------\n");
        
        // Get Best candidates for replication
        ArrayList<Pair<Table, ServerGroup>> pairs = graph.getHighestReplicationScorePairs(3, replicationCandidates, serverGroups);
    
        // For each pair, decide how many servers to add in light of adding the replicated table
        for (Pair<Table, ServerGroup> pair : pairs) {
        	Table replicatedTable = pair.getFirst();
        	ServerGroup group = pair.getSecond();
        	
        	System.out.println("Replicated table: " + replicatedTable.getName());
        	group.addReplicatedTable(replicatedTable, 0, maxServers);
        	System.out.println("Replicate to: " + group.toString());
        }
        
        System.out.println("-----------------");
        System.out.println("Printing final server groups");
        for (ServerGroup group : serverGroups) {
        	System.out.println(group.toString());
        }
        // Create Table Statements for TPC-H; comment out if not using TPC-H
        CreateTableStatements.print(serverGroups);
        
        System.out.println("\n----------------------\n");

        // map every server to the server groups it belongs to
        HashMap<Server, ArrayList<ServerGroup>> serverToServerGroup = Util518.newHashMap();
        for (ServerGroup group : serverGroups) {
            for (Server server : group.getServers()) {
		        ArrayList<ServerGroup> groups;
		        if (!serverToServerGroup.containsKey(server)) {
		            groups = Util518.newArrayList();
		            serverToServerGroup.put(server, groups);
		        } else {
		            groups = serverToServerGroup.get(server);
		        }
		        groups.add(group);
            }
        }
        
        // print server followed by the server groups it belongs to
        StringBuilder builder = new StringBuilder();
        int serverNum = 0;
        for (Server server : serverToServerGroup.keySet()) {
            builder.append("Server" + ++serverNum + ": ");
            ArrayList<ServerGroup> groups = serverToServerGroup.get(server);
            for (int i = 0; i < groups.size(); i++) {
                ServerGroup group = groups.get(i);
                builder.append(group.getName());
                if (i != groups.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }
    
    public static void main(String[] args) {
    	ArrayList<Table> tables = Table.getTablesFromModel("../data_models/tpc-h.model");
		ArrayList<Query> queryList = QueryLib.getQueryList("../query_logs/onefilequeries.sql");
		
		colocate(tables, queryList, new MemorySize(1.5, Size.GB), 3);
    }
}
