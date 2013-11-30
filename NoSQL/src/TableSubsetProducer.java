

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NavigableSet;


public class TableSubsetProducer {
	
	/**
	 * Given quries, baseline threshold, and maxSize, produces candidate list of table subsets
	 * 
	 * @param queries
	 * @param baseline
	 * @param maxSize
	 * @return
	 */
	public static ArrayList<TableSubset> produce(ArrayList<Query> queries, double baseline, int maxSize) {
		ArrayList<HashMap<TableSubset, Integer>> candidateSets = new ArrayList<HashMap<TableSubset, Integer>>();
		
		// Produce table subsets of size 1
		candidateSets.add(new HashMap<TableSubset, Integer>());
		HashMap<TableSubset, Integer> singleton = candidateSets.get(0);
		
		for (Query query : queries) {
			for (Column column : query.getReferencedColumns()) {
				TableSubset subset = new TableSubset();
				subset.addColumn(column);
				if (!singleton.containsKey(subset))
					singleton.put(subset, 1);
			}
		}
		
		// Algorithm for finding subsets of increasing size
		int i = 1;
		while (i < maxSize && candidateSets.get(i).keySet().size() > 0) {
			HashMap<TableSubset, Integer> candidateSet = candidateSets.get(i - 1);
			HashMap<TableSubset, Integer> newCandidateSet = new HashMap<TableSubset, Integer>();
			
			for (Query query : queries) {
				TableSubset tbset = new TableSubset(query.getReferencedColumns());
				ArrayList<TableSubset> sizedCombos = combos(tbset.getColumns(), i);
				for (TableSubset subset : sizedCombos) {
					if (!newCandidateSet.containsKey(subset)) {
						ArrayList<TableSubset> smallerSubsets = subsetOneSizeSmaller(subset);
						for (TableSubset smallerSubset : smallerSubsets) {
							if (candidateSet.containsKey(smallerSubset) && CostEstimator.normalizedCost(subset, queries) >= baseline) {
								newCandidateSet.put(subset,1);
							}
						}
					}
				}
			}
			
			candidateSets.add(newCandidateSet);
			i++;
		}
		
		return null;
	}
	
	/**
	 * Given subset of size n, returns all subset of size (n - 1)
	 * 
	 * @param subset
	 * @return
	 */
	private static ArrayList<TableSubset> subsetOneSizeSmaller(TableSubset subset) {
		ArrayList<TableSubset> allSubsets = new ArrayList<TableSubset>();
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
		ArrayList<TableSubset> allSubsets = new ArrayList<TableSubset>();

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
