package io.egen.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
	
	
	private static final String DB_URL ="jdbc:mysql://localhost:3306/rrs_db";
	private static final String DB_USER ="root";
	private static final String DB_PASSWORD ="root";
	
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded successfully");
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading JDBC Driver"+e.getMessage());
			e.printStackTrace();
		}
	}

	public static Connection connect(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Connection successful");
		} catch (SQLException e) {
			System.err.println("Error in getting Connection"+e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public static void main(String args[]){
		DBUtils.connect();
	}
}
