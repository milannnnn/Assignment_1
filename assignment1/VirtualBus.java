package assignment1;

import java.util.ArrayList;

public class VirtualBus {
	
	public ArrayList<MyObject> includedConnNodes;
	public ArrayList<MyObject> includedTerminals;
	
	public VirtualBus(){
		includedConnNodes = new ArrayList<MyObject>();
		includedTerminals = new ArrayList<MyObject>();
	}
	
	//### To push one node to the virtual bus
	public void addOneNode(MyObject connNode){
		includedConnNodes.add(connNode);
	}
	//### To push MULTIPLE nodes to the virtual bus
	public void addMultNodes(ArrayList<MyObject> nodes){
		for(int k=0; k<nodes.size(); k++){
			includedConnNodes.add(nodes.get(k));
		}
	}	
	//### To push ONE terminal to the virtual bus
	public void addOneTerminal(MyObject terminal){
		includedTerminals.add(terminal);
	}
	//### To push MULTIPLE terminals to the virtual bus
	public void addMultTerminals(ArrayList<MyObject> terminals){
		for(int k=0; k<terminals.size(); k++){
			includedTerminals.add(terminals.get(k));
		}
	}
	//### To remove ONE terminal from the virtual bus
	public void killOneTerminal(MyObject terminal){
		String id = terminal.object_id;
		for(int k=0; k<includedTerminals.size(); k++){
			if(includedTerminals.get(k).object_id.equals(id)){
				includedTerminals.remove(k);
				break;
			}
		}
	}
	//### To remove MULTIPLE Terminals from the virtual bus
	public void killMultTerminals(ArrayList<MyObject> terminals){
		for(int j=0; j<terminals.size(); j++){		
			String id = terminals.get(j).object_id;
			for(int k=0; k<includedTerminals.size(); k++){
				if(includedTerminals.get(k).object_id.equals(id)){
					includedTerminals.remove(k);
					break;
				}
			}
		}
	}	
	

	//### Print Included Nodes
	public void printOut(){
		System.out.println("-------------------------------------");
		System.out.println("##### Included Nodes:");
		for(int k=0; k<includedConnNodes.size(); k++){
			System.out.println(includedConnNodes.get(k).object_id);
		}
		System.out.println("\n##### Included Terminals:");
		for(int k=0; k<includedTerminals.size(); k++){
			System.out.println(includedTerminals.get(k).object_id);
		}
	}
	
}

















////####### Checked Addition (NOT NEEDED ANYMORE) #######
////### To check if the node is already included
//public boolean checkIfNodeNotIncl(MyObject node){
//	boolean notFoundFlag = true;
//	for(int k=0; k<includedConnNodes.size();k++){
//		if(includedConnNodes.get(k).object_id.equals(node.object_id)){
//			notFoundFlag = false;
//			break;
//		}
//	}
//	return notFoundFlag;
//}
////### To check if the terminal is already included
//public boolean checkIfTerminalNotIncl(MyObject terminal){
//	boolean notFoundFlag = true;
//	for(int k=0; k<includedTerminals.size();k++){
//		if(includedTerminals.get(k).object_id.equals(terminal.object_id)){
//			notFoundFlag = false;
//			break;
//		}
//	}
//	return notFoundFlag;
//}
////### First if Check if included (by ID) then Push ONE Node
//public void checkAndAddOneNode(MyObject connNode){
//	if(checkIfNodeNotIncl(connNode)){
//		includedConnNodes.add(connNode);
//	}
//}
////### First if Check if included (by ID) then Push MULTIPLE Nodes
//public void checkAndAddMultNodes(ArrayList<MyObject> nodes){
//	for(int k=0; k<nodes.size(); k++){
//		if(checkIfNodeNotIncl(nodes.get(k))){
//			includedConnNodes.add(nodes.get(k));
//		}
//	}
//}	
////### First if Check if included (by ID) then Push ONE Terminal
//public void checkAndAddOneTerminal(MyObject terminal){
//	if(checkIfTerminalNotIncl(terminal)){
//		includedTerminals.add(terminal);
//	}
//}
////### First if Check if included (by ID) then Push MULTIPLE Terminals
//public void checkAndAddMultTerminals(ArrayList<MyObject> terminals){
//	for(int k=0; k<terminals.size(); k++){
//		if(checkIfTerminalNotIncl(terminals.get(k))){
//			includedTerminals.add(terminals.get(k));
//		}
//	}
//}

