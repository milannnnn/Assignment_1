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

// TODO - Maybe update it so that it can read multiple children tags with the same name (not just to read the first and finish)!!!

public class MyParser {
	
	// --------------------------------------------------------------------------------------------------
    //###################################################################################################
	public Document readFile(String file_path){
	// Method for Loading and Normalizing a XML File
	//
	// doc       -> normalized XML document
	// file_path -> path to desired XML file
		
		Document doc = null;
		try{			
			// Read EQ XML file
			File xmlFile = new File (file_path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
		}
			
		// ###### Catching Common Exceptions ######
		catch (FileNotFoundException e){
			System.out.println("The specified XML file was not found, please check the given path:");
			System.out.println(file_path);
			terminateProgram();
		}
		catch (IOException e){ 
			System.out.println("I/O/Handling Problems (reading, writing, permissions, space, etc.), please check given path:");
			System.out.println(file_path);
			terminateProgram();
		}
		catch(Exception e){
			e.printStackTrace();
			terminateProgram();
		}
		return doc;
	}
	
	// -------------------------------------------------------------------------------------------------------------
    //##############################################################################################################
	public ArrayList<MyObject> parseXML(Document doc_eq, Document doc_ssh, String object, String[] data){
	// Method for Parsing given CIM XML files (EQ and SSH) for Given Object over Required Data Fields
	// (returns an Array List of found objects with filled in data fields - check class MyObject)
	//
	// 		ObjectList -> generated list of parsed objects
	//		doc_eq     -> normalized EQ  CIM XML document
	//		doc_SSH    -> normalized SSH CIM XML document
	//		object     -> required object name (identifier)
	//		data       -> list of wanted data fields names
		
		ArrayList<MyObject> ObjectList = new ArrayList<MyObject>();

		// Find all Elements with Required Object
		NodeList NL1 = doc_eq.getElementsByTagName(object);
		NodeList NL2 = doc_ssh.getElementsByTagName(object);
		
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
		
		return ObjectList;
	}
	
	// --------------------------------------------------------------------------------------------------
    //###################################################################################################
	public String searchForSimpleData(Element el, NodeList NL2, String data, String id){
	// Method for Extracting Required Simple Data (contained within the EQ node or its SSH counterpart)
	//
	// el 	-> given EQ element
	// NL2 	-> SSH node list of required object
	// data	-> required data
	// id	-> ID of given element
		
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
				System.out.println("Please check for data field \""+data+"\" inside \""+id+"\" object!!!\n");
				
				// Kill the program - Force the Human to correct its mistake (so it feels important)!!! 
				// (otherwise we could have also continued the PROGRAM by writing a NULL element)!!!
				terminateProgram();
			}						
		}
		return null;
	}
	
	// --------------------------------------------------------------------------------------------------
    //###################################################################################################
	public String searchForVoltageID(String req_volt_id, Document doc_eq){
	// Method for Extracting Base Voltage ID Data - not contained within the analyzed EQ/SSH element
	// (first it finds the referenced Voltage Level node, then it reads the Base Voltage attribute)
	// 
	// req_volt_id	-> given voltage level ID (taken from equipment container)
	// doc_eq		-> normalized EQ document

		NodeList NL = doc_eq.getElementsByTagName("cim:VoltageLevel");
		for(int j=0; j<NL.getLength(); j++){
			Element el = (Element) NL.item(j);
			if(el.getAttribute("rdf:ID").equals(req_volt_id.substring(1))){
				//return el.getElementsByTagName("cim:IdentifiedObject.name").item(0).getTextContent();
				//return el.getAttribute("rdf:ID");
				el = (Element)el.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
				String tmpStr = el.getAttribute("rdf:resource");
				if(! tmpStr.equals("")){
					// remove the '#' from the start of the string
					return tmpStr.substring(1);
				}
				break;
			}
		}
		System.out.println("WARNING: Required Base Voltage ID not found (check Voltage Level "+req_volt_id.substring(1)+")");
		terminateProgram();
		return null;
	}
	
	// --------------------------------------------------------------------------------------------------
    //###################################################################################################
	public String fishForData(Node nd){
	// Method for Extracting Required Data Contained within Given Node (in Tags or Attributes)

		// If the tag is empty, check the "rdf:resource" attribute
		if(nd.getTextContent().equals("")){
			return ((Element)nd).getAttribute("rdf:resource");
		}		

		// If the tag is not empty, just return the tag string
		else{
			return nd.getTextContent();
		}
	}
	
	// --------------------------------------------------------------------------------------------------
    //###################################################################################################
	public void terminateProgram(){
	// Method for terminating the program (in case of exceptions and errors)
		System.out.println("\n=> Program Intentionally Terminated (Kill it before it lays eggs!!!)");
		System.exit(0);
	}

}