package controller;

//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.lang.Thread.State;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
//import java.util.Properties;
import java.util.Random;

//import org.omg.CORBA.TIMEOUT;

import model.ClientEvents;
import model.Protocol;
import model.ProtocolException;
import model.ServerFTPModel;
import view.ClientInput;
import view.ClientOutput;

public class ClientFTP<IClientLogger> implements Runnable, Protocol, ClientEvents {

	//private PrintWriter os;
	private final Socket sc;
	private Socket s2;
	private ServerSocket ds;
	private int port21;
	private InputStream ins;
	//private OutputStream ous;
	private ClientInput in;
	private ClientOutput out;
	private String user = "";
	private File home = null;
	private File rootDir = null;
	private File pos = null;
	private boolean stop = false;
	private String renameTo = "";

	private enum ClientState {
		AUTH, NON_AUTH
	};

	private ClientState state = ClientState.NON_AUTH;

	public ClientFTP(Socket s, File serveur, int port) {
		this.sc = s;
		this.rootDir = serveur;
		this.port21 = port;

	}

	@Override
	public void run() {
		System.out.println("run");
		try (Socket s1 = sc) {
			in = new ClientInput(s1.getInputStream(), this);
			out = new ClientOutput(s1.getOutputStream());
			out.welcomeMsg_220();
			in.doRun();

		} catch (IOException | ProtocolException e) {
			// TODO Auto-generated catch block

			if (!stop) {
				finish();
			}
		}
	}

