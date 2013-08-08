package app.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlUtil {
	
	private static Logger logger = Logger.getLogger(XmlUtil.class.getName());  
	
	public static NodeList readXML(File xmlfile, String tag) throws Exception {
			NodeList list = null;
			DocumentBuilderFactory factory =
	            DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.parse(xmlfile);
	        Element rootElement = doc.getDocumentElement();
	        list = rootElement.getElementsByTagName(tag);
	        
			return list;
	}
	
	public static Document addXMLItems(File xmlfile,String tag) throws Exception {
		DocumentBuilderFactory factory =
	            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;
		if(xmlfile.length()==0)  { 
			doc = builder.newDocument();
			Element rootElement = doc.createElement(tag+"s");
			doc.appendChild(rootElement);
		}else {
			doc = builder.parse(xmlfile);
		}
		return doc;
	}
	
	public static void removeXMLItems(File xmlfile,String tag,String value) throws Exception {
		
		NodeList list = null;
		DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		//Here we do the actual parsing
        Document doc = builder.parse(xmlfile);
        Element rootElement = doc.getDocumentElement();
        list = rootElement.getElementsByTagName(tag);
        
        Element element =null;   
        for(int i=0;i<list.getLength();i++){   
	        element = (Element)list.item(i);   
	        if((element.getElementsByTagName("name").item(0).getTextContent()).trim().equals(value)){   
	        	element.getParentNode().removeChild(element);   
	        }	
	   }
       
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(xmlfile);
 
		transformer.transform(source, result);
 
		logger.log(Level.INFO,"xml data removed!");
        
	}

}
