package com.quizapp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.quizapp.dao.QuizDAO;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;

public class QuizSelectionFrame extends JFrame {
	private final User user;
	private final QuizDAO quizDAO = new QuizDAO();

	private JList<String> quizList = new JList<>();
	private DefaultListModel<String> quizModel = new DefaultListModel<>();
	private List<Quiz> quizzes;

	public QuizSelectionFrame(User user) {
		this.user = user;

		setTitle("Select a Quiz");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Prevent maximize
		setResizable(false);

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panel.setBackground(new Color(245, 245, 245));

		JLabel lblTitle = new JLabel("Available Quizzes");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(new Color(52, 73, 94));
		panel.add(lblTitle, BorderLayout.NORTH);

		quizList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		quizList.setBackground(Color.WHITE);
		quizList.setModel(quizModel);

		JScrollPane scroll = new JScrollPane(quizList);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		panel.add(scroll, BorderLayout.CENTER);

		JButton btnStart = new JButton("Start Quiz");
		btnStart.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnStart.setBackground(new Color(46, 204, 113));
		btnStart.setForeground(Color.WHITE);
		btnStart.setFocusPainted(false);

		btnStart.addActionListener(this::handleStartQuiz);

		panel.add(btnStart, BorderLayout.SOUTH);

		getContentPane().add(panel);

		loadQuizzes();

		setVisible(true);
	}

	private void loadQuizzes() {
		try {
			quizzes = quizDAO.findAllActiveQuizzes();
			quizModel.clear();
			for (Quiz q : quizzes) {
				quizModel.addElement(q.getTitle() + " (" + q.getTotalMarks() + " marks)");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to load quizzes: " + ex.getMessage());
		}
	}

	private void handleStartQuiz(ActionEvent e) {
		int index = quizList.getSelectedIndex();
		if (index == -1) {
			JOptionPane.showMessageDialog(this, "Please select a quiz first.");
			return;
		}

		try {
			Quiz selectedQuiz = quizDAO.findActiveQuizById(quizzes.get(index).getId());
			if (selectedQuiz == null) {
				JOptionPane.showMessageDialog(this, "Quiz is not available anymore.");
				return;
			}

			new QuizFrame(user, selectedQuiz).setVisible(true);
			dispose(); // close selection screen
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error starting quiz: " + ex.getMessage());
		}
	}
}
