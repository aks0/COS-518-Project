import java.util.ArrayList;


public class Query {

	private ArrayList<Column> columns;
	
	Query() {
		columns = new ArrayList<Column>();
	}

	public Query addColumn(Column column) {
		this.columns.add(column);
		return this;
	}
	
	public Column getColumn(int index) {
		return this.columns.get(index);
	}
}
