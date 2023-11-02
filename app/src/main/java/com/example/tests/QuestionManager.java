package com.example.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class QuestionManager {
    private List<Question> questions;

    public QuestionManager(InputStream inputStream) {
        questions = new ArrayList<>();
        loadQuestionsFromStream(inputStream);
    }

    private void loadQuestionsFromStream(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int questionCount = 0;
            String questionText = null;
            List<String> options = new ArrayList<>();
            List<String> correctAnswers = new ArrayList<>();
            QuestionType questionType = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("# ")) {
                    // Найдено новое количество вопросов, сохраняем предыдущий вопрос, если есть
                    if (questionText != null) {
                        questionCount = Integer.parseInt(line.substring(2).trim());
                        if (correctAnswers.size() == 1) {
                            questionType = QuestionType.SINGLE_CHOICE;
                        } else if (correctAnswers.size() > 1) {
                            questionType = QuestionType.MULTIPLE_CHOICE;
                        }
                        Question question = new Question(questionText, questionType, options, correctAnswers);
                        questions.add(question);
                        options.clear();
                        correctAnswers.clear();
                    }
                } else if (line.startsWith("$ ")) {
                    questionText = line.substring(2).trim();
                } else if (line.startsWith("^ ")) {
                    String[] answers = line.substring(2).trim().split(", ");
                    for (String answer : answers) {
                        options.add(answer);
                    }
                } else if (line.startsWith("* ")) {
                    String[] correctAnswer = line.substring(2).trim().split(", ");
                    for (String correct : correctAnswer) {
                        correctAnswers.add(correct);
                    }
                }
            }

            // Добавляем последний вопрос
            if (questionText != null) {
                if (correctAnswers.size() == 1) {
                    questionType = QuestionType.SINGLE_CHOICE;
                } else if (correctAnswers.size() > 1) {
                    questionType = QuestionType.MULTIPLE_CHOICE;
                }
                Question question = new Question(questionText, questionType, options, correctAnswers);
                questions.add(question);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public int getCount(List<Question> questions) {return questions.size();}
}
