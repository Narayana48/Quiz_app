package com.quizapp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.quizapp.dao.AttemptDAO;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;

public class QuizFrame extends JFrame {
	private final Quiz quiz;
	private final List<Question> questions;
	private final AttemptDAO attemptDAO = new AttemptDAO();
	private long attemptId;
	private int index = 0;
	private int remaining; // seconds
	private Timer timer;

	private JLabel lblTitle = new JLabel();
	private JLabel lblTimer = new JLabel();
	private JTextArea txtQuestion = new JTextArea(3, 40);
	private JRadioButton optA = new JRadioButton();
	private JRadioButton optB = new JRadioButton();
	private JRadioButton optC = new JRadioButton();
	private JRadioButton optD = new JRadioButton();
	private ButtonGroup group = new ButtonGroup();
	private JButton btnPrev = new JButton(" Previous");
	private JButton btnNext = new JButton("Next ");
	private JButton btnSubmit = new JButton("Submit");

	private Map<Integer, Character> userAnswers = new HashMap<>();

	public QuizFrame(User user, Quiz quiz) throws Exception {
		this.quiz = quiz;
		this.questions = quiz.getQuestions();
		this.remaining = quiz.getDurationSec();

		setTitle("Quiz: " + quiz.getTitle());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 550);
		setLocationRelativeTo(null);

		// Background panel with gradient
		JPanel backgroundPanel = new JPanel(new BorderLayout(15, 15)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				GradientPaint gp = new GradientPaint(0, 0, new Color(58, 123, 213), // top blue
						0, getHeight(), new Color(58, 213, 178)); // bottom teal
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		backgroundPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Top bar
		JPanel top = new JPanel(new BorderLayout());
		top.setOpaque(false);

		lblTitle.setText(quiz.getTitle() + " (" + quiz.getTotalMarks() + " marks)");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(Color.WHITE);

		lblTimer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTimer.setForeground(new Color(255, 230, 0));

		top.add(lblTitle, BorderLayout.WEST);
		top.add(lblTimer, BorderLayout.EAST);

		// Question card
		JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
		questionPanel.setBackground(Color.WHITE);
		questionPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
						BorderFactory.createEmptyBorder(12, 12, 12, 12)));

		txtQuestion.setLineWrap(true);
		txtQuestion.setWrapStyleWord(true);
		txtQuestion.setEditable(false);
		txtQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		txtQuestion.setBackground(Color.WHITE);

		JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
		optionsPanel.setBackground(Color.WHITE);

		styleRadio(optA);
		styleRadio(optB);
		styleRadio(optC);
		styleRadio(optD);

		optionsPanel.add(optA);
		optionsPanel.add(optB);
		optionsPanel.add(optC);
		optionsPanel.add(optD);

		group.add(optA);
		group.add(optB);
		group.add(optC);
		group.add(optD);

		questionPanel.add(new JScrollPane(txtQuestion), BorderLayout.NORTH);
		questionPanel.add(optionsPanel, BorderLayout.CENTER);

		// Bottom buttons
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
		bottom.setOpaque(false);

		styleButton(btnPrev, new Color(52, 152, 219));
		styleButton(btnNext, new Color(46, 204, 113));
		styleButton(btnSubmit, new Color(231, 76, 60));

		bottom.add(btnPrev);
		bottom.add(btnNext);
		bottom.add(btnSubmit);

		backgroundPanel.add(top, BorderLayout.NORTH);
		backgroundPanel.add(questionPanel, BorderLayout.CENTER);
		backgroundPanel.add(bottom, BorderLayout.SOUTH);

		getContentPane().add(backgroundPanel);

		// Start attempt
		this.attemptId = attemptDAO.startAttempt(user.getId(), quiz.getId());

		// Timer
		timer = new Timer(1000, e -> {
			remaining--;
			lblTimer.setText("Time Left :- " + formatTime(remaining));
			if (remaining <= 0) {
				submitAndShowResult(user, false);
			}
		});
		timer.start();

		// Button actions
		btnPrev.addActionListener(ae -> saveAndPrev());
		btnNext.addActionListener(ae -> saveAndNext());
		btnSubmit.addActionListener(ae -> submitAndShowResult(user, true));

		loadQuestion();
		setVisible(true);
	}

	private void styleRadio(JRadioButton radio) {
		radio.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		radio.setForeground(new Color(44, 62, 80));
		radio.setBackground(Color.WHITE);
	}

	private void styleButton(JButton btn, Color bg) {
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
	}

	private void loadQuestion() {
		if (index >= questions.size()) {
			btnNext.setEnabled(false);
			return;
		}
		Question q = questions.get(index);
		txtQuestion.setText("Q" + (index + 1) + ". " + q.getText() + "   [" + q.getMarks() + " marks]");
		optA.setText("A) " + q.getOptionA());
		optB.setText("B) " + q.getOptionB());
		optC.setText("C) " + q.getOptionC());
		optD.setText("D) " + q.getOptionD());

		group.clearSelection();
		Character prev = userAnswers.get(q.getId());
		if (prev != null) {
			switch (prev) {
			case 'A' -> optA.setSelected(true);
			case 'B' -> optB.setSelected(true);
			case 'C' -> optC.setSelected(true);
			case 'D' -> optD.setSelected(true);
			}
		}

		btnPrev.setEnabled(index > 0);
		btnNext.setEnabled(index < questions.size() - 1);
	}

	private Character getSelection() {
		if (optA.isSelected())
			return 'A';
		if (optB.isSelected())
			return 'B';
		if (optC.isSelected())
			return 'C';
		if (optD.isSelected())
			return 'D';
		return null;
	}

	private void saveCurrentAnswer() {
		try {
			Question q = questions.get(index);
			Character sel = getSelection();
			userAnswers.put(q.getId(), sel);
			if (sel != null) {
				attemptDAO.saveAnswer(attemptId, q, sel);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	private void saveAndNext() {
		saveCurrentAnswer();
		if (index < questions.size() - 1) {
			index++;
			loadQuestion();
		}
	}

	private void saveAndPrev() {
		saveCurrentAnswer();
		if (index > 0) {
			index--;
			loadQuestion();
		}
	}

	private void submitAndShowResult(User user, boolean askConfirm) {
		if (askConfirm && timer.isRunning()) {
			int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit the quiz?",
					"Confirm Submission", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (choice != JOptionPane.YES_OPTION)
				return;
		}

		timer.stop();
		saveCurrentAnswer();

		try {
			int totalScore = 0;
			int maxScore = 0;
			for (Question q : questions) {
				maxScore += q.getMarks();
				Character answer = userAnswers.get(q.getId());
				if (answer != null && answer == q.getCorrectOption()) {
					totalScore += q.getMarks();
				}
			}

			attemptDAO.finalizeAttempt(attemptId, totalScore);
			new ResultFrame(user, quiz, attemptId, totalScore).setVisible(true);
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	private String formatTime(int sec) {
		int m = sec / 60;
		int s = sec % 60;
		return String.format("%02d:%02d", m, s);
	}
}
