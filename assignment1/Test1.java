package assignment1;

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
		
		CreateDataBase DB = new CreateDataBase();
		DB.CreateDBDefault();

	}

}
