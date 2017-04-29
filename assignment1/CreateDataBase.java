package assignment1;

import java.util.ArrayList;

import org.w3c.dom.Document;

//Milan remember to change file path for you and also password for SQL. You just need to uncomment line 35-36-173

public class CreateDataBase {
	
	// declare the used variables
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
//	private ArrayList<String[]> forKeyTabNameList;
	
	// ############################################################################################################
	// default constructor
	public CreateDataBase(){
		
//		eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
		
		eq_path  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
		ssh_path = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
//		eq_path  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\Total_MG_T1_EQ_V2.xml";
//		ssh_path = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\Total_MG_T1_SSH_V2.xml";
		
		// Object List: list of CIM objects to store in database
		objectList = new String[]{"cim:BaseVoltage","cim:Substation","cim:VoltageLevel","cim:GeneratingUnit","cim:RegulatingControl","cim:SynchronousMachine","cim:PowerTransformer", "cim:EnergyConsumer","cim:PowerTransformerEnd","cim:Breaker","cim:RatioTapChanger"};
		
		// Table Names List: list of names to be given to each table
		TableNames = new String[]{"BaseVoltage","Substation","VoltageLevel","GeneratingUnit","RegulatingControl","SynchronousMachine",
				"PowerTransformer", "EnergyConsumer","PowerTransformerEnd","Breaker","RatioTapChanger"};
			
		
		// Attributes List for every CIM object
		BaseVoltageAttribute = new String[] {"cim:BaseVoltage.nominalVoltage"};
		SubstationAttribute = new String[]{"cim:IdentifiedObject.name","cim:Substation.Region"};
		VolatgeLevelAttribute = new String[]{"cim:IdentifiedObject.name","cim:VoltageLevel.Substation","cim:VoltageLevel.BaseVoltage"};
		GeneratingUnitAttribute = new String[]{"cim:IdentifiedObject.name","cim:GeneratingUnit.maxOperatingP","cim:GeneratingUnit.minOperatingP","cim:Equipment.EquipmentContainer"};
		RegulatingControlAttribute = new String[]{"cim:IdentifiedObject.name","cim:RegulatingControl.targetValue"};
		SynchronousMachineAttribute = new String[]{"cim:IdentifiedObject.name","cim:RotatingMachine.ratedS","cim:RotatingMachine.p","cim:RotatingMachine.q","cim:RotatingMachine.GeneratingUnit","cim:RegulatingCondEq.RegulatingControl", "cim:Equipment.EquipmentContainer","baseVoltage"};
		PowerTransformerAttribute = new String[]{"cim:IdentifiedObject.name","cim:Equipment.EquipmentContainer"};
		EnergyConsumerAttribute = new String[]{"cim:IdentifiedObject.name","cim:EnergyConsumer.p","cim:EnergyConsumer.q","cim:Equipment.EquipmentContainer","baseVoltage"};
		PowerTransformerEndAttribute = new String[]{"cim:IdentifiedObject.name","cim:PowerTransformerEnd.r","cim:PowerTransformerEnd.x","cim:PowerTransformerEnd.PowerTransformer","cim:TransformerEnd.BaseVoltage"};
		BreakerAttribute = new String[]{"cim:IdentifiedObject.name","cim:Switch.open","cim:Equipment.EquipmentContainer","baseVoltage"};
		RatioTapChangerAttribute = new String[]{"cim:IdentifiedObject.name","cim:TapChanger.step"};
				
		// Create an array list with all the attributes for each object
		AttList = new ArrayList<String[]>();
		AttList.add(BaseVoltageAttribute);
		AttList.add(SubstationAttribute);
		AttList.add(VolatgeLevelAttribute);
		AttList.add(GeneratingUnitAttribute);
		AttList.add(RegulatingControlAttribute);
		AttList.add(SynchronousMachineAttribute);
		AttList.add(PowerTransformerAttribute);
		AttList.add(EnergyConsumerAttribute);
		AttList.add(PowerTransformerEndAttribute);
		AttList.add(BreakerAttribute);
		AttList.add(RatioTapChangerAttribute);
		
		// Column Names List: list of names we want to give to each column
		String[] BaseVoltageColName = new String[] {"ID","nominalVoltage"};
		String[] SubstationColName = new String[]{"ID","IdentifiedObject_name","Substation_Region"};
		String[] VolatgeLevelColName = new String[]{"ID","IdentifiedObject_name","VoltageLevel_Substation","VoltageLevel_BaseVoltage"};
		String[] GeneratingUnitColName = new String[]{"ID","IdentifiedObject_name","GeneratingUnit_maxOperatingP","GeneratingUnit_minOperatingP","Equipment_EquipmentContainer"};
		String[] RegulatingControlColName = new String[]{"ID","IdentifiedObject_name","RegulatingControl_targetValue"};
		String[] SynchronousMachineColName = new String[]{"ID","IdentifiedObject_name","RotatingMachine_ratedS","RotatingMachine_p","RotatingMachine_q","RotatingMachine_GeneratingUnit","RegulatingCondEq_RegulatingControl", "Equipment_EquipmentContainer","baseVoltage"};
		String[] PowerTransformerColName = new String[]{"ID","IdentifiedObject_name","Equipment_EquipmentContainer"};
		String[] EnergyConsumerColName = new String[]{"ID","IdentifiedObject_name","EnergyConsumer_p","EnergyConsumer_q","Equipment_EquipmentContainer","baseVoltage"};
		String[] PowerTransformerEndColName = new String[]{"ID","IdentifiedObject_name","PowerTransformerEnd_r","PowerTransformerEnd_x","PowerTransformerEnd_PowerTransformer","TransformerEnd_BaseVoltage"};
		String[] BreakerColName = new String[]{"ID","IdentifiedObject_name","Switch_open","Equipment_EquipmentContainer","baseVoltage"};
		String[] RatioTapChangerColName = new String[]{"ID","IdentifiedObject_name","TapChanger_step"};
		// Array list which contains all arrays defined above
		ColumNamesList = new ArrayList<String[]>();
		ColumNamesList.add(BaseVoltageColName);
		ColumNamesList.add(SubstationColName);
		ColumNamesList.add(VolatgeLevelColName);
		ColumNamesList.add(GeneratingUnitColName);
		ColumNamesList.add(RegulatingControlColName);
		ColumNamesList.add(SynchronousMachineColName);
		ColumNamesList.add(PowerTransformerColName);
		ColumNamesList.add(EnergyConsumerColName);
		ColumNamesList.add(PowerTransformerEndColName);
		ColumNamesList.add(BreakerColName);
		ColumNamesList.add(RatioTapChangerColName);
		
//		// foreign key table  names
//		String[] BaseVoltageforKeyTabName = new String[0];
//		String[] SubstationforKeyTabName = new String[0];
//		String[] VolatgeLevelforKeyTabName = new String[]{"Substation","BaseVoltage"};
//		String[] GeneratingUnitforKeyTabName = new String[]{"Substation"};
//		String[] RegulatingControlforKeyTabName = new String[0];
//		String[] SynchronousMachineforKeyTabName = new String[]{"GeneratingUnit","RegulatingControl", "VoltageLevel","BaseVoltage"};
//		String[] PowerTransformerforKeyTabName = new String[]{"Substation"};
//		String[] EnergyConsumerforKeyTabName = new String[]{"VoltageLevel","BaseVoltage"};
//		String[] PowerTransformerEndforKeyTabName = new String[]{"PowerTransformer","BaseVoltage"};
//		String[] BreakerforKeyTabName = new String[]{"VoltageLevel","BaseVoltage"};
//		String[] RatioTapChangerforKeyTabName = new String[0];
//		forKeyTabNameList = new ArrayList<String[]>();
//		forKeyTabNameList.add(BaseVoltageforKeyTabName);
//		forKeyTabNameList.add(SubstationforKeyTabName);
//		forKeyTabNameList.add(VolatgeLevelforKeyTabName);
//		forKeyTabNameList.add(GeneratingUnitforKeyTabName);
//		forKeyTabNameList.add(RegulatingControlforKeyTabName);
//		forKeyTabNameList.add(SynchronousMachineforKeyTabName);
//		forKeyTabNameList.add(PowerTransformerforKeyTabName);
//		forKeyTabNameList.add(EnergyConsumerforKeyTabName);
//		forKeyTabNameList.add( PowerTransformerEndforKeyTabName);
//		forKeyTabNameList.add(BreakerforKeyTabName);
//		forKeyTabNameList.add(RatioTapChangerforKeyTabName);
	}
	
