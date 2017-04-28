package assignment1_milan;

import java.util.ArrayList;

public class CopyOfTest {

	public static void main(String[] args) {
		
	    String[] a = {"1", "2"};
	    String[] a2 = new String[a.length + 1];
	    a2[0] = "0";
	    System.arraycopy(a, 0, a2, 1, a.length);
	    
	    for(int i=0;i<a2.length;i++){
	    	System.out.println(a2[i]);
	    }
		
	    ArrayList<String> sfs = new ArrayList<String>();
	    sfs.add("test test");
	    System.out.println(sfs.get(0));
	    
	    
	}
}



















