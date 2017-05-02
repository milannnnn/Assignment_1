package assignment1;

import java.sql.*;
import java.util.ArrayList;
//import java.util.Scanner;

public class SQLprinter {
	// remember to import driver library ----> use bildbath
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/";
	private final String USER;
	private final String PASS;
	// define the name of the database
	public String dataBaseName = "assignment_1_MMMM";
	Connection conn;
	Statement stmt;
	
	// ############################################################################################################
	// constructor to initialize the database
	// Inputs:
		// USER: username of SQL database
		// PASS: password of SQL 
	public SQLprinter(String USER,String PASS){
		// JDBC driver name and database URL
		// Database credentials
		this.USER = USER;
		this.PASS = PASS;
		conn = null;
		stmt = null;
		try{
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// Execute a query to create database
//			System.out.println("Creating database...");
			stmt = conn.createStatement();
			// remove restriction to be able to look for names too, instead of IDs only
			stmt.executeUpdate("SET SQL_SAFE_UPDATES = 0");
			// Create database if it doesn't exist
			String sql = "drop database if exists " + dataBaseName; 
			stmt.executeUpdate(sql);
			sql = "create database if not exists " + dataBaseName; 
			stmt.executeUpdate(sql);
//			System.out.println("Database created successfully...");
			// Connect to the created database 
//			conn = DriverManager.getConnection(DB_URL + dataBaseName, USER, PASS);
			
		}
		catch(SQLException se){
			//Handle errors for JDBC
//			se.printStackTrace();
			System.out.println("\nSQL error! Please check the SQL server, Driver, URL, Connection, Username and Password, and try again!!!");
			kill();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			kill();
		}
	}
	
	// ############################################################################################################
	// default constructor with default user and password
	public SQLprinter(){
		this("root","root");
	}
	
	// ############################################################################################################
	// method to create a new table
	// INPUTS:
		// tableName: name of the table
		// attributesName: name of the columns as String Array
		// forKeyPos: position of the foreignKey
		// forKeyTabName: name of the table the foreign key is referred to
		// PRIMARY KEY IS THE FIRST ELEMENT
	public void  insertTable(String tableName, String[] attributesName, ArrayList<Integer> forKeyPos, String[] forKeyTabName){
		try{
			// Connect to the created database 
			conn = DriverManager.getConnection(DB_URL + dataBaseName, USER, PASS);
			// use the database
			String sql = "use " + dataBaseName;
			stmt.executeUpdate(sql);
			// delete the table if it already exists
//			sql = "drop table if exists " + tableName; 
//			System.out.println(sql);
//			stmt.executeUpdate(sql);
			// create the table, all the quantities defined as varchar of 50 length
			sql = "create table if not exists " + tableName + "(";
			for(int i = 0; i<attributesName.length; i++){
				sql = sql + attributesName[i] + " varchar(50), ";  
			}
			// Insert primary and foreign key
			// PRIMARY KEY IS THE FIRST ELEMENT
			sql = sql + "primary key (" + attributesName[0] + "), ";
			
			if(forKeyPos.isEmpty() == false){
				
				if(forKeyPos.size()==forKeyTabName.length){
			
					for(int i=0; i<forKeyPos.size(); i++){
						sql = sql + "index (" + attributesName[forKeyPos.get(i)] + "), ";
						sql = sql + "foreign key (" + attributesName[forKeyPos.get(i)] + ") references " + forKeyTabName[i] + "(ID), ";
					}
				}
				else{
					System.out.println("forKeyPos.size is not equal to forKeyTabName.length: " + forKeyPos.size() + " != " + forKeyTabName.length);
				}
			}
			// delete the last comma and add semicolon 
			sql = sql.substring(0, sql.length()-2) + ");";
//			System.out.println(sql);
			stmt.executeUpdate(sql);
			System.out.println("Created table in given database successfully...");
		}
		catch(SQLException se){
			//Handle errors for JDBC
			//se.printStackTrace();
			System.out.println("\nSQL error! Please check the SQL server, Driver, URL, Connection, Username and Password, and try again!!!");
			kill();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			kill();
		}	
	}
	
	// ############################################################################################################
	// insert data in a table using prepared statement
	// INPUTS:
	// tableName: name of the table where to insert data
	// data: String array with data to insert
		// the length of data should be the same as attributesName
	public void insertData(String tableName, String[] data){
		try{			
			String query = "insert into " + tableName + " values(";			
			for(int i = 0; i<(data.length-1); i++){
				query = query + "?,";
			}
			query = query + "?);";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			for(int i = 0; i<(data.length); i++){
				
				// remove the has before the ID
				if(data[i].substring(0,1).equals("#")){
					data[i]=data[i].substring(1, data[i].length());
				}
				
				preparedStmt.setString(i+1, data[i]);
			}
			preparedStmt.execute();
			System.out.println("Data inserted...");
		}
		catch(SQLIntegrityConstraintViolationException ecc){
			ecc.printStackTrace();
			System.out.println("The foreign key you are trying to add doesn't exist as primary key in any other table!");
			System.out.println("Probably because Synchronous_Machine_NL-G2's GeneratingUnit points towards a ThermalGeneratingUnit rdf:ID= _ca80ee09-3bed-4884-bc28-6dc89d067289, which is not a table");
			kill();
		}
		catch(SQLException se){
			//Handle errors for JDBC
			//se.printStackTrace();
			System.out.println("\nSQL error! Please check the SQL server, Driver, URL, Connection, Username and Password, and try again!!!");
			kill();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			kill();
		}	
	}
	
	// ############################################################################################################
	// update the table
	// INPUTS:
		// tableName: name of the table where to apply the updates
		// columnName: String array with the name of the columns where to apply the changes
		// attributesValue: String array with value to be updated
		// primaryKeyName: name of the primary key column where to apply the changes
		// primaryKeyValue: value of the primary key
	public void upDate(String tableName, String[] columnName, String[] attributesValue, String primaryKeyName, String primaryKeyValue){
		if(attributesValue.length!=columnName.length){
			System.out.println("The number of attributes and numerical values doesn't match");
			kill();
		}else{
			try{
				String query = "update " + tableName + " set ";
				for(int i=0; i<columnName.length; i++){
					query = query + columnName[i] + "=?, "; 
				}
				query = query.substring(0, query.length()-2) + " where " + primaryKeyName + " =?;";
//				System.out.println(query);
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				for(int i=0; i<attributesValue.length+1; i++){
					if(i==attributesValue.length){
						preparedStmt.setString(i+1, primaryKeyValue);
					}else{
						preparedStmt.setString(i+1, attributesValue[i]);
					}
				}
				preparedStmt.execute();
				System.out.println("The table is updated...");
			}
			catch(SQLException se){
				//Handle errors for JDBC
				//se.printStackTrace();
				System.out.println("\nSQL error! Please check the SQL server, Driver, URL, Connection, Username and Password, and try again!!!");
				kill();
			}
			catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
				kill();
			}	
		}
	}
	
	// ############################################################################################################
	// close connection with database
	public void exit(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("\nError while closing SQL connection! Please check the SQL server, Driver, URL, Connection, Username and Password, and try again!!!");
			kill();
		}
	}
	
	// ############################################################################################################
	// method to kill the program
	@SuppressWarnings("deprecation")
	public void kill(){
		System.out.println("\n=> Program Intentionally Terminated (Kill it before it lays eggs!!!)");
		Thread.currentThread().stop();
	}
	
}


