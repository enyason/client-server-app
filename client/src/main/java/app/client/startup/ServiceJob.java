package app.client.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.logging.Level;  
import java.util.logging.Logger;  

import app.socket.ServiceOption;

public class ServiceJob extends ClientJobInf {
	
	private static Logger logger = Logger.getLogger(ServiceJob.class.getName());  
	
	/**
	 * invoke addbird service.
	 * @throws IOException 
	 */
	public void addbird(ServiceOption serviceOption, Socket socket)  {
		if(logger.isLoggable(Level.INFO)) logger.info("addbird invoking...");
		//user input...
		String inputname = serviceOption.name().toLowerCase();
		String inputvalue = "";
		InputStreamReader stdin = new InputStreamReader(System.in) ;
		BufferedReader bufin = new BufferedReader(stdin) ;
		try{
			System.out.print("name:");
			String name = bufin.readLine();
			inputvalue += "[" + name +"]";
			
			System.out.print("color:");
			String color = bufin.readLine();
			inputvalue += "[" + color +"]";
			
			System.out.print("weight:");
			String weightStr = bufin.readLine();
			int weight = Integer.parseInt(weightStr);
			inputvalue += "[" + weight +"]" ;
			
			System.out.print("height:");
			String heightStr = bufin.readLine();
			int height = Integer.parseInt(heightStr);
			inputvalue += "[" + height +"]" ;
			
		}catch(NumberFormatException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}		
		
		process(socket, inputname, inputvalue);
				
	}
	
	/**
	 * invoke addsighting service.
	 */
	public void addsighting(ServiceOption serviceOption, Socket socket) {
		if(logger.isLoggable(Level.INFO)) logger.info("addsighting invoking...");
		//user input...
		String inputname = serviceOption.name().toLowerCase();
		String inputvalue = "";
		InputStreamReader stdin = new InputStreamReader(System.in) ;
		BufferedReader bufin = new BufferedReader(stdin) ;
		try{
			System.out.print("bird name:");
			String bird = bufin.readLine();
			inputvalue += "[" + bird +"]";
			
			System.out.print("location:");
			String location = bufin.readLine();
			inputvalue += "[" + location +"]";
			
			System.out.print("date:[yyyy-MM-dd HH:mm:ss]");
			String dateStr = bufin.readLine();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.parse(dateStr);
			inputvalue += "[" + dateStr +"]" ;
			
		} catch (ParseException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}		
		
		process(socket, inputname, inputvalue);
	}
	
	/**
	 * invoke remove service.
	 */
	public void remove(ServiceOption serviceOption, Socket socket) {
		if(logger.isLoggable(Level.INFO)) logger.info("remove invoking...");
		//user input...
		String inputname = serviceOption.name().toLowerCase();
		String inputvalue = "";
		InputStreamReader stdin = new InputStreamReader(System.in) ;
		BufferedReader bufin = new BufferedReader(stdin) ;
		try{
			System.out.print("bird name:");
			String bird = bufin.readLine();
			inputvalue += "[" + bird +"]";
			
		}  catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}		
		
		process(socket, inputname, inputvalue);
	}
	
	/**
	 * invoke listbirds service.
	 */
	public void listbirds(ServiceOption serviceOption, Socket socket) {
		if(logger.isLoggable(Level.INFO)) logger.info("listbirds invoking...");
		
		String inputname = serviceOption.name().toLowerCase();
		String inputvalue = "";
		
		process(socket, inputname, inputvalue);
	}
	
	/**
	 * invoke listsightings service.
	 */
	public void listsightings(ServiceOption serviceOption, Socket socket) {
		if(logger.isLoggable(Level.INFO)) logger.info("listsightings invoking...");
		
		String inputname = serviceOption.name().toLowerCase();
		String inputvalue = "";
		InputStreamReader stdin = new InputStreamReader(System.in) ;
		BufferedReader bufin = new BufferedReader(stdin) ;
		try{
			System.out.print("bird name:");
			String bird = bufin.readLine();
			inputvalue += "[" + bird +"]";
			
			System.out.print("start date:[yyyy-MM-dd HH:mm:ss]");
			String startdateStr = bufin.readLine();
			DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf1.parse(startdateStr);
			inputvalue += "[" + startdateStr +"]" ;
			
			System.out.print("end date:[yyyy-MM-dd HH:mm:ss]");
			String enddateStr = bufin.readLine();
			DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf2.parse(enddateStr);
			inputvalue += "[" + enddateStr +"]" ;
			
		}  catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
		}		
		
		process(socket, inputname, inputvalue);
	}


}
