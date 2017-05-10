package assignment1;

import java.util.ArrayList;

//### SIMPLE Object Container Class - for Abstracting Connected Nodes
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
		System.out.println("##### Included Nodes:");
		for(int k=0; k<includedConnNodes.size(); k++){
			System.out.println(includedConnNodes.get(k).object_id);
		}
		System.out.println("\n##### Included Terminals:");
		for(int k=0; k<includedTerminals.size(); k++){
			System.out.println(includedTerminals.get(k).object_id);
		}
		System.out.println("-------------------------------------");
	}
	
}