package assignment1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import assignment1.AdmittanceMatrix;
import assignment1.CreateDataBase;

public class Gui extends JFrame {
	
	// Declare objects that will appear on the interface
	// TextFields
	private JTextField tf1;
	private JPasswordField tf2;
	private JTextField BasePowerText;
	// buttons
	private JRadioButton defb;
	private JRadioButton cusb;
	private ButtonGroup group;
	private JButton buttDB;
	private JButton buttYM;
	private JButton loadEQ;
	private JButton loadSSH;
	// file chooser
	private JFileChooser fc; 
	// set boolean variable to allow the choice of custom options
	boolean	customPath = false;
	// set default variable
	double basePower = 1000;
	private String USER = "root";
	private String PASS = "root";
//	String EQpath  = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
//	String SSHpath = "C:\\Users\\Milan\\Desktop\\KTH\\Semester 2\\Computer Applications in Power Systems - EH2745\\Assignments\\1st\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
	String EQpath  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
	String SSHpath = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
	
	// ############################################################################################################
	public Gui(){
		
		// set interface title
		super("Project 1");
		// use a pre-determined layout, i.e. FlowLayout
		setLayout(new FlowLayout());
		
		// create graphical objects
		// TEXTFIELDS
		tf1 = new JTextField("MySQL USERNAME", 40);
		tf1.setToolTipText("insert MySQL USERNAME and press Enter");
		// by default they are set not editable
		tf1.setEditable(customPath);
		add(tf1);
		tf2 = new JPasswordField("MySQL PASSWORD", 40);
		tf2.setEditable(customPath);
		tf2.setToolTipText("insert MySQL PASSWORD and press Enter");
		add(tf2);
		BasePowerText = new JTextField("Base Power [MW]. Default value: 1000 MW", 40);
		BasePowerText.setEditable(customPath);
		BasePowerText.setToolTipText("insert Base Power [MW] and press Enter");
		add(BasePowerText);
		
		// create RADIO BUTTONS
		defb = new JRadioButton("default options", true);
		// true is checked, false in unchecked
		cusb = new JRadioButton("custum options", false);
		add(defb);
		add(cusb);
		// group the radiobuttons together
		group = new ButtonGroup();
		group.add(defb);
		group.add(cusb);
		
		// create buttons to load xml files
		// set by default not enabled 
		loadEQ = new JButton("Load EQ");
		loadEQ.setEnabled(customPath);
		add(loadEQ);
		loadSSH = new JButton("Load SSH");
		loadSSH.setEnabled(customPath);
		add(loadSSH);
	
		// create buttons to execute dataBase creation and Y-matrix building
		buttDB = new JButton("Create relational DB");
		add(buttDB);
		buttYM = new JButton("Create Admittance matrix");
		add(buttYM);
		
		// create File Chooser
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
//		fc.setCurrentDirectory(new File("C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1"));
		// limit choosable files to xml
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".xml", "xml");
		fc.setFileFilter(filter);
		// set by default not visible 
		fc.setEnabled(customPath);
		fc.setVisible(customPath);
		
		// create events handlers
		// handle text fields enter insertion
		EnterHandler enterhandler = new EnterHandler();
		tf1.addActionListener(enterhandler);
		tf2.addActionListener(enterhandler);
		BasePowerText.addActionListener(enterhandler);
		
		// handle default-custom options with radio buttons
		ClickHandler clickhandler = new ClickHandler();
		defb.addItemListener(clickhandler);
		cusb.addItemListener(clickhandler);
		
		// handle paths choice
		FileChoiceHandler FCHandler = new FileChoiceHandler();
		loadEQ.addActionListener(FCHandler);
		loadSSH.addActionListener(FCHandler);
		
		// handle DB creation button
		ButtonDBHandler buttonDBhandler = new ButtonDBHandler();
		buttDB.addActionListener(buttonDBhandler);
		
