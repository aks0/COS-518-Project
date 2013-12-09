import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Client for demo v0 
 */
public class DemoClient {
    public static double baseline = 1;
    public static int maxSize = 4;
    
    public static void main(String[] args) {
        List<Table> tables = Table.getTablesFromModel("./data_models/data1.model");
        ArrayList<Query> queryList = QueryLib.getQueryList("query_logs/queries.sql");
        HashSet<TableSubset> subsets = TableSubsetProducer.produce(queryList, baseline, maxSize);
        HashSet<TableSubset> selectedSubsets = new HashSet<TableSubset>();
        for (Query query : queryList) {
            ArrayList<TableSubset> candidateSubsets = new ArrayList<TableSubset>();
            for (TableSubset subset : subsets) {
                if (query.referencesColumns(subset.getColumns())) {
                    candidateSubsets.add(subset);
                }
            }
            TableSubset bestSubset = CostEstimator.findBestSubset(query, candidateSubsets);
            System.out.println(bestSubset);
            System.out.println();
            selectedSubsets.add(bestSubset);
        }
    }
}
