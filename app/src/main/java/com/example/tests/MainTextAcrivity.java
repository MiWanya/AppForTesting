package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainTextAcrivity extends AppCompatActivity {

    TextView qText = findViewById(R.id.QuestionText);
    List<Question> questionList = new ArrayList<>();
    int CurrentQuestion = 0;
    int Count = questionList.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        try {
            // Откройте или создайте файл во внутренней памяти приложения
            FileInputStream inputStream = openFileInput("questions.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            String questionText = null;
            List<String> options = new ArrayList<>();
            String correctAnswer = null;
            QuestionType questionType = null;

            while ((line = reader.readLine()) != null) {
                // Разбиваем строку на части, разделенные ": "
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];

                    if (key.equals("Вопрос")) {
                        questionText = value;
                    } else if (key.equals("Ответы")) {
                        // Разбиваем строку с вариантами ответов на отдельные варианты
                        String[] optionsArray = value.split(", ");
                        options = Arrays.asList(optionsArray);
                    } else if (key.equals("Правильный ответ")) {
                        correctAnswer = value;
                    } else if (key.equals("Тип вопроса")) {
                        questionType = QuestionType.valueOf(value);
                    }
                } else if (line.isEmpty()) {
                    // Пустая строка разделяет вопросы
                    if (questionText != null && !options.isEmpty() && correctAnswer != null && questionType != null) {
                        // Создаем объект вопроса и добавляем его в коллекцию
                        if (questionText != null && !options.isEmpty() && correctAnswer != null && questionType != null) {
                            // Создаем объект вопроса и добавляем его в коллекцию
                            Question question = new Question(questionText, questionType, options, correctAnswer);
                            questionList.add(question);
                        }
                    }

                    // Сбрасываем переменные для следующего вопроса
                    questionText = null;
                    options = new ArrayList<>();
                    correctAnswer = null;
                    questionType = null;
                }
            }

            // Закрываем потоки
            reader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте ошибку чтения файла
        }

        try {
            Question currentQuestion = questionList.get(CurrentQuestion);

            // Установите текст текущего вопроса на TextView
            qText.setText(currentQuestion.GetQuestionText());
        } catch (IndexOutOfBoundsException e) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }



}