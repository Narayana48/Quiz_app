package com.quizapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.quizapp.db.DB;
import com.quizapp.model.User;

public class UserDAO {

	// ✅ Find user by username
	public User findByUsername(String username) throws Exception {
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User u = new User();
					u.setId(rs.getInt("id"));
					u.setUsername(rs.getString("username"));
					u.setPassword(rs.getString("password")); // plain text now
					u.setRole(rs.getString("role"));
					return u;
				}
			}
		}
		return null;
	}

	// ✅ Verify password (no bcrypt, just plain text check)
	public boolean verifyPassword(String enteredPassword, String storedPassword) {
		return enteredPassword.equals(storedPassword);
	}

	// ✅ Save a new user (registration)
	public void save(User user) throws Exception {
		String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
		try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword()); // store plain text (⚠️ hash in real apps)
			ps.setString(3, user.getRole() != null ? user.getRole() : "USER");
			ps.executeUpdate();
		}
	}
}
