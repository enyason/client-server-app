package test.app.server.startup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.RequestObject;
import app.util.SerializableUtil;

public class ClientSimulator {
	
	private static Logger logger = Logger.getLogger(ClientSimulator.class.getName());  
   
	
    public static void main(String[] args) throws Exception {  
		//100 clients.
		for (int i = 0; i < 100; i++) {  
	            final int idx = i;  
	            new Thread(new Runnable() {  
	          
		        @Override
		        public void run() {  
		            SocketChannel socketChannel = null;  
		            try {  
		                socketChannel = SocketChannel.open();  
		                SocketAddress socketAddress = new InetSocketAddress(3000);  
		                socketChannel.connect(socketAddress);  
		  
		                Object requestObject = new RequestObject("request_" + idx, "request_" + idx);  
		                logger.info(requestObject.toString());  
		                sendData(socketChannel, requestObject);  
		                  
		                Object responseObject = receiveData(socketChannel);  
		                logger.info(responseObject.toString());  
		            } catch (Exception ex) {  
		            	ex.printStackTrace();
		                logger.log(Level.SEVERE,ex.getMessage());  
		            } finally {  
		                try {  
		                    socketChannel.close();  
		                } catch(Exception ex) {}  
		            }  
		        }  
	  
		        private void sendData(SocketChannel socketChannel, Object requestObject) throws IOException {  
		            byte[] bytes = SerializableUtil.toBytes(requestObject);  
		            ByteBuffer buffer = ByteBuffer.wrap(bytes);  
		            socketChannel.write(buffer);  
		            socketChannel.socket().shutdownOutput();  
		        }  
	  
		        private Object receiveData(SocketChannel socketChannel) throws IOException {  
		            Object responseObject = null;  
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		              
		            try {  
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
		                responseObject = SerializableUtil.toObject(bytes);  
		              //  responseObject = (ResponseObject) obj;  
		            } finally {  
		                try {  
		                    baos.close();  
		                } catch(Exception ex) {}  
		            }  
		            return responseObject;  
		        } 
	      }).start();
		}
    }	
}
