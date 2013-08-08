package app.server.startup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.caching.CacheManager;
import app.socket.RequestObject;
import app.socket.ResponseObject;
import app.util.SerializableUtil;

/**
 * Startup/Shutdown for server. 
 * @author fanfan
 *
 */
public class Server extends Thread {
	
	public final static Server server = new Server();
	
	/**
	 * server port, default is 3000.
	 */
	private int port = 3000;
	
	/**
	 * data location folder, default is "serverdata" in user home directory.
	 */
	private String data = System.getProperty("user.home") +
				System.getProperty("file.separator") +
				"serverdata";
	/**
	 * number of worker threads to process requests, default is 2.
	 */
	private int proc_count = 2;
	
	/**
	 * The executer to handle the incomming requests.
	 */
	private ExecutorService revExecutor;  
	
	private Selector selector;
	
	private ServerSocketChannel socketChannel;
	
	
	
	/**
	 * The executer that handles to save in-memory data to data folder.
	 */
	private ScheduledExecutorService saveDataJobExecutor;
	
	/**
     * Use shutdown hook flag.
     */
    protected boolean useShutdownHook = true;

	/**
	 * shutdown hook
	 */
	private Thread shutdownHook = null;
	
	
	private static Logger logger = Logger.getLogger(Server.class.getName());
	
	
	private Server() {
		
	}

