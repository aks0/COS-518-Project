import java.util.ArrayList;


public class Table {

	private String name;
	private ArrayList<Column> columns;
	
	Table(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
