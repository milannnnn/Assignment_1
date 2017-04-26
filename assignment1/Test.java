package assignment1;

import java.util.ArrayList;
import org.w3c.dom.Document;

public class Test {

	public static void main(String[] args) {
//		String eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		String ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
		String eq_path  = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
		String ssh_path = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";
		
		
		String object = "cim:EnergyConsumer";
		String[] load_data = {"cim:IdentifiedObject.name","cim:EnergyConsumer.p","cim:EnergyConsumer.q","cim:Equipment.EquipmentContainer","baseVoltage"};
		
//		String object = "cim:SynchronousMachine";
//		String[] load_data = {"cim:IdentifiedObject.name","cim:RotatingMachine.ratedS","cim:RotatingMachine.p","cim:RotatingMachine.q","cim:RotatingMachine.GeneratingUnit",
//				"cim:RegulatingCondEq.RegulatingControl", "cim:Equipment.EquipmentContainer","baseVoltage"};
		
		MyParser parser = new MyParser();
		
		Document doc_eq  = parser.readFile( eq_path);
		Document doc_ssh = parser.readFile(ssh_path);
		
		ArrayList<MyObject> objects = parser.parseXML(doc_eq, doc_ssh, object, load_data);
		
		for(int k=0;k<objects.size();k++){
			objects.get(k).printOut(k+1);
		}
	}
}
