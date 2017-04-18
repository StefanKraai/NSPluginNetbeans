package nl.hu.nl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	List<Station> stations = new ArrayList<Station>();
	
	public double getHeight(String stationName){
		Station station = null;
		for(int i = 0; i < stations.size(); i++){
			if(stations.get(i).getName().equals(stationName)){
				station = stations.get(i);
				break;
			}
			
		}
		if(station ==  null){
			System.out.println("geen station");
		}
		try {
			URL url;
			url = new URL("https://maps.googleapis.com/maps/api/elevation/xml?locations=" + station.getLat() + "," + station.getLon() + "&key=AIzaSyBYm7OC95LqnbqJph1xEqaRjYAvoXZ1j1A");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String r = "";
	        String l = "";
			while((l = in.readLine()) != null){
				r = r + in.readLine();
			}
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse(url.openStream());
	        NodeList nodeList = document.getDocumentElement().getChildNodes();
	        for (int i = 0; i < nodeList.getLength(); i++) {
	        	// circelen door stations
	        	Node node1 = nodeList.item(i);
	        	if(node1.getNodeType()==Node.ELEMENT_NODE){
	        		Element element1 = (Element) node1;
	        		if(element1.getTagName().equals("result")){
	        			NodeList nodeList2 = node1.getChildNodes();
	        			for (int g = 0; g < nodeList2.getLength(); g++) {
	        				Node node2 = nodeList2.item(g);
	        				if(node2.getNodeType()==Node.ELEMENT_NODE){
	        	        		Element element2 = (Element) node2;
	        	        		if(element2.getTagName().equals("elevation")){
	        	        			return Double.parseDouble(element2.getTextContent());
	        	        		}
	        				}
	        			}
	        		}
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 900000000;
	}
	
	public List<Station> GetStationsFromNS() throws Exception{
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication ("arpad.kolkert@student.hu.nl", "IkrnqWgRzc57EZfJAl0jJ-pmuxJOgAq-g4lYkhyBUf0v1-bZ0hZLdA".toCharArray());
		    }
		});
		URL url;
		url = new URL("http://webservices.ns.nl/ns-api-stations-v2");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String r = "";
        String l = "";
        try {
			while((l = in.readLine()) != null){
				r = r + in.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(url.openStream());
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
        	// circelen door stations
        	Node station = nodeList.item(i);
        	if(station.getNodeType()==Node.ELEMENT_NODE){
	        	NodeList stationElementen = station.getChildNodes();
	        	
	        	String code = "";
	        	String type = "";
	        	String nameKort = "";
	        	String nameMiddel = "";
	        	String nameLang = "";
	        	String country = "";
	        	int UICode = 0;
	        	double lat = 0;
	        	double lon = 0;
	        	
	        	for (int j = 0; j < stationElementen.getLength(); j++){
	        		// circelen door station gegevens
	        		Node name = stationElementen.item(j);
	        		if(name.getNodeType()==Node.ELEMENT_NODE){
	            		Element StationElement = (Element) name;
	            		if(StationElement.getTagName().equals("Code")){
	            			code = StationElement.getTextContent();
	            		}
	            		if(StationElement.getTagName().equals("Type")){
	            			type = StationElement.getTextContent();
	            		}
	            		if(StationElement.getTagName().equals("Namen")){
	            			NodeList namen = name.getChildNodes();
	            			for (int k = 0; k < namen.getLength(); k++) {
	            	        	// circelen door alle namen
	            	        	Node naam = namen.item(k);
	            	        	if(naam.getNodeType()==Node.ELEMENT_NODE){
	            		        	Element naamElement = (Element) naam;
	            		        	if(naamElement.getTagName().equals("Kort")){
	            		        		nameKort = naamElement.getTextContent();
	            		        	}
	            		        	if(naamElement.getTagName().equals("Middel")){
	            		        		nameMiddel = naamElement.getTextContent();
	            		        	}
	            		        	if(naamElement.getTagName().equals("Lang")){
	            		        		nameLang = naamElement.getTextContent();
	            		        	}
	            	        	}
	            			}
	            		}
	            		if(StationElement.getTagName().equals("Land")){
	            			country = StationElement.getTextContent();
	            		}
	            		if(StationElement.getTagName().equals("UICCode")){
	            			UICode = Integer.parseInt(StationElement.getTextContent());
	            		}
	            		if(StationElement.getTagName().equals("Lat")){
	            			lat = Double.parseDouble(StationElement.getTextContent());
	            		}
	            		if(StationElement.getTagName().equals("Lon")){
	            			lon = Double.parseDouble(StationElement.getTextContent());
	            		}
	            	}
	        		
	        	}
	        	stations.add(new Station(code, type, nameKort, nameMiddel, nameLang, country, UICode, lat, lon));
        	}
        }
        return stations;
    }
}
