package assignment1_milan;

import java.util.ArrayList;

public class CopyOfTest {

	public static void main(String[] args) {

		double basePower = 1000; // in MW!!!!!!!!!!!!!!
		
//		String eq_path  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//		String ssh_path = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
		String eq_path  = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_EQ_V2.xml";
		String ssh_path = "C:/Users/Milan/Desktop/KTH/Semester 2/Computer Applications in Power Systems - EH2745/Lab - Cim2Power/CIM2Matpower_lab_session/Total_MG_T1_SSH_V2.xml";

		
		AdmittanceMatrix test = new AdmittanceMatrix();
		
		Complex[][] admMat = test.calculateAdmMatrix(eq_path, ssh_path, basePower);
	}
}



















