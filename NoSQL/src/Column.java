
public class Column {
	private Table table;
	public Table getTable() {
		return table;
	}

	private String name;
	private Column foreignKeyReference;
	private boolean isPrimary;
	
	Column(Table table, String name) {
		this.table = table;
		this.name = name;
		foreignKeyReference = null;
		isPrimary = false;
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
	
	@Override
	public String toString() {
		return table.toString() + "#" + name;
	}
}
