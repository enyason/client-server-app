package app.client.startup;

import java.io.IOException;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.RequestObject;
import app.socket.ResponseObject;

public class ClientJobInf {
	
	private static Logger logger = Logger.getLogger(ClientJobInf.class.getName());  
	
	/**
	 * process input data.
	 * @param socket
	 * @param rname
	 * @param rvalue
	 */
	protected void process(Socket socket, String inputname,String inputvalue) {
		
		//socket sent/rev data...
		RequestObject requestObject = new RequestObject(inputname, inputvalue);
		ResponseObject responseObject = new ResponseObject();
		try {
			socket.connect();
			socket.sendData(requestObject);
			responseObject = socket.receiveData();
			socket.close();
		}catch(IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}finally {
			socket.close();
		}
		//output...
		System.out.print(responseObject.toString());
	}

}
