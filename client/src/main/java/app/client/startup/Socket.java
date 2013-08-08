package app.client.startup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.RequestObject;
import app.socket.ResponseObject;
import app.util.SerializableUtil;

public class Socket {
	
	private static Logger logger = Logger.getLogger(Socket.class.getName());  
	
	//------- properties.
	private InetSocketAddress serveraddr ;
	
	private SocketChannel socketChannel;
	
	//--------constructor.
	public Socket(InetSocketAddress serveraddr) {
		this.serveraddr = serveraddr;
	}
	
	
	//--------methods.
	/**
	 * 
	 * @param serveraddr
	 * @throws IOException  
	 */
	public void connect() throws IOException {
		
			socketChannel = SocketChannel.open();  
            SocketAddress socketAddress = serveraddr;  
            socketChannel.connect(socketAddress);  
	            
	}
	
	/**
	 * send data.
	 * @param socketChannel
	 * @param requestObject
	 * @throws IOException
	 */
	public void sendData(RequestObject requestObject) throws IOException {  
	    byte[] bytes = SerializableUtil.toBytes(requestObject);  
        ByteBuffer buffer = ByteBuffer.wrap(bytes);  
        socketChannel.write(buffer);  
        socketChannel.socket().shutdownOutput();  
    }  
		
	/**
	 * receive data.
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */
    public ResponseObject receiveData() throws IOException {  
    	ResponseObject responseObject = null;  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
          
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);  
        byte[] bytes;  
        int count = 0;  
        while ((count = socketChannel.read(buffer)) >= 0) {  
            buffer.flip();  
            bytes = new byte[count];  
            buffer.get(bytes);  
            baos.write(bytes);  
            buffer.clear();  
        }  
        bytes = baos.toByteArray();  
        responseObject = (ResponseObject)SerializableUtil.toObject(bytes);  
        return responseObject;  
    }
    
    /**
     * close socket.
     */
	public void close() {
		 try {  
            socketChannel.close();  
        } catch(Exception ex) {
       	 logger.log(Level.SEVERE,ex.getMessage());
       	 ex.printStackTrace();
        }  
		
	}

	
	//----------------- getter/setter methods.
	/**
	 * @return the socketChannel
	 */
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}


	/**
	 * @param socketChannel the socketChannel to set
	 */
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

}
