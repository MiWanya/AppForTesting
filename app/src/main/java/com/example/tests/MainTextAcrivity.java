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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        // Получаем InputStream и инициализируем QuestionManager внутри onCreate.
        InputStream inputStream;
        try {
            inputStream = getAssets().open("questions.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        QuestionManager questionManager = new QuestionManager(inputStream);
        List<Question> questions = questionManager.getQuestions();

        // Находим TextView в макете.
        TextView textView = findViewById(R.id.QuestionText);

        // Получаем первый вопрос (предположим, что он первый в списке).
        Question question = questions.get(0);

        // Получаем текст вопроса и устанавливаем его в TextView.
        String questionText = question.GetQuestionText();
        textView.setText(questionText);
    }
}