package com.quizapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	// ✅ Change DB name if needed
	private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
	private static final String USER = "root";
	private static final String PASS = "Narayana@4820";

	static {
		try {
			// Load MySQL JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("MySQL Driver not found!", e);
		}
	}

	public static Connection get() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
}