	/**
	 * server start.
	 */
	@Override
	public void start() {
		init();
		super.start();
		try {
			datainit();
		}catch(Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage());
			System.exit(1);
		}

	}	
	
	private void init() {
		if( revExecutor == null ) {
			revExecutor = Executors.newFixedThreadPool(proc_count);
		}
		//register shutdown hook.
		if (shutdownHook == null) {
            shutdownHook = new ShutdownHook();
        }
        Runtime.getRuntime().addShutdownHook(shutdownHook);
	}     
        
    private void datainit() throws Exception {
    	new CacheManager(data).load();
        
        if( saveDataJobExecutor  == null ) {
        	saveDataJobExecutor = Executors.newScheduledThreadPool(1);
        }
        //set initialDelay = 600ms, and period = 600ms as default.
        int initialDelay = 600;
        int period = 600;
        saveDataJobExecutor.scheduleAtFixedRate(new Runnable() {
        	@Override
        	public void run(){
        		new CacheManager(data).sync();
        	}
        }, initialDelay, period, TimeUnit.MILLISECONDS);
        
	}
	
	@Override
	public void run() {	
		
		try{
			selector = Selector.open();
			socketChannel = ServerSocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.socket().setReuseAddress(true);  
			socketChannel.socket().bind(new InetSocketAddress(port));  
			socketChannel.register(selector, SelectionKey.OP_ACCEPT);
			logger.info(" Server is starting... ");
			
			while (true) {
				selector.select();
				
				Iterator<SelectionKey> it = selector.selectedKeys().iterator(); 
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					
					if(!key.isValid())
						continue;
					
					if(key.isValid() && key.isAcceptable()) {
						logger.info(" Selector Accept ");
						SocketChannel channel = socketChannel.accept();
						channel.configureBlocking( false );
						channel.finishConnect();
						channel.register(selector, SelectionKey.OP_READ);
						continue;
					}

					if(key.isValid() && key.isReadable()) {

						logger.info(" Selector Read "+key);
						key.interestOps(0);
						execute(key);  
						continue;
					}
					
				}
			}
		}catch (ClosedChannelException ex) {  
	         logger.log(Level.SEVERE,ex.getMessage());  
	         System.exit(1);
        } catch (IOException ex) {  
        	 logger.log(Level.SEVERE,ex.getMessage()); 
        	 System.exit(1);
        } finally {  
            try {  
                selector.close();  
            } catch(Exception ex) {}  
            try {  
                socketChannel.close();  
            } catch(Exception ex) {}  
        }  
	}
	/**
	 * handle the incomming request.
	 * @param key
	 * @throws IOException
	 */
	private void execute(final SelectionKey key) throws IOException {
		
		Runnable rn = new Runnable(){ 
			
			@Override
			public void run(){
			    SocketChannel socketChannel = null;  
		        try {  
		        	socketChannel = (SocketChannel) key.channel();
		        	RequestObject requestObject = receiveData(socketChannel);
		        	logger.info(requestObject.toString());  
		            
		        	ResponseObject responseObject = JobProcessor.process(requestObject);
		        	
		        	sendData(socketChannel, responseObject);  
		            logger.info(responseObject.toString());  
		            
		        } catch (IOException e) {
					e.printStackTrace();
				} finally {  
		            try {  
		                socketChannel.close();  
		            } catch(Exception ex) {}  
		        }  
			}
		};
		revExecutor.execute(rn);
    }  
	
	/**
	 * receive data.
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */
	 private static RequestObject receiveData(SocketChannel socketChannel) throws IOException {  
	        RequestObject requestObject = null;  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        ByteBuffer buffer = ByteBuffer.allocate(1024);  
	          
	        try {  
	            byte[] bytes;  
	            int size = 0;  
	            while ((size = socketChannel.read(buffer)) >= 0) {  
	                buffer.flip();  
	                bytes = new byte[size];  
	                buffer.get(bytes);  
	                baos.write(bytes);  
	                buffer.clear();  
	            }  
	            bytes = baos.toByteArray();  
	            requestObject = (RequestObject)SerializableUtil.toObject(bytes);  
	        } finally {  
	            try {  
	                baos.close();  
	            } catch(Exception ex) {}  
	        }  
	        return requestObject;  
	 }  
  
	 /**
	  * send data.
	  * @param socketChannel
	  * @param myResponseObject
	  * @throws IOException
	  */
	 private static void sendData(SocketChannel socketChannel, ResponseObject responseObject) throws IOException {  
        byte[] bytes = SerializableUtil.toBytes(responseObject);  
        ByteBuffer buffer = ByteBuffer.wrap(bytes);  
        socketChannel.write(buffer);  
	 }  
	
	/**
	 * stop server.
	 * @param timeout
	 * @throws IOException
	 * @throws InterruptedIOException
	 */
	public void quit() {
		 
		//remove hooker.
		 if (useShutdownHook) {
             Runtime.getRuntime().removeShutdownHook(shutdownHook);
		 }
		 
		 //save in-memory data.
		 new CacheManager(data).sync();
		 
		 //socket.close().
		 try {
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}
	}
	//------------------------------------preprocess methods
	/**
     * Process the specified command line arguments, and return
     * <code>true</code> if we should continue processing; otherwise
     * return <code>false</code>.
     *
     * @param args Command line arguments to process
     */
	public boolean arguments(String[] args) {
		
		String val = "";
		for (int i = 0 ; args.length > 1 && i < args.length; i++) {
			
			if( val.equals("port") ) {
				try{
					int port = Integer.parseInt(args[i]);
					setPort(port);
					val = "";
				}catch(NumberFormatException e) {
					logger.log(Level.SEVERE,e.getMessage());
					usage();
					return (false);
				}
			}else if( val.equals("data") ) {
				String data = args[i]; 
				data = System.getProperty("user.home") +
					System.getProperty("file.separator") +
					data;
				setData(data);
				val = "";
			}else if( val.equals("proc_count") ) {
				try{
					int proc_count = Integer.parseInt(args[i]);
					setProc_count(proc_count);
					val = "";
				}catch(NumberFormatException e) {
					logger.log(Level.SEVERE,e.getMessage());
					usage();
					return (false);
				}	
			}
			
			if ( args[i].equals("-port") ) {
				val = "port";
			}else if ( args[i].equals("-data") ) {
				val = "data";
			}else if ( args[i].equals("-proc_count") ) {
				val = "proc_count";
			}else if (args[i].equals("-help") ) {
				usage();
				return (false) ;
			}else {
				usage();
				return (false) ;
			}
		}
		return (true);
	}

    /**
     * Print usage information for this application.
     */
	protected void usage(){
		System.out.println
        ("usage: java app.Server"
         + " [ -port {port} ]"
         + " [ -data {data} ] "
         + " [ -proc_number {proc_number} ] "
         + " { -help }");
	}
	
	 // --------------------------------------- ShutdownHook Inner Class

    /**
     * Shutdown hook which will perform a clean shutdown of Server if needed.
     */
    protected class ShutdownHook extends Thread {

        @Override
        public void run() {
            try {
                    Server.this.quit();
                    logger.info("Shutdown request received");
                
            } catch (Throwable ex) {
                logger.log(Level.SEVERE,ex.getMessage());
            }
        }
    }


	
	//----------------------------------override getter/setter methods
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the proc_count
	 */
	public int getProc_count() {
		return proc_count;
	}

	/**
	 * @param proc_count the proc_count to set
	 */
	public void setProc_count(int proc_count) {
		this.proc_count = proc_count;
	}
	
//	/**
//	 * @return the storeExecuter
//	 */
//	public ExecutorService getStoreExecuter() {
//		return storeExecuter;
//	}
//
//	/**
//	 * @param storeExecuter the storeExecuter to set
//	 */
//	public void setStoreExecuter(ExecutorService storeExecuter) {
//		this.storeExecuter = storeExecuter;
//	}
	
	//----------------------------------------main method 
	public static void main(String[] args) {
		
		boolean isValid = Server.server.arguments(args);
		if ( !isValid ) System.exit(1);
		
		Datastore datastore = new Datastore(Server.server.getData());
		isValid = datastore.setup();
		if ( !isValid ) System.exit(1);
		
		Server.server.start();
		
		
	}
}
