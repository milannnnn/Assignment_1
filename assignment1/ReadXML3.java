package assignment1;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ReadXML3 {
	public static void main(String[] args){
		
		// Attributes List for every CIM object
		String[] BaseVoltageAttribute = {"cim:BaseVoltage.nominalVoltage"};
		String[] SubstationAttribute = {"cim:IdentifiedObject.name","cim:Substation.Region"};
		String[] VolatgeLevelAttribute = {"cim:IdentifiedObject.name","cim:VoltageLevel.Substation","cim:VoltageLevel.BaseVoltage"};
		String[] GeneratingUnitAttribute = {"cim:IdentifiedObject.name","cim:GeneratingUnit.maxOperatingP","cim:GeneratingUnit.minOperatingP","cim:Equipment.EquipmentContainer"};
		String[] SynchronousMachineAttribute = {"cim:IdentifiedObject.name","cim:RotatingMachine.ratedS","cim:RotatingMachine.p","cim:RotatingMachine.q","cim:RotatingMachine.GeneratingUnit",
				"cim:RegulatingCondEq.RegulatingControl", "cim:Equipment.EquipmentContainer"};
		String[] RegulatingControlAttribute = {"cim:IdentifiedObject.name","cim:RegulatingControl.targetValue"};
		String[] PowerTransformerAttribute = {"cim:IdentifiedObject.name","cim:Equipment.EquipmentContainer"};
		String[] EnergyConsumerAttribute = {"cim:IdentifiedObject.name","cim:EnergyConsumer.p","cim:EnergyConsumer.q","cim:Equipment.EquipmentContainer"};
		String[] PowerTransformerEndAttribute = {"cim:IdentifiedObject.name","cim:PowerTransformerEnd.r","cim:PowerTransformerEnd.x",
				"cim:PowerTransformerEnd.PowerTransformer","cim:TransformerEnd.BaseVoltage"};
		String[] BreakerAttribute = {"cim:IdentifiedObject.name","cim:Switch.open","cim:Equipment.EquipmentContainer"};
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
				NodeList newNodeList1 = doc_eq.getElementsByTagName(tagList[i]);
				NodeList newNodeList2 = doc_SSH.getElementsByTagName(tagList[i]);
				if(newNodeList2 == null || newNodeList2.getLength() == 0 ){
					newNodeList2 = newNodeList1;
				}
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
					Node node1 = newNodeList1.item(i); 
					Node node2 = newNodeList2.item(i);
					extractNode(node1,node2,k,AttList);
				}
			}
		}
		
		
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// Method to extract data and attributes from a node
	public static void extractNode (Node node1, Node node2, int k, ArrayList<String[]> AttList){
		// converts node to element 
		Element element1 = (Element) node1;
		Element element2 = (Element) node2;
		// gets the element ID and prints it
		String rfdID = element1.getAttribute("rdf:ID");
		System.out.println("rdfID: " + rfdID + "\n");
		// From the ArrayList "AttList", it extracts the array containing the data tags of a specific CIM object identified by k
		String[] attributes = AttList.get(k/2);
		
//		String name0 = node.getNodeName();
//		Node node1 = node.getLastChild();
//		String name1 = node1.getNodeName();
//		System.out.println(name0 + " last child's name is " + "\t" + name1 + "\n");
		
		for(int l = 0; l < attributes.length; l++){
			
			// It reads the value related to a specific tag "attributes[l]"
			
			
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
					Element nodelistIDnode = (Element) nodelistID;
					// it read and prints the value of that attribute
					String resourceID = nodelistIDnode.getAttribute("rdf:resource");
	//				System.out.println("I'm using this");
					System.out.println(attributes[l] + " rdf:resource=" + "\t" + resourceID + "\n");	
				}			
			}
			catch(java.lang.NullPointerException e){
				String nameSSH = element2.getElementsByTagName(attributes[l]).item(0).getTextContent();
				
				if(!nameSSH.isEmpty()){
					// if the data is not empty it prints it out
					System.out.println(attributes[l] + "\t" + nameSSH + "\n");
				}
				else{
					// if the data is empty it means that child has no data, just an attribute, so it reads the attribute
					// it extracts the node identified by that tag "attributes[l]"
					Node nodelistID2 = element2.getElementsByTagName(attributes[l]).item(0);
					Element nodelistIDnode2 = (Element) nodelistID2;
					// it read and prints the value of that attribute
					String resourceID2 = nodelistIDnode2.getAttribute("rdf:resource");
	//				System.out.println("I'm using this");
					System.out.println(attributes[l] + " rdf:resource=" + "\t" + resourceID2 + "\n");	
				}		
				
			}
		}		
	}
}


