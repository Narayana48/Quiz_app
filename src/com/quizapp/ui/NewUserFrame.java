package com.quizapp.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.quizapp.dao.UserDAO;
import com.quizapp.model.User;

public class NewUserFrame extends JFrame {
	private final UserDAO userDAO = new UserDAO();

	public NewUserFrame() {
		setTitle("New User Registration");
		setSize(350, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false); // Prevent maximizing the window

		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		JLabel lblTitle = new JLabel("Create New Account", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setForeground(new Color(72, 61, 139));

		JTextField txtUser = new JTextField();
		JPasswordField txtPass = new JPasswordField();

		JButton btnRegister = new JButton("Register");
		btnRegister.setBackground(new Color(46, 204, 113));
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));

		panel.add(lblTitle);
		panel.add(new JLabel("Username"));
		panel.add(txtUser);
		panel.add(new JLabel("Password"));
		panel.add(txtPass);
		panel.add(btnRegister);

		add(panel);

		// Register button action
		btnRegister.addActionListener(ae -> {
			try {
				String username = txtUser.getText().trim();
				String password = new String(txtPass.getPassword());

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Username and Password are required");
					return;
				}

				// Save new user
				User user = new User();
				user.setUsername(username);
				user.setPassword(password); // DAO should hash password
				user.setRole("STUDENT");

				userDAO.save(user);
				JOptionPane.showMessageDialog(this, "User registered successfully!");
				dispose();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
