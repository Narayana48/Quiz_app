package com.quizapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.quizapp.db.DB;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;

public class QuizDAO {

	// ✅ Fetch all active quizzes (used in QuizSelectionFrame)
	public List<Quiz> findAllActiveQuizzes() throws Exception {
		String query = "SELECT id, title, description, duration_sec, total_marks, is_active "
				+ "FROM quizzes WHERE is_active=1";
		List<Quiz> quizzes = new ArrayList<>();

		try (Connection con = DB.get();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Quiz quiz = new Quiz(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration_sec"), rs.getInt("total_marks"), rs.getBoolean("is_active"));
				quizzes.add(quiz);
			}
		}
		return quizzes;
	}

	// ✅ Fetch a quiz by ID and load shuffled questions
	public Quiz findActiveQuizById(int quizId) throws Exception {
		String query = "SELECT id, title, description, duration_sec, total_marks, is_active "
				+ "FROM quizzes WHERE id=? AND is_active=1";

		try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, quizId);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;

				Quiz quiz = new Quiz(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration_sec"), rs.getInt("total_marks"), rs.getBoolean("is_active"));

				// Load and shuffle questions
				quiz.setQuestions(loadQuestions(quiz.getId()));
				return quiz;
			}
		}
	}

	// ✅ Load and shuffle questions for a quiz
	public List<Question> loadQuestions(int quizId) throws Exception {
		String query = "SELECT id, text, option_a, option_b, option_c, option_d, correct_opt, marks "
				+ "FROM questions WHERE quiz_id=?";
		List<Question> questions = new ArrayList<>();

		try (Connection con = DB.get(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, quizId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Question q = new Question(rs.getInt("id"), quizId, rs.getString("text"), rs.getString("option_a"),
							rs.getString("option_b"), rs.getString("option_c"), rs.getString("option_d"),
							rs.getString("correct_opt").charAt(0), rs.getInt("marks"));
					questions.add(q);
				}
			}
		}

		// Shuffle for each attempt so user gets random order
		Collections.shuffle(questions, new Random());
		return questions;
	}
}
