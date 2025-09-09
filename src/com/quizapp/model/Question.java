package com.quizapp.model;

public class Question {
	private int id;
	private int quizId;
	private String text;
	private String optionA, optionB, optionC, optionD;
	private char correctOption; // 'A', 'B', 'C', 'D'
	private int marks;

	// Field to store user's selected answer
	private Character userAnswer;

	// ✅ No-arg constructor (useful for frameworks like Hibernate/JDBC)
	public Question() {
	}

	// ✅ Parameterized constructor (matches QuizDAO.loadQuestions())
	public Question(int id, int quizId, String text, String optionA, String optionB, String optionC, String optionD,
			char correctOption, int marks) {
		this.id = id;
		this.quizId = quizId;
		this.text = text;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.correctOption = correctOption;
		this.marks = marks;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public char getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(char correctOption) {
		this.correctOption = correctOption;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	public Character getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(Character userAnswer) {
		this.userAnswer = userAnswer;
	}

	public Character getUserAnswer(Character userAnswer) {
		return userAnswer;
	}
}
