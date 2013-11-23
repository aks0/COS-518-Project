import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.nodes.TTable;

/**
 * Modified GSP client for analyzing create/read/update/delete statements
 * from provided SQL query files
 * @originalAuthor - Gudu Software
 */
public class CrudAnalyzer {
    public static final String SELECT_LABEL = "select";
    public static final String CREATE_TABLE_LABEL = "createtable";
    public static final String DELETE_LABEL = "delete";
    public static final String INSERT_LABEL = "insert";
    public static final String UPDATE_LABEL = "update";
    
    enum crudAction  {
        select(0, SELECT_LABEL),
        createtable(1,CREATE_TABLE_LABEL),
        delete(2,DELETE_LABEL) ,
        insert(3,INSERT_LABEL),
        update(4,UPDATE_LABEL);
    
        private final int id;
    
        public int getId() {
            return id;
        }
    
        public String getLabel() {
            return label;
        }
    
        private final String label;
    
        crudAction(final int id, final String label) {
            this.id = id;
            this.label = label;
        }
    
    };
    
    class tableEffected{
    //    public final static int select = 0;
    //    public final static int createtable = 1;
    //    public final static int delete = 2;
    //    public final static int insert = 3;
    //    public final static int update = 4;
    
        TObjectName tablename;
        int[] statetments;
        public tableEffected(){
            statetments = new int[5];
        }
    }
    
    class crud{
       public final static int 	max_tables = 100;
       tableEffected[] tables ;
       int total_tables;
       EDbVendor dbvendor;
       String sql;
    
    
        public crud(EDbVendor db, String sql){
            this.dbvendor = db;
            this.sql = sql;
            tables = new tableEffected[max_tables];
            total_tables = 0;
        }
    
        public String run(){
            TGSqlParser sqlparser = new TGSqlParser(this.dbvendor);
            sqlparser.setSqltext(sql);
            int ret = sqlparser.parse();
            if (ret != 0){
                return sqlparser.getErrormessage();
            }
    
            StringBuilder builder = new StringBuilder();
            for (int i=0;i<sqlparser.sqlstatements.size();i++){
                builder.append(analyzeStmt(sqlparser.sqlstatements.get(i)));
            }
            return builder.toString();
        }
    
        protected String analyzeStmt(TCustomSqlStatement stmt){
            String analysis = "";
            for(int i=0;i<stmt.tables.size();i++){
                if  (stmt.tables.getTable(i).isBaseTable())
                {
                    if ( (stmt.dbvendor == EDbVendor.dbvmssql)
                            &&( (stmt.tables.getTable(i).getFullName().equalsIgnoreCase("deleted"))
                                ||(stmt.tables.getTable(i).getFullName().equalsIgnoreCase("inserted"))
                               )
                      ){
                        continue;
                    }
    
                  switch(stmt.sqlstatementtype){
                      case sstselect:
                          analysis += addTable(stmt.tables.getTable(i),crudAction.select);
                          break;
                      case sstcreatetable:
                          analysis += addTable(stmt.tables.getTable(i),crudAction.createtable);
                          break;
                      case sstdelete:
                          if (i == 0){
                              analysis += addTable(stmt.tables.getTable(i),crudAction.delete);
                          }else{
                              analysis += addTable(stmt.tables.getTable(i),crudAction.select);
                          }
                          break;
                      case sstinsert:
                          analysis += addTable(stmt.tables.getTable(i),crudAction.insert);
                          break;
                      case sstupdate:
                          if (i == 0){
                            analysis += addTable(stmt.tables.getTable(i),crudAction.update);
                          }else{
                            analysis += addTable(stmt.tables.getTable(i),crudAction.select);
                          }
                          break;
                      default:
                          System.out.println(stmt.sqlstatementtype.toString()+" was not supported, you can extend this demo to support this kind SQL statement.");
                          break;
                  }
    
                }
                //System.out.println(stmt.tables.getTable(i).getFullName());
            }
    
            for (int i=0;i<stmt.getStatements().size();i++){
               analyzeStmt(stmt.getStatements().get(i));
            }
            return analysis;
        }
    
    
        String addTable(TTable pTable,crudAction pkind){
            StringBuilder result = new StringBuilder();
            int kind = pkind.getId();
    
            TObjectName tablename;
            tablename = pTable.getTableName();
    
            // append select, insert, or update
            result.append(pkind.getLabel() + "\n");
    
            String columnName;
            for (int j=0;j<pTable.getObjectNameReferences().size();j++){
              columnName = pTable.getObjectNameReferences().getObjectName(j).getColumnNameOnly();
              if (!pTable.getObjectNameReferences().getObjectName(j).isTableDetermined()){
                  columnName = columnName + "(not determined)";
              }
              // append table and column name
              result.append(tablename + "." + columnName + "\n");
            }
    
            //check is this table already in tables
            boolean isFound = false;
    
            for(int i=0;i<total_tables;i++){
                if (tables[i].tablename.toString().compareToIgnoreCase(tablename.toString()) == 0){
                   tables[i].statetments[kind]++;
                    isFound = true;
                    break;
                }
            }
    
            if (!isFound){
              tables[total_tables] = new  tableEffected();
              tables[total_tables].tablename = tablename;
              tables[total_tables].statetments[kind]++;
              total_tables++;
             }
             return result.toString();
        }
    }
 
    /**
     * Analyze the provided SQL query String for
     * information on accessed tables and columns
     * in CRUD
     * @param query
     * @return String representation of analysis
     * in the form of:
     *    CRUD type
     *    tableName.columnName
     *    ...
     */
    public String analyze(String query) {
        EDbVendor dbVendor = EDbVendor.dbvmssql;
        crud go = new crud(dbVendor, query);
        return go.run();
    }
}