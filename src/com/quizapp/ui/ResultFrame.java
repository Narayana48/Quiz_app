package com.quizapp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;

public class ResultFrame extends JFrame {

	public ResultFrame(User user, Quiz quiz, long attemptId, int score) {
		setTitle("Quiz Result");
		setSize(650, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Main container
		JPanel container = new JPanel(new BorderLayout(15, 15));
		container.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		container.setBackground(new Color(245, 245, 245));

		// Header panel
		JPanel header = new JPanel(new GridLayout(0, 1, 5, 5));
		header.setBackground(new Color(245, 245, 245));

		JLabel lblUser = new JLabel("User: " + user.getUsername());
		JLabel lblQuiz = new JLabel("Quiz: " + quiz.getTitle());
		JLabel lblScore = new JLabel(
				"Score: " + score + "/" + quiz.getQuestions().stream().mapToInt(Question::getMarks).sum());

		lblUser.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblQuiz.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblScore.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblScore.setForeground(new Color(33, 150, 83));

		header.add(lblUser);
		header.add(lblQuiz);
		header.add(lblScore);

		// Breakdown panel
		JPanel breakdownPanel = new JPanel();
		breakdownPanel.setLayout(new BoxLayout(breakdownPanel, BoxLayout.Y_AXIS));
		breakdownPanel.setBackground(new Color(245, 245, 245));

		int qno = 1;
		for (Question q : quiz.getQuestions()) {
			JPanel card = new JPanel(new GridLayout(0, 1));
			card.setBackground(Color.WHITE);
			card.setBorder(
					BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
							BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JLabel lblQ = new JLabel("Q" + qno++ + ". " + q.getText() + " [" + q.getMarks() + " marks]");
			lblQ.setFont(new Font("SansSerif", Font.BOLD, 14));

			// Display user answer or "-"
			String userAns = (q.getUserAnswer() != null) ? q.getUserAnswer().toString() : "-";

			// Calculate marks for this question
			int obtainedMarks = (q.getUserAnswer() != null && q.getUserAnswer() == q.getCorrectOption()) ? q.getMarks()
					: 0;

			JLabel lblAns = new JLabel(
					"Correct: " + q.getCorrectOption() + "   |   Your: " + userAns + "   |   Marks: " + obtainedMarks);
			lblAns.setFont(new Font("SansSerif", Font.PLAIN, 13));
			lblAns.setForeground(new Color(70, 70, 70));

			card.add(lblQ);
			card.add(lblAns);

			breakdownPanel.add(card);
			breakdownPanel.add(Box.createVerticalStrut(8));
		}

		JScrollPane scroll = new JScrollPane(breakdownPanel);
		scroll.setBorder(null);
		scroll.getViewport().setBackground(new Color(245, 245, 245));

		// Footer with Back button
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		footer.setBackground(new Color(245, 245, 245));
		JButton btnBack = new JButton("Back to Home");
		btnBack.setFont(new Font("SansSerif", Font.PLAIN, 14));
		btnBack.setBackground(new Color(66, 133, 244));
		btnBack.setForeground(Color.WHITE);
		btnBack.setFocusPainted(false);
		btnBack.setPreferredSize(new Dimension(150, 35));

		btnBack.addActionListener(e -> {
			dispose(); // close result window
			new LoginFrame().setVisible(true); // go back to login
		});

		footer.add(btnBack);

		// Add panels
		container.add(header, BorderLayout.NORTH);
		container.add(scroll, BorderLayout.CENTER);
		container.add(footer, BorderLayout.SOUTH);

		getContentPane().add(container);
	}
}
