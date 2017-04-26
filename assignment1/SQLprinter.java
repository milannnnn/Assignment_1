package assignment1;

import java.sql.*;

public class SQLprinter {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/";
	private final String USER;
	private final String PASS;
	public String dataBaseName = "assignment_1_MMMM";
	Connection conn;
	Statement stmt;
	
	public SQLprinter(String USER,String PASS){
		// JDBC driver name and database URL
		// Database credentials
		this.USER = USER;
		this.PASS = PASS;
//		USER = "root";
//		PASS = "Callandor14"; // insert the password to SQL server
		conn = null;
		stmt = null;
		try{
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// Execute a query to create database
			System.out.println("Creating database...");
			stmt = conn.createStatement();
			stmt.executeUpdate("SET SQL_SAFE_UPDATES = 0");
			
			String sql = "create database if not exists " + dataBaseName; // Create database students
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully...");
			// Connect to the created database STUDENTS and create table REGISTRATION
			conn = DriverManager.getConnection(DB_URL + dataBaseName, USER, PASS);
			
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}			
	}
	
	public SQLprinter(){
		this("root","root");
	}
	public void  insertTable(String tableName, String[] attributesName){
		try{
			conn = DriverManager.getConnection(DB_URL + dataBaseName, USER, PASS);
			String sql = "use " + dataBaseName;
			stmt.executeUpdate(sql);
			sql = "drop table if exists " + tableName; 
			stmt.executeUpdate(sql);
			sql = "create table if not exists " + tableName + "(";
			for(int i = 0; i<attributesName.length; i++){
				sql = sql + attributesName[i] + " varchar(50),";  
			}
			// PRIMARY KEY IS THE FIRST ELEMENT
			sql = sql + "primary key (" + attributesName[0] + "));";
//			System.out.println(sql);
			stmt.executeUpdate(sql);
			System.out.println("Created table in given database successfully...");
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}	
	}
	
	public void insertData(String tableName, String[] data){
		try{			
			String query = "insert into " + tableName + " values(";			
			for(int i = 0; i<(data.length-1); i++){
				query = query + "?,";
			}
			query = query + "?);";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			for(int i = 0; i<(data.length); i++){
				preparedStmt.setString(i+1, data[i]);
			}
			preparedStmt.execute();
			System.out.println("Data inserted...");
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}	
	}
	
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
				System.out.println(query);
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
				se.printStackTrace();
			}
			catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
			}	
		}
	}
	
	public void exit(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void kill(){
		System.out.println("\n=> Program Intentionally Terminated (Kill it before it lays eggs!!!)");
		System.exit(0);
	}
	
}


