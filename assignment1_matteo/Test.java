package assignment1_matteo;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
//		String eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		String ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
//		String eq_path  = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
//		String ssh_path = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";
		String eq_path  = "C:/Users/Matteo/Documents/Kic InnoEnergy/KTH/Computer application/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
	    String ssh_path = "C:/Users/Matteo/Documents/Kic InnoEnergy/KTH/Computer application/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";
		
		String object = "cim:EnergyConsumer";
		String[] load_data = {"cim:IdentifiedObject.name","cim:EnergyConsumer.p","cim:EnergyConsumer.q","cim:Equipment.EquipmentContainer","baseVoltage"};
		
//		String object = "cim:SynchronousMachine";
//		String[] load_data = {"cim:IdentifiedObject.name","cim:RotatingMachine.ratedS","cim:RotatingMachine.p","cim:RotatingMachine.q","cim:RotatingMachine.GeneratingUnit",
//				"cim:RegulatingCondEq.RegulatingControl", "cim:Equipment.EquipmentContainer"};
		
		//MyParser parser = new MyParser();
		MyParser parser = new MyParser();
//		ArrayList<MyObject> objects = parser.parseXML(eq_path, ssh_path, object, load_data);
//		
//		for(int k=0;k<objects.size();k++){
//			objects.get(k).printOut(k+1);
//		}
		
		String[] CN_Name = {"cim:IdentifiedObject.name"};
		ArrayList<MyObject> ConnectivityNodesList = parser.parseXML(eq_path, ssh_path, "cim:ConnectivityNode", CN_Name );
		String[] T_Name = {"cim:Terminal.ConnectivityNode","cim:Terminal.ConductingEquipment"};
		ArrayList<MyObject> TotTerminalsList = parser.parseXML(eq_path, ssh_path, "cim:Terminal", T_Name );
		
		ArrayList<MyNode> VirtualNodesList = new ArrayList<MyNode>();
		
		
		for(int i=0; i<ConnectivityNodesList.size(); i++){
			MyObject ConnectivityNode = ConnectivityNodesList.get(i);
			ArrayList<MyObject> TerminalsList = new ArrayList<MyObject>();
			
			for(int k=0; k<TotTerminalsList.size(); k++){
				MyObject Terminal = TotTerminalsList.get(k);
//				System.out.println(ConnectivityNode.object_id + "equals?" + Terminal.data_attributes[0].substring(1));	
				if(ConnectivityNode.object_id.equals(Terminal.data_attributes[0].substring(1))){
//					System.out.println(ConnectivityNode.object_id + "equals?" + Terminal.data_attributes[0].substring(1));
					TerminalsList.add(Terminal);
//					System.out.println("I'm using this");
				}
			}
			MyNode NewNode = new MyNode(ConnectivityNode,TerminalsList);
			NewNode.printOut();
			VirtualNodesList.add(NewNode);			
		}
		
		
		
		
//		
//		for(int k=0;k<ConnectivityNodesList.size();k++){
//			ConnectivityNodesList.get(k).printOut(k+1);
//		}
//		for(int k=0;k<TerminalsList.size();k++){
//			TerminalsList.get(k).printOut(k+1);
//		}
		
	}
}
