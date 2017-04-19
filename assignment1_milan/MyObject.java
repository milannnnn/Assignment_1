package assignment1_milan;

public class MyObject {
	
	private String object_type;
	private String object_id;
	private String[] data_fields;
	private String[] data_attributes;
	
	public MyObject(){}
	
	public MyObject(String ob, String id, String[] df, String[] da){
		object_type 	= ob;
		object_id 		= id;
		data_fields 	= df;
		data_attributes = da;
	}
	
	public void printOut(int i){
		System.out.println(i + ") " + object_id + ":\n");
		for (int n=0; n<data_fields.length; n++){
			System.out.println(data_fields[n] + "\t" + data_attributes[n]);
		}
		System.out.println("\n");
	}

	public void printObjectType(){
		String s = "------------------------------------------------";
		System.out.println(s+"\n"+object_type+s+"\n");
	}
}
