package assignment1;
import java.awt.Color;

import javax.swing.JFrame;

public class GuiMain {
	
	// launch interface
	public static void main(String[] args) {
		Gui GUI = new Gui();
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set size and visibility
		GUI.setSize(500, 500);
		GUI.setResizable(false);
		GUI.setVisible(true);

	}

}
