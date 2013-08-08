package test.app.server.startup;

//import org.jmock.Expectations;
import org.jmock.Mockery;

import app.server.startup.Server;
import junit.framework.TestCase;

public class ServerTest extends TestCase {
	
	Mockery context = new Mockery();
	
	@Override  
    public void setUp() throws Exception {  
		 
	}  

	//-------------- test arguments().
	
    public void testArguments() {
    	
    	String[] args = {"-port","7001","-data","data","-proc_count","4"};
        Server.server.arguments(args);
        // verify
        assertTrue(Server.server.getPort()==7001);
        assertEquals(System.getProperty("user.home") +
				System.getProperty("file.separator") + 
				"data",
				Server.server.getData());
        assertTrue(Server.server.getProc_count()==4);
        
    }
    
    public void testArgumentswithNULL() {
    	
        String[] args = {};
        Server.server.arguments(args);
        // verify
        assertTrue(Server.server.getPort()==3000);
        assertEquals(System.getProperty("user.home") +
				System.getProperty("file.separator") + 
				"serverdata",Server.server.getData());
        assertTrue(Server.server.getProc_count()==2);
        
    }
    
    public void testArgumentswithInvalid() {
    	
 	    String[] args = {"-port","-data"};
        boolean isValid = Server.server.arguments(args);
        assertTrue(isValid==false);
        
    }
}
