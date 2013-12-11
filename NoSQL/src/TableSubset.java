import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class TableSubset {
    private TreeSet<Column> tableset;
    
    TableSubset() {
        tableset = new TreeSet<Column>(new ColumnComparator());
    }
    
    TableSubset(Set<Column> columns) {
        tableset = new TreeSet<Column>(new ColumnComparator());
        tableset.addAll(columns);
    }
    
    public TableSubset addColumn(Column column) {
        this.tableset.add(column);
        return this;
    }
    
    public TreeSet<Column> getColumns() {
        return this.tableset;
    }

    /**
     * To support equality among TableSubsets based on sorted Column names
     * only.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TableSubset)) {
            return false;
        }
        TableSubset other_table_subset = (TableSubset) obj;
        if (this.tableset.size() != other_table_subset.tableset.size()) {
            return false;
        }

        boolean is_equal = true;
        Iterator<Column> iter1 = this.tableset.iterator();
        Iterator<Column> iter2 = other_table_subset.tableset.iterator();
        while(is_equal && iter1.hasNext() && iter2.hasNext()) {
            is_equal = is_equal && iter1.next().equals(iter2.next());
        }
        return is_equal;
    }
    
    /**
     * HashCode override follows with overriding the equals method.
     * 
     * If tbset1.equals(tbset2), then tbset1.hashCode() == tbset2.hashCode()
     */
    @Override
    public int hashCode() {
        int result = 17;
        int MOD = 1000000007;
        for (Iterator<Column> iter = tableset.iterator(); iter.hasNext();) {
            result = (31 * result + iter.next().hashCode()) % MOD;
        }
        return result;
    }

    class ColumnComparator implements Comparator<Column>{

        @Override
        public int compare(Column c1, Column c2) {
            String table1 = c1.getTable().getName();
            String table2 = c2.getTable().getName();
            int diff = table1.compareTo(table2);
            if (diff != 0) {
                return diff;
            } else {
                return c1.getName().compareTo(c2.getName());
            }
        }
    }

    @Override
    public String toString() {
        return this.tableset.toString();
    }

    /** For testing
     *
     * @param args
     */
    public static void main(String args[]) {
        List<Table> tables =
                Table.getTablesFromModel("./data_models/data1.model");
        Set<Column> columns = new HashSet<Column>();
        for (Table table : tables) {
            columns.addAll(table.getColumns());
        }
        TableSubset tbset = new TableSubset(columns);
        // should produced columns sorted by table and then sorted by column
        System.out.println(tbset);
    }
}
