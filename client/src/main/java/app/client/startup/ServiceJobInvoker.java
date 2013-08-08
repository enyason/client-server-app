package app.client.startup;

import java.lang.reflect.Method;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.ServiceOption;

/**
 * handle service option.
 * @author fanfan
 *
 */
public class ServiceJobInvoker {
	
	private static Logger logger = Logger.getLogger(ServiceJobInvoker.class.getName());  
	
	/**
	 * call service handler method thru serviceOption.
	 */
	public static void invoke(ServiceOption serviceOption, Socket socket){
		String method = serviceOption.name().toLowerCase();
		try {
			Class<?> clazz = Class.forName("app.client.startup.ServiceJob");
			Method m = clazz.getMethod(method, new Class[]{ServiceOption.class,Socket.class});
			m.invoke(clazz.newInstance(), new Object[]{serviceOption,socket});
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