		// handle Y matrix creation button
		ButtonYMHandler buttonYMhandler = new ButtonYMHandler();
		buttYM.addActionListener(buttonYMhandler);
	}
	
	
	// ############################################################################################################
	// handle default-custom options with radio buttons
	private class ClickHandler implements ItemListener{
		public void itemStateChanged(ItemEvent event){
			// if default options are selected
			if(defb.isSelected() == true){
				// set the variable to default values
				USER = "root";
				PASS = "root";
				EQpath  = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_EQ_V2.xml";
				SSHpath = "C:\\Users\\Matteo\\Documents\\Kic InnoEnergy\\KTH\\Computer application\\Assignment 1\\MicroGridTestConfiguration_T1_BE_SSH_V2.xml";
				// set boolean variable for custom choice false
				customPath = false;
				// set all graphical objects to uneditable except from the button to actually create DB and Y-matrix
				tf1.setEditable(customPath);
				tf2.setEditable(customPath);
				buttDB.setEnabled(!customPath);
				buttYM.setEnabled(!customPath);
				loadEQ.setEnabled(customPath);
				loadSSH.setEnabled(customPath);
				fc.setEnabled(customPath);
				BasePowerText.setEditable(customPath);
			}
			// if custom options are selected
			else if(cusb.isSelected() == true){
				// set boolean variable for custom choice true
				customPath = true;
				// set USER field editable
				tf1.setEditable(customPath);
				// set button to create DB and Y-matrix not editable
				buttDB.setEnabled(!customPath);
				buttYM.setEnabled(!customPath);
			}
		}
	}
	
	// ############################################################################################################
	// handle enter press on text fields
	private class EnterHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
			// when enter is pressed on USER field
			if(event.getSource()==tf1){
				// set USER equal to content of text field
				USER = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "USERNAME successfully inserted");
				// set USER not editable
				tf1.setEditable(!customPath);
				// set PASSWORD editable
				tf2.setEditable(customPath);
			}
			// when enter is pressed on PASS field
			else if(event.getSource()==tf2){
				// set UPASSequal to content of text field
				PASS = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "PASSWORD successfully inserted");
				// enable EQ file selection button
				loadEQ.setEnabled(customPath);
				// set PASSWORD uneditable
				tf2.setEditable(!customPath);
				// set file chooser visible
				fc.setVisible(customPath);
				fc.setEnabled(customPath);
		
			}
			// when enter is pressed on BasePower field
			else if(event.getSource()==BasePowerText){
				// store the inserted field as a String
				String basePowerString = event.getActionCommand();
				try{
					// try to convert basePowerString to double
					basePower = Double.parseDouble(basePowerString);
					JOptionPane.showMessageDialog(null, "Base Power successfully inserted");
					// enable Y-matrix button
					buttYM.setEnabled(customPath);
				}
				catch(NumberFormatException ex){ 
					// if the inserted text in not a number ask to digit again and disable Y-matrix button
					JOptionPane.showMessageDialog(null, "Base Power has to be a number!!");
					buttYM.setEnabled(!customPath);
				}
			}
		}
	}
	
	// ############################################################################################################
	// handle File chooser
	private class FileChoiceHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		    //Handle open button action
			// when loadEQ button is clicked
		    if (e.getSource() == loadEQ) {
		    	int returnVal = fc.showOpenDialog(Gui.this);
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		// take the path of the selected file 
			        File file = fc.getSelectedFile();
			        EQpath = file.getAbsolutePath();
			        // enable loadSSH button
			        loadSSH.setEnabled(customPath);
		    	}
		    }
		 // when loadSSH button is clicked
		    else if (e.getSource() == loadSSH) {
		    	int returnVal = fc.showOpenDialog(Gui.this);
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		// take the path of the selected file 
			        File file = fc.getSelectedFile();
			        SSHpath = file.getAbsolutePath();
			        // enable DB creation button
			        buttDB.setEnabled(customPath);
			        // enable base power text insertion
			        BasePowerText.setEditable(customPath);
		    	}
		    }
		}
	}
	
	// ############################################################################################################
	// handle DB creation button
	private class ButtonDBHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			// try to create database 
			try{
				CreateDataBase DB = new CreateDataBase(EQpath, SSHpath, USER, PASS);
				DB.CreateDBDefault();
				JOptionPane.showMessageDialog(null, "Database Created Successfully!");
			}
			
			catch(Exception SQLe){
				// if exception is thrown and custom option is active, ask to check user and psw
				if(customPath){
					JOptionPane.showMessageDialog(null, "Check USER and PASSWORD!!");
					tf1.setEditable(customPath);
				}
				// if exception is thrown and default option is active, ask to pass to custom option
				else{
					JOptionPane.showMessageDialog(null, "Default user and passord don't work! \nSelect custom options and insert them!");
				}
			}
		}
	}
	// ############################################################################################################
	// handle Y-Matrix creation button
	private class ButtonYMHandler implements ActionListener{					
		public void actionPerformed(ActionEvent event){	
			// create admittance matrix
			AdmittanceMatrix AM = new AdmittanceMatrix();
			assignment1.Complex[][] AMCompl = AM.calculateAdmMatrix(EQpath, SSHpath, basePower);
			// create empty string matrix
			String[][] AMString = new String[AMCompl.length][AMCompl.length];
			// convert Complex matrix to String matrix
			for(int k=0; k<AMCompl.length; k++){
				for(int j=0; j<AMCompl.length; j++){
					// extract Complex number
					assignment1.Complex val = AMCompl[k][j];
					// save real and imaginary parts
					double real = val.re();
					double im = val.im();
					String sign = "";
					String space = "";
					// if real part is positive add plus
					if(im >= 0){
						sign = "+";
					}
					// if real part is negative add minus
					else{
						im = -im;
						sign = "-";
					}
					// add minus to real negative part
					if(real < 0){
						real = -real;
						space = "-";
					}
					// add empty space before real positive part
					else{
						space =" ";
					}
					// set format of each element
					// take 4 decimals points
					AMString[k][j] = String.format( space + "%.4f " + sign + " %.4f" + "i", real , im);
				}
			}
			// create Array of columns names
			String[] names = new String[AMString.length];
			for( int i=0; i<AMString.length; i++){
				names[i]= Integer.toString(i+1);
			}
			// create table with given matrix and column names
			JTable Table = new JTable(AMString, names);
			// disable autoresize
			Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			// set Font
			Table.setFont(new Font("Serif",Font.PLAIN, 22));
			// set column width
			TableColumn column = null;
		    for (int i = 0; i < AMString.length; i++) {
		        column = Table.getColumnModel().getColumn(i);
		        column.setPreferredWidth(200); 
		    }  
		    // set rows' height
		    Table.setRowHeight(50);
		    // create a scroll pane and add the table to it
			JScrollPane scrollPane = new JScrollPane(Table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setFont(new Font("Serif",Font.PLAIN, 50));
			// set window size and limits
			int width = AMCompl.length * 205;
			if(width > 1900 ){
				width = 1900;
			}
			int length = AMCompl.length * 55;
			if (length>1000){
				length=900;
			}
			scrollPane.setPreferredSize( new Dimension(width,length));
			// add scroll pane to window
			JOptionPane.showMessageDialog(null, scrollPane, "Admittance Matrix", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
