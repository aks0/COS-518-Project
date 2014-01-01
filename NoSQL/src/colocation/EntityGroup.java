package colocation;

import java.util.HashSet;
import java.util.Iterator;

import materializedViews.Table;

public class EntityGroup implements Iterable<Table> {
    private Table center;
    private HashSet<Table> entities;
    
    public EntityGroup(Table center) {
        this.center = center;
        entities = new HashSet<Table>();
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
    
    public boolean contains(Table entity) {
        return entities.contains(entity);
    }
    
    public boolean onlyContainsCenter() {
        return entities.size() == 1;
    }

    @Override
    public Iterator<Table> iterator() {
        return entities.iterator();
    }
}
