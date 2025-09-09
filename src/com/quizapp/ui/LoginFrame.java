package com.quizapp.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.quizapp.dao.UserDAO;
import com.quizapp.model.User;

public class LoginFrame extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginFrame() {
		setTitle("QuizApp - Login");
		setSize(450, 380);
		setMinimumSize(new Dimension(400, 320));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// === Use GridBagLayout for true centering ===
		setLayout(new GridBagLayout());

		// ---- Change frame background to a modern soft gradient color ----
		getContentPane().setBackground(new Color(0, 63, 211)); // soft blue

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;

		// === Root wrapper with vertical layout ===
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setOpaque(false); // make root transparent so background shows
		root.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

		// This ensures root panel always stays centered and grows with frame
		JPanel centerWrapper = new JPanel(new GridBagLayout());
		centerWrapper.setOpaque(false); // transparent wrapper
		centerWrapper.add(root);
		add(centerWrapper, gbc);

		// === Title ===
		JLabel titleLabel = new JLabel("QuizApp Login", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE); // white title for contrast
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		root.add(titleLabel);
		root.add(Box.createRigidArea(new Dimension(0, 25))); // spacing

		// === Login form ===
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.WHITE); // keep form white to contrast with background
		formPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
						BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		GridBagConstraints fgbc = new GridBagConstraints();
		fgbc.fill = GridBagConstraints.HORIZONTAL;
		fgbc.insets = new java.awt.Insets(10, 10, 10, 10);

		JLabel userLabel = new JLabel("Username:");
		userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		fgbc.gridx = 0;
		fgbc.gridy = 0;
		formPanel.add(userLabel, fgbc);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		usernameField.setPreferredSize(new Dimension(250, 35));
		usernameField.setMinimumSize(new Dimension(200, 35));
		fgbc.gridx = 1;
		fgbc.gridy = 0;
		fgbc.weightx = 1.0;
		formPanel.add(usernameField, fgbc);

		JLabel passLabel = new JLabel("Password:");
		passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		fgbc.gridx = 0;
		fgbc.gridy = 1;
		fgbc.weightx = 0;
		formPanel.add(passLabel, fgbc);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		passwordField.setPreferredSize(new Dimension(250, 35));
		passwordField.setMinimumSize(new Dimension(200, 35));
		fgbc.gridx = 1;
		fgbc.gridy = 1;
		fgbc.weightx = 1.0;
		formPanel.add(passwordField, fgbc);

		JButton loginBtn = new JButton("Login");
		styleButton(loginBtn, new Color(46, 204, 113)); // green button
		fgbc.gridx = 0;
		fgbc.gridy = 2;
		fgbc.gridwidth = 2;
		fgbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(loginBtn, fgbc);

		formPanel.setMaximumSize(new Dimension(400, 200));
		root.add(formPanel);
		root.add(Box.createRigidArea(new Dimension(0, 20))); // spacing

		// === Bottom New User button ===
		JButton newUserBtn = new JButton("New User? Create Account");
		styleButton(newUserBtn, new Color(241, 196, 15)); // gold button for accent
		newUserBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		root.add(newUserBtn);

		// === Actions ===
		loginBtn.addActionListener(e -> login());
		newUserBtn.addActionListener(e -> new NewUserFrame().setVisible(true));
	}

	private void login() {
		String username = usernameField.getText().trim();
		String password = new String(passwordField.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			UserDAO userDAO = new UserDAO();
			User user = userDAO.findByUsername(username);

			if (user != null && userDAO.verifyPassword(password, user.getPassword())) {
				JOptionPane.showMessageDialog(this, "Welcome " + user.getUsername() + "!", "Login Successful",
						JOptionPane.INFORMATION_MESSAGE);

				new QuizSelectionFrame(user).setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void styleButton(JButton button, Color bgColor) {
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
