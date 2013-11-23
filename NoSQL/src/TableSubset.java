import java.util.ArrayList;


public class TableSubset {
	private ArrayList<Column> tableset;
	
	TableSubset() {
		tableset = new ArrayList<Column>();
	}
	
	TableSubset(ArrayList<Column> columns) {
		tableset = new ArrayList<Column>(columns);
	}
	
	public TableSubset addColumn(Column column) {
		this.tableset.add(column);
		return this;
	}
	
	public Column getColumn(int index) {
		return this.tableset.get(index);
	}
	
	public ArrayList<Column> getColumns() {
		return this.tableset;
	}
}
