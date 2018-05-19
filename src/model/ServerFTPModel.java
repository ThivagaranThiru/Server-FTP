package model;

import java.util.TreeMap;

public class ServerFTPModel {

	private static final TreeMap <String,ClientEvents> clientList = new TreeMap<>();
	private static final TreeMap <String,String> LoginList = new TreeMap<>();
	
	public static synchronized boolean registerUser(String name,String pass, ClientEvents c) {
		if ( name.equals("") || existUserName(name)) {
			return false;
		}
		clientList.put(name, c);
		LoginList.put(name, pass);
		return true;
	}
	
	public static synchronized void unregisterUser(String name) {
		if (existUserName(name)) {
			clientList.remove(name);
			LoginList.remove(name);
		}
	}

	public static synchronized boolean existUserName(String name) {
		return clientList.containsKey(name);// && LoginList.containsKey(name); 
	}
	// renNameUser
	synchronized static void clearAll() {
		clientList.values().forEach(c -> c.shutdownRequested());
	}

	public static String getPassword(String user) {
		// TODO Auto-generated method stub
		return LoginList.get(user);
	}

}
