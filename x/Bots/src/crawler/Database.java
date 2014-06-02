package crawler;

import java.sql.*;

public class Database {

	public static Connection get() throws Exception {
		
		// this will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Mysql driver not found.");
			e.printStackTrace();
		}
	    
		try {
			
			// setup the connection with the DB.
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/pricebox?user=root&password=");
			
			return conn;
		    
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
