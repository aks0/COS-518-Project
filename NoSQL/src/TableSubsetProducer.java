

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;


public class TableSubsetProducer {
    
    /**
     * Given queries, baseline threshold, and maxSize, produces candidate list of table subsets
     * 
     * @param queries
     * @param baseline
     * @param maxSize
     * @return
     */
    public static HashSet<TableSubset> produce(ArrayList<Query> queries, double baseline, int maxSize) {
        ArrayList<HashSet<TableSubset>> candidateSets = Util518.newArrayList();
        
        // Produce table subsets of size 1
        candidateSets.add(new HashSet<TableSubset>());
        HashSet<TableSubset> singleton = candidateSets.get(0);
        
        for (Query query : queries) {
            if (CostEstimator.normalizedCost(query) >= baseline) {
                for (Column column : query.getReferencedColumns()) {
                    TableSubset subset = new TableSubset();
                    subset.addColumn(column);
                    singleton.add(subset);
                }
            }
        }
        
        // Algorithm for finding subsets of increasing size
        int i = 1;
        int subsetSize = i + 1;
        while (subsetSize < maxSize && candidateSets.get(i - 1).size() > 0) {
            HashSet<TableSubset> oldCandidateSet = candidateSets.get(i - 1);
            HashSet<TableSubset> newCandidateSet = Util518.newHashSet();
            
            for (Query query : queries) {
                TableSubset querySubset = new TableSubset(query.getReferencedColumns());
                ArrayList<TableSubset> sizedCombos = combos(querySubset.getColumns(), subsetSize);
                for (TableSubset subset : sizedCombos) {
                    if (!newCandidateSet.contains(subset)) {
                        ArrayList<TableSubset> smallerSubsets = subsetOneSizeSmaller(subset);
                        for (TableSubset smallerSubset : smallerSubsets) {
                            if (oldCandidateSet.contains(smallerSubset) && CostEstimator.normalizedCost(subset, queries) >= baseline) {
                                newCandidateSet.add(subset);
                            }
                        }
                    }
                }
            }
            
            candidateSets.add(newCandidateSet);
            i++;
            subsetSize++;
        }
        
        HashSet<TableSubset> combinedSubsets = new HashSet<TableSubset>();
        for (HashSet<TableSubset> candidateSet : candidateSets) {
            combinedSubsets.addAll(candidateSet);
        }
        return combinedSubsets;
    }
    
    /**
     * Given subset of size n, returns all subset of size (n - 1)
     * 
     * @param subset
     * @return
     */
    private static ArrayList<TableSubset> subsetOneSizeSmaller(TableSubset subset) {
        ArrayList<TableSubset> allSubsets = Util518.newArrayList();
        for (Column column1 : subset.getColumns()) {
            TableSubset smallSubset = new TableSubset();
            for (Column column2 : subset.getColumns()) {
                if (!column1.equals(column2)) {
                    smallSubset.addColumn(column2);
                }
            }
            allSubsets.add(smallSubset);
        }
        
        return allSubsets;
    }
    
    /**
     * Returns all subsets of size 'size' from table subset passed in
     * 
     * @param subset
     * @param offset
     * @param size
     * @return
     */
    private static ArrayList<TableSubset> combos(
            NavigableSet<Column> columns, int size) {
        ArrayList<TableSubset> allSubsets = Util518.newArrayList();

        // if size is 0 then return empty subset
        if (size == 0) {
            allSubsets.add(new TableSubset());
            return allSubsets;
        }

        int i = 0;
        for (Iterator<Column> iter = columns.iterator();
            i < (columns.size() - size + 1) && iter.hasNext();
            i++) {
            Column pivot_col = iter.next();
            ArrayList<TableSubset> subsets =
                combos(columns.tailSet(pivot_col, false), size-1);
            for (TableSubset smallSubset: subsets) {
                smallSubset.addColumn(pivot_col);
            }
            allSubsets.addAll(subsets);
        }
        return allSubsets;
    }
}
