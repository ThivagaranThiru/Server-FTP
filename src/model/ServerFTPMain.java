package model;

import java.io.IOException;

public class ServerFTPMain {
	
	public static void main(String[] args) {
		
		int port_cmd = args.length == 1 ? Integer.parseInt(args[0]) : 1234;
		int port_data = args.length == 1 ? Integer.parseInt(args[0]) : 1024 ;
		
		try {
		
			ServerFTPCore core = new ServerFTPCore(port_cmd , port_data);
		
		} catch (IOException e) {
			System.out.println("Error during initialisation: " + e.toString());
		}
	}
}