	private synchronized void finish() {
		System.out.println("finish");
		if (!stop) {
			rein();
			stop = true;
			out.serviceClosing_221();
			try {
				sc.close();
				out.notLoggedIn_530();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void rein() {
		System.out.println("rein");
		state = ClientState.NON_AUTH;
		user = "";
		pos = home;
	}

	@Override
	public void sendUser(String name) {
		System.out.println("user");
		//rein();
		this.user = name;
		out.WaitForPass_331(name);
	}

	@Override
	public void sendPass(String pwd) {
		System.out.println("pass");
		if (ServerFTPModel.existUserName(user)) {
			if (ServerFTPModel.getPassword(user).equals(pwd)) {
				out.loggedIn_230();
			} else {
				out.notLoggedIn_530();
			}

		} else {
			ServerFTPModel.registerUser(user, pwd, this);
			out.loggedIn_230();
		}
		pos = new File(rootDir,user);
		pos.mkdir();
		pos = rootDir;
		pos.getPath();
		state = state.AUTH;
	}

	@Override
	public void sendPwd() {
		System.out.println("pwd");
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		out.pathCreated_257(pos.getAbsolutePath());
	}

	@Override
	public void sendCwd(String dir) {
		System.out.println("cwd");
		if (state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		File dire = new File(pos,dir);
		if (!dire.exists() || !dire.isDirectory()) {
			out.requestNotTaken_550();
			return;
		}
		
		pos = dire;

		sendPwd();
	}

	@Override
	public void sendList() {
		if (state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();

		}
		
		/*
		 * File dir = new File(System.getProperty("user.dir")); File[] list =
		 * dir.listFiles(); String listContenu = ""; for (int i = 0; i < list.length;
		 * i++) { out.DebugMessage(list[i].getName()+""+list[i].length()+""+new
		 * Date(list[i].lastModified())); }
		 */

		/*
		 * Path currentPath = Paths.get("."); File[] dirList =
		 * currentPath.toFile().listFiles(); for (File f : dirList) {
		 * out.DebugMessage(f.getName()); } out.DebugMessage("$");
		 */
		
		out.DebugMessage("150 statut du fichier ok");
		String remoteDirPath = "/" + pos.getPath();
		File fil = new File(remoteDirPath);
		File[] files = fil.listFiles();
		for (File file : files) {
			String details = file.getName();
			if (file.isDirectory()) {
				out.DebugMessage("d" + "rwxr-xr-x" + " " + user + " " + details + " " + file.length() + " " + new Date(file.lastModified()));
			}
			if (file.isFile()) {
				out.DebugMessage("-" + "rwxr-xr-x" + " " + user + " " + details + " " + file.length() + " " + new Date(file.lastModified()));
			}

		}
	
	}
	
	@Override
	public void sendCDUP() {
		System.out.println("CDUP");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		pos = pos.getParentFile();
		out.requestFileCompleted_250();
		sendPwd();
	}

	@Override
	public void sendQuit() {
		System.out.println("Quit");
		// TODO Auto-generated method stub
		Protocol.super.sendQuit();
		finish();
	}

	@Override
	public void sendPasv() {
		System.out.println("Pasv");
		if (state == ClientState.NON_AUTH)
			out.notLoggedIn_530();

		Random r = new Random();
		int DataPort = r.nextInt(3000) + 1500;
		System.out.println(DataPort);
		int p1 = DataPort / 256;
		int p2 = DataPort - (p1 * 256);
		out.passiveMode_227(p1, p2);

		try {

			ds = new ServerSocket(DataPort);
			s2 = ds.accept();
			out.opningDataCnx_225();
			System.out.println("connect to port" + DataPort);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void sendMKD(String fileName) {
		System.out.println("MKD");
		if (state == ClientState.NON_AUTH) {
			out.requestNotTaken_550();

		}
		File newDir = new File(pos, fileName);
		if (newDir.mkdir()) {
			out.pathCreated_257(newDir.getPath());
		} else {
			out.requestNotTaken_550();
		}
	}

	@Override
	public void sendStor(String FileName) {
		System.out.println("Stor");
		if (state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
		}
		File fileToStor = new File(pos, FileName);
		byte buffer[] = new byte[1024 * 16];
		int cmp;

		if (s2 == null) {
			sendPasv();
		}

		try {
			OutputStream ous = new FileOutputStream(fileToStor);
			ins = s2.getInputStream();// need for cmd port to ini s2
			fileToStor.createNewFile();

			while ((cmp = ins.read(buffer)) != -1) {
				ous.write(buffer, 0, cmp);
			}

			s2.close();

		} catch (IOException e) {

			out.closingDataCnx_226();
		}

	}

	@Override
	public void sendPort(String parametres) {
		System.out.println("port");
		int intPort, x, y;
		String ipAdress = null;
		if (state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
		}
		//ServerSocket ds;
		String[] args = parametres.split(",");

		if (args.length != 6) {
			out.syntaxError_500();
		}

		x = Integer.parseInt(args[4]);
		y = Integer.parseInt(args[5]);
		intPort = (x * 256) + y;
		ipAdress = args[0] + "." + args[1] + "." + args[2] + "." + args[3];
		System.out.println(ipAdress + " " + intPort);

		try {

			s2 = new Socket(ipAdress, intPort);
			out.opningDataCnx_225();
			System.out.println("connect to port" + intPort);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void sendNoop(String f) {
		System.out.println("noop");
		out.DebugMessage(f);
	}

	@Override
	public void sendRetr(String fileName) {
		System.out.println("retr");
		if (state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
		}
		if (s2 == null) {
			sendPasv();
			out.opningCanalError_425();
		}
		File fileToRetr = new File(pos, fileName);

		if (!fileToRetr.exists() || fileToRetr.isDirectory() || fileToRetr == null) {
			out.fileActionNotToken_450();
		}
		byte[] buff = new byte[8192];

		int cmp;
		try {

			InputStream in = new FileInputStream(fileToRetr);
			OutputStream os = s2.getOutputStream();

			while ((cmp = in.read(buff)) > 0) {
				os.write(buff, 0, cmp);
			}
			out.requestFileCompleted_250();

			s2.close();

		} catch (IOException ex) {
			out.closingDataCnx_226();
			;
		}

	}
	
	@Override
	public void sendMLSD() {
		System.out.println("mlsd");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		if(pos.isDirectory()) {
		out.messageMLSD(pos.list());
		}
	}
	
	@Override
	public void sendNLST() {
		System.out.println("nlst");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		File[] files = pos.listFiles();
		
		out.messageNLST(files);
	}
	
	@Override
	public void sendDELE(String path) {
		System.out.println("dele");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		File file = new File(pos,path);
		if(file.delete()) {
			out.requestFileCompleted_250();
		}else out.requestNotTaken_550();
	}
	
	@Override
	public void sendRMD(String path) {
		System.out.println("rmd");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		File file = new File(pos,path);
		if(file.isDirectory()) {
			removeDir(file);
			file.delete();
			out.requestFileCompleted_250();
		}else out.requestNotTaken_550();
	}
	
	public void removeDir(File rep) {
		System.out.println("removedir");
		File [] ListFiles = rep.listFiles();
		for(int i = 0; i < ListFiles.length; i++) {
			if(ListFiles[i].isDirectory()) {
				removeDir(ListFiles[i]);
				ListFiles[i].delete();
			}else ListFiles[i].delete();
		}
	}
	
	@Override
	public void sendSIZE(String path) {
		System.out.println("size");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		File file = new File(pos,path);
		
		if(file.exists()) {
			long octet = file.length();
			out.messageSIZE(octet);
		}else {
			out.requestNotTaken_550();
		}
	}
	
	@Override
	public void sendMDTM(String path) {
		System.out.println("mdtm");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		File file = new File(pos,path);
		
		if(file.exists()) {
			long octet = file.lastModified();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
			Date d = new Date(octet);
			out.messageDATE(sdf.format(d));
		}else {
			out.requestNotTaken_550();
		}
	}
	
	@Override
	public void sendRNFR(String path) {
		System.out.println("rnfr");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		File file = new File(pos,path);
		
		if(file.exists()) {
			renameTo = path;
			out.fileStatusOk_150();
		}else {
			out.requestNotTaken_550();
		}
	}
	
	@Override
	public void sendRNTO(String path) {
		System.out.println("rnto");
		// TODO Auto-generated method stub
		if(state == ClientState.NON_AUTH) {
			out.notLoggedIn_530();
			return;
		}
		
		File file = new File(pos,renameTo);
		File rename = new File(pos,path);
		file.renameTo(rename);
		out.fileStatusOk_150();
	}

	@Override
	public void sendFeat() {
		System.out.println("feat");
		out.messageFeat();
	}
	
	@Override
	public void sendType() {
		System.out.println("type");
		out.messageType();

	}
	
	@Override
	public void shutdownRequested() {

	}
}
