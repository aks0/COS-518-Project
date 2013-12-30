package materializedViews;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Library for turning a queries log into a list of Query objects
 */
public class QueryLib {

	/**
	 * Creates an ArrayList containing a Query object for every query in the
	 * file at filepath.
	 * 
	 * @param filepath
	 * @return ArrayList of Query objects
	 */
	public static ArrayList<Query> getQueryList(String filepath) {
		StringBuilder query = new StringBuilder();
		ArrayList<Query> queryList = Util518.newArrayList();
		try {
			Scanner scanner = new Scanner(new File(filepath));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// queries in the file are separated by empty lines
				if (line.isEmpty()) {
					queryList.add(new Query(query.toString()));
					query = new StringBuilder();
				} else {
					query.append(line + "\n");
					// append if EOF is reached
					if (!scanner.hasNextLine()) {
						queryList.add(new Query(query.toString()));
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return queryList;
	}

	/**
	 * test main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<Table> tables = Table
				.getTablesFromModel("./data_models/data1.model");
		ArrayList<Query> queries = getQueryList("query_logs/queries.sql");
		for (Query query : queries) {
			for (Column column : query.getReferencedColumns()) {
				System.out.println(column);
			}
			System.out.println();
		}
	}
}
