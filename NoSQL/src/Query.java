import java.util.ArrayList;
import java.util.Scanner;

/**
 *  Class for representing queries
 */
public class Query {
    private String type; // type of query this represents (select, update, delete, insert)
	private ArrayList<Column> referencedColumns;  // list of columns referenced by this query
	
	/**
	 * Default constructor
	 */
	Query() {
		referencedColumns = new ArrayList<Column>();
	}
	
	/**
	 * Construct an instance of Query based on a supplied
	 * SQL query String
	 * @param query
	 */
	Query(String query) {
		referencedColumns = new ArrayList<Column>();
		
		// use CrudAnalyzer to analyze what columns are accessed in the query
	    String analysis = (new CrudAnalyzer()).analyze(query);
	    Scanner scanner = new Scanner(analysis);
	    while (scanner.hasNextLine()) {
	        String line = scanner.nextLine();
	        switch(line) {
	            case CrudAnalyzer.SELECT_LABEL:
	            case CrudAnalyzer.INSERT_LABEL:
	            case CrudAnalyzer.DELETE_LABEL:
	            case CrudAnalyzer.UPDATE_LABEL:
	                type = line;
	                break;
	            default:
	                // split line = <tableName>.<columnName> into its components
	                String[] split = line.split("\\.");
	                if (split.length == 2) {
	                    String tableName = split[0];
	                    String columnName = split[1];
	                    Table table = Table.getInstance(tableName);
                        referencedColumns.add(new Column(table, columnName));
	                }
	                break;
	        }
	    }
	    scanner.close();
	}

	public ArrayList<Column> getReferencedColumns() {
	    return referencedColumns;
	}
	
	public Query addReferencedColumn(Column column) {
		this.referencedColumns.add(column);
		return this;
	}
	
	public Column getReferencedColumn(int index) {
		return this.referencedColumns.get(index);
	}
}
