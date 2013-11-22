import java.util.ArrayList;


public class TableSubset {
	private ArrayList<Column> tableset;
	
	TableSubset() {
		tableset = new ArrayList<Column>();
	}
	
	public TableSubset addColumn(Column column) {
		this.tableset.add(column);
		return this;
	}
	
	public Column getColumn(int index) {
		return this.tableset.get(index);
	}
}
