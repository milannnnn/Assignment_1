package assignment1_milan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class MyParser {
	
	public ArrayList<MyObject> parseXML(String eq_path, String ssh_path, String object, String[] data){
	// Parses the given CIM XML files (EQ and SSH) for required object with given data fields
	// and returns an Array List of found objects with filled data fields
	// 		ObjectList -> generated list of found objects (check class MyObject)
	//		eq_path    -> file path to the EQ CIM XML file
	//		ssh_path   -> file path to the SSH CIM XML file
	//		object     -> required object name (identifier)
	//		data       -> list of wanted data fields names
		
		ArrayList<MyObject> ObjectList = new ArrayList<MyObject>();
				
		try{			
			// Read EQ XML file
			File XmlFileEQ = new File (eq_path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc_eq = dBuilder.parse(XmlFileEQ);
			doc_eq.getDocumentElement().normalize();
			
			// Read the SSH XML file
			File XmlFileSSH = new File (ssh_path);
			Document doc_SSH = dBuilder.parse(XmlFileSSH);
			doc_SSH.getDocumentElement().normalize();

			// Find all Elements with Required Object
			NodeList NL1 = doc_eq.getElementsByTagName(object);
			NodeList NL2 = doc_SSH.getElementsByTagName(object);
			
			// Parse all Elements found in the EQ node list (since all elements from SSH are already contained within EQ)
			for(int j=0; j<NL1.getLength(); j++){
				Element el = (Element) NL1.item(j);
				String id = el.getAttribute("rdf:ID");
				String[] data_attr = new String[data.length];
				// Parse over all Required Data fields
				for(int k=0; k<data.length; k++){
					// If we are looking for the Base Voltage (which is not contained within the element) 
					// First find the EqContainer, and then from that we can find the corresponding VoltLevel
					if(data[k].equals("baseVoltage")){
						String req_volt_id = searchForSimpleData(el,NL2,"cim:Equipment.EquipmentContainer",id);
						data_attr[k] =  searchForVoltageID(req_volt_id, doc_eq);
					}
					// If we are looking for other simple data (already contained within element) - we parse normally
					else{
						data_attr[k] = searchForSimpleData(el, NL2, data[k], id);
					}
				}
				// Push the acquired object into the Array List
				ObjectList.add(new MyObject(object,id,data,data_attr));
			}
		}
		
		// ###### Catching Some Common Exceptions ######
		catch (FileNotFoundException e){
			System.out.println("The specified XML files were not found, please check the given path / filename!!!");
		}
		catch (IOException e){ 
			System.out.println("I/O/Handling Problems - Please Check Reading, Writing, Permissions, Space, etc!!!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ObjectList;
	}

	// #####################################################################
	// ##### ADDITIONAL METHODS USED INSIDE THE MAIN "parseXML" METHOD #####
	// #####################################################################	
	
	
	// --------------------------------------------------------------------------------
	// ###### Method for Extracting Simple Data (contained WITHIN Given Element) ######
	public String searchForSimpleData(Element el, NodeList NL2, String data, String id){
		String data_attr;
		// First search for required tag in the EQ element!!!
		try{
				// Look both inside the Tag and Attribute (check fishForData method)
				data_attr = fishForData(el.getElementsByTagName(data).item(0));
				return data_attr;
		}
		// If not found there, search through the SSH node
		catch(java.lang.NullPointerException e1){
			try{
				boolean notFoundFlag = true;
				Element el2 = null;
				// But first we need to find the correct node (same ID)
				for(int l=0; l<NL2.getLength(); l++){
					el2 = (Element) NL2.item(l);
					if(el2.getAttribute("rdf:about").substring(1).equals(id)){
						notFoundFlag = false;
						break;
					}
				}
				// Throw an exception if the required ID was not found
				if(notFoundFlag){
					throw new NullPointerException();
				}
				// If the ID was found, look for required data inside tag / attribute
				data_attr = fishForData(el2.getElementsByTagName(data).item(0));
				return data_attr;
			}
			// If it still throws an exception we have a problem with required data field!!!
			catch(java.lang.NullPointerException e){
				System.out.println("WARNING: Required data attribute not found (probably missing or misspelled)");
				System.out.println("Please check the data field \""+data+"\" for the \""+id+"\" object!!!\n");
				System.out.println("=> Program Intentionally Terminated (Kill it before it lays eggs!!!)");
				// Kill the program - Force the Human to correct its mistake (so it feels important)!!! 
				// (otherwise we could have also continued the code by writing a NULL element)!!!
				System.exit(0);
			}						
		}
		return null;
	}
	
	// -------------------------------------------------------------------------------------
	// ###### Method for Base Voltage Data (from Given Equipment Container Reference) ######
	public String searchForVoltageID(String req_volt_id, Document doc_eq){
		
		NodeList NL = doc_eq.getElementsByTagName("cim:VoltageLevel");
		for(int j=0; j<NL.getLength(); j++){
			Element el = (Element) NL.item(j);
			if(el.getAttribute("rdf:ID").equals(req_volt_id.substring(1))){
				return el.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				//return el.getAttribute("rdf:ID");
			}
		}
		System.out.println("WARNING: Required Base Voltage ID ("+req_volt_id.substring(1)+") not found");
		System.out.println("=> Program Intentionally Terminated (please check your EQ XML file)");
		System.exit(0);
		return null;
	}
	
	// ----------------------------------------------------------------------------------------
	// ###### Method that Searches for Required Data (first in tags, then in attributes) ######
	public String fishForData(Node nd){
		
		// If the tag is empty, check the "rdf:resource" attribute
		if(nd.getTextContent().equals("")){
			return ((Element)nd).getAttribute("rdf:resource");
		}		

		// If the tag is not empty, just return the tag string
		else{
			return nd.getTextContent();
		}
	}

}