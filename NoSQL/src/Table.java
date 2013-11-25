import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Table {

	private static Map<String, Table> tables = new HashMap<String, Table>();
	private String name;
	private ArrayList<Column> columns;
	// TODO: need to set this parameter
	private int size;
	
	public int getSize() {
		return size;
	}

	/**
	 * Use the factory method {@link Table#getInstance} to get a Table
	 * instance. Ensures uniqueness of Table instances.
	 * @param name
	 * @throws Exception
	 */
	private Table(String name) throws Exception {
		if (tables.containsKey(name)) {
			throw new Exception("A table with this name already exists");
		}
		this.name = name;
		columns = new ArrayList<Column>();
		tables.put(name, this);
	}
	
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

	/**
	 * Table factory for generating new tables or returning an already
	 * existing instance. It ensures that only once instance of each table
	 * exists.
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
	 * Assumes that the data model is in the following format
	 * 
	 * <model>
	 * 
	 * <table>
	 * <name>table_name</name>
	 * <column>col1</column>
	 * <column>col2</column>
	 * <column>col3
	 * <primary/>
	 * </column>
	 * </table>
	 * 
	 * <table>....</table>
	 *
	 * </model>
	 * 
	 * Foreign Key constraints can be added later when needed.
	 * @param model_path
	 * @return Table instance for that data model
	 * @throws FileNotFoundException
	 */
	public static ArrayList<Table> getTablesFromModel(String model_path)
			throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File(model_path));
		NodeList xml_tables = doc.getElementsByTagName("table");
		ArrayList<Table> tables = new ArrayList<Table>();
		for (int i = 0; i < xml_tables.getLength(); i++) {
			Table table = null;
			try {
				table = getTableFromNode(xml_tables.item(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
			tables.add(table);
		}
		return tables;
	}
	
	private static Table getTableFromNode(Node item) throws Exception {
		if (item.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) item;
			String table_name =
				eElement.getElementsByTagName("name").item(0).getTextContent();
			Table table = getInstance(table_name);
			NodeList columns = eElement.getElementsByTagName("column");
			for (int i = 0; i < columns.getLength(); i++) {
				Column column = getColumnFromNode(table, columns.item(i));
				table.addColumn(column);
			}
			return table;
		}
		return null;
	}

	private static Column getColumnFromNode(Table table, Node item) {
		if (item.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) item;
			String column_name = item.getTextContent();
			Column column = new Column(table, column_name);
			if (eElement.getElementsByTagName("primary").getLength() == 1) {
				column.primary();
			}
			return column;
		}
		return null;
	}

	/**
	 * For testing
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void main(String args[])
			throws SAXException, IOException, ParserConfigurationException {
		List<Table> tables = Table.getTablesFromModel("./data_models/data1.model");
		for(Table table : tables) {
			System.out.println(table.getColumns());
		}
	}
}
