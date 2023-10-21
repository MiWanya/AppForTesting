package com.example.tests;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionManagerHelper {
    private Context context;
    private List<Question> questions;
    private int currentQuestion;
    private TextView questionTextView;
    private TextView optionTextView;
    private List<Boolean> usedOptions;

    public QuestionManagerHelper(Context context, int currentQuestion, TextView questionTextView, TextView optionTextView) {
        this.context = context;
        this.currentQuestion = currentQuestion;
        this.questionTextView = questionTextView;
        this.optionTextView = optionTextView;
    }

    public void initialize() {
        try {
            InputStream inputStream = context.getAssets().open("questions1.txt");
            QuestionManager questionManager = new QuestionManager(inputStream);
            questions = questionManager.getQuestions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usedOptions = new ArrayList<>(Collections.nCopies(4, false));
    }

    public void loadQuestion() {
        Log.d("QuestionManagerHelper", "Loading question: " + currentQuestion);
        if (currentQuestion >= 0 && currentQuestion < questions.size()) {
            Question question = questions.get(currentQuestion);
            List<String> options = question.getOptions();

            String questionText = question.GetQuestionText();
            questionTextView.setText(questionText);

            StringBuilder addTextInOption = new StringBuilder();

            for (int i = 0; i < options.size(); i++) {
                addTextInOption.append((i + 1) + ") " + options.get(i));
                if (i < options.size() - 1) {
                    addTextInOption.append("\n");
                }
            }

            Log.d("QuestionManagerHelper", "Options: " + addTextInOption.toString()); // Добавьте этот вывод
            optionTextView.setText(addTextInOption.toString());

        }
    }

    public int getQuestionsCount() {
        return questions.size();
    }

    public void setCurrentQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < questions.size()) {
            currentQuestion = questionIndex;
        }
    }
    }