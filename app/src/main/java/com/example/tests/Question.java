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

    public String GetQuestionText(){ //Получить вопрос
        return questionText;
    }

    public void setQuestionText(String questionText){ //Задать вопрос
        this.questionText = questionText;
    }

    public QuestionType getQuestionType(){ //Получит тип вопроса
        return questionType;
    }

    public void setQuestionType(QuestionType questionType){ //Задать тип вопроса
        this.questionType = questionType;
    }

    public List<String> getOptions(){ //Получить варианты ответов
        return options;
    }

    public void setOptions (List<String> options){ //Задать варианты ответов
        this.options = options;
    }

    public String getCorrectAnswer(){ //Получить правильный ответ
        return correctAnswer;
    }

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
