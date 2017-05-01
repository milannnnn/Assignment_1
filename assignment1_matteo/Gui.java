package assignment1_matteo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.StringWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableRow;

import assignment1.AdmittanceMatrix;
import assignment1.CreateDataBase;

public class Gui extends JFrame {
	
	private JTextField tf1;
	private JPasswordField tf2;
	private JTextField BasePowerText;
	private JRadioButton defb;
	private JRadioButton cusb;
	private ButtonGroup group;
	private JButton buttDB;
	private JButton buttYM;
	private JButton loadEQ;
	private JButton loadSSH;
	private JFileChooser fc; 
	boolean	customPath = false;
	double basePower = 1000;
	String USER = "root";
//	String PASS = "root";
	String PASS = "Callandor14";
//	String EQpath  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//	String SSHpath = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
	String EQpath  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
	String SSHpath = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
	
	public Gui(){
		
		super("Project 1");
		setLayout(new FlowLayout());
		
		// create graphical objects
		tf1 = new JTextField("MySQL USERNAME", 40);
		tf1.setToolTipText("insert MySQL USERNAME and press Enter");
		tf1.setEditable(customPath);
		add(tf1);
		tf2 = new JPasswordField("MySQL PASSWORD", 40);
		tf2.setEditable(customPath);
		tf2.setToolTipText("insert MySQL PASSWORD and press Enter");
		add(tf2);
		
		BasePowerText = new JTextField("Base Power", 40);
		BasePowerText.setEditable(customPath);
		BasePowerText.setToolTipText("insert Base Power and press Enter");
		add(BasePowerText);
		
		defb = new JRadioButton("default options", true);
		// true is checked, false in unchecked
		cusb = new JRadioButton("custum options", false);
		add(defb);
		add(cusb);
		
		group = new ButtonGroup();
		group.add(defb);
		group.add(cusb);
		
		loadEQ = new JButton("Load EQ");
		loadEQ.setEnabled(customPath);
		add(loadEQ);
		
		loadSSH = new JButton("Load SSH");
		loadSSH.setEnabled(customPath);
		add(loadSSH);
		
		buttDB = new JButton("Create relational DB");
		add(buttDB);
		
		buttYM = new JButton("Create Admittance matrix");
		add(buttYM);
		
		fc = new JFileChooser();
//		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		fc.setCurrentDirectory(new File("C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
		fc.setFileFilter(filter);
		fc.setEnabled(customPath);
		fc.setVisible(customPath);
		
		// create events handlers
		// handle USER and PASS
		EnterHandler enterhandler = new EnterHandler();
		tf1.addActionListener(enterhandler);
		tf2.addActionListener(enterhandler);
		BasePowerText.addActionListener(enterhandler);
		
		// handle default option
		ClickHandler clickhandler = new ClickHandler();
		defb.addItemListener(clickhandler);
		cusb.addItemListener(clickhandler);
		
		// handle paths
		FileChoiceHandler FCHandler = new FileChoiceHandler();
		loadEQ.addActionListener(FCHandler);
		loadSSH.addActionListener(FCHandler);
		
		
		ButtonDBHandler buttonDBhandler = new ButtonDBHandler();
		buttDB.addActionListener(buttonDBhandler);
		
		
		ButtonYMHandler buttonYMhandler = new ButtonYMHandler();
		buttYM.addActionListener(buttonYMhandler);
	}
		
	
	private class ClickHandler implements ItemListener{
		
		
		// set the font to the font object that was passed in
		public void itemStateChanged(ItemEvent event){
			
			if(defb.isSelected() == true){
				USER = "root";
				PASS = "root";
				EQpath  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
				SSHpath = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
				customPath = false;
				tf1.setEditable(customPath);
				tf2.setEditable(customPath);
				buttDB.setEnabled(!customPath);
				buttYM.setEnabled(!customPath);
				loadEQ.setEnabled(customPath);
				loadSSH.setEnabled(customPath);
				fc.setEnabled(customPath);
				BasePowerText.setEditable(customPath);
				
			}
			else if(cusb.isSelected() == true){
				customPath = true;
				tf1.setEditable(customPath);
				buttDB.setEnabled(!customPath);
				buttYM.setEnabled(!customPath);
			}
//			JOptionPane.showMessageDialog(null, customPath);
		}
	}
	
