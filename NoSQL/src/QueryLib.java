import java.util.ArrayList;


public class QueryLib {

	private ArrayList<Query> queries;
	
	QueryLib() {
		queries = new ArrayList<Query>();
	}
	
	public QueryLib addQuery(Query query) {
		this.queries.add(query);
		return this;
	}
	
	public Query getQuery(int index) {
		return this.queries.get(index);
	}
}
