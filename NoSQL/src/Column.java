
public class Column {
	private Table table;
	private String name;
	private Column foreignKeyReference;
	private boolean isPrimary;

	Column(Table table, String name) {
		this.table = table;
		this.name = name;
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

	@Override
	public String toString() {
		return table.toString() + "#" + name;
	}
}
