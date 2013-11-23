import java.util.ArrayList;
import java.util.Scanner;

/**
 *  Class for representing queries
 */
public class Query {
	private ArrayList<Column> selectColumns;
	private ArrayList<Column> insertColumns;
	
	/**
	 * Default constructor
	 */
	Query() {
		selectColumns = new ArrayList<Column>();
		insertColumns = new ArrayList<Column>();
	}
	
	/**
	 * Construct an instance of Query based on a supplied
	 * SQL query String
	 * @param query
	 */
	Query(String query) {
		selectColumns = new ArrayList<Column>();
		insertColumns = new ArrayList<Column>();
		
		ArrayList<Column> columns = null;
		// use CrudAnalyzer to analyze what columns are accessed in the query
	    String analysis = (new CrudAnalyzer()).analyze(query);
	    Scanner scanner = new Scanner(analysis);
	    while (scanner.hasNextLine()) {
	        String line = scanner.nextLine();
	        switch(line) {
	            case CrudAnalyzer.SELECT_LABEL:
	                columns = selectColumns;
	                break;
	            case CrudAnalyzer.INSERT_LABEL:
	                columns = insertColumns;
	                break;
	            default:
	                // split line = <tableName>.<columnName> into its components
	                String[] split = line.split("\\.");
	                if (split.length == 2 && columns != null) {
	                    String tableName = split[0];
	                    String columnName = split[1];
                        columns.add(new Column(new Table(tableName), columnName));
	                }
	                break;
	        }
	    }
	    scanner.close();
	}

	public ArrayList<Column> getSelectColumns() {
	    return selectColumns;
	}
	
	public ArrayList<Column> getInsertColumns() {
	    return insertColumns;
	}
	
	public Query addSelectColumn(Column column) {
		this.selectColumns.add(column);
		return this;
	}
	
	public Column getSelectColumn(int index) {
		return this.selectColumns.get(index);
	}
}
