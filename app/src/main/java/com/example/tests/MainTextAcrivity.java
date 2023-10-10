package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainTextAcrivity extends AppCompatActivity {

    int CurrentQuestion = 0;

    // Метод для получения уникального варианта ответа
    private String getUniqueOption(List<String> options, List<Boolean> usedOptions, Random random) {
        int index;
        do {
            index = random.nextInt(options.size());
        } while (usedOptions.get(index));

        // Помечаем вариант ответа как использованный
        usedOptions.set(index, true);

        return options.get(index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        // Получаем InputStream и инициализируем QuestionManager внутри onCreate
        InputStream inputStream;
        try {
            inputStream = getAssets().open("questions.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Создаём массив вопросов
        QuestionManager questionManager = new QuestionManager(inputStream);
        List<Question> questions = questionManager.getQuestions();

        // Находим TextView в макете
        TextView questionTextView = findViewById(R.id.QuestionText);
        TextView optionTextView = findViewById(R.id.optionText);

        // Находим RelativeLayout с идентификатором "Answers" в макете
       // RelativeLayout answersLayout = findViewById(R.id.Answers);

        // Получаем первый вопрос (предположим, что он первый в списке)
        Question question = questions.get(CurrentQuestion);

        // Получаем список вариантов ответов.
        List<String> options = question.getOptions();

        // Получаем текст вопроса и устанавливаем его в TextView
        String questionText = question.GetQuestionText();
        questionTextView.setText(questionText);
        List<String> optionText = question.getOptions();

        // Создайте строку с текстом, который вы хотите добавить
        StringBuilder addTextInOption = new StringBuilder();

        for (int i=0; i<=3; i++){
            if (i!=3){
                addTextInOption.append((i+1) + ") ").append(optionText.get(i)).append("\n");
            }
            else {
                addTextInOption.append((i+1) + ") ").append(optionText.get(i));
            }
        }

        optionTextView.setText(addTextInOption);

        // Создаем список для отслеживания использованных вариантов ответов
        List<Boolean> usedOptions = new ArrayList<>(Collections.nCopies(options.size(), false));

        Random random = new Random();

        // Названия кнопок
        Button answer1 = findViewById(R.id.Answer1);
        Button answer2 = findViewById(R.id.Answer2);
        Button answer3 = findViewById(R.id.Answer3);
        Button answer4 = findViewById(R.id.Answer4);
        answer1.setText(getUniqueOption(options, usedOptions, random));
        answer2.setText(getUniqueOption(options, usedOptions, random));
        answer3.setText(getUniqueOption(options, usedOptions, random));
        answer4.setText(getUniqueOption(options, usedOptions, random));
    }



}