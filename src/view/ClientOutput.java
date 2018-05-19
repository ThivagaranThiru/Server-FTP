package view;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;


import model.Protocol;

public class ClientOutput implements Protocol {
	
	private PrintWriter os;
	
	public ClientOutput(OutputStream out) {
		this.os = new PrintWriter(out,true);
	}

	@Override
	public void loggedIn_230() {
		
		os.println("230 User logged on");
	}

	@Override
	public void syntaxError_500() {
	
		os.println("500 Syntax error");
		
	}

	@Override
	public void argumentError_501() {
		
		os.println("501 Syntax error in parameters or arguments.");
	}

	@Override
	public void pathCreated_257(String path) {
		
		os.println("257 \""+ path + "\"");
	}
	@Override
	public void userNameOk_250() {
		
		os.println("250 Requested file action okay, completed.");
	}

	@Override
	public void requestNotTaken_550() {
		
		os.println("550 Requested action not taken.");
	}

	@Override
	public void requestFileCompleted_250() {
		
		os.println("250 Requested file action completed.");
	}

	@Override
	public void fileStatusOk_150() {
		
		os.println("150 File status okay");
	}

	@Override
	public void closingDataCnx_226() {
		
		os.println("226 Closing data connection.");
	}

	@Override
	public void messageFeat() {
		os.println("USER PASS PASV");
	}

	@Override
	public void opningDataCnx_225() {
		
		os.println("225 Data connection open ");
	}

	@Override
	public void opningCanalError_425() {
		
		os.println("425 Can't open data connection.");
	}

	@Override
	public void passiveMode_227(int p1, int p2) {
		
		os.println("227 Entering passive mode (127.0.0.1,"+p1+","+p2+")");
		
	}

	@Override
	public void loggedOut_231() {
		
		os.println("231 User logged out");
	}
	@Override
	public void WaitForPass_331(String name) {
		os.println("331 passsword required for"+" "+name);
	}

	@Override
	public void fileActionNotToken_450() {
		os.println("450 Requested file action not taken");
	}
	
	@Override
	public void welcomeMsg_220() {
		os.println("220 Welcome FTP Server");
		
	}
	

	@Override
	public void messageType() {
		os.println("200 TYPE is now 8-bit binary");
	}
	
	@Override
	public synchronized void messageMLSD(String[] mlst) {
		for (int i = 0; i < mlst.length; i++) {
			os.println(mlst[i]);
		}
	}
	
	@Override
	public synchronized void messageNLST(File[] files) {
		for (int i = 0; i < files.length; i++) {
			if(!files[i].isDirectory()) {
				os.println(files[i].getName());
			}
		}
	}
	
	@Override
	public synchronized void messageSIZE(long octet) {
		os.println("The is "+ octet +" octets");

	}
	
	@Override
	public void messageDATE(String date) {
		// TODO Auto-generated method stub
		os.println("The date is "+ date);
	}
	
	
	public void messag(String date) {
		// TODO Auto-generated method stub
		os.println(date);
	}
	
	

	@Override
	public void messageList(File[] files) {
		for (int i = 0; i < files.length; i++) {
				os.println(files[i].getName());
		}
	}
}
