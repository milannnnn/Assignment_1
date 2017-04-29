package assignment1_milan;

import java.util.ArrayList;

import org.w3c.dom.Document;

// TODO All Busbar Sections Have EXACTLY ONE TERMINAL - Check This (Remove from Electrical Connectivity Check)!!!

public class AdmittanceMatrix {
	
	public Complex[][] calculateAdmMatrix(String eq_path, String ssh_path, double basePower){
	//public void main(String[] args) {
		
//		double basePower = 1000; // in MW!!!!!!!!!!!!!!
//		
//		String eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		String ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
////		String eq_path  = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
////		String ssh_path = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";
		
		MyParser parser = new MyParser();
		String object;
		String[] data_fields;
		
		Document doc_eq  = parser.readFile( eq_path);
		Document doc_ssh = parser.readFile(ssh_path);

		//###### 1) Extract Connectivity Nodes:
		object = "cim:ConnectivityNode";
		data_fields = new String[0];
		ArrayList<MyObject> connectivityNodes = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		
		//###### 2) Extract Terminals:
		object = "cim:Terminal";
		data_fields = new String[] {"cim:Terminal.ConductingEquipment", "cim:Terminal.ConnectivityNode", "cim:ACDCTerminal.connected"};
		ArrayList<MyObject> terminals = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		// Remove all terminals that are not connected
		for(int k=0;k<terminals.size();k++){
			if(!terminals.get(k).extractDataFiled("cim:ACDCTerminal.connected").equals("true")){
				terminals.remove(k--);
			}
		}
		
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
		
		//###### 5) Extract AC Line Segments:
		object = "cim:ACLineSegment";
		data_fields = new String[] {"cim:ACLineSegment.r", "cim:ACLineSegment.x", "cim:ACLineSegment.bch", "cim:ACLineSegment.gch", "cim:ConductingEquipment.BaseVoltage"};
		ArrayList<MyObject> acLineSegments = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		
		//###### 6) Extract Power Transformer Ends:
		object = "cim:PowerTransformerEnd";
		data_fields = new String[] {"cim:PowerTransformerEnd.r", "cim:PowerTransformerEnd.x", "cim:PowerTransformerEnd.b", "cim:PowerTransformerEnd.g", "cim:TransformerEnd.BaseVoltage","cim:TransformerEnd.Terminal","cim:PowerTransformerEnd.PowerTransformer"};
		ArrayList<MyObject> powerTransformerEnds = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		// Fix the Transformer Terminals so the Conducting Eq. Tag Points to a Winding and not the Whole Transformer
		for(int k=0;k<powerTransformerEnds.size();k++){
			String terminalId = powerTransformerEnds.get(k).extractDataFiled("cim:TransformerEnd.Terminal").substring(1);
			for(int j=0; j<terminals.size(); j++){
				if(terminals.get(j).object_id.equals(terminalId)){
					terminals.get(j).changeDataField("cim:Terminal.ConductingEquipment", "#" + powerTransformerEnds.get(k).object_id);
					break;
				}
			}
		}
		
		//###### 7) Extract Base Voltages:
		object = "cim:BaseVoltage";
		data_fields = new String[] {"cim:BaseVoltage.nominalVoltage"};
		ArrayList<MyObject> baseVoltages = parser.parseXML(doc_eq, doc_ssh, object, data_fields);
		
		//###### Initialize each Connectivity Node as a separate Virtual Bus:
		ArrayList<VirtualBus> virtualBuses = new ArrayList<VirtualBus>();
		for(int k=0; k<connectivityNodes.size();k++){
			VirtualBus tmpBus = new VirtualBus();
			
			MyObject tmpNode = connectivityNodes.get(k);
			ArrayList<MyObject> tmpTerminals = getConnectedTerminals(tmpNode,terminals);
			
			tmpBus.addOneNode(tmpNode);
			tmpBus.addMultTerminals(tmpTerminals);
			
			virtualBuses.add(tmpBus);
		}
		
		//###### Check if the Virtual Buses are Connected, and if so, Merge them
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
		System.out.println("\n\nTotal Size: " + virtualBuses.size()+"\n\n");
		
		//###### Initialize the YBusMatrix to complex zeros!!!
		Complex[][] YBusMatrix = new Complex[virtualBuses.size()][virtualBuses.size()];
		for(int k=0;k<virtualBuses.size();k++){
			for(int j=0;j<virtualBuses.size();j++){
				YBusMatrix[k][j] = new Complex();
			}
		}
		
		//###### Add all the Lines to the Y Bus Matrix:
		for(int k=0; k<virtualBuses.size(); k++){
			for(int t=0; t<virtualBuses.get(k).includedTerminals.size(); t++){
				MyObject terminal = virtualBuses.get(k).includedTerminals.get(t);
				String objectId = isTerminalInsideObjects(terminal,acLineSegments);
				if(!objectId.equals("")){
					MyObject line = findObjectById(objectId, acLineSegments);
					for(int j=k+1; j<virtualBuses.size(); j++){
						if(isLineConnToBus(objectId,virtualBuses.get(j))){
							Complex[] tmpAdmittance = calcLineAdmittances(line,baseVoltages,basePower);
							
							// Add the Line  Admittances to the Y Bus
							YBusMatrix[k][j] = YBusMatrix[k][j].minus(tmpAdmittance[0]);
							YBusMatrix[j][k] = YBusMatrix[j][k].minus(tmpAdmittance[0]);
							YBusMatrix[k][k] = YBusMatrix[k][k].plus(tmpAdmittance[0]);
							YBusMatrix[j][j] = YBusMatrix[j][j].plus(tmpAdmittance[0]);
							
							// Add the Shunt Admittances to the Y Bus
							YBusMatrix[k][k] = YBusMatrix[k][k].plus(tmpAdmittance[1]);
							YBusMatrix[j][j] = YBusMatrix[j][j].plus(tmpAdmittance[1]);
						}
					}
				}
			}
		}
		
		//###### Add all the Transformer Windings to the Y Bus Matrix:
		for(int k=0; k<virtualBuses.size(); k++){
			for(int t=0; t<virtualBuses.get(k).includedTerminals.size(); t++){
				MyObject terminal = virtualBuses.get(k).includedTerminals.get(t);
				String windingId = isTerminalInsideObjects(terminal,powerTransformerEnds);
				if(!windingId.equals("")){
					MyObject winding1 = findObjectById(windingId, powerTransformerEnds);
					String trafoId = winding1.extractDataFiled("cim:PowerTransformerEnd.PowerTransformer").substring(1);
					for(int j=k+1; j<virtualBuses.size(); j++){

						// Check if the Given Transformer is Connected to the j-th Bus, and if so add the Admittance to the Bus
						String winding2Id = isTrafoConnToBus(trafoId, virtualBuses.get(j), powerTransformerEnds);
						if(!winding2Id.equals("")){
							MyObject winding2 = findObjectById(winding2Id, powerTransformerEnds);
							Complex[] tmpAdmittance = calcTrafoAdmittances(winding1, winding2, baseVoltages, basePower);
							
							// Add the Line  Admittances to the Y Bus
							YBusMatrix[k][j] = YBusMatrix[k][j].minus(tmpAdmittance[0]);
							YBusMatrix[j][k] = YBusMatrix[j][k].minus(tmpAdmittance[0]);
							YBusMatrix[k][k] = YBusMatrix[k][k].plus(tmpAdmittance[0]);
							YBusMatrix[j][j] = YBusMatrix[j][j].plus(tmpAdmittance[0]);
							
							// Add the Shunt Admittances to the Y Bus
							YBusMatrix[k][k] = YBusMatrix[k][k].plus(tmpAdmittance[1]);
							YBusMatrix[j][j] = YBusMatrix[j][j].plus(tmpAdmittance[1]);
							//System.out.println(tmpAdmittance[1]);
						}
					}
				}
			}
		}
		
		
		for(int k=0; k<virtualBuses.size(); k++){
			for(int j=0; j<virtualBuses.size(); j++){
				System.out.print(YBusMatrix[k][j] + "\t");
			}
			System.out.print("\n");
		}
		
		return YBusMatrix;
	}
	
	
	// #######################################################################################################################
	// Checks if the given terminal is connected to a busbar / breaker, and if so returns the list of all connected terminals:
	public boolean checkForElecticalConnection(VirtualBus firstBus, VirtualBus secondBus, ArrayList<MyObject> busbarSections, ArrayList<MyObject> breakers){
		
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
	public ArrayList<MyObject> getConnectedTerminals(MyObject connNode, ArrayList<MyObject> terminals){
		
		ArrayList<MyObject> connectedTerminals = new ArrayList<MyObject>();
		String nodeId = connNode.object_id;
		
		for(int k=0; k<terminals.size(); k++){
			if(terminals.get(k).extractDataFiled("cim:Terminal.ConnectivityNode").substring(1).equals(nodeId)){
				connectedTerminals.add(terminals.get(k));
			}
		}
		
		return connectedTerminals;
	}

	// ##################################################
	// Finds the Object by its ID, from a List of Objects
	public MyObject findObjectById(String objectId, ArrayList<MyObject> listOfObjects){
		
		for(int q=0;q<listOfObjects.size();q++){
			if(listOfObjects.get(q).object_id.equals(objectId)){
				return listOfObjects.get(q);
			}
		}
		return null;
	}

	// ######################################################################################################
	// Checks if Terminal is connected to given Objects and if so returns Object ID (otherwise it returns "")
	public String isTerminalInsideObjects(MyObject terminal, ArrayList<MyObject> listOfObjects){
		
		String condEqId = terminal.extractDataFiled("cim:Terminal.ConductingEquipment").substring(1);
		for(int k=0; k<listOfObjects.size(); k++){
			if(listOfObjects.get(k).object_id.equals(condEqId)){
				return condEqId;
			}
		}		
		return "";
	}

	// ###########################################################
	// Checks if certain Object is connected to given Virtual Bus:
	public boolean isLineConnToBus(String objectID, VirtualBus vBus){
		
		for(int k=0; k<vBus.includedTerminals.size();k++){
			if(vBus.includedTerminals.get(k).extractDataFiled("cim:Terminal.ConductingEquipment").substring(1).equals(objectID)){
				return true;
			}
		}
		
		return false;
	}

	// ###########################################################
	// Checks if certain Object is connected to given Virtual Bus:
	public String isTrafoConnToBus(String trafoId, VirtualBus vBus, ArrayList<MyObject> powerTransformerEnds){
		
		for(int k=0; k<vBus.includedTerminals.size(); k++){
			String windingId = isTerminalInsideObjects(vBus.includedTerminals.get(k), powerTransformerEnds);
			if(!windingId.equals("")){
				MyObject winding = findObjectById(windingId, powerTransformerEnds);
				if(winding.extractDataFiled("cim:PowerTransformerEnd.PowerTransformer").substring(1).equals(trafoId)){
					return windingId;
				}
			}
		}
		return "";
	}

	// ###############################################################################
	// Calculates the COMPLEX Line and Shunt Admittances of an AC Line Segment !!!!!!!
	public Complex[] calcLineAdmittances(MyObject line, ArrayList<MyObject> baseVoltages, double basePower){
		double r=0;	double x=0;
		double b=0;	double g=0;
		double baseVolt=0;
		if(!line.object_type.equals("cim:ACLineSegment")){
			System.out.println("Analyzed Object not an AC Line Segment - Cannot Calculate Admittance!!!");
			System.out.println("Please Check Object "+line.object_type+" \"" +line.object_id+ "\"" );
			terminateProgram();
		}
		baseVolt = getBaseVoltValue(line.extractDataFiled("cim:ConductingEquipment.BaseVoltage"),baseVoltages);
		double baseImp = baseVolt*baseVolt/basePower;
		
		r = Double.parseDouble(line.extractDataFiled("cim:ACLineSegment.r"))/baseImp;
		x = Double.parseDouble(line.extractDataFiled("cim:ACLineSegment.x"))/baseImp;
		b = Double.parseDouble(line.extractDataFiled("cim:ACLineSegment.bch"))*baseImp;
		g = Double.parseDouble(line.extractDataFiled("cim:ACLineSegment.gch"))*baseImp;
		
		// Pack the Line and Shunt Admittance together (first line, then shunt)
		Complex[] admittancePackage = new Complex[2];
		admittancePackage[0] = (new Complex(r, x)).reciprocal();
		admittancePackage[1] =  new Complex(g/2.0, b/2.0);
		return admittancePackage;
	}
	
	// ########################################################################################
	// Calculates the COMPLEX Line and Shunt Admittances of an Transformer Winding Pair !!!!!!!
	public Complex[] calcTrafoAdmittances(MyObject winding1, MyObject winding2, ArrayList<MyObject> baseVoltages, double basePower){
		double r1, x1, b1, g1, baseVolt1, baseImp1;
		double r2, x2, b2, g2, baseVolt2, baseImp2;
		double r, x, b, g;
		
		if(!winding1.object_type.equals("cim:PowerTransformerEnd") || !winding2.object_type.equals("cim:PowerTransformerEnd")){
			System.out.println("Analyzed Objects not Transformer Windings - Cannot Calculate Admittance!!!");
			System.out.println("Please Check Object "+winding1.object_type+" \"" +winding1.object_id+ "\"" );
			System.out.println("Please Check Object "+winding2.object_type+" \"" +winding2.object_id+ "\"" );
			terminateProgram();
		}

		baseVolt1 = getBaseVoltValue(winding1.extractDataFiled("cim:TransformerEnd.BaseVoltage"),baseVoltages);
		baseVolt2 = getBaseVoltValue(winding2.extractDataFiled("cim:TransformerEnd.BaseVoltage"),baseVoltages);
		baseImp1 = baseVolt1*baseVolt1/basePower;
		baseImp2 = baseVolt2*baseVolt2/basePower;
		
		r1 = Double.parseDouble(winding1.extractDataFiled("cim:PowerTransformerEnd.r"))/baseImp1;
		x1 = Double.parseDouble(winding1.extractDataFiled("cim:PowerTransformerEnd.x"))/baseImp1;
		b1 = Double.parseDouble(winding1.extractDataFiled("cim:PowerTransformerEnd.b"))*baseImp1;
		g1 = Double.parseDouble(winding1.extractDataFiled("cim:PowerTransformerEnd.g"))*baseImp1;
		
		r2 = Double.parseDouble(winding2.extractDataFiled("cim:PowerTransformerEnd.r"))/baseImp2;
		x2 = Double.parseDouble(winding2.extractDataFiled("cim:PowerTransformerEnd.x"))/baseImp2;
		b2 = Double.parseDouble(winding2.extractDataFiled("cim:PowerTransformerEnd.b"))*baseImp2;
		g2 = Double.parseDouble(winding2.extractDataFiled("cim:PowerTransformerEnd.g"))*baseImp2;
		
		r = r1+r2;
		x = x1+x2;
		b = b1+b2;
		g = g1+g2;

		// Pack the Line and Shunt Admittance together (first line, then shunt)
		Complex[] admittancePackage = new Complex[2];
		admittancePackage[0] = (new Complex(r, x)).reciprocal();
		admittancePackage[1] =  new Complex(g/2.0, b/2.0);
		return admittancePackage;
		
	}

	// #########################################################################
	// Finds the Value of Base Voltage based on Given Base Voltage Reference ID:
	public double getBaseVoltValue(String baseVoltRef, ArrayList<MyObject> baseVoltages){
		for(int k=0; k<baseVoltages.size(); k++){
			if(baseVoltages.get(k).object_id.equals(baseVoltRef.substring(1))){
				return Double.parseDouble(baseVoltages.get(k).extractDataFiled("cim:BaseVoltage.nominalVoltage"));
			}
		}
		System.out.println("Base Voltage \"" + baseVoltRef.substring(1)+ "\" not found, please check XML files");
		terminateProgram();
		return 0;
	}
	
	// #####################################################################
	// Method for terminating the program (in case of exceptions and errors)
	public void terminateProgram(){
		System.out.println("\n=> Program Intentionally Terminated (Kill it before it lays eggs!!!)");
		System.exit(0);
	}
}
