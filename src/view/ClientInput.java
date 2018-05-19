package view;

//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
//import controller.ClientFTP;
import model.Protocol;
import model.ProtocolException;

public class ClientInput {

	private InputStream in;
	private Protocol Client;
	boolean stop = false;
	

	public ClientInput(InputStream in, Protocol Client) {
		this.in = in;
		this.Client = Client;
	}

	public void doRun() throws IOException, ProtocolException {

		String strUser, strPass, strPath, strFileName, strCmd;
		ArrayList<String> args;
		

		try (LineBufferedInputStream is = new LineBufferedInputStream(in)) {

			while (!stop) {

				String line = is.readLine();
				args = getArgsFromLine(line);

				if (args == null) {
					throw new ProtocolException("Invalid input");
				}
				if (args.get(0) == null) {
					throw new ProtocolException("Invalid input");
				}

				strCmd = args.get(0).toUpperCase();
				System.out.println(line);

				switch (strCmd) {
				case "USER":
					System.out.println("userIN");
					if (args.size() == 2) {
						strUser = args.get(1);
						Client.sendUser(strUser);
					}else {
						Client.argumentError_501();
					}
					break;
				case "PASS":
					if (args.size() == 2) {
						strPass = args.get(1);
						Client.sendPass(strPass);
					} else {
						Client.argumentError_501();
					}
					break;
				case "PASV":
					Client.sendPasv();
					break;
				case "STOR":
					if (args.size() == 2) {
						strFileName = args.get(1);
						Client.sendStor(strFileName);
					
					} else {
						Client.argumentError_501();
					}
					break;
				case "PORT":
					if (args.size() == 2) {
						strPath = args.get(1);
						Client.sendPort(strPath);
					} else {
						Client.argumentError_501();
					}
					break;
				case "MLSD":
					Client.sendMLSD();
					break;
				case "RETR":
					if (args.size() == 2) {
						strFileName = args.get(1);
						Client.sendRetr(strFileName);
					} else {
						Client.argumentError_501();
					}
					break;
				case "SYST":
					Client.DebugMessage("502 Commande non implementee");
				case "FEAT":
					Client.sendFeat();
					break;
				case "PWD":
					Client.sendPwd();
					break;
				case "TYPE":
					Client.sendType();
					break;
				/*case "LIST": 
				    Client.sendList();
				    break;*/
				case "CWD":
					if (args.size() == 2) {
						strPath = args.get(1);
						Client.sendCwd(strPath);
					} else {
						Client.argumentError_501();
					}
					break;
				case "MKD":
					if (args.size() == 2) {
						strFileName = args.get(1);
						Client.sendMKD(strFileName);
					} else {
						Client.argumentError_501();
					}
					break;
				case "NLST":
					Client.sendNLST();
					break;
				case "DELE":
					if(args.size() == 2) {
						strFileName = args.get(1);
						Client.sendDELE(strFileName);
					}else {
						Client.argumentError_501();
					}
					break;
				case "RMD": 
					if(args.size() == 2) {
						strFileName = args.get(1);
						Client.sendRMD(strFileName);
					}else {
						Client.argumentError_501();
					}
					break;
				case "SIZE":
					if(args.size() == 2) {
						strFileName = args.get(1);
						Client.sendSIZE(strFileName);
					}else {
						Client.argumentError_501();
					}
					break;
				case "MDTM":
					if(args.size() == 2) {
						strFileName = args.get(1);
						Client.sendMDTM(strFileName);
					}else {
						Client.argumentError_501();
					}
					break;
				case "RNFR":
					if(args.size() == 2) {
						strFileName = args.get(1);
						Client.sendRNFR(strFileName);
					}else {
						Client.argumentError_501();
					}
					break;
				case "RNTO":
					if(args.size() == 2) {
						strFileName = args.get(1);
						Client.sendRNTO(strFileName);
					}else {
						Client.argumentError_501();
					}
					break;
				case "QUIT":
					Client.sendQuit();
					break;
				case "CDUP":
					Client.sendCDUP();
					break;
				/*case "LIST":
					Client.DebugMessage("150 statut du fichier ok");
					break;*/
				default:
					Client.sendNoop(strCmd);
				}
			}
		}
	}

	private ArrayList<String> getArgsFromLine(String line) {
		if (line == null) {
			return null;
		}
		String[] mots = line.split(" ");
		ArrayList<String> l = new ArrayList<>();
		for (int i = 0; i < mots.length; i++) {
			l.add(mots[i]);
		}
		return l;
	}

}
