package com.example.tests;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionManagerHelper {
    private Context context;
    private int currentQuestion;
    private TextView questionTextView;
    private TextView optionTextView;
    private List<String> questions;
    private List<List<String>> options;
    private List<List<String>> correctAnswers;
    private QuestionType questionTypes;

    public QuestionManagerHelper(Context context, int currentQuestion, TextView questionTextView, TextView optionTextView) {
        this.context = context;
        this.currentQuestion = currentQuestion;
        this.questionTextView = questionTextView;
        this.optionTextView = optionTextView;
    }

    public void initialize() {
        try {
            InputStream inputStream = context.getAssets().open("questions.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            questions = new ArrayList<>();
            options = new ArrayList<>();
            correctAnswers = new ArrayList<>();

            String line;
            List<String> questionOptions = new ArrayList<>();
            List<String> questionCorrectAnswers = new ArrayList<>();
            QuestionType questionType = null;

            while ((line = reader.readLine()) != null) {

                    if (line.startsWith("# ")) {
                        if (!questionOptions.isEmpty()) {
                            questions.add(questionOptions.get(0));
                            options.add(new ArrayList<>(questionOptions.subList(1, questionOptions.size())));
                            correctAnswers.add(questionCorrectAnswers);
                            questionTypes.equals(questionType);
                            questionOptions.clear();
                            questionCorrectAnswers.clear();
                        }
                    } else if (line.startsWith("$ ")) {
                        questions.add(line.substring(2).trim());
                    } else if (line.startsWith("^ ")) {
                        questionOptions.addAll(Arrays.asList(line.substring(2).trim().split(", ")));
                    } else if (line.startsWith("* ")) {
                        questionCorrectAnswers.addAll(Arrays.asList(line.substring(2).trim().split(", ")));
                        if (questionCorrectAnswers.size()>=2){
                            questionType.equals(QuestionType.MULTIPLE_CHOICE);
                        }
                        else {
                            questionType.equals(QuestionType.SINGLE_CHOICE);
                        }
                    }
            }


            if (!questionOptions.isEmpty()) {
                questions.add(questionOptions.get(0));
                options.add(new ArrayList<>(questionOptions.subList(1, questionOptions.size())));
                correctAnswers.add(questionCorrectAnswers);
                questionTypes.equals(questionType);
            }

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadQuestion() {
        Log.d("QuestionManagerHelper", "Loading question: " + currentQuestion);
        if (currentQuestion >= 0 && currentQuestion < questions.size()) {
            String questionText = questions.get(currentQuestion);
            List<String> optionList = options.get(currentQuestion);

            questionTextView.setText(questionText);

            StringBuilder addTextInOption = new StringBuilder();

            for (int i = 0; i < optionList.size(); i++) {
                addTextInOption.append((i + 1) + ") " + optionList.get(i));
                if (i < optionList.size() - 1) {
                    addTextInOption.append("\n");
                }
            }

            Log.d("QuestionManagerHelper", "Options: " + addTextInOption.toString());
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