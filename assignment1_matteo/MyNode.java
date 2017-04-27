package assignment1_matteo;

import java.util.ArrayList;

public class MyNode {
	private MyObject ConnectivityNode;
	private ArrayList<MyObject> TerminalsList;
	
	public MyNode(){};
	
	public MyNode(MyObject ConnectivityNode, ArrayList<MyObject> TerminalsList){
		this.ConnectivityNode = ConnectivityNode;
		this.TerminalsList = TerminalsList;
		
	}
	
	public void printOut(){
		String space = "------------------------------------------------------";
		System.out.println(space);
		System.out.println(ConnectivityNode.data_attributes[0] + " ID: " + ConnectivityNode.object_id);
		System.out.println(space);
		for (int n=0; n<TerminalsList.size(); n++){
			System.out.println(n+1 + ") Terminal's ID: " + TerminalsList.get(n).object_id);
			System.out.println("Conducting equipment's ID: " + TerminalsList.get(n).data_attributes[1]);
			System.out.println("Connectivity node ID: " + TerminalsList.get(n).data_attributes[0]);
		}
		System.out.println(space);
		System.out.println("\n");
	}

}
