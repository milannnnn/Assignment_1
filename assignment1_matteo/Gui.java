package assignment1_matteo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

public class Gui extends JFrame {
	
	private JTextField tf1;
	private JTextField tf2;
	private JRadioButton defb;
	private JRadioButton cusb;
	private ButtonGroup group;
	private JButton butt;
	boolean	customPath;
	String EQpath = "";
	String SSHpath= "";
	
	public Gui(){
		
		super("Project 1");
		setLayout(new FlowLayout());
		
		tf1 = new JTextField("Enter EQ file path here: C:\\Users\\Matteo\\...\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml", 50);
		tf1.setToolTipText("insert EQ file path and press Enter");
		tf1.setEditable(customPath);
		add(tf1);
		tf2 = new JTextField("Enter SSH file path here: C:\\Users\\Matteo\\...\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml", 50);
		tf2.setEditable(customPath);
		tf2.setToolTipText("insert SSH file path and press Enter");
		add(tf2);
		
		defb = new JRadioButton("default path", true);
		// true is checked, false in unchecked
		cusb = new JRadioButton("custum path", false);
		add(defb);
		add(cusb);
		
		group = new ButtonGroup();
		group.add(defb);
		group.add(cusb);
		
		butt = new JButton("PARSE");
		add(butt);
		
		EnterHandler enterhandler = new EnterHandler();
		tf1.addActionListener(enterhandler);
		tf2.addActionListener(enterhandler);
		
		ClickHandler clickhandler = new ClickHandler();
		defb.addItemListener(clickhandler);
		cusb.addItemListener(clickhandler);
		
		ButtonHandler buttohandler = new ButtonHandler();
		butt.addActionListener(buttohandler);
	}
			
	private class EnterHandler implements ActionListener{
			
		public void actionPerformed(ActionEvent event){
				
//			String EQpath =""; 
//			String SSHpath= "";
				
			if(event.getSource()==tf1){
				EQpath = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "Path successfully inserted");
			}
			else if(event.getSource()==tf2){
				SSHpath= event.getActionCommand();
				JOptionPane.showMessageDialog(null, "Path successfully inserted");
		
			}
			
		}
	}
	
	private class ClickHandler implements ItemListener{
		
		
		// set the font to the font object that was passed in
		public void itemStateChanged(ItemEvent event){
			
			if(defb.isSelected() == true){
				customPath = false;
			}
			else if(cusb.isSelected() == true){
				customPath = true;
			}
			tf1.setEditable(customPath);
			tf2.setEditable(customPath);
//			JOptionPane.showMessageDialog(null, customPath);
		}
	}
	
	private class ButtonHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			if(customPath == true){
				if(EQpath == "" || SSHpath == ""){
					JOptionPane.showMessageDialog(null, "Press Enter after inserting new paths", "PATH MISSING", JOptionPane.WARNING_MESSAGE);
				}
				else{
					JTextArea textArea = new JTextArea(ReadXML2.main(null, customPath, EQpath, SSHpath));
					JScrollPane scrollPane = new JScrollPane(textArea); 
					scrollPane.setFont(new Font("Serif",Font.PLAIN, 50));
					textArea.setLineWrap(true);  
					textArea.setWrapStyleWord(true); 
					scrollPane.setPreferredSize( new Dimension( 800, 800 ) );
					JOptionPane.showMessageDialog(null, scrollPane, "Parsed Text",  
					                                       JOptionPane.PLAIN_MESSAGE);
				}
			}
			else{
//				ReadXML2.main(null, customPath, EQpath, SSHpath);
				JTextArea textArea = new JTextArea(ReadXML2.main(null, customPath, EQpath, SSHpath));
				JScrollPane scrollPane = new JScrollPane(textArea); 
				scrollPane.setFont(new Font("Serif",Font.PLAIN, 50));
				textArea.setLineWrap(true);  
				textArea.setWrapStyleWord(true); 
				scrollPane.setPreferredSize( new Dimension( 800, 800 ) );
				JOptionPane.showMessageDialog(null, scrollPane, "Parsed Text",  
				                                       JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	
	
}

