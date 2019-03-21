package model;

import java.util.ArrayList;
import java.util.Observable;

public class ServerList {
	private ArrayList<Server> servers;
	
	public ServerList() {
		servers = new ArrayList<Server>();
	}
	
	public ArrayList<Server> getServers() {
		return servers;
	}
	
	public int getServerIndex(Server server) {
		return servers.indexOf(server);
	}
	
	public void addServer(Server s) {
		servers.add(s);
	}
	
	public void removeServer() {
		if (servers.size() > 0) {
			Server s = servers.get(servers.size()-1);
			s.setActive(false);
			servers.remove(s);
		}
	}
}
