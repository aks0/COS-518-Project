import gudusoft.gsqlparser.*;

/**
 * GSP client for analyzing tables and columns accessed
 * from provided SQL queries 
 */
public class CrudAnalyzer {
    private static final EDbVendor dbVendor = EDbVendor.dbvmssql;

    /**
     * Analyze the provided SQL query String for
     * information on accessed tables and columns
     * in CRUD
     * @param query
     * @return String representation of analysis
     * in the form of:
     *    statement type
     *    tableName.columnName
     *    ...
     */
    public String analyze(String query) {
        StringBuilder builder = new StringBuilder();
        // use GSP to find all statements in the SQL
        TGSqlParser sqlparser = new TGSqlParser(dbVendor);
        // get Join analysis
        builder.append(new JoinRelationAnalyze(query).getAnalysisResult());
        sqlparser.sqltext = query;
        int ret = sqlparser.parse();
        
        if (ret == 0){
            // iterate over every statement found to fill in tables and columns arraylists
            for (int i = 0; i < sqlparser.sqlstatements.size(); i++){
                analyzeStmt(sqlparser.sqlstatements.get(i), builder);
            }
        } else {
            System.err.println(sqlparser.getErrormessage() + "\n");
        }
        return builder.toString();
    }

    /**
     * Uses statement object stmt to fill in StringBuilder result
     * with lines in the form of:
     *    statement type
     *    tableName.columnName
     *    ...
     * @param stmt
     * @param result
     */
    private void analyzeStmt(TCustomSqlStatement stmt, StringBuilder result){
        for (int i = 0; i < stmt.tables.size(); i++) {
            if (stmt.tables.getTable(i).isBaseTable()) {
                // corner case
                String operation = stmt.tables.getTable(i).getFullName();
                if (operation.equalsIgnoreCase("deleted") || operation.equalsIgnoreCase("inserted")) {
                    continue;
                }
                
                // determine statement type and append to result
                switch(stmt.sqlstatementtype) {
                case sstselect:
                    result.append(Query.SELECT_LABEL + "\n");
                    break;
                case sstinsert:
                    result.append(Query.INSERT_LABEL + "\n");
                    break;
                case sstdelete:
                    result.append(Query.DELETE_LABEL + "\n");
                    break;
                case sstupdate:
                    result.append(Query.UPDATE_LABEL + "\n");
                    break;
                default:
                    break;
                }
                // find tables and columns from the statement and append
                // tableName.columnName to result
                for (int j = 0; j < stmt.tables.getTable(i).getObjectNameReferences().size(); j++) {
                    // append table name
                    result.append(stmt.tables.getTable(i).getFullName());
                    result.append(".");
                    // append column name
                    result.append(stmt.tables.getTable(i).getObjectNameReferences().getObjectName(j).getColumnNameOnly());
                    result.append("\n");
                }
            } 
        }
        if (stmt.getWhereClause() != null) {
            result.append(new WhereCondition(stmt.getWhereClause().getCondition()).getColumnsString());
        }
        for (int i = 0; i < stmt.getStatements().size(); i++){
           analyzeStmt(stmt.getStatements().get(i), result);
        }
    }
}