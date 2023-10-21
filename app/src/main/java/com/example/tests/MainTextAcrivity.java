package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    Random random = new Random();
    QuestionManagerHelper questionManagerHelper;
    private Button lastClickedButton; // Последняя нажатая кнопка
    Button PreviousQuestion;
    Button NextQuestion;
    Button Answer1;
    Button Answer2;
    Button Answer3;
    Button Answer4;

    int colorGray2;
    int colorBlue;
    int colorGold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        // Получаем цвета из ресурсов
        colorGray2 = ContextCompat.getColor(this, R.color.Gray2);
        colorBlue = ContextCompat.getColor(this, R.color.Blue);
        colorGold = ContextCompat.getColor(this, R.color.Gold);

        TextView questionTextView = findViewById(R.id.QuestionText);
        TextView optionTextView = findViewById(R.id.optionText);

        PreviousQuestion = findViewById(R.id.PreviosQuestion);
        NextQuestion = findViewById(R.id.NextQuestion);
        Answer1 = findViewById(R.id.Answer1);
        Answer2 = findViewById(R.id.Answer2);
        Answer3 = findViewById(R.id.Answer3);
        Answer4 = findViewById(R.id.Answer4);

        PreviousQuestion.setBackgroundColor(colorGray2);
        NextQuestion.setBackgroundColor(colorBlue);

        questionManagerHelper = new QuestionManagerHelper(this, CurrentQuestion, questionTextView, optionTextView);
        questionManagerHelper.initialize();
        questionManagerHelper.loadQuestion();

        PreviousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalQuestions = questionManagerHelper.getQuestionsCount();
                if (CurrentQuestion > 0) {
                    PreviousQuestion.setBackgroundColor(colorBlue);
                    CurrentQuestion -= 1;
                    questionManagerHelper.setCurrentQuestion(CurrentQuestion);
                    questionManagerHelper.loadQuestion();
                }
                if (CurrentQuestion < totalQuestions){
                    NextQuestion.setBackgroundColor(colorBlue);
                }
                if (CurrentQuestion == 0){
                    PreviousQuestion.setBackgroundColor(colorGray2);
                }
                if (CurrentQuestion == totalQuestions) {
                    NextQuestion.setBackgroundColor(colorGray2);
                }

                setDefaultColors(colorBlue);
            }
        });

        NextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalQuestions = questionManagerHelper.getQuestionsCount();
                if (CurrentQuestion < totalQuestions - 1) {
                    NextQuestion.setBackgroundColor(colorBlue);
                    CurrentQuestion += 1;
                    questionManagerHelper.setCurrentQuestion(CurrentQuestion);
                    questionManagerHelper.loadQuestion();
                }
                if (CurrentQuestion == totalQuestions - 1){
                    NextQuestion.setBackgroundColor(colorGray2);
                }
                if (CurrentQuestion > 0) {
                    PreviousQuestion.setBackgroundColor(colorBlue);
                }
                setDefaultColors(colorBlue);
            }
        });

        Answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor((Button) view);

            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor((Button) view);
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor((Button) view);
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColor((Button) view);
            }
        });
    }

    private void changeButtonColor (Button button) {
        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundColor(colorBlue);
        }

        // Устанавливаем новую последнюю нажатую кнопку
        lastClickedButton = button;

        // Устанавливаем новый цвет для текущей кнопки
        button.setBackgroundColor(colorGold);
    }

    private void setDefaultColors(int color){
        Answer1.setBackgroundColor(color);
        Answer2.setBackgroundColor(color);
        Answer3.setBackgroundColor(color);
        Answer4.setBackgroundColor(color);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}