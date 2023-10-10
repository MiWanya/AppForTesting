package com.example.tests;

import android.widget.TextView;

import java.util.List;

public class Question {
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private QuestionType questionType;

    //Добавить вопрос
    public Question(String questionText, QuestionType questionType, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    //Получить текст вопроса
    public String GetQuestionText(){ //Получить вопрос
        return questionText;
    }

    // Установить текст вопроса
    public void setQuestionText(String questionText){ //Задать вопрос
        this.questionText = questionText;
    }

    // Получить тип вопроса
    public QuestionType getQuestionType(){ //Получит тип вопроса
        return questionType;
    }

    // Установить тип вопроса
    public void setQuestionType(QuestionType questionType){ //Задать тип вопроса
        this.questionType = questionType;
    }

    // Получить варианты ответов
    public List<String> getOptions(){ //Получить варианты ответов
        return options;
    }

    // Установить варианты ответов
    public void setOptions (List<String> options){ //Задать варианты ответов
        this.options = options;
    }

    // Получить правильный ответ
    public String getCorrectAnswer(){ //Получить правильный ответ
        return correctAnswer;
    }

    //Установить правильный ответ
    public void setCorrectAnswer(String correctAnswer){ //Задать правильный ответ
        this.correctAnswer = correctAnswer;
    }

}

enum QuestionType { //Типы вопросов
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    TRUE_OR_FALSE,
    TYPE_ANSWER;
}
