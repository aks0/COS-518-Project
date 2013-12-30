package colocation;

import materializedViews.Table;

public class TablePair {
    private Table primaryTable;
    private Table foreignTable;

    public TablePair(Table primaryTable, Table foreignTable) {
        this.primaryTable = primaryTable;
        this.foreignTable = foreignTable;
    }
    
    public Table getPrimaryTable() {
        return primaryTable;
    }
    
    public Table getForeignTable() {
        return foreignTable;
    }
}
