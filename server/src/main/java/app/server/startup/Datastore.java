package app.server.startup;

import java.io.File;

import java.util.logging.Level;  
import java.util.logging.Logger;  
/**
 * 
 * @author fanfan
 *
 */
public class Datastore {
	
	private static Logger logger = Logger.getLogger(Datastore.class.getName());
	File datafolder;
	
	public Datastore (String data) {
		datafolder = new File(data);
	}
	
	/**
	 * if data folder is not exist, then create it, 
	 */
	public boolean exists() {
		
		if ( !datafolder.exists() ) {
			return (false);
		}else {
			return (true);
		}
		

	}
	
	/**
	 * create data folder.
	 */
	public boolean create() {
		
		try{
			datafolder.mkdir();
			datafolder.setReadable(true);
			datafolder.setWritable(true);
			return (true);
		}catch(SecurityException e) {
			logger.log(Level.SEVERE,e.getMessage());
			return (false);
		}
		
	}
	 
	/**
	 * check data folder permission are read and write, 
	 * then return <code>true</code>,
	 * otherwise return <code>false</code>.
	 */
	public boolean isAccess() {
			
			if(datafolder.canRead() && datafolder.canWrite()) {
				return (true);
			}else {
				return (false);
			}
	}
	
	public boolean setup() {
		
		if ( !exists() ) {
			if (!create ()) {
				//fail to create data folder.
				return (false) ;
			}
		}
		
		return isAccess();
			
	}
		
}
