package assignment1_milan;

import java.util.ArrayList;
import org.w3c.dom.Document;

// TODO REMOVE all STATIC from Methods !#!#!#!#!#!#!#!#!#!#!#!#!
// TODO All Busbar Sections Have EXACTLY ONE TERMINAL - Check This!!!


// TODO Add All Connectivity Nodes as Virtual Busses
//      then make a method that checks if 2 Busses are connected (if so merge them and kill the later)
//      then run it through a double for loop
// TODO Modify checkForElectricConn so that it returns the positions of connected nodes
// 		(then in the main code u use that to add and remove those nodes and its terminals)


public class GenAdmMatrix {
	
	//public void initializeBuses(){
	public static void main(String[] args) {
		
		String eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
		String ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
//		String eq_path  = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
//		String ssh_path = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";
		
		MyParser parser = new MyParser();
		String object;
		String[] data_fields;
		
		Document doc_eq  = parser.readFile( eq_path);
		Document doc_ssh = parser.readFile(ssh_path);

		//###### 1) Extract Connectivity Nodes:
		object = "cim:ConnectivityNode";
		data_fields = new String[0];
		ArrayList<MyObject> connectivityNodes = parser.parseXML(doc_eq, doc_ssh, object, data_fields);

//		for(int k=0;k<connectivityNodes.size();k++){
//			connectivityNodes.get(k).printOut(k+1);
//		}		

		
		//###### 2) Extract Terminals:
		object = "cim:Terminal";
		data_fields = new String[] {"cim:Terminal.ConnectivityNode","cim:Terminal.ConductingEquipment", "cim:ACDCTerminal.connected"};
		ArrayList<MyObject> terminals = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		// Remove all terminals that are not connected
		for(int k=0;k<terminals.size();k++){
			if(!terminals.get(k).extractDataFiled("cim:ACDCTerminal.connected").equals("true")){
				terminals.remove(k--);
			}
		}
		
//		for(int k=0;k<terminals.size();k++){
//			terminals.get(k).printOut(k+1);
//		}		
		
		//###### 3) Extract Busbar Sections:
		//THIS ONE WE PROBABLY DON'T NEED!!!
		object = "cim:BusbarSection";
		data_fields = new String[0];
		ArrayList<MyObject> busbarSections = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		
		//###### 4) Extract Breakers (just closed ones):
		object = "cim:Breaker";
		data_fields = new String[] {"cim:Switch.open"};
		ArrayList<MyObject> breakers = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		// Remove all breakers that are open
		for(int k=0;k<breakers.size();k++){
			if(!breakers.get(k).extractDataFiled("cim:Switch.open").equals("false")){
				breakers.remove(k--);
			}
		}
		
		ArrayList<VirtualBus> virtualBuses = new ArrayList<VirtualBus>();
		
		//###### Initialize each Connectivity Node as a separate Virtual Bus:
		for(int k=0; k<connectivityNodes.size();k++){
			VirtualBus tmpBus = new VirtualBus();
			
			MyObject tmpNode = connectivityNodes.get(k);
			ArrayList<MyObject> tmpTerminals = getConnectedTerminals(tmpNode,terminals);
			
			tmpBus.addOneNode(tmpNode);
			tmpBus.addMultTerminals(tmpTerminals);
			
			virtualBuses.add(tmpBus);
		}
		
		// Check if the Virtual Buses are Connected, and if so, Merge them
		for(int k=0; k<virtualBuses.size(); k++){
			for(int j=k+1; j<virtualBuses.size(); j++){
				if(checkForElecticalConnection(virtualBuses.get(k), virtualBuses.get(j), busbarSections, breakers)){
					virtualBuses.get(k).addMultNodes(virtualBuses.get(j).includedConnNodes);
					virtualBuses.get(k).addMultTerminals(virtualBuses.get(j).includedTerminals);
					virtualBuses.remove(j--);
				}
			}
		}
		
		for(int k=0; k<virtualBuses.size(); k++){
			virtualBuses.get(k).printOut();
		}
		System.out.println("\n\nTotal Size: " + virtualBuses.size());
	}
	
	
	// ###################################################################################################################### 
	// Checks if the given terminal is connected to a busbar / breaker, and if so returns the list of all connected terminals
	public static boolean checkForElecticalConnection(VirtualBus firstBus, VirtualBus secondBus, ArrayList<MyObject> busbarSections, ArrayList<MyObject> breakers){
		
		for(int j=0; j<firstBus.includedTerminals.size();j++){
			String condEqId = firstBus.includedTerminals.get(j).extractDataFiled("cim:Terminal.ConductingEquipment").substring(1);
			boolean isBusbarOrBreaker = false;
			
			// Check if the given terminal is connected to busbar (probably not necessary, but just in case)
			for(int k=0; k<busbarSections.size(); k++){
				if(busbarSections.get(k).object_id.equals(condEqId)){
					isBusbarOrBreaker = true;
					break;
				}
			}
			
			// Check if the given terminal is connected to breaker
			for(int k=0; k<breakers.size(); k++){
				if(breakers.get(k).object_id.equals(condEqId) || isBusbarOrBreaker){
					isBusbarOrBreaker = true;
					break;
				}
			}
			
			// If it is, check if it is contained in Second Bus, and if so, return TRUE
			if(isBusbarOrBreaker){
				for(int k=0; k<secondBus.includedTerminals.size(); k++){
					if(secondBus.includedTerminals.get(k).extractDataFiled("cim:Terminal.ConductingEquipment").substring(1).equals(condEqId)){
						return true;
					}
				}
			}	
		}
		
		// Else (if they are not connected with breaker or busbar, return FALSE)
		return false;
	}
	
	
	// ########################################################### 
	// Returns all Terminals connected to given Connectivity Node:
	public static ArrayList<MyObject> getConnectedTerminals(MyObject connNode, ArrayList<MyObject> terminals){
		
		ArrayList<MyObject> connectedTerminals = new ArrayList<MyObject>();
		String nodeId = connNode.object_id;
		
		for(int k=0; k<terminals.size(); k++){
			if(terminals.get(k).extractDataFiled("cim:Terminal.ConnectivityNode").substring(1).equals(nodeId)){
				connectedTerminals.add(terminals.get(k));
			}
		}
		
		return connectedTerminals;
	}
			

}

























