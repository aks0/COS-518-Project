import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QueryGenerator {

    private final static String qInnerTempl =
            "SELECT %s \nFROM %s \nWHERE %s = \"test\"";
    private final static String qOuterTempl =
            "SELECT %s \nFROM %s \nWHERE %s IN \n(%s)";

    public static String genOuterQuery(List<Table> tables) {
        if (tables == null || tables.size() == 0) {
            return null;
        }

        Pair<String, Column> inner_data = genInnerQuery(tables);
        String inner_query = inner_data.getFirst();
        Column inner_select_col = inner_data.getSecond();
        
        Random rand = new Random();
        Column where_column = inner_select_col.isForeignReference()
                ? inner_select_col.getForeignKeyReference()
                : inner_select_col;

        // compute the tables to be used in the join
        List<Table> join_tables = new ArrayList<Table>();
        join_tables.add(where_column.getTable());
        Table join_table2 = null;
        while (join_table2 == null) {
            Table candidate = tables.get(rand.nextInt(tables.size()));
            if (candidate.equals(join_tables.get(0))) {
                continue;
            }
            join_table2 = candidate;
        }
        join_tables.add(join_table2);

        // compute the select columns
        List<Column> select_columns = new ArrayList<Column>();
        for (int i = 0; i < 3; i++) {
            Table r_table = join_tables.get(rand.nextInt(join_tables.size()));
            ArrayList<Column> columns = r_table.getColumns();
            select_columns.add(columns.get(rand.nextInt(columns.size())));
        }

        // add the select columns
        String str_select_columns = "";
        for (Column col : select_columns) {
            String column_name = col.getTable().getName() + "." + col.getName();
            str_select_columns += column_name + ", ";
        }
        str_select_columns =
                str_select_columns.substring(0, str_select_columns.length()-2);

        // add the join tables
        String str_join_tables = "";
        for (Table table : join_tables) {
            str_join_tables += table.getName() + ", ";
        }
        str_join_tables =
                str_join_tables.substring(0, str_join_tables.length()-2);

        // add the where column
        String str_where_column = where_column.getTable().getName() +
                "." + where_column.getName();

        // add to the query
        String query = String.format(
                qOuterTempl,
                str_select_columns,
                str_join_tables,
                str_where_column,
                inner_query
        );
        return query;
    }
    
    public static Pair<String, Column> genInnerQuery(List<Table> tables) {
        if (tables == null || tables.size() == 0) {
            return null;
        }
        Random rand = new Random();
        Table table = tables.get(rand.nextInt(tables.size()));
        List<Column> columns = table.getColumns();
        Column select_column = null;
        boolean is_primary = false;
        for (Column col : columns) {
            if (col.isForeignReference()) {
                // if no foreign column has been found yet
                if (select_column == null || is_primary) {
                    select_column = col;
                // a previous foreign column is found, replace with 50%
                // probability 
                } else if (rand.nextDouble() > .5) {
                    select_column = col;
                }
            } else if (col.isPrimary()) {
                select_column = col;
                is_primary = true;
            }
        }
        
        Column where_column = columns.get(rand.nextInt(columns.size()));
        String query = String.format(
                qInnerTempl,
                table.getName() + "." + select_column.getName(),
                table.getName(),
                table.getName() + "." + where_column.getName()
        );
        return new Pair<String, Column>(query, select_column);
    }

    public static void main(String[] args) throws IOException {
        List<Table> tables =
                Table.getTablesFromModel("./data_models/data1.model");
        System.out.println(genOuterQuery(tables));
        int workload_size = 10;
        PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new FileWriter("./query_logs/query_workload" +
                                workload_size +".sql")));
        for (int i = 0; i < workload_size; i++) {
            pw.println(genOuterQuery(tables));
            pw.println();
        }
        pw.close();
    }
}