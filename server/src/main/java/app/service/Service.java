package app.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;  

import app.caching.BirdCache;
import app.caching.ComparatorSighting;
import app.caching.SightingCache;
import app.domain.Bird;
import app.domain.Sighting;
import app.exception.DuplicateException;
import app.exception.NotFoundException;
import app.socket.RequestObject;
import app.socket.ResponseObject;

public class Service implements ServiceInf {
	
	private static Logger logger = Logger.getLogger(Service.class.getName());  
	   
	@Override
	public ResponseObject addbird(RequestObject requestObject) {
		logger.info("addbird calling...");
		String name = "";
		String output = "";
		try {
			Bird bird = new Bird();
			String inputvalue = requestObject.getValue();
			String[] vs = inputvalue.split("]");
			name = vs[0].substring(1);
			String color = vs[1].substring(1);
			int weight = Integer.parseInt(vs[2].substring(1));
			int height = Integer.parseInt(vs[3].substring(1));
			bird.setName(name);
			bird.setColor(color);
			bird.setWeight(weight);
			bird.setHeight(height);
			
			boolean isnew = true;
			isnew = BirdCache.addbirds.add(bird);
			if( !isnew ) throw new DuplicateException();
			isnew = BirdCache.birds.add(bird);
			if( !isnew ) throw new DuplicateException();
			
			//add to Bird Cache.
			output = "Bird <" + name + "> successfully added to the database.";
		}catch (DuplicateException e) {
			output = "Failed to add. bird <" + name + "> already exists.";
		}catch (Exception e) {
			e.printStackTrace();
			output = "Failed to add bird <" + name + ">, " + e.getMessage();
		}
		return new ResponseObject(output);
	}

	@Override
	public ResponseObject addsighting(RequestObject requestObject) {
		logger.info("addsighting calling...");
		String name = "";
		String output = "";
		try {
			Sighting sighting = new Sighting();
			String inputvalue = requestObject.getValue();
			String[] vs = inputvalue.split("]");
			name = vs[0].substring(1);
			sighting.setBirdname(name);
			
			String location = vs[1].substring(1);
			sighting.setLocation(location);
			
			String dateStr = vs[2].substring(1);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateStr);
			sighting.setDate(date);
			
			SightingCache.addSigntings.add(sighting);
			//sort SightingCache.sightings.
			ComparatorSighting comparator=new ComparatorSighting();
			Collections.sort(SightingCache.signtings, comparator);
			
			
			output = "sighting for bird <" + name + "> successfully added to the database.";
		}catch (Exception e) {
			output = "Failed to add sighting for <" + name + ">, " + e.getMessage();
		}
		return new ResponseObject(output);
	}

	@Override
	public ResponseObject remove(RequestObject requestObject) {
		logger.info("remove calling...");
		String name = "";
		String output = "";
		try {
			String inputvalue = requestObject.getValue();
			name = inputvalue.substring(1,inputvalue.length()-1);
			Bird bird = new Bird();
			bird.setName(name);
			
			if(BirdCache.birds.contains(bird)){
				BirdCache.birds.remove(bird);
				BirdCache.addbirds.remove(bird);
				BirdCache.removebirds.add(name);
				output = "bird <" + name + "> successfully removed from the database.";
			}else {
				throw new NotFoundException();
			}
			
		}catch (NotFoundException e) {
			output = "Not Found the bird. Failed to remove bird <" + name + ">, " + e.getMessage();
			
		}catch (Exception e) {
			output = "Failed to remove bird <" + name + ">, " + e.getMessage();
		}
		return new ResponseObject(output);
	}

	@Override
	public ResponseObject listbirds(RequestObject requestObject) {
		logger.info("listbirds calling...");
		String output = "";
		try {
			output = "listbirds\n";
			for (Bird bird: BirdCache.birds) {
				output += "name:<"+ bird.getName() +">," +
				"color:<"+ bird.getColor() +">," +
				"weight:<"+ bird.getWeight() +">," +
				"height:<"+ bird.getHeight() +">," +
				"\n";
			}
			
		}catch (Exception e) {
			output = "Failed to listbirds, " + e.getMessage();
		}
		return new ResponseObject(output);
	}

	@Override
	public ResponseObject listsightings(RequestObject requestObject) {
		logger.info("listsightings calling...");
		String nameregx = "";
		String output = "";
		try {
			String inputvalue = requestObject.getValue();
			String[] vs = inputvalue.split("]");
			nameregx = vs[0].substring(1);
			
			String startdateStr = vs[1].substring(1);
			DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startdate = sdf1.parse(startdateStr);
		
			String enddateStr = vs[2].substring(1);
			DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date enddate = sdf2.parse(enddateStr);
			
			output = "listsightings\n";
			
			for(Sighting sighting:SightingCache.signtings) {
				Pattern p = Pattern.compile(nameregx);
				Matcher m = p.matcher(sighting.getBirdname());
				while(m.find()) {
					String name = sighting.getBirdname();
					String location = sighting.getLocation();
					Date date = sighting.getDate();
					//date in [startdate,enddate]
					if(date.compareTo(startdate)>=0 && date.compareTo(enddate)<=0) {
						output += "name:<"+ name + ">,"+
								"location:<"+ location +">,"+
								"date:<" + date + ">";
					}
				}
			}
			
		}catch (Exception e) {
			output = "Failed to listsightings, " + e.getMessage();
		}
		return new ResponseObject(output);
	}

}
