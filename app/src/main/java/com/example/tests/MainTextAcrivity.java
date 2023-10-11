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
    int CurrentQuestion = 1;
    Random random = new Random();
    QuestionManagerHelper questionManagerHelper;
    TextView questionTextView;
    TextView optionTextView;
    Button PreviousQuestion;
    Button NextQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        int colorGray2 = ContextCompat.getColor(this, R.color.Gray2);
        int colorBlue = ContextCompat.getColor(this, R.color.Blue);
        TextView questionTextView = findViewById(R.id.QuestionText);
        TextView optionTextView = findViewById(R.id.optionText);
        Button PreviousQuestion = findViewById(R.id.PreviosQuestion);
        Button NextQuestion = findViewById(R.id.NextQuestion);

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
                if (CurrentQuestion == 0){
                    PreviousQuestion.setBackgroundColor(colorGray2);
                }
                if (CurrentQuestion == totalQuestions) {
                    NextQuestion.setBackgroundColor(colorGray2);
                }
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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}