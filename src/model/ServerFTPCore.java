package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.ClientFTP;


public class ServerFTPCore extends Thread {
	private int port_cmd;
	private int port_data;
	static Socket data_socket;
	static InputStream in;
	static OutputStream out;
	private boolean stop = false;
	private ClientFTP Client;

	public ServerFTPCore(int p1, int p2) throws IOException {
		this.port_cmd = p1;
		this.port_data = p2;
		System.out.println("Server FTP started ..");
		
		this.start();
	}

	public void run() {

		try (ServerSocket ss = new ServerSocket(port_cmd)) {
			
			ss.setSoTimeout(1000);
			File rootDir = new File("ROOT");
			rootDir.mkdir();
			
			
			while(!stop) {
				try {
					
					Socket s = ss.accept();
					Client = new ClientFTP (s, rootDir, port_data);
					

					System.out.println("you connected to service with port"+" "+port_cmd );
					new Thread(Client).start();
					
					
				}catch(SocketTimeoutException ex) {}
			}
		
		}catch (IOException e) {
			System.out.println("Could not bind port " + port_cmd);
			Logger.getLogger(ServerFTPCore.class.getName()).log(Level.SEVERE, null, e);
			finish();
		}
	}
		
		public synchronized void finish() {
			ServerFTPModel.clearAll();
			stop = true;
		}

	}
