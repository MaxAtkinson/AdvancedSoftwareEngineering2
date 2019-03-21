package model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * 
 *
 */
public class ServerList {
	private ArrayList<Server> servers;
	
	
	/**
	 * Constructor - instantiates list of servers
	 */
	public ServerList() {
		servers = new ArrayList<Server>();
	}
	
	/** 
	 * Get list of all servers
	 * @return ArrayList of Server
	 */
	public ArrayList<Server> getServers() {
		return servers;
	}
	
	/** 
	 * Get the index of a server from Server object
	 * @param server
	 * @return index in server list
	 */
	public int getServerIndex(Server server) {
		return servers.indexOf(server);
	}
	
	/**
	 * Add a new Server object to the server list
	 * @param s previously created Server object
	 */
	public void addServer(Server s) {
		servers.add(s);
	}
	
	/**
	 * Delete the last opened server from the list
	 */
	public void removeServer() {
		if (servers.size() > 0) {
			Server s = servers.get(servers.size()-1);
			s.setActive(false);
			servers.remove(s);
		}
	}
}
