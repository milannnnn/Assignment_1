package assignment1;
import javax.swing.JFrame;

public class GuiMain {
	
	// launch interface
	public static void main(String[] args) {
		Gui GUI = new Gui();
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set size and visibility
		GUI.setSize(500, 200);
		GUI.setVisible(true);

	}

}
