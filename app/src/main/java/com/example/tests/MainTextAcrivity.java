package com.example.tests;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private Button lastClickedButton; // Последняя нажатая кнопка
    Button PreviousQuestion, NextQuestion, Answer1, Answer2, Answer3, Answer4;

    int colorGray2, colorBlue, colorGold;
    int currentQuestionIndex = 1; // Индекс текущего вопроса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        // Получаем цвета из ресурсов
        colorGray2 = ContextCompat.getColor(this, R.color.Gray2);
        colorBlue = ContextCompat.getColor(this, R.color.Blue);
        colorGold = ContextCompat.getColor(this, R.color.Gold);

        // Получаем текстовые поля
        TextView questionTextView = findViewById(R.id.QuestionText);
        TextView optionTextView = findViewById(R.id.optionText);

        // Получаем кнопки
        PreviousQuestion = findViewById(R.id.PreviosQuestion);
        NextQuestion = findViewById(R.id.NextQuestion);
        Answer1 = findViewById(R.id.Answer1);
        Answer2 = findViewById(R.id.Answer2);
        Answer3 = findViewById(R.id.Answer3);
        Answer4 = findViewById(R.id.Answer4);

        // Устанавливаем цвет
        PreviousQuestion.setBackgroundColor(colorGray2);
        NextQuestion.setBackgroundColor(colorBlue);

        // Создаем список вопросов
        List<Question> questionsList = new ArrayList<>();

        // Создайте диалоговое окно с вопросом
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Завершить тестирование?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Здесь можно разместить код для завершения тестирования
                finish();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        try {
            // Открываем файл в папке assets
            InputStream inputStream = getAssets().open("questions.txt");

            // Создаем InputStreamReader для чтения текста из InputStream
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Переменные для хранения вопросов, ответов и правильных ответов
            String question = "";
            List<String> options = new ArrayList<>();
            List<String> correctAnswers = new ArrayList<>();
            QuestionType questionType = QuestionType.SINGLE_CHOICE;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("$ ")) {
                    // Это строка с текстом вопроса
                    question = line.substring(2);
                } else if (line.startsWith("^ ")) {
                    // Это строка с вариантами ответов и правильными ответами
                    String content = line.substring(2);
                    String[] parts = content.split(",");

                    if (parts.length > 1) {
                        questionType = QuestionType.MULTIPLE_CHOICE;
                    }

                    List<String> questionOptions = new ArrayList<>();
                    List<String> questionCorrectAnswers = new ArrayList<>();

                    for (String part : parts) {
                        if (part.startsWith("*")) {
                            questionCorrectAnswers.add(part.substring(1));
                        } else {
                            questionOptions.add(part);
                        }
                    }

                    options.addAll(questionOptions);

                    correctAnswers.addAll(questionCorrectAnswers);
                } else if (line.isEmpty()) {
                    // Пустая строка обозначает конец вопроса
                    Question question1 = new Question(question, questionType, new ArrayList<>(options), new ArrayList<>(correctAnswers));
                    questionsList.add(question1);

                    // Очищаем переменные для следующего вопроса
                    question = "";
                    options.clear();
                    correctAnswers.clear();
                    questionType = QuestionType.SINGLE_CHOICE;
                }
            }

            // Закрываем потоки
            bufferedReader.close();
            reader.close();
            inputStream.close();

            // Теперь у вас есть список вопросов questionsList, который вы можете использовать в вашем приложении.
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!questionsList.isEmpty()) {
            if (currentQuestionIndex < questionsList.size()) {
                Log.d("Questions", "Number of questions: " + questionsList.size());
                Question question = questionsList.get(currentQuestionIndex);
                questionTextView.setText(question.GetQuestionText());

                // Создаем строку для вариантов ответов
                StringBuilder optionsText = new StringBuilder();
                List<String> options = question.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    optionsText.append((i + 1) + ". " + options.get(i)); // Нумерация вариантов ответов
                    if (i < options.size() - 1) {
                        optionsText.append("\n"); // Перенос строки между вариантами
                    }
                }
                // Устанавливаем текст вариантов ответов
                optionTextView.setText(optionsText.toString());
            } else {
                // Покажите диалоговое окно
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        PreviousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Возврат к предыдущему вопросу
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--; // Возвращаемся к предыдущему вопросу

                    Question question = questionsList.get(currentQuestionIndex);
                    questionTextView.setText(question.GetQuestionText());

                    StringBuilder optionsText = new StringBuilder();
                    List<String> options = question.getOptions();
                    for (int i = 0; i < options.size(); i++) {
                        optionsText.append((i + 1) + ". " + options.get(i));
                        if (i < options.size() - 1) {
                            optionsText.append("\n");
                        }
                    }
                    optionTextView.setText(optionsText.toString());
                }
                // Если есть ещё вопросы для кнопки Продолжить
                if (currentQuestionIndex < questionsList.size()){
                    NextQuestion.setBackgroundColor(colorBlue);
                }
                // Первый вопрос
                if (currentQuestionIndex == 1){
                    PreviousQuestion.setBackgroundColor(colorGray2);
                }
                // Возврат назад с первого вопроса
                if (currentQuestionIndex == 0){
                    Intent intent = new Intent(MainTextAcrivity.this, MainActivity.class);
                    startActivity(intent);
                }

                setDefaultColors(colorBlue);
            }
        });

        NextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход к следующему вопросу
                if (currentQuestionIndex < questionsList.size()) {
                    currentQuestionIndex++; // Переключаемся на следующий вопрос

                    if (currentQuestionIndex < questionsList.size()) {
                        Question question = questionsList.get(currentQuestionIndex);
                        questionTextView.setText(question.GetQuestionText());

                        StringBuilder optionsText = new StringBuilder();
                        List<String> options = question.getOptions();
                        for (int i = 0; i < options.size(); i++) {
                            optionsText.append((i + 1) + ". " + options.get(i));
                            if (i < options.size() - 1) {
                                optionsText.append("\n");
                            }
                        }
                        optionTextView.setText(optionsText.toString());
                    } else {
                        // Покажите диалоговое окно
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                // Последний вопрос
                if (currentQuestionIndex == questionsList.size() - 1){
                    NextQuestion.setBackgroundColor(colorGold);
                }
                // Стандартный цвет кнопок
                if (currentQuestionIndex > 0) {
                    PreviousQuestion.setBackgroundColor(colorBlue);
                }
                setDefaultColors(colorBlue);
            }
        });

        Answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColorSingle((Button) view);
            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeButtonColorSingle((Button) view);
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonColorSingle((Button) view);
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeButtonColorSingle((Button) view);
            }
        });
    }
        private void changeButtonColorSingle (Button button) {
        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundColor(colorBlue);
        }

        // Устанавливаем новую последнюю нажатую кнопку
        lastClickedButton = button;

        // Устанавливаем новый цвет для текущей кнопки
        button.setBackgroundColor(colorGold);
    }

        private void changeButtonColorMulti (Button button) {
            if (button.getDrawingCacheBackgroundColor() == colorBlue) {
                button.setBackgroundColor(colorGold);
            } else {
                button.setBackgroundColor(colorBlue);
            }
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