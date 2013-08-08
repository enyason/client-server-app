package app.caching;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.domain.Bird;
import app.domain.Sighting;
import app.util.XmlUtil;

public class CacheManager {
	
	private static Logger logger = Logger.getLogger(CacheManager.class.getName());  
	
	private String data;
	
	public CacheManager(String data) {
		this.data = data;
	}
	
	/**
	 * load 
	 * @throws Exception 
	 */
	public void load() throws Exception{
		loadbirds();
		loadsightings();
		
	}
	
	private void loadbirds() throws Exception {
		File birdfile = new File(data + 
				System.getProperty("file.separator") +
				"bird.xml");
		
		if(!birdfile.exists()) {
			birdfile.createNewFile();
			birdfile.canRead();
			birdfile.canWrite();
		}
		if(birdfile.length()==0) return;
		
		String tag = "bird";
		NodeList birdlists = XmlUtil.readXML(birdfile,tag);
		//Here we do the actual parsing
        for(int i=0;i<birdlists.getLength();i++) {
			Node birdnode = birdlists.item(i);
			if(birdnode.getNodeType() == Node.ELEMENT_NODE){
				 
				Element birdElement = (Element)birdnode;
				Bird bird = new Bird();
				
				NodeList nameList = birdElement.getElementsByTagName("name");
				Element nameElement = (Element)nameList.item(0);
				bird.setName(nameElement.getTextContent());
				
				
				NodeList colorList = birdElement.getElementsByTagName("color");
				Element colorElement = (Element)colorList.item(0);
				bird.setColor(colorElement.getTextContent());
				
				NodeList weightList = birdElement.getElementsByTagName("weight");
				Element weightElement = (Element)weightList.item(0);
				bird.setWeight(Integer.parseInt(weightElement.getTextContent()));
				
				NodeList heightList = birdElement.getElementsByTagName("height");
				Element heightElement = (Element)heightList.item(0);
				bird.setHeight(Integer.parseInt(heightElement.getTextContent()));
				
				BirdCache.birds.add(bird);
				
			}	
		}
	}
	
	private void loadsightings() throws Exception {
		
		File sightingfile = new File(data + 
				System.getProperty("file.separator") +
				"sighting.xml");
		
		if(!sightingfile.exists()) {
			sightingfile.createNewFile();
			sightingfile.canRead();
			sightingfile.canWrite();
		}
		if(sightingfile.length()==0) {return;}
		
		String tag = "sighting";
		NodeList sightinglists = XmlUtil.readXML(sightingfile,tag);
		//Here we do the actual parsing
        for(int i=0;i<sightinglists.getLength();i++) {
			Node sightingnode = sightinglists.item(i);
			if(sightingnode.getNodeType() == Node.ELEMENT_NODE){
				 
				Element sightingElement = (Element)sightingnode;
				Sighting sighting = new Sighting();
				
				NodeList nameList = sightingElement.getElementsByTagName("name");
				Element nameElement = (Element)nameList.item(0);
				sighting.setBirdname(nameElement.getTextContent());
				
				
				NodeList locationList = sightingElement.getElementsByTagName("location");
				Element colorElement = (Element)locationList.item(0);
				sighting.setLocation(colorElement.getTextContent());
				
				NodeList dateList = sightingElement.getElementsByTagName("date");
				Element dateElement = (Element)dateList.item(0);
				sighting.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateElement.getTextContent()));
				
				SightingCache.signtings.add(sighting);
				
			}	
		}
	}
	/**
	 * sync
	 */
	public void sync() {
		
		try {
			File birdfile = new File(data + 
					System.getProperty("file.separator") +
					"bird.xml");
			File sightingfile = new File(data + 
					System.getProperty("file.separator") +
					"sighting.xml");
			
			//addbirds.
			String tag1 = "bird";
			Document doc1 = XmlUtil.addXMLItems(birdfile,tag1);
			for(Bird bird:BirdCache.addbirds) {
				Element rootElement = doc1.getDocumentElement();
				Element birdE = doc1.createElement(tag1);
				rootElement.appendChild(birdE);
				
				Element name = doc1.createElement("name");
				name.appendChild(doc1.createTextNode(bird.getName()));
				birdE.appendChild(name);
		 
				Element color = doc1.createElement("color");
				color.appendChild(doc1.createTextNode(bird.getColor()));
				birdE.appendChild(color);
				
				Element weight = doc1.createElement("weight");
				weight.appendChild(doc1.createTextNode(new Integer(bird.getWeight()).toString()));
				birdE.appendChild(weight);
				
				Element height = doc1.createElement("height");
				height.appendChild(doc1.createTextNode(new Integer(bird.getHeight()).toString()));
				birdE.appendChild(height);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc1);
				StreamResult result = new StreamResult(birdfile);
		 
				transformer.transform(source, result);
		 
				logger.log(Level.INFO,"File saved!");
			}
			
			//removebirds.
			for(String name:BirdCache.removebirds) {
				XmlUtil.removeXMLItems(birdfile,tag1, name);
			}
			//addsightings.
			String tag2 = "sighting";
			Document doc2 = XmlUtil.addXMLItems(sightingfile,tag2);
			for(Sighting sighting:SightingCache.addSigntings) {
				Element rootElement = doc2.getDocumentElement();
				Element sightingE = doc2.createElement(tag2);
				rootElement.appendChild(sightingE);
				
				Element name = doc2.createElement("name");
				name.appendChild(doc2.createTextNode(sighting.getBirdname()));
				sightingE.appendChild(name);
		 
				Element location = doc2.createElement("location");
				location.appendChild(doc2.createTextNode(sighting.getLocation()));
				sightingE.appendChild(location);
				
				Element date = doc2.createElement("date");
				date.appendChild(doc2.createTextNode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sighting.getDate()).toString()));
				sightingE.appendChild(date);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc2);
				StreamResult result = new StreamResult(sightingfile);
		 
				transformer.transform(source, result);
		 
				logger.log(Level.INFO,"File saved!");
			}
			
			//clear cache.
			BirdCache.addbirds.clear();
			BirdCache.removebirds.clear();
			SightingCache.addSigntings.clear();
		}catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE,e.getMessage());
	
			//TODO:rollback...
		}	
	}

}
