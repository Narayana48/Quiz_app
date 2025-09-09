package com.quizapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.quizapp.db.DB;
import com.quizapp.model.Question;

public class AttemptDAO {

	// Start a new attempt
	public long startAttempt(int userId, int quizId) throws Exception {
		String q = "INSERT INTO attempts (user_id, quiz_id, score, attempt_time) VALUES (?, ?, 0, NOW())";
		try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, userId);
			ps.setInt(2, quizId);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					return rs.getLong(1);
			}
		}
		throw new Exception("Failed to start attempt");
	}

	// Save/update answer for a question
	public void saveAnswer(long attemptId, Question q, Character selectedOpt) throws Exception {
		String sql = """
				INSERT INTO attempt_answers (attempt_id, question_id, chosen_opt, selected_opt, is_correct, awarded_marks)
				VALUES (?, ?, ?, ?, ?, ?)
				ON DUPLICATE KEY UPDATE
				    chosen_opt=VALUES(chosen_opt),
				    selected_opt=VALUES(selected_opt),
				    is_correct=VALUES(is_correct),
				    awarded_marks=VALUES(awarded_marks)
				""";

		boolean isCorrect = (selectedOpt != null && selectedOpt == q.getCorrectOption());
		int marks = isCorrect ? q.getMarks() : 0;

		try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, attemptId);
			ps.setInt(2, q.getId());
			ps.setString(3, selectedOpt == null ? null : selectedOpt.toString());
			ps.setString(4, selectedOpt == null ? "-" : selectedOpt.toString());
			ps.setBoolean(5, isCorrect);
			ps.setInt(6, marks);
			ps.executeUpdate();
		}
	}

	// Finalize attempt and set total marks
	public void finalizeAttempt(long attemptId, int totalMarks) throws Exception {
		String sql = "UPDATE attempts SET finished_at=NOW(), score=? WHERE id=?";
		try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, totalMarks);
			ps.setLong(2, attemptId);
			ps.executeUpdate();
		}
	}
}
