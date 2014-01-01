package materializedViews;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Table {
    // total number of updates to all tables
    private static int totalUpdates;
    // total number of row across all tables
    private static int totalSize;
	private static Map<String, Table> tables = Util518.newHashMap();
	private static ArrayList<Pair<Column, Pair<String, String>>> foreignReferences = Util518
			.newArrayList();

	private String name;
	private ArrayList<Column> columns;
	private int size;
	// number of updates to this table
    private int updates;

	public int getSize() {
		return size;
	}
	
	public static int getTotalSize() {
	    return totalSize;
	}

	/**
	 * Use the factory method {@link Table#getInstance} to get a Table instance.
	 * Ensures uniqueness of Table instances.
	 * 
	 * @param name
	 * @throws Exception
	 */
	private Table(String name) throws Exception {
		if (tables.containsKey(name)) {
			throw new Exception("A table with this name already exists");
		}
		this.name = name;
		columns = Util518.newArrayList();
		tables.put(name, this);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public Table addColumn(Column column) {
		this.columns.add(column);
		return this;
	}

	public ArrayList<Column> getColumns() {
		return this.columns;
	}

    public void incrementUpdates() {
        updates++;
        totalUpdates++;
    }
    
    public int getUpdates() {
        return updates;
    }
	
    public static int getTotalUpdates() {
        return totalUpdates;
    }
    
    public double getUpdateRate() {
        return updates/totalUpdates;
    }
    
	/**
	 * Table factory for generating new tables or returning an already existing
	 * instance. It ensures that only once instance of each table exists.
	 * 
	 * @param table_name
	 * @return Table instance
	 */
	public static Table getInstance(String table_name) {
		if (!tables.containsKey(table_name)) {
			try {
				return new Table(table_name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return tables.get(table_name);
		}
		return null;
	}
	
	/**
	 * Return the table with foreign key (if it exists)
	 * 
	 * @param table1
	 * @param table2
	 * @return
	 */
	public static Table findParentTable(Table table1, Table table2) {
		
		for (Column column1 : table1.getColumns()) {
			for (Column column2 : table2.getColumns()) {
			    // table 1 has foreign version of table 2 column
				if (column1.getForeignKeyReference() != null && column1.getForeignKeyReference().equals(column2))
					return table2;
			}
		}
		
		for (Column column1 : table2.getColumns()) {
			for (Column column2 : table1.getColumns()) {
			    // table 2 has foreign version of table 1 column
				if (column1.getForeignKeyReference() != null && column1.getForeignKeyReference().equals(column2))
					return table1;
			}
		}
		
		return null;
			
	}

	/**
	 * Assumes that the data model is in the following format
	 * 
	 * <model>
	 * 
	 * <table>
	 * <name>table_name</name> <rows>integer number of rows</rows>
	 * <column><name>col1</name></column> <column> <name>col2</name>
	 * <foreign><ftable>tab2</ftable><fcolumn>col_ref</fcolumn></foreign>
	 * </column> <column><name>col3</name> <primary/> </column> <column>
	 * </table>
	 * 
	 * <table>
	 * ....
	 * </table>
	 * 
	 * </model>
	 * 
	 * Foreign Key constraints can be added later when needed.
	 * 
	 * @param model_path
	 * @return Table instance for that data model
	 * @throws FileNotFoundException
	 */
	public static ArrayList<Table> getTablesFromModel(String model_path) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(model_path));
			NodeList xml_tables = doc.getElementsByTagName("table");
			ArrayList<Table> tables = Util518.newArrayList();
			for (int i = 0; i < xml_tables.getLength(); i++) {
				Table table = null;
				try {
					table = getTableFromNode(xml_tables.item(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
				tables.add(table);
			}
			processForeignColumns();
			return tables;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void processForeignColumns() {
		for (Pair<Column, Pair<String, String>> pair : foreignReferences) {
			Column column = pair.getFirst();
			String ftable_name = pair.getSecond().getFirst();
			String fcolumn_name = pair.getSecond().getSecond();
			Column fcolumn = Column.getInstance(ftable_name, fcolumn_name);
			column.setForeignKeyReference(fcolumn);
			/*
			 * System.out.println(
			 * String.format("FColumn: %s references Column %s.", column,
			 * fcolumn) );
			 */
		}
	}

	private static Table getTableFromNode(Node item) throws Exception {
		if (item.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) item;
			String table_name = eElement.getElementsByTagName("name").item(0)
					.getTextContent();
			Table table = getInstance(table_name);
			if (eElement.getElementsByTagName("rows").getLength() == 1) {
				Element row_item = (Element) eElement.getElementsByTagName(
						"rows").item(0);
				table.size = Integer.parseInt(row_item.getTextContent());
				totalSize += table.size;
			}
			NodeList columns = eElement.getElementsByTagName("column");
			for (int i = 0; i < columns.getLength(); i++) {
				Column column = getColumnFromNode(table_name, columns.item(i));
				table.addColumn(column);
			}
			return table;
		}
		return null;
	}

	private static Column getColumnFromNode(String table_name, Node item) {
		if (item.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) item;
			String column_name = eElement.getElementsByTagName("name").item(0)
					.getTextContent();
			Column column = Column.getInstance(table_name, column_name);
			// if the column is defined as primary
			if (eElement.getElementsByTagName("primary").getLength() == 1) {
				column.primary();
			}
			// if the column is defined as a foreign key reference
			if (eElement.getElementsByTagName("foreign").getLength() == 1) {
				processForeignNode(column,
						eElement.getElementsByTagName("foreign").item(0));
			}
			return column;
		}
		return null;
	}

	private static void processForeignNode(Column column, Node foreign_node) {
		if (foreign_node.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) foreign_node;
			Node table_node = eElement.getElementsByTagName("ftable").item(0);
			Node column_node = eElement.getElementsByTagName("fcolumn").item(0);
			String table_name = table_node.getTextContent();
			String column_name = column_node.getTextContent();
			foreignReferences.add(new Pair<Column, Pair<String, String>>(
					column, new Pair<String, String>(table_name, column_name)));
		}
	}

	/**
	 * To support equality among tables based on Table name only.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Table)) {
			return false;
		}
		Table other_table = (Table) obj;
		return this.name.equals(other_table.name);
	}

	/**
	 * HashCode override follows with overriding the equals method.
	 * 
	 * If table1.equals(table2), then table1.hashCode() == table2.hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.name.hashCode();
		return result;
	}

	/**
	 * For testing
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		List<Table> tables = Table
				.getTablesFromModel("./data_models/data1.model");
		for (Table table : tables) {
			System.out.print("(" + table.getSize() + ") ");
			System.out.println(table.getColumns());
		}
	}
}
