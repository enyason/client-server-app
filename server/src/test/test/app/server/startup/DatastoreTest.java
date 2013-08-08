package test.app.server.startup;

import java.io.File;

import app.server.startup.Datastore;
import junit.framework.TestCase;

public class DatastoreTest extends TestCase {
	
	public void testSetup(){
		
		  String data = System.getProperty("user.home") +
					System.getProperty("file.separator") + 
					"serverdata" ;
		  Datastore datastore = new Datastore(data);
		  boolean isReady = datastore.setup();
		  assertTrue(isReady==true);
		  File datafolder = new File(data);
		  assertTrue(datafolder.canRead()&&datafolder.canWrite()==true);
		  
	  }

}
