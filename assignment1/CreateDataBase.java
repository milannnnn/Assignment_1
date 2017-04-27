package assignment1;

import java.util.ArrayList;

import org.w3c.dom.Document;

//Milan remember to change file path for you and also password for SQL. You just need to uncomment line 31-31-152

public class CreateDataBase {
	private String eq_path;
	private String ssh_path;
	private String[] objectList;
	private String[] TableNames;
	private String[] BaseVoltageAttribute;
	private String[] SubstationAttribute;
	private String[] VolatgeLevelAttribute;
	private String[] GeneratingUnitAttribute;
	private String[] SynchronousMachineAttribute;
	private String[] RegulatingControlAttribute;
	private String[] PowerTransformerAttribute;
	private String[] PowerTransformerEndAttribute;
	private String[] EnergyConsumerAttribute;
	private String[] BreakerAttribute;
	private String[] RatioTapChangerAttribute;
	private ArrayList<String[]> AttList;
	private ArrayList<String[]> ColumNamesList;
	
	
	public CreateDataBase(){
		
//		eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
		
		eq_path  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
		ssh_path = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
		// Object List
		objectList = new String[]{"cim:BaseVoltage","cim:Substation","cim:VoltageLevel","cim:GeneratingUnit","cim:SynchronousMachine","cim:RegulatingControl","cim:PowerTransformer", "cim:EnergyConsumer","cim:PowerTransformerEnd","cim:Breaker","cim:RatioTapChanger"};
		
		// Table Names List
		TableNames = new String[]{"BaseVoltage","Substation","VoltageLevel","GeneratingUnit","SynchronousMachine",
				"RegulatingControl","PowerTransformer", "EnergyConsumer","PowerTransformerEnd","Breaker","RatioTapChanger"};
			
		
		// Attributes List for every CIM object
		BaseVoltageAttribute = new String[] {"cim:BaseVoltage.nominalVoltage"};
		SubstationAttribute = new String[]{"cim:IdentifiedObject.name","cim:Substation.Region"};
		VolatgeLevelAttribute = new String[]{"cim:IdentifiedObject.name","cim:VoltageLevel.Substation","cim:VoltageLevel.BaseVoltage"};
		GeneratingUnitAttribute = new String[]{"cim:IdentifiedObject.name","cim:GeneratingUnit.maxOperatingP","cim:GeneratingUnit.minOperatingP","cim:Equipment.EquipmentContainer"};
		SynchronousMachineAttribute = new String[]{"cim:IdentifiedObject.name","cim:RotatingMachine.ratedS","cim:RotatingMachine.p","cim:RotatingMachine.q","cim:RotatingMachine.GeneratingUnit","cim:RegulatingCondEq.RegulatingControl", "cim:Equipment.EquipmentContainer","baseVoltage"};
		RegulatingControlAttribute = new String[]{"cim:IdentifiedObject.name","cim:RegulatingControl.targetValue"};
		PowerTransformerAttribute = new String[]{"cim:IdentifiedObject.name","cim:Equipment.EquipmentContainer"};
		EnergyConsumerAttribute = new String[]{"cim:IdentifiedObject.name","cim:EnergyConsumer.p","cim:EnergyConsumer.q","cim:Equipment.EquipmentContainer","baseVoltage"};
		PowerTransformerEndAttribute = new String[]{"cim:IdentifiedObject.name","cim:PowerTransformerEnd.r","cim:PowerTransformerEnd.x","cim:PowerTransformerEnd.PowerTransformer","cim:TransformerEnd.BaseVoltage"};
		BreakerAttribute = new String[]{"cim:IdentifiedObject.name","cim:Switch.open","cim:Equipment.EquipmentContainer","baseVoltage"};
		RatioTapChangerAttribute = new String[]{"cim:IdentifiedObject.name","cim:TapChanger.step"};
				
		// Create an array list with all the attributes
		AttList = new ArrayList<String[]>();
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
		
		// Column Names List
		String[] BaseVoltageColName = new String[] {"rdf_ID","nominalVoltage"};
		String[] SubstationColName = new String[]{"ID","IdentifiedObject_name","Substation_Region"};
		String[] VolatgeLevelColName = new String[]{"ID","IdentifiedObject_name","VoltageLevel_Substation","VoltageLevel_BaseVoltage"};
		String[] GeneratingUnitColName = new String[]{"ID","IdentifiedObject_name","GeneratingUnit_maxOperatingP","GeneratingUnit_minOperatingP","Equipment_EquipmentContainer"};
		String[] SynchronousMachineColName = new String[]{"ID","IdentifiedObject_name","RotatingMachine_ratedS","RotatingMachine_p","RotatingMachine_q","RotatingMachine_GeneratingUnit","RegulatingCondEq_RegulatingControl", "Equipment_EquipmentContainer","baseVoltage"};
		String[] RegulatingControlColName = new String[]{"ID","IdentifiedObject_name","RegulatingControl_targetValue"};
		String[] PowerTransformerColName = new String[]{"ID","IdentifiedObject_name","Equipment_EquipmentContainer"};
		String[] EnergyConsumerColName = new String[]{"ID","IdentifiedObject_name","EnergyConsumer_p","EnergyConsumer_q","Equipment_EquipmentContainer","baseVoltage"};
		String[] PowerTransformerEndColName = new String[]{"ID","IdentifiedObject_name","PowerTransformerEnd_r","PowerTransformerEnd_x","PowerTransformerEnd_PowerTransformer","TransformerEnd_BaseVoltage"};
		String[] BreakerColName = new String[]{"ID","IdentifiedObject_name","Switch_open","Equipment_EquipmentContainer","baseVoltage"};
		String[] RatioTapChangerColName = new String[]{"ID","IdentifiedObject_name","TapChanger_step"};
		ColumNamesList = new ArrayList<String[]>();
		ColumNamesList.add(BaseVoltageColName);
		ColumNamesList.add(SubstationColName);
		ColumNamesList.add(VolatgeLevelColName);
		ColumNamesList.add(GeneratingUnitColName);
		ColumNamesList.add(SynchronousMachineColName);
		ColumNamesList.add(RegulatingControlColName);
		ColumNamesList.add(PowerTransformerColName);
		ColumNamesList.add(EnergyConsumerColName);
		ColumNamesList.add(PowerTransformerEndColName);
		ColumNamesList.add(BreakerColName);
		ColumNamesList.add(RatioTapChangerColName);
	}
	
