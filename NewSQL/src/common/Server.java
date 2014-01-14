package common;

public class Server {
	private MemorySize size;
	
	public Server(MemorySize size) {
		this.size = size;
	}
	
	public MemorySize getSize() {
		return size;
	}
}