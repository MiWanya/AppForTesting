package com.example.tests;

import android.widget.TextView;

import java.util.List;

public class Question {
    private int id;
    private String questionText;
    private List<String> options;
    private List<String> correctAnswer;
    private QuestionType questionType;

    //Добавить вопрос
    public Question(String questionText, QuestionType questionType, List<String> options, List<String> correctAnswer) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
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

    // Получить вариант ответов
    public String getOption(int index) {
        // Проверка, чтобы индекс не выходил за пределы списка вариантов
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        } else {
            // Обработка ошибки, например, возврат null или пустой строки
            return "";
        }
    }


    // Установить варианты ответов
    public void setOptions (List<String> options){ //Задать варианты ответов
        this.options = options;
    }

    // Получить правильный ответ
    public List<String> getCorrectAnswer(){ //Получить правильный ответ
        return correctAnswer;
    }

    //Установить правильный ответ
    public void setCorrectAnswer(List<String> correctAnswer){ //Задать правильный ответ
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "UserAnswer{" +
                ", selectedOptions=" + correctAnswer +
                '}';
    }

}

enum QuestionType { //Типы вопросов
    SINGLE_CHOICE,
    MULTIPLE_CHOICE;
}
