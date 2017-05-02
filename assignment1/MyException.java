package assignment1;

@SuppressWarnings("serial")
public class MyException extends Exception {
	String s1;
	MyException(String s2) {
	   s1 = s2;
	} 
	@Override
	public String toString() { 
	   return ("Output String = "+s1);
	}
}
