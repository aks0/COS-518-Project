package common;

import java.util.ArrayList;
import java.util.List;

class Server {
	private MemorySize size;
	
	public Server(MemorySize size) {
		this.size = size;
	}
	
	public MemorySize getSize() {
		return size;
	}
}

public class ServerGroup {
	private List<Server> servers;
	
	public ServerGroup(int n, MemorySize avgServerSize) {
		servers = new ArrayList<Server>();
		for (int i = 0; i < n; ++i) {
			servers.add(new Server(avgServerSize));
		}
	}
}
