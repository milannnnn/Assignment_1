package assignment1;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ReadXML {
	public static void main(String[] args){
		
		// Attributes List for every CIM object
		String[] BaseVoltageAttribute = {"cim:BaseVoltage.nominalVoltage"};
		String[] SubstationAttribute = {"cim:IdentifiedObject.name","cim:Substation.Region"};
		String[] VolatgeLevelAttribute = {"cim:IdentifiedObject.name","cim:VoltageLevel.Substation","cim:VoltageLevel.BaseVoltage"};
		String[] GeneratingUnitAttribute = {"cim:IdentifiedObject.name","cim:GeneratingUnit.maxOperatingP","cim:GeneratingUnit.minOperatingP","cim:Equipment.EquipmentContainer"};
		String[] SynchronousMachineAttribute = {"cim:IdentifiedObject.name","cim:RotatingMachine.ratedS","cim:RotatingMachine.p","cim:RotatingMachine.q","cim:RotatingMachine.GeneratingUnit",
				"cim:RegulatingCondEq.RegulatingControl", "cim:Equipment.EquipmentContainer","baseVoltage"};
		String[] RegulatingControlAttribute = {"cim:IdentifiedObject.name","cim:RegulatingControl.targetValue"};
		String[] PowerTransformerAttribute = {"cim:IdentifiedObject.name","cim:Equipment.EquipmentContainer"};
		String[] EnergyConsumerAttribute = {"cim:IdentifiedObject.name","cim:EnergyConsumer.p","cim:EnergyConsumer.q","cim:Equipment.EquipmentContainer","baseVoltage"};
		String[] PowerTransformerEndAttribute = {"cim:IdentifiedObject.name","cim:PowerTransformerEnd.r","cim:PowerTransformerEnd.x",
				"cim:PowerTransformerEnd.PowerTransformer","cim:TransformerEnd.BaseVoltage"};
		String[] BreakerAttribute = {"cim:IdentifiedObject.name","cim:Switch.open","cim:Equipment.EquipmentContainer","baseVoltage"};
		String[] RatioTapChangerAttribute = {"cim:IdentifiedObject.name","cim:TapChanger.step"};
		
		// Create an array list with all the attributes
		ArrayList<String[]> AttList = new ArrayList<String[]>();
		AttList.add(BaseVoltageAttribute);
		AttList.add(SubstationAttribute);
		AttList.add(VolatgeLevelAttribute);
		AttList.add(GeneratingUnitAttribute);
		AttList.add(SynchronousMachineAttribute);
		AttList.add(RegulatingControlAttribute);
		AttList.add(PowerTransformerAttribute);
		AttList.add(EnergyConsumerAttribute);
		AttList.add(PowerTransformerEndAttribute);
		AttList.add(BreakerAttribute);
		AttList.add(RatioTapChangerAttribute);
				
		try{
			
			// Read XML file EQ
			File XmlFileEQ = new File ("C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc_eq = dBuilder.parse(XmlFileEQ);
			
			// Read XML file SSH
			File XmlFileSSH = new File ("C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml");
			Document doc_SSH = dBuilder.parse(XmlFileSSH);
			
			// normalize CIM XML file
			doc_eq.getDocumentElement().normalize();
			doc_SSH.getDocumentElement().normalize();
			
			ArrayList<NodeList> NodeListList = new ArrayList<NodeList>();
//			String[] nodeListNames = {"bvList","subList","volList","guList","genList","rcList","ptList","ecList","pteList","bList","rtcList"};
			// Array with all the CIM objects tags
			String[] tagList = {"cim:BaseVoltage","cim:Substation","cim:VoltageLevel","cim:GeneratingUnit","cim:SynchronousMachine",
					"cim:RegulatingControl","cim:PowerTransformer", "cim:EnergyConsumer","cim:PowerTransformerEnd","cim:Breaker","cim:RatioTapChanger"};
			// It fill out the ArrayList "NodeListList", where each object is a list of nodes characterized by the same tag, i.e. newNodeList
			for(int i=0; i<tagList.length; i++){
				// Node list obtained looking for tags in EQ document
				NodeList newNodeList1 = doc_eq.getElementsByTagName(tagList[i]);
				// Node list obtained looking for tags in SSH document
				NodeList newNodeList2 = doc_SSH.getElementsByTagName(tagList[i]);
				// Some tags are not present in the SSH file, hence, we fill that list we the same not of list1, just to avoid handling an empty list
				if(newNodeList2 == null || newNodeList2.getLength() == 0 ){
					newNodeList2 = newNodeList1;
				}
				// both lists are added to the same ArrayList
				// even position are then relative to NodeList1
				// odd positions are relative to NodeList2
				NodeListList.add(newNodeList1);
				NodeListList.add(newNodeList2);
			}
			
			
			// … cycle through each list to extract required data
			for(int k = 0; k < NodeListList.size(); k +=2){
				// It extracts each element of NodeListList, called newNodeList, which is a list of nodes having the same tag 
				System.out.println("\n" + "---------------------------------------" + "\n" + tagList[k/2] + "\n" + "---------------------------------------" + "\n");
				NodeList newNodeList1 = NodeListList.get(k);
				NodeList newNodeList2 = NodeListList.get(k+1);
			
				for (int i = 0; i < newNodeList1.getLength(); i++) {
					// From the node list newNodeList, it extracts each node and invokes extractNode method
					// newNodeList1 and newNodeList2 have the same length, because they are referred to the same CIM objects.
					Node node1 = newNodeList1.item(i); 
					Node node2 = newNodeList2.item(i);
					extractNode(node1,node2,k,i,AttList,doc_eq);
				}
			}
		}
		
		
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// Method to extract data and attributes from a node
	public static void extractNode (Node node1, Node node2, int k, int i, ArrayList<String[]> AttList, Document doc_eq){
		// converts nodes to elements
		Element element1 = (Element) node1;
		Element element2 = (Element) node2;
		// gets the element ID and prints it
		String rfdID = element1.getAttribute("rdf:ID");
		System.out.println( "\n" + (i+1) + ") rdfID: " + rfdID + "\n");
		// From the ArrayList "AttList", it extracts the array containing the data tags of a specific CIM object identified by k
		String[] attributes = AttList.get(k/2);
				
		for(int l = 0; l < attributes.length; l++){
			
			// It reads the value related to a specific tag "attributes[l]"
			
			// It start looking for the tag/attribute "attributes[l]", inside element1, which is obtained from EQ document
			try{
				String name = element1.getElementsByTagName(attributes[l]).item(0).getTextContent();
			
				if(!name.isEmpty()){
					// if the data is not empty it prints it out
					System.out.println(attributes[l] + "\t" + name + "\n");
				}
				else{
					// if the data is empty it means that child has no data, just an attribute, so it reads the attribute
					// it extracts the node identified by that tag "attributes[l]"
					Node nodelistID = element1.getElementsByTagName(attributes[l]).item(0);
					Element nodelistIDelement = (Element) nodelistID;
					// it read and prints the value of that attribute
					String resourceID = nodelistIDelement.getAttribute("rdf:resource");
	//				System.out.println("I'm using this");
					System.out.println(attributes[l] + " rdf:resource=" + "\t" + resourceID + "\n");	
				}			
			}
			// if the tags is not in EQ file, Java will throw a "java.lang.NullPointerException",
			// we catch it and we look for that tag in the SSH document
			catch(java.lang.NullPointerException e){
				
				// We try to see if the tag is present
				try{				
					String nameSSH = element2.getElementsByTagName(attributes[l]).item(0).getTextContent();
					// if the data is not empty it prints it out
					if(!nameSSH.isEmpty()){
						// if the data is not empty it prints it out
						System.out.println(attributes[l] + "\t" + nameSSH + "\n");
					}
					else{
						// if the data is empty it means that child has no data, just an attribute, so it reads the attribute
						// it extracts the node identified by that tag "attributes[l]"
						Node nodelistID2 = element2.getElementsByTagName(attributes[l]).item(0);
						Element nodelistIDelement2 = (Element) nodelistID2;
						// it read and prints the value of that attribute
						String resourceID2 = nodelistIDelement2.getAttribute("rdf:resource");
		//				System.out.println("I'm using this");
						System.out.println(attributes[l] + " rdf:resource=" + "\t" + resourceID2 + "\n");	
					}
				}
				// if the tags is neither in EQ file, nor in the SSH file, Java will throw a "java.lang.NullPointerException",
				// this means the tag is the baseVoltage tag, which should be derived from the equipmentCointainer ID 
				// it determines the base voltage
				catch(java.lang.NullPointerException er){
					// It reads the ID of the EquipmentContainer
					Node ECIDnode = element1.getElementsByTagName("cim:Equipment.EquipmentContainer").item(0);
					Element ECIDelement = (Element) ECIDnode;
					// it read and prints the value of that attribute
					String ECID = ECIDelement.getAttribute("rdf:resource");
					// It removes the hash before the ID
					ECID = ECID.substring(1);
					
					// It creates a nodeList with all the nodes tagged as "cim:VoltageLevel"
					NodeList voltageLevelList = doc_eq.getElementsByTagName("cim:VoltageLevel");
					Element voltageLevelEl = null;
					// It loops through all the nodes and selects the one with the corresponding ID, i.e. ECID
					for(int t=0; t<voltageLevelList.getLength(); t++){
						Node voltageLevelnode = voltageLevelList.item(t);
						Element newvoltageLevel = (Element) voltageLevelnode;
						boolean ver = newvoltageLevel.getAttribute("rdf:ID").equals(ECID);
						if(ver == true){
							voltageLevelEl =  newvoltageLevel;
						}
					}
					// It extracts the child "BaseVoltage" and reads its ID
					Node BVnode = voltageLevelEl.getElementsByTagName("cim:VoltageLevel.BaseVoltage").item(0);
					Element BVelement = (Element) BVnode;
					String BVID = BVelement.getAttribute("rdf:resource");
					// It prints out the tag "baseVolatge rdf:ID " and gives the found ID
					System.out.println(attributes[l] + " rdf:ID" + "\t" + BVID + "\n");
					// it removes the hash from the ID
					BVID = BVID.substring(1);
		
					// It creates a nodeList with all the nodes tagged as "cim:BaseVoltage"
					NodeList baseVoltagelList = doc_eq.getElementsByTagName("cim:BaseVoltage");
					Element baseVoltageEl = null;
					// It loops through all the nodes and selects the one with the corresponding ID, i.e. BVID
					for(int t=0; t<baseVoltagelList.getLength(); t++){
						Node baseVoltagelnode = baseVoltagelList.item(t);
						Element newbaseVoltagel = (Element) baseVoltagelnode;
						boolean ver = newbaseVoltagel.getAttribute("rdf:ID").equals(BVID);
						if(ver == true){
							baseVoltageEl =  newbaseVoltagel;
						}
					}			
					// it gets the base voltage and prints it
					String BV = baseVoltageEl.getElementsByTagName("cim:BaseVoltage.nominalVoltage").item(0).getTextContent();
					System.out.println(attributes[l] + " = " + BV + "\n");
				}
			}
		}		
	}
}


