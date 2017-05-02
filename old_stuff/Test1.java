package assignment1;

import java.util.ArrayList;

import org.w3c.dom.Document;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SQLprinter sqlprinter = new SQLprinter("root","Callandor14");
//		String[] att = {"cane","figa"};
//		sqlprinter.insertTable("Matteo", att);
//		String[] data = {"22","1000000"};
//		sqlprinter.insertData("Matteo", data);
//		String[] data1 = {"23","55"};
//		sqlprinter.insertData("Matteo", data1);
//		String[] att1 = {"figa"};
//		String[] data2 = {"1","2"};
//		sqlprinter.upDate("Matteo", att, data2, "cane", "22");
//		sqlprinter.exit();
		System.out.println("Helloooooo");
		MyParser parser = new MyParser();
		String eq_path  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
		String ssh_path = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		Document doc_eq  = parser.readFile( eq_path);
		Document doc_ssh = parser.readFile(ssh_path);
		String tempObjectName = "cim:ThermalGeneratingUnit";
		String[] GeneratingUnitAttribute = new String[]{"cim:IdentifiedObject.name","cim:GeneratingUnit.maxOperatingP","cim:GeneratingUnit.minOperatingP","cim:Equipment.EquipmentContainer"};
		ArrayList<MyObject> tempObjects = parser.parseXML(doc_eq, doc_ssh, tempObjectName, GeneratingUnitAttribute);
		System.out.println(tempObjects.size());
		
		for(int i=0; i<3 ;i++){
			System.out.println("Hello world");
			System.out.println(tempObjects.get(0));
		}
//		CreateDataBase DB = new CreateDataBase(eq_path,ssh_path);
//		CreateDataBase DB = new CreateDataBase();
//		DB.CreateDBDefault();

	}

}
