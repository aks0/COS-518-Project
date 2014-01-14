package colocation;

import materializedViews.Table;

public class TablePair {
    private Table parentTable;
    private Table childTable;

    public TablePair(Table parentTable, Table childTable) {
        this.parentTable = parentTable;
        this.childTable = childTable;
    }
    
    public Table getParentTable() {
        return parentTable;
    }
    
    public Table getChildTable() {
        return childTable;
    }
}
