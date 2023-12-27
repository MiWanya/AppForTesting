package com.example.tests;

public class QuestionItem {
    private int questionNumber;
    private int textColor;  // Добавляем новое поле для цвета текста

    public QuestionItem(int questionNumber, int textColor) {
        this.questionNumber = questionNumber;
        this.textColor = textColor;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}

