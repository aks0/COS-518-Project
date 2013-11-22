
public class Column {
	private Table table;
	private String name;
	private Column foreignKeyReferencing;
	private boolean isPrimary;
	
	Column(Table table, String name) {
		this.table = table;
		this.name = name;
		foreignKeyReferencing = null;
		isPrimary = false;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public void setForeignKeyReferencing(Column foreignKeyFor) {
		this.foreignKeyReferencing = foreignKeyFor;
	}
}
