package com.quizapp.model;

import java.util.List;

public class Quiz {
	private int id;
	private String title;
	private String description;
	private int durationSec;
	private int totalMarks;
	private boolean active;
	private List<Question> questions;

	public Quiz(int id, String title, String description, int durationSec, int totalMarks, boolean active) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.durationSec = durationSec;
		this.totalMarks = totalMarks;
		this.active = active;
	}

	// Getters & setters
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public int getDurationSec() {
		return durationSec;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public boolean isActive() {
		return active;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
