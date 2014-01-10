package materializedViews;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Class for representing queries
 */
public class Query {
    public static final String SELECT_LABEL = "select";
    public static final String DELETE_LABEL = "delete";
    public static final String INSERT_LABEL = "insert";
    public static final String UPDATE_LABEL = "update";
    public static final String JOIN_LABEL = "join";
    private String type; // type of query this represents (select, update,
                            // delete, insert)
    private Set<Column> referencedColumns; // list of columns referenced by this
                                            // query
    private Set<Column> whereColumns; // list of columns involved in where by
                                        // this query
    private ArrayList<Pair<Column, Column>> equijoinedColumns; // list of columns equi-joined by
                                            // this query

    /**
     * Default constructor
     */
    Query() {
        referencedColumns = Util518.newHashSet();
        whereColumns = Util518.newHashSet();
        equijoinedColumns = Util518.newArrayList();
    }

    /**
     * Construct an instance of Query based on a supplied SQL query String
     * 
     * @param query
     */
    Query(String query) {
        referencedColumns = Util518.newHashSet();
        whereColumns = Util518.newHashSet();
        equijoinedColumns = Util518.newArrayList();
        computeReferencedColumns(query);
    }

    /**
     * use CrudAnalyzer to analyze what columns are accessed in the query
     */
    private void computeReferencedColumns(String query) {
        String analysis = (new CrudAnalyzer()).analyze(query);
        Scanner scanner = new Scanner(analysis);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line) {
            case SELECT_LABEL:
                type = line;
                break;
            case INSERT_LABEL:
                type = line;
                break;
            case DELETE_LABEL:
                type = line;
                break;
            case UPDATE_LABEL:
                type = line;
                line = scanner.nextLine();
                Table.getInstance(line).incrementUpdates();
                break;
            case JOIN_LABEL:
                line = scanner.nextLine();
                // split <tableName1>.<columnName1> <tableName2>.<columnName2>
                String[] equijoinedColumnsSplit = line.split(" ");
                Pair<String, String> firstNamePair = splitNameString(equijoinedColumnsSplit[0]);
                Pair<String, String> secondNamePair = splitNameString(equijoinedColumnsSplit[1]);
                Column firstColumn = Column.getInstance(
                        firstNamePair.getFirst(), firstNamePair.getSecond());
                Column secondColumn = Column.getInstance(
                        secondNamePair.getFirst(), secondNamePair.getSecond());
                // primary column, then foreign column
                if (!firstColumn.getTable().equals(secondColumn.getTable())) {
                    if (firstColumn.isPrimary()) {
                        equijoinedColumns.add(new Pair<Column, Column>(firstColumn, secondColumn));
                    } else {
                        equijoinedColumns.add(new Pair<Column, Column>(secondColumn, firstColumn));
                    }
                }
                break;
            default:
                Pair<String, String> namePair = splitNameString(line);
                if (namePair.getSecond().equals("*")) {
                    referencedColumns.addAll(Table.getInstance(namePair.getFirst()).getColumns());
                } else {
                    referencedColumns.add(Column.getInstance(namePair.getFirst(),
                        namePair.getSecond()));
                }
                break;
            }
        }
        scanner.close();
    }

    /**
     * splits line of format <tableName>.<columnName> into its components as a
     * Pair of Strings
     * 
     * @param line
     * @return
     */
    public Pair<String, String> splitNameString(String line) {

        String[] split = line.split("\\.");
        if (split.length == 2) {
            return new Pair<String, String>(split[0], split[1]);
        } else {
            throw new RuntimeException(
                    "Parsing table and column name failed for: " + line);
        }
    }

    public Set<Column> getReferencedColumns() {
        return referencedColumns;
    }

    /**
     * If the given columns are referenced in the query
     * 
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

    public ArrayList<Pair<Column, Column>> getEquijoinedColumns() {
        return equijoinedColumns;
    }
}