	// ############################################################################################################
	// constructor to customize all the variables declared above
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
	
	// ############################################################################################################
	// create the requested database starting from the declared variables
	// INPUTS:
		// String eq_path 
		// String ssh_path 
		// String[] objectList 
		// ArrayList<String[]> AttList 
		// String[] TableNames 
		// ArrayList<String[]> ColumNamesList
	
	private void CreateDB(String eq_path, String ssh_path, String[] objectList, ArrayList<String[]> AttList, String[] TableNames, ArrayList<String[]> ColumNamesList){
		// create SQLprinter object, which in turn, creates and initializes the DB
//		SQLprinter newSQLPrinter = new SQLprinter();
		SQLprinter newSQLPrinter = new SQLprinter("root","Callandor14");
		
		// for each object of objectList, it extracts the name of the table (TableName), the name of the object (ObjectName), the name of the attributes to parse (Attributes),
		// the name to give to each column (ColumNames)
		for(int i=0; i<objectList.length;i++){
			String TableName = TableNames[i];
			String ObjectName = objectList[i];
			String[] Attributes = AttList.get(i);
			String[] ColumNames = ColumNamesList.get(i);
//			String[] forKeyTabName = forKeyTabNameList.get(i);
			
			// call the method CreateOneTable, to create one table using the extrcted info
			CreateOneTable(eq_path, ssh_path,  ObjectName, Attributes, TableName, ColumNames, newSQLPrinter);
		}
		// close connection with database
		newSQLPrinter.exit();
		System.out.println("DATABASE CREATED SUCCESSFULLY =)");
	}
	// ############################################################################################################
	// it does the same as CreateDB, but using default data
	public void CreateDBDefault(){
		CreateDB(eq_path, ssh_path, objectList, AttList, TableNames, ColumNamesList);
	}
	
	
	// ############################################################################################################
	// method to create one table
	// it creates the requested table and adds the parsed data into it, defining primary and foreign keys
	public void CreateOneTable(String eq_path, String ssh_path, String ObjectName, String[] Attributes, String TableName, String[] ColumNames, SQLprinter newSQLPrinter){
		// create a MyParser object
		MyParser parser = new MyParser();
		// check whether the number of attributes we want to parse is consistent with the number of columns' name
		if(Attributes.length+1 !=  ColumNames.length){
			System.out.println("Attributes number doesn't match the number of columns");
			// if it does not, kill the program
			parser.terminateProgram();
		}
		else{
			
			// ignore the relation between substation and region since we don't create a table for region
			// don't add foreign key constrain
			if(ObjectName.equals("cim:Substation")){
				Document doc_eq  = parser.readFile( eq_path);
				Document doc_ssh = parser.readFile(ssh_path);
				
				// parse the XML files and return an ArrayList of parsed objects
				ArrayList<MyObject> objects = parser.parseXML(doc_eq, doc_ssh, ObjectName, Attributes);
				
				// go through the list extract each object and add required data to DB
				for(int i=0; i<objects.size(); i++){
					MyObject Myobject = objects.get(i);
					// extract parsed data
					String [] ParsedValue = Myobject.ext_data;
					// create a new array value where the first element is the object ID and the others are the parsed values: ParsedValue
					String[] value = new String[ParsedValue.length+1];
					value[0]=Myobject.object_id;
					for(int k=1; k<ParsedValue.length+1;k++){
						value[k]=ParsedValue[k-1];
					}
					// it creates a table is it doesn't exist and it determines primary key
						// no foreign key is used, 
						// set the name of the table the foreign key refers to, to an empty array of Strings
						// set the position of the column where to add the foreign key, as an empty ArrayList
					newSQLPrinter.insertTable(TableName, ColumNames, new ArrayList<Integer>(), new String[0]);
					// insert parsed data in the table
					newSQLPrinter.insertData(TableName, value);
				}
			}
			
			// for objects different from substation
			else{
				Document doc_eq  = parser.readFile( eq_path);
				Document doc_ssh = parser.readFile(ssh_path);
				ArrayList<MyObject> objects = parser.parseXML(doc_eq, doc_ssh, ObjectName, Attributes);
				
				for(int i=0; i<objects.size(); i++){
					// initialize value used later on in a for loop
					int l=0;
					// pass is the value used to determine, the necessity of using a foreing key, default value is false
					boolean pass = false;
					// initialize an ArrayList of Integers containing the position of the column of the table where a foreign key has to be added
					ArrayList<Integer> forKeyPos = new ArrayList<Integer>();
					// initialize an ArrayList of Strings containing the name of the tables a certain foreign key is referred to
						// it represents the table where the foreign is a primary key
					ArrayList<String> forKeyTabName = new ArrayList<String>();
					// initialize an Array of strings the ArrayList (forKeyTabName) will be converted to
					String[] forKeyTabNameArray = new String[0];
					// extract each parsed object
					MyObject Myobject = objects.get(i);
					
					// array with parsed data from Myobject
					String [] ParsedValue = Myobject.ext_data;
					// create a new array value where the first element is the object ID and the others are the parsed values: ParsedValue
					String[] value = new String[ParsedValue.length+1];
					value[0]=Myobject.object_id;
					for(int k=1; k<ParsedValue.length+1;k++){
						value[k]=ParsedValue[k-1];
						// if the parsed value starts with the hash, it is a relation and, therefore,
						//  we store its position in the array to determine a foreign-key constrain 
						if(value[k].substring(0, 1).equals("#")){
							forKeyPos.add(k);
							// set pass equal to true since a foreign key has to be used
							pass = true;
						}
					}
					
					// extract the list of required attributes from Myobject, they are an array originally contained in AttList
					String [] reqData = Myobject.req_data;
					// if a foreign key has to be used, find the name of the table that foreign key refers to
					if(pass == true){
						// go through the required attributes 
						for(int g=0; g<reqData.length; g++){
							
							// find the required attribute at the position given by forKeyPos
								// forKeyPos.get(l)-1, minus 1 is used because forKeyPos takes into account the ID of the object which is not present in reqData
							if(g==(forKeyPos.get(l)-1)){
								// from the String reqData[g], take only the part after the '.', which represents the name of the table
								String reducedData = reduceString(reqData[g],'.');
								// there's no table called EquipmentContainer, since it points either to Substation or to VoltageLevel
								if(reducedData.equals("EquipmentContainer")){
									// if the potential table name is EquipmentContainer and the object is either GeneratingUnit or PowerTransformer, 
									// set the table name to Substation
									if(ObjectName.equals("cim:GeneratingUnit") || ObjectName.equals("cim:PowerTransformer")){
										reducedData = "Substation";
									}
									// otherwise set it to VoltageLevel
									else{
										reducedData = "VoltageLevel";
									}
								}
								// add the name of the table to the ArrayList forKeyTabName
								forKeyTabName.add(reducedData);
								l++;
							}
						}
						// convert the ArrayList forKeyTabName, to an Array of Strings forKeyTabNameArray
						forKeyTabNameArray = forKeyTabName.toArray(new String[forKeyTabName.size()]);
						
					}
					// if pass is false, set forKeyTabNameArray as an empty Array
					else{
						forKeyTabNameArray = new String[0];
					}

					// it creates a table is it doesn't exist and it determines primary and foreign keys
//					newSQLPrinter.insertTable(TableName, ColumNames, forKeyPos, forKeyTabName);
					newSQLPrinter.insertTable(TableName, ColumNames, forKeyPos, forKeyTabNameArray);
					// insert parsed data in the table
					newSQLPrinter.insertData(TableName, value);
				}
			}
		}
	}
	
	// ############################################################################################################
	// it reduces a String to a substring, starting from a separator till the end
	private String reduceString(String string, char separator){
		int index = string.indexOf(separator);
		String subString = string.substring(index+1, string.length());
		return subString;
	}
	
}
