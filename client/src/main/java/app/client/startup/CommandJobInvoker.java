package app.client.startup;

import java.lang.reflect.Method;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.CommandOption;

/**
 * handle command option.
 * @author fanfan
 *
 */
public class CommandJobInvoker {
	
	private static Logger logger = Logger.getLogger(CommandJobInvoker.class.getName());  
	
    /**
	 * call command handler method thru commandOption.
	 */
	public static void invoke( CommandOption commandOption, Socket socket ) {
		String method = commandOption.name().toLowerCase();
		try {
			Class<?> clazz = Class.forName("app.client.startup.CommandJob");
			Method m = clazz.getMethod(method, new Class[]{CommandOption.class, Socket.class});
			m.invoke(clazz.newInstance(), new Object[]{commandOption, socket});
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE,e.getMessage());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.log(Level.SEVERE,e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			e.printStackTrace();
		}	
	}
	
}
