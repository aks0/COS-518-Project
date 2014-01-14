package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import colocation.EntityGroup;
import materializedViews.Column;
import materializedViews.Pair;
import materializedViews.Table;
import materializedViews.Util518;

public class CreateTableStatements {

public static final String NATION = 
"CREATE TABLE NATION  ( NATIONKEY  INTEGER NOT NULL,\n" +
"            NAME       CHAR(25) NOT NULL,\n" +
"            REGIONKEY  INTEGER NOT NULL,\n" +
"            COMMENT    VARCHAR(152))\n";

public static final String REGION = 
"CREATE TABLE REGION  ( REGIONKEY  INTEGER NOT NULL,\n" +
"            NAME       CHAR(25) NOT NULL,\n" +
"            COMMENT    VARCHAR(152))\n";

public static final String PART = 
"CREATE TABLE PART  ( PARTKEY     INTEGER NOT NULL,\n" +
"          NAME        VARCHAR(55) NOT NULL,\n" +
"          MFGR        CHAR(25) NOT NULL,\n" +
"          BRAND       CHAR(10) NOT NULL,\n" +
"          TYPE        VARCHAR(25) NOT NULL,\n" +
"          SIZE        INTEGER NOT NULL,\n" +
"          CONTAINER   CHAR(10) NOT NULL,\n" +
"          RETAILPRICE DECIMAL(15,2) NOT NULL,\n" +
"          COMMENT     VARCHAR(23) NOT NULL )\n";

public static final String SUPPLIER = 
"CREATE TABLE SUPPLIER ( SUPPKEY     INTEGER NOT NULL,\n" +
"             NAME        CHAR(25) NOT NULL,\n" +
"             ADDRESS     VARCHAR(40) NOT NULL,\n" +
"             NATIONKEY   INTEGER NOT NULL,\n" +
"             PHONE       CHAR(15) NOT NULL,\n" +
"             ACCTBAL     DECIMAL(15,2) NOT NULL,\n" +
"             COMMENT     VARCHAR(101) NOT NULL)\n";

public static final String PARTSUPP = 
"CREATE TABLE PARTSUPP ( PARTKEY     INTEGER NOT NULL,\n" +
"             SUPPKEY     INTEGER NOT NULL,\n" +
"             AVAILQTY    INTEGER NOT NULL,\n" +
"             SUPPLYCOST  DECIMAL(15,2)  NOT NULL,\n" +
"             COMMENT     VARCHAR(199) NOT NULL )\n";

public static final String CUSTOMER = 
"CREATE TABLE CUSTOMER ( CUSTKEY     INTEGER NOT NULL,\n" +
"             NAME        VARCHAR(25) NOT NULL,\n" +
"             ADDRESS     VARCHAR(40) NOT NULL,\n" +
"             NATIONKEY   INTEGER NOT NULL,\n" +
"             PHONE       CHAR(15) NOT NULL,\n" +
"             ACCTBAL     DECIMAL(15,2)   NOT NULL,\n" +
"             MKTSEGMENT  CHAR(10) NOT NULL,\n" +
"             COMMENT     VARCHAR(117) NOT NULL)\n";

public static final String ORDERS = 
"CREATE TABLE ORDERS  ( ORDERKEY       INTEGER NOT NULL,\n" +
"           CUSTKEY        INTEGER NOT NULL,\n" +
"           ORDERSTATUS    CHAR(1) NOT NULL,\n" +
"           TOTALPRICE     DECIMAL(15,2) NOT NULL,\n" +
"           ORDERDATE      DATE NOT NULL,\n" +
"           ORDERPRIORITY  CHAR(15) NOT NULL,  -- R\n" +
"           CLERK          CHAR(15) NOT NULL,  -- R\n" +
"           SHIPPRIORITY   INTEGER NOT NULL,\n" +
"           COMMENT        VARCHAR(79) NOT NULL)\n";

public static final String LINEITEM = 
"CREATE TABLE LINEITEM ( ORDERKEY    INTEGER NOT NULL,\n" +
"             PARTKEY     INTEGER NOT NULL,\n" +
"             SUPPKEY     INTEGER NOT NULL,\n" +
"             LINENUMBER  INTEGER NOT NULL,\n" +
"             QUANTITY    DECIMAL(15,2) NOT NULL,\n" +
"             EXTENDEDPRICE  DECIMAL(15,2) NOT NULL,\n" +
"             DISCOUNT    DECIMAL(15,2) NOT NULL,\n" +
"             TAX         DECIMAL(15,2) NOT NULL,\n" +
"             RETURNFLAG  CHAR(1) NOT NULL,\n" +
"             LINESTATUS  CHAR(1) NOT NULL,\n" +
"             SHIPDATE    DATE NOT NULL,\n" +
"             COMMITDATE  DATE NOT NULL,\n" +
"             RECEIPTDATE DATE NOT NULL,\n" +
"             SHIPINSTRUCT CHAR(25) NOT NULL,  -- R\n" +
"             SHIPMODE     CHAR(10) NOT NULL,  -- R\n" +
"             COMMENT      VARCHAR(44) NOT NULL)\n";

private static final String PARTITION = "PARTITION BY COLUMN (";
private static final String COLOCATE = "COLOCATE WITH (";
private static final String REPLICATE = "REPLICATE";
private static final String SERVER_GROUPS = "SERVER GROUPS (";
private static final String CLAUSE_END = ")";

private static final HashMap<Table, String> tableToStatement;
static
{
    tableToStatement = new HashMap<Table, String>();
    tableToStatement.put(Table.getInstance("nation"), NATION);
    tableToStatement.put(Table.getInstance("region"), REGION);
    tableToStatement.put(Table.getInstance("part"), PART);
    tableToStatement.put(Table.getInstance("supplier"), SUPPLIER);
    tableToStatement.put(Table.getInstance("partsupp"), PARTSUPP);
    tableToStatement.put(Table.getInstance("customer"), CUSTOMER);
    tableToStatement.put(Table.getInstance("orders"), ORDERS);
    tableToStatement.put(Table.getInstance("lineitem"), LINEITEM);
}

public static void print(ArrayList<ServerGroup> serverGroups) {
    StringBuilder builder = new StringBuilder();
    
    HashMap<Table, ArrayList<ServerGroup>> replicatedTableToServerGroup = Util518.newHashMap();
    
    // create statements for partitioned tables
    for (ServerGroup serverGroup : serverGroups) {
		for (EntityGroup entityGroup : serverGroup.getEntityGroups()) {
		    for (Table replicatedTable : entityGroup.getReplicatedTables()) {
		        ArrayList<ServerGroup> groups;
		        if (!replicatedTableToServerGroup.containsKey(replicatedTable)) {
		            groups = Util518.newArrayList();
		            replicatedTableToServerGroup.put(replicatedTable, groups);
		        } else {
		            groups = replicatedTableToServerGroup.get(replicatedTable);
		        }
		        groups.add(serverGroup);
		    }
		    
		    Table center = entityGroup.getCenter();
		    builder.append(tableToStatement.get(center));
		    String partitionKey = "";
		    for (Column column : center.getColumns()) {
		        if (column.isPrimary()) {
		            partitionKey = column.getName();
		            break;
		        }
		    }
		    String partitionClause = PARTITION + partitionKey.toUpperCase() + CLAUSE_END + "\n";
	        String colocateClause = COLOCATE + center.getName().toUpperCase() + CLAUSE_END + "\n";
	        String serverGroupsClause = SERVER_GROUPS + serverGroup.getName() + CLAUSE_END + ";\n\n";
	        
		    builder.append(partitionClause);
		    builder.append(serverGroupsClause);
		    

		    for (Table entity : entityGroup.getEntities()) {
		        if (!entity.equals(center)) {
		            builder.append(tableToStatement.get(entity));
		            builder.append(partitionClause);
		            builder.append(colocateClause);
        		    builder.append(serverGroupsClause);
		        }
		    }
		}
    }
    
    for (Table table : replicatedTableToServerGroup.keySet()) {
        builder.append(tableToStatement.get(table));
        builder.append(REPLICATE + "\n");
        ArrayList<ServerGroup> groups = replicatedTableToServerGroup.get(table);
        String serverGroupsClause = SERVER_GROUPS;
        for (int i = 0; i < groups.size(); i++) {
            ServerGroup group = groups.get(i);
            serverGroupsClause += group.getName();
            if (i != groups.size() - 1) {
                serverGroupsClause += ",";
            }
        }
        serverGroupsClause += CLAUSE_END + ";\n\n";
        builder.append(serverGroupsClause);
    }
    System.out.println(builder.toString());
    
}
}