	public CreateDataBase(String eq_path, String ssh_path, String[] objectList, String[] TableNames,
	String[] BaseVoltageAttribute, String[] SubstationAttribute, String[] VolatgeLevelAttribute,
	String[] GeneratingUnitAttribute, String[] SynchronousMachineAttribute, String[] RegulatingControlAttribute,
	String[] PowerTransformerAttribute,	 String[] PowerTransformerEndAttribute,	 String[] EnergyConsumerAttribute,
	String[] BreakerAttribute, 	String[] RatioTapChangerAttribute,	 ArrayList<String[]> AttList,
	ArrayList<String[]> ColumNamesList){
		
		this.eq_path = eq_path;
		this.ssh_path = ssh_path;
		this.objectList = objectList;
		this.TableNames = TableNames;
		this.BaseVoltageAttribute = BaseVoltageAttribute;
		this.SubstationAttribute = SubstationAttribute;
		this.VolatgeLevelAttribute = VolatgeLevelAttribute;
		this.GeneratingUnitAttribute = GeneratingUnitAttribute;
		this.SynchronousMachineAttribute = SynchronousMachineAttribute;
		this.RegulatingControlAttribute = RegulatingControlAttribute;
		this.PowerTransformerAttribute = PowerTransformerAttribute;
		this.PowerTransformerEndAttribute = PowerTransformerEndAttribute;
		this.EnergyConsumerAttribute = EnergyConsumerAttribute;
		this.BreakerAttribute = BreakerAttribute;
		this.RatioTapChangerAttribute = RatioTapChangerAttribute;
		this.AttList = AttList;
		this.ColumNamesList = ColumNamesList;
		
	}
	
	
	public void CreateDB(String eq_path, String ssh_path, String[] objectList, ArrayList<String[]> AttList, String[] TableNames, ArrayList<String[]> ColumNamesList){
		for(int i=0; i<objectList.length;i++){
			String TableName = TableNames[i];
			String ObjectName = objectList[i];
			String[] Attributes = AttList.get(i);
			String[] ColumNames = ColumNamesList.get(i);
			CreateOneTable(eq_path, ssh_path,  ObjectName, Attributes, TableName, ColumNames);
		}
		System.out.println("DATABASE CREATED SUCCESSFULLY =)");
	}
	
	public void CreateDBDefault(){
		CreateDB(eq_path, ssh_path, objectList, AttList, TableNames, ColumNamesList);
	}
	
	
	
	public void CreateOneTable(String eq_path, String ssh_path, String ObjectName, String[] Attributes, String TableName, String[] ColumNames){
		MyParser parser = new MyParser();
		if(Attributes.length+1 !=  ColumNames.length){
			System.out.println("Attributes number doesn't match the number of columns");
			parser.terminateProgram();
		}
		else{
			// SQLprinter newSQLPrinter = new SQLprinter();
			SQLprinter newSQLPrinter = new SQLprinter("root","Callandor14");
			newSQLPrinter.insertTable(TableName, ColumNames);
			
			Document doc_eq  = parser.readFile( eq_path);
			Document doc_ssh = parser.readFile(ssh_path);
			
			ArrayList<MyObject> objects = parser.parseXML(doc_eq, doc_ssh, ObjectName, Attributes);
			
			for(int i=0; i<objects.size(); i++){
				MyObject Myobject = objects.get(i);
				
				String [] ParsedValue = Myobject.ext_data;
				String[] value = new String[ParsedValue.length+1];
				value[0]=Myobject.object_id;
				for(int k=1; k<ParsedValue.length+1;k++){
					value[k]=ParsedValue[k-1];
				}
				newSQLPrinter.insertData(TableName, value);
			}
		}
		
	}

}
