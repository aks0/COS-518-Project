import gudusoft.gsqlparser.JoinRelationAnalyze;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *  Class for representing queries
 */
public class Query {
    public static final String SELECT_LABEL = "select";
    public static final String DELETE_LABEL = "delete";
    public static final String INSERT_LABEL = "insert";
    public static final String UPDATE_LABEL = "update";
    public static final String JOIN_LABEL = "join";
    public static final String WHERE_LABEL = "where";
    private String type; // type of query this represents (select, update, delete, insert)
    private Set<Column> referencedColumns;  // list of columns referenced by this query
    private Set<Column> whereColumns;  // list of columns involved in where by this query
    private Set<Pair<Column, Column>> joinedColumns;  // list of columns joined by this query
    
    /**
     * Default constructor
     */
    Query() {
        referencedColumns = new HashSet<Column>();
        whereColumns = new HashSet<Column>();
        joinedColumns = new HashSet<Pair<Column, Column>>();
    }
    
    /**
     * Construct an instance of Query based on a supplied
     * SQL query String
     * @param query
     */
    Query(String query) {
        referencedColumns = new HashSet<Column>();
        whereColumns = new HashSet<Column>();
        joinedColumns = new HashSet<Pair<Column, Column>>();
        computeReferencedColumns(query);
    }

    /**
     *  use CrudAnalyzer to analyze what columns are accessed in the query
     */
    private void computeReferencedColumns(String query) {
        String analysis = (new CrudAnalyzer()).analyze(query);
        Scanner scanner = new Scanner(analysis);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch(line) {
                case SELECT_LABEL:
                case INSERT_LABEL:
                case DELETE_LABEL:
                case UPDATE_LABEL:
                    type = line;
                    break;
                case WHERE_LABEL:
                    line = scanner.nextLine();
                    Pair<String, String> namePair = splitNameString(line);
                    whereColumns.add(
                            Column.getInstance(namePair.getFirst(), namePair.getSecond()));
                    break;
                case JOIN_LABEL:
                    line = scanner.nextLine();
                    // split <tableName1>.<columnName1> <tableName2>.<columnName2>
                    String[] joinedColumnsSplit = line.split(" ");
                    Pair<String, String> firstNamePair = splitNameString(joinedColumnsSplit[0]);
                    Pair<String, String> secondNamePair = splitNameString(joinedColumnsSplit[1]);
                    Column firstColumn = Column.getInstance(firstNamePair.getFirst(), firstNamePair.getSecond());
                    Column secondColumn = Column.getInstance(secondNamePair.getFirst(), secondNamePair.getSecond());
                    // in cases that primary and foreign column joined, put primary column as first
                    if (!firstColumn.isPrimary()) {
                        Column temp = firstColumn;
                        firstColumn = secondColumn;
                        secondColumn = temp;
                    }
                    getJoinedColumns().add(new Pair<Column, Column>(firstColumn, secondColumn));
                    break;
                default:
                    namePair = splitNameString(line);
                    referencedColumns.add(
                            Column.getInstance(namePair.getFirst(), namePair.getSecond()));
                    break;
            }
        }
        scanner.close();
    }
    
    /**
     * splits line of format <tableName>.<columnName> into its components
     * as a Pair of Strings
     * @param line
     * @return
     */
    public Pair<String, String> splitNameString(String line) {
        
        String[] split = line.split("\\.");
        if (split.length == 2) {
            return new Pair<String, String>(split[0], split[1]);
        } else {
            throw new RuntimeException("Parsing table and column name failed for: " + line);
        }
    }

    public Set<Column> getReferencedColumns() {
        return referencedColumns;
    }

    /**
     * If the given columns are referenced in the query
     * @param columns
     * @return true if the given columns are referenced in the query
     */
    public boolean referencesColumns(Set<Column> columns) {
        if (columns == null) {
            return false;
        }
        for (Column column : columns) {
            if (!this.referencedColumns.contains(column)) {
                return false;
            }
        }
        return true;
    }

    public Set<Column> getWhereColumns() {
        return whereColumns;
    }
    
    public Set<Pair<Column, Column>> getJoinedColumns() {
        return joinedColumns;
    }
}
