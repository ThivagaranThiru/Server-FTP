package model;

import java.io.File;

public interface Protocol {
	
	//*****InPut*****//
	
	default void sendUser(String name) {};
	
	default void sendPass(String pwd) {};
	
	default void sendPwd() {};
	
	default void sendCwd(String dir) {};
	
	default void sendQuit() {};
	
	default void sendPasv() {};
	
	default void sendMKD(String fileName) {};
	
	default void sendMLSD() {};
	
	default void sendRetr(String fileName) {};
	
	default void sendCDUP() {};
	
	default void sendStor(String FileName) {};
	
	default void rein() {};
	
	default void sendPort(String newPort) {};
	
	default void sendNoop(String AnyThing) {};
	
	default void sendList() {};
	
	default void sendType() {};
	
	default void welcomeMsg_220() {};
	
	default void sendNLST() {};
	
	default void sendDELE(String strFileName) {};
	
	default void sendRMD(String strFileName) {};
	
	default void sendSIZE(String strFileName) {};
	
	default void sendMDTM(String strFileName) {};
	
	default void sendRNFR(String strFileName) {};
	
	default void sendRNTO(String strFileName) {};
	
	default void sendFeat() {};

	
	//*****OutPut*****//
	
	
	
	default void notLoggedIn_530() {};
	
	default void serviceClosing_221() {};
	
	default void DebugMessage(String s){};
	
	default void loggedIn_230() {};
	
	default void syntaxError_500() {};
	
	default void argumentError_501() {};
	
	default void pathCreated_257(String path) {};
	
	default void userNameOk_250() {};
	
	default void requestNotTaken_550() {};
	
	default void requestFileCompleted_250() {};
	
	default void fileStatusOk_150() {};
	
	default void closingDataCnx_226() {};
	
	default void opningDataCnx_225() {};
	
	default void opningCanalError_425() {};
	
	default void passiveMode_227(int p1,int p2) {};
	
	default void loggedOut_231() {};
	
	default void WaitForPass_331(String user) {};
	
	default void fileActionNotToken_450() {};
	
	default void messageFeat(){};
	
	default void messageType() {};

	default void messageList(File[] files) {};

	default void messageMLSD(String[] mlst) {};

	default void messageNLST(File[] files) {};

	default void messageSIZE(long octet) {};

	default void messageDATE(String date) {};

}
