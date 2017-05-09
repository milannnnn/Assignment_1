package assignment1;

public class Test {

	public static void main(String[] args) {
//		String eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		String ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
		String eq_path  = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
		String ssh_path = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";
		
//		String eq_path  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		String ssh_path = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		
		CreateDataBase DB = new CreateDataBase(eq_path,ssh_path,"root","root");
//		CreateDataBase DB = new CreateDataBase();
		DB.CreateDBDefault();
		
		double basePower = 1000; // in MW!!!!!!!!!!!!!!
		AdmittanceMatrix_withBusbarChecking test = new AdmittanceMatrix_withBusbarChecking();
		test.calculateAdmMatrix(eq_path, ssh_path, basePower);
		
	}
}