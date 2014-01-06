package colocation;

import java.util.HashSet;
import java.util.Iterator;

import materializedViews.Table;

public class EntityGroup implements Iterable<Table> {
    private Table center;
    private HashSet<Table> entities;
    private HashSet<Table> replicatedTables;
    
    public EntityGroup(Table center) {
        this.center = center;
        entities = new HashSet<Table>();
        replicatedTables = new HashSet<Table>();
        entities.add(center);
    }
    
    public Table getCenter() {
        return center;
    }
    
    public HashSet<Table> getEntities() {
        return entities;
    }
    
    public void add(Table entity) {
        entities.add(entity);
    }
    
    public void addReplicatedTable(Table table) {
    	replicatedTables.add(table);
    }
    
    public boolean contains(Table entity) {
        return entities.contains(entity);
    }
    
    public boolean onlyContainsCenter() {
        return entities.size() == 1;
    }
    
    /**
     * Returns total size of entity group in bytes
     * TODO: add replicated tables?
     * @return
     */
    public long getSize() {
    	long size = 0;
    	
    	Iterator<Table> iterator = this.iterator();
    	while (iterator.hasNext()) {
    		Table table = iterator.next();
    		size += table.getSizeInBytes();
    	}
    	
    	return size;
    }
    
    /**
     * Returns size in bytes of one partition of entity group (assuming perfect partitioning)
     * and include replicated tables
     * 
     * @return
     */
    public long getPartitionedSize(int numServers) { 
    	return getSize()/numServers + getReplicatedSize();
    }
    
    /**
     * Get size in bytes of all replicated tables in entity group
     * 
     * @return
     */
    public long getReplicatedSize() {
    	long replicatedSize = 0;
    	for (Table table : replicatedTables) {
    		replicatedSize += table.getSizeInBytes();
    	}
    	
    	return replicatedSize;
    }

    @Override
    public Iterator<Table> iterator() {
        return entities.iterator();
    }
    
    public String toString() {
    	String str = "";
    	str += "Center: " + center.getName() + "\n";
    	str += "Partitioned Tables: \n";
        for (Table table : entities) {
        	str += table.getName() + "\n";
        }
        for (Table table : replicatedTables) {
        	str += table.getName() + "\n";
        }
        
        return str;
    }
}
