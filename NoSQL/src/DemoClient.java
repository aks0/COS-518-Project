import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Client for demo v0 
 */
public class DemoClient {
    public static double baseline = 1;
    public static int maxSize = 10;
    
    public static void main(String[] args) {
        List<Table> tables = Table.getTablesFromModel("./data_models/data1.model");
        ArrayList<Query> queryList = QueryLib.getQueryList("query_logs/queries.sql");
//        ArrayList<Query> queryList = QueryLib.getQueryList("query_logs/query_workload500.sql");
        HashSet<TableSubset> subsets = TableSubsetProducer.produce(queryList, baseline, maxSize);
//        System.out.println(subsets.size());
        HashSet<TableSubset> selectedSubsets = new HashSet<TableSubset>();
        int i = 1;
        for (Query query : queryList) {
            ArrayList<TableSubset> candidateSubsets = new ArrayList<TableSubset>();
            for (TableSubset subset : subsets) {
                if (query.referencesColumns(subset.getColumns())) {
                    candidateSubsets.add(subset);
                }
            }
            TableSubset bestSubset = CostEstimator.findBestSubset(query, candidateSubsets);
            selectedSubsets.add(bestSubset);
            System.out.println("\nBest column combination for query " + i++ + ": " + bestSubset);
            System.out.println("Cost : " + CostEstimator.denormalizedCost(query, bestSubset));
            System.out.println("\n\n\n");
        }
    }
}
