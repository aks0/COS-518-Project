package materializedViews;
public class Pair<T1, T2> {

	private T1 e1;
	private T2 e2;

	public Pair(T1 e1, T2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public T1 getFirst() {
		return e1;
	}

	public T2 getSecond() {
		return e2;
	}

	@Override
	public String toString() {
		return e1.toString() + "#" + e2.toString();
	}

	@Override
	public int hashCode() {
		int result = 17;
		result += this.e1.hashCode() / 2;
		result += this.e2.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> otherPair = (Pair<?, ?>) obj;
		return otherPair.getFirst().equals(e1)
				&& otherPair.getSecond().equals(e2);
	}
}
