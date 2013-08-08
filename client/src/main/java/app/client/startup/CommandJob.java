package app.client.startup;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.CommandOption;

public class CommandJob extends ClientJobInf {
	
	private static Logger logger = Logger.getLogger(CommandJob.class.getName());  
	
	/**
	 * invoke quit command.
	 */
	public void quit(CommandOption commandOption, Socket socket) {
		if(logger.isLoggable(Level.INFO)) logger.info("quit invoking...");
		
		String inputname = commandOption.name().toLowerCase();
		String inputvalue = "";
		
		process( socket, inputname, inputvalue );
		
		System.exit(0);
		
		
	}

}
