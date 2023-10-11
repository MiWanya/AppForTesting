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
            String questionText = null;
            List<String> options = new ArrayList<>();
            String correctAnswer = null;
            QuestionType questionType = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Вопрос: ")) {
                    // Найден новый вопрос, сохраняем предыдущий, если есть
                    if (questionText != null && options.size() > 0 && correctAnswer != null && questionType != null) {
                        Question question = new Question(questionText, questionType, options, correctAnswer);
                        questions.add(question);
                        options.clear();
                    }
                    questionText = line.substring(8).trim();
                    questionType = QuestionType.SINGLE_CHOICE; // Здесь вы можете указать тип вопроса, который вам нужен
                } else if (line.startsWith("Ответы: ")) {
                    String[] answers = line.substring(8).trim().split(", ");
                    for (String answer : answers) {
                        options.add(answer);
                    }
                } else if (line.startsWith("Правильный ответ: ")) {
                    correctAnswer = line.substring(18).trim();
                }
            }

            // Добавляем последний вопрос
            if (questionText != null && options.size() > 0 && correctAnswer != null && questionType != null) {
                Question question = new Question(questionText, questionType, options, correctAnswer);
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