	private class EnterHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
				
//			String EQpath =""; 
//			String SSHpath= "";
				
			if(event.getSource()==tf1){
				USER = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "USERNAME successfully inserted");
				tf1.setEditable(!customPath);
				tf2.setEditable(customPath);
			}
			else if(event.getSource()==tf2){
				PASS = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "PASSWORD successfully inserted");
				loadEQ.setEnabled(customPath);
				tf2.setEditable(!customPath);
				fc.setVisible(customPath);
				fc.setEnabled(customPath);
		
			}
			else if(event.getSource()==BasePowerText){
				String basePowerString = event.getActionCommand();
				try{
					basePower = Double.parseDouble(basePowerString);
					JOptionPane.showMessageDialog(null, "Base Power successfully inserted");
					buttYM.setEnabled(customPath);
				}
				catch(NumberFormatException ex){ // handle your exception
					JOptionPane.showMessageDialog(null, "Base Power has to be a number!!");
				}
			}
		}
	}
	
	
	// handle File chooser
	private class FileChoiceHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
		    //Handle open button action.
		    if (e.getSource() == loadEQ) {
		    	int returnVal = fc.showOpenDialog(Gui.this);
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            EQpath = file.getAbsolutePath();
			            loadSSH.setEnabled(customPath);
		    	}
		    }
		    else if (e.getSource() == loadSSH) {
		    	int returnVal = fc.showOpenDialog(Gui.this);
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            SSHpath = file.getAbsolutePath();
			            buttDB.setEnabled(customPath);
			            BasePowerText.setEditable(customPath);
		    	}
		    }
		}
	}
	
	private class ButtonDBHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			try{
				CreateDataBase DB = new CreateDataBase(EQpath, SSHpath, USER, PASS);
				DB.CreateDBDefault();
				JOptionPane.showMessageDialog(null, "Database Created Successfully!");
			}
			catch(Exception SQLe){
//				SQLe.printStackTrace();
				if(customPath){
					JOptionPane.showMessageDialog(null, "Check USER and PASSWORD!!");
					tf1.setEditable(customPath);
				}
				else{
					JOptionPane.showMessageDialog(null, "Default user and passord don't work! \nSelect custom options and insert them!");
				}
			}
		}
	}
	
	private class ButtonYMHandler implements ActionListener{			
				
		public void actionPerformed(ActionEvent event){	
			
			AdmittanceMatrix AM = new AdmittanceMatrix();
			assignment1.Complex[][] AMCompl = AM.calculateAdmMatrix(EQpath, SSHpath, basePower);
			String[][] AMString = new String[AMCompl.length][AMCompl.length];
			for(int k=0; k<AMCompl.length; k++){
				for(int j=0; j<AMCompl.length; j++){
					assignment1.Complex val = AMCompl[k][j];
					double real = val.re();
					double im = val.im();
					String sign = "";
					String space = "";
					if(im >= 0){
						sign = "+";
					}
					else{
						im = -im;
						sign = "-";
					}
					if(real < 0){
						real = -real;
						space = "-";
					}
					else{
						space =" ";
					}
					AMString[k][j] = String.format( space + "%.4f " + sign + " %.4f" + "i", real , im) ;
				}
			}
			
			String[] names = new String[AMString.length];
			for( int i=0; i<AMString.length; i++){
				names[i]= Integer.toString(i+1);
			}
			JTable Table = new JTable(AMString, names);
			Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			Table.setFont(new Font("Serif",Font.PLAIN, 22));
			
			TableColumn column = null;
		    for (int i = 0; i < AMString.length; i++) {
		        column = Table.getColumnModel().getColumn(i);
		        column.setPreferredWidth(200); 
		    }  
		    Table.setRowHeight(50);
			JScrollPane scrollPane1 = new JScrollPane(Table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane1.setFont(new Font("Serif",Font.PLAIN, 50));
			int width1 = AMCompl.length * 205;
			if(width1 > 1900 ){
				width1 = 1900;
			}
			int length1 = AMCompl.length * 55;
			if (length1>1000){
				length1=900;
			}
			scrollPane1.setPreferredSize( new Dimension(width1,length1));
			JOptionPane.showMessageDialog(null, scrollPane1, "Admittance Matrix",  
			                                       JOptionPane.PLAIN_MESSAGE);
		}
	}
}
