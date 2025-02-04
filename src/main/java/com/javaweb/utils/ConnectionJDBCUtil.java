package com.javaweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.javaweb.repository.entity.RentAreaEntity;

public class ConnectionJDBCUtil {
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "nhuphuong1416";
	
	public static Connection getConnection() {
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connect database error from ConnectionJDBCUtil ....");
		}
		return conn;
	}
}
