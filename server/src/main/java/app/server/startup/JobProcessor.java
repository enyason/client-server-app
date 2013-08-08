package app.server.startup;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import java.lang.reflect.Method;

import app.service.ServiceInf;
import app.socket.CommandOption;
import app.socket.RequestObject;
import app.socket.ResponseObject;
import app.socket.ServiceOption;

public class JobProcessor {
	
	private static Logger logger = Logger.getLogger(JobProcessor.class.getName());  
	
	private ServiceInf service;
	
	public JobProcessor (RequestObject requestObject) {
		
	}
	
	public static ResponseObject process(RequestObject requestObject) {
		
		boolean isCommand = false;
    	boolean isService = false;
    	
    	for(CommandOption s: CommandOption.values()){
    		if( (requestObject.getName().toUpperCase()).equals(s.name())) {
    			isCommand = true;
    		}	
    	}
    	
    	for(ServiceOption s: ServiceOption.values()){
    		if( (requestObject.getName().toUpperCase()).equals(s.name())) {
    			isService = true;
    		}	
    	}
    	
    	ResponseObject responseObject = new ResponseObject();
    	//invoke service.
    	if( isService ) {
    		String method = requestObject.getName().toLowerCase();
    		try {
    			Class<?> clazz = Class.forName("app.service.Service");
    			Method m = clazz.getMethod(method, new Class[]{RequestObject.class});
    			responseObject = (ResponseObject) m.invoke(clazz.newInstance(), new Object[]{requestObject});
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
    	
    	if( isCommand && requestObject.getName().toUpperCase().equals("QUIT")) {
    		//to stop server.
    		responseObject = new ResponseObject("Server will quit...");
    		
    	} 
    	return responseObject;
	}
	
	/**
	 * @return the service
	 */
	public ServiceInf getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ServiceInf service) {
		this.service = service;
	}



}
