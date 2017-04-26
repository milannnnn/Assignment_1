package assignment1;

public class MyObject {
	
	public String object_type;
	public String object_id;
	public String[] req_data;
	public String[] ext_data;
	
	public MyObject(){}
	
	public MyObject(String ob, String id, String[] df, String[] da){
		object_type	= ob;
		object_id 	= id;
		req_data 	= df;
		ext_data 	= da;
	}
	
	public void printOut(int i){
		System.out.println(i + ") " + object_id + ":\n");
		for (int n=0; n<req_data.length; n++){
			System.out.println(req_data[n] + "\t" + ext_data[n]);
		}
		System.out.println("\n");
	}

	public void printObjectType(){
		String s = "------------------------------------------------";
		System.out.println(s+"\n"+object_type+s+"\n");
	}
}
