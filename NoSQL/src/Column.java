import java.util.HashMap;
import java.util.Map;


public class Column {

	private static Map<String, Column> columns = new HashMap<String, Column>();
	private Table table;
	private String name;
	private Column foreignKeyReference;
	private boolean isPrimary;

	/**
	 * Use the factory method {@link Column#getInstance} to get a Column
	 * instance. Ensures uniqueness of Column instances.
	 * @param table
	 * @param col_name
	 * @throws Exception
	 */
	private Column(Table table, String col_name) throws Exception {
		if (columns.containsKey(toString(table, col_name))) {
			throw new Exception("A column with this name already exists");
		}
		this.table = table;
		this.name = col_name;
		foreignKeyReference = null;
		isPrimary = false;
	}
	
	public Table getTable() {
		return table;
	}
	
	public Column setForeignKeyReference(Column reference) {
		this.foreignKeyReference = reference;
		return this;
	}
	
	public Column getForeignKeyReference() {
		return this.foreignKeyReference;
	}
	
	public boolean isForeignReference() {
	    return this.foreignKeyReference != null;
	}

	public Column primary() {
		this.isPrimary = true;
		return this;
	}
	
	public boolean isPrimary() {
	    return this.isPrimary;
	}

	/**
	 * To support equality among columns based on Table name and Column name
	 * only.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Column)) {
			return false;
		}
		Column other_column = (Column) obj;
		boolean is_column_same = this.name.equals(other_column.name);
		boolean is_table_same =
				this.table.getName().equals(other_column.table.getName());
		return is_column_same && is_table_same;
	}
	
	/**
	 * HashCode override follows with overriding the equals method.
	 * 
	 * If col1.equals(col2), then col1.hashCode() == col2.hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.name.hashCode();
		result = 31 * result + this.table.getName().hashCode();
		return result;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return table.toString() + "#" + name;
	}
	
	private static String toString(Table table, String col_name) {
		return table.toString() + "#" + col_name;
	}

	/**
	 * Column factory for generating new Columns or returning an already
	 * existing instance. It ensures that only once instance of each column
	 * exists.
	 * @param table
	 * @param column_name
	 * @return Column instance
	 */
	public static Column getInstance(Table table, String col_name) {
		String name = toString(table, col_name);
		if (!columns.containsKey(name)) {
			try {
				return new Column(table, col_name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return columns.get(name);
		}
		return null;
	}
}