//// ###################################################################################################################### 
//// Checks if the given terminal is connected to a busbar / breaker, and if so returns the list of all connected terminals
//public static ArrayList<MyObject> oldCheckForElecticalConnection(MyObject terminal, ArrayList<MyObject> busbarSections, ArrayList<MyObject> breakers, ArrayList<MyObject> terminals){
//	
//	ArrayList<MyObject> connectedTerminals = new ArrayList<MyObject>();
//	String condEqId = terminal.extractDataFiled("cim:Terminal.ConductingEquipment").substring(1);
//	boolean idFound = false;
//	
//	// Check if it's connected to a busbar
//	for(int k=0; k<busbarSections.size(); k++){
//		if(busbarSections.get(k).object_id.equals(condEqId)){
//			idFound = true;
//			break;
//		}
//	}
//	// Check if it's connected to a breaker
//	for(int k=0; k<breakers.size(); k++){
//		if(breakers.get(k).object_id.equals(condEqId) || idFound){
//			idFound = true;
//			break;
//		}
//	}
//	// If it's connected to a busbar/breaker find all connected terminals and return them
//	if(idFound){
//		for(int k=0; k<terminals.size(); k++){
//			if(terminals.get(k).extractDataFiled("cim:Terminal.ConductingEquipment").substring(1).equals(condEqId)){
//				connectedTerminals.add(terminals.get(k));
//			}
//		}
//	}
//	// If it's not connected to a busbar/breaker just return the initial terminal
//	else{
//		connectedTerminals.add(terminal);
//	}
//	return connectedTerminals;		
//}
