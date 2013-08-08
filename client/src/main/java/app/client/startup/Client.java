package app.client.startup;

import java.net.InetSocketAddress;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.CommandOption;
import app.socket.ServiceOption;

public class Client {  
  
	
	public static final Client client = new Client();
	
	private static Logger logger = Logger.getLogger(Client.class.getName());  
	
	/**
	 * server port, default is 3000.
	 */
	private int serverPort = 3000;
	
	/**
	 * flag for service call.
	 */
	private boolean isService = false;
	
	/**
	 * flag for command call.
	 */
	private boolean isCommand = false;
	
	/**
	 * service option name.
	 */
	private ServiceOption serviceOption;
	
	/**
	 * command option name.
	 */
	private CommandOption commandOption;
	
	private Client() {
		
	}

	//----------------------methods.
	/**
     * Process the specified command line arguments, and return
     * <code>true</code> if we should continue processing; otherwise
     * return <code>false</code>.
     *
     * @param args Command line arguments to process
     */
	public boolean arguments(String[] args) {
		if( args.length<1 ) {
			usage();
			return (false) ;
		}
		
		boolean isServerPort = false;
		for( int i=0; i<args.length; i++) {
			
			//get serverPort value.
			if(isServerPort) {
				try {
					int port = Integer.parseInt(args[i]);
					setServerPort(port);
					isServerPort = false;
				}catch(NumberFormatException e) {
					logger.log(Level.SEVERE,e.getMessage());
					usage();
					return (false);
				}
			}
			
			if ( args[i].equals("-serverPort") ) {
				isServerPort = true;
			}else if ( args[i].equals("-addbird") ) {
				setService ( true ) ;
				setServiceOption(ServiceOption.ADDBIRD);
				
			}else if ( args[i].equals("-addsighting") ) {
				setService ( true ) ;
				setServiceOption(ServiceOption.ADDSIGHTING);
				
			}else if ( args[i].equals("-listbirds") ) {
				setService ( true ) ;
				setServiceOption(ServiceOption.LISTBIRDS);
				
			}else if ( args[i].equals("-listsightings") ) {
				setService ( true ) ;
				setServiceOption(ServiceOption.LISTSIGHTINGS);
				
			}else if ( args[i].equals("-remove") ) {
				setService ( true ) ;
				setServiceOption(ServiceOption.REMOVE);
				
			}else if ( args[i].equals("-quit") ) {
				setCommand( true );
				setCommandOption(CommandOption.QUIT);
				
				
			}else if ( args[i].equals("-help") ) {
				usage();
				return(false);
			}else {
				usage();
				return(false);
			}
		}
		
		return (true);
	}
	
	protected void usage(){
		System.out.println
        ("usage: java app.Client"
         + " [ -serverPort {port} ]"
         + " [ -addbird | -addsighting | -listbirds | -listbirds | -listsightings | -remove | -quit ] "
         + " { -help }");
	}
	
	
	//----------------------getter/setter methods.

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the port to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * @return the isService
	 */
	public boolean isService() {
		return isService;
	}

	/**
	 * @param isService the isService to set
	 */
	public void setService(boolean isService) {
		this.isService = isService;
	}

	/**
	 * @return the isCommand
	 */
	public boolean isCommand() {
		return isCommand;
	}

	/**
	 * @param isCommand the isCommand to set
	 */
	public void setCommand(boolean isCommand) {
		this.isCommand = isCommand;
	}
	
	/**
	 * @return the serviceOption
	 */
	public ServiceOption getServiceOption() {
		return serviceOption;
	}

	/**
	 * @param serviceOption the serviceOption to set
	 */
	public void setServiceOption(ServiceOption serviceOption) {
		this.serviceOption = serviceOption;
	}

	/**
	 * @return the commandOption
	 */
	public CommandOption getCommandOption() {
		return commandOption;
	}

	/**
	 * @param commandOption the commandOption to set
	 */
	public void setCommandOption(CommandOption commandOption) {
		this.commandOption = commandOption;
	}
	
	//----------------------main method.
	public static void main(String[] args) throws Exception {  
	
		boolean isValid = Client.client.arguments(args);
		if ( !isValid ) System.exit(1);
		
		Socket socket = new Socket(new InetSocketAddress(Client.client.getServerPort()));
		if( Client.client.isService ) {
			ServiceJobInvoker.invoke( Client.client.getServiceOption() , socket );
			
		}
		
		if( Client.client.isCommand ) {
			CommandJobInvoker.invoke( Client.client.getCommandOption() , socket );
		}
		
		System.exit(0);
		
	}  
     
}  
	