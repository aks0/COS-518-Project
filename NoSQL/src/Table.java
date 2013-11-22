import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Table {

	private String name;
	private ArrayList<Column> columns;
	
	Table(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
	
	public Table addColumn(Column column) {
		this.columns.add(column);
		return this;
	}
	
	/**
	 * Assumes that the data model is in the following format
	 * 
	 * table_name
	 * col1
	 * col2
	 * col3 : primary
	 * .
	 * .
	 * .
	 * coln
	 * 
	 * Foreign Key constraints can be added later when needed.
	 * @param model_path
	 * @return Table instance for that data model
	 * @throws FileNotFoundException
	 */
	public static Table getTableFromModel(String model_path) throws FileNotFoundException {
		Scanner ob = new Scanner(new File(model_path));
		String name = ob.nextLine();
		Table table = new Table(name);
		while(ob.hasNextLine()) {
			String column_info = ob.nextLine();
			StringTokenizer data = new StringTokenizer(column_info, ": ");
			Column column = new Column(table, data.nextToken());
			if (data.hasMoreTokens()) {
				if (data.nextToken().equals("primary")) {
					column.primary();
				}
			}
			table.addColumn(column);
		}
		ob.close();
		return table;
	}
}
