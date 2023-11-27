package com.example.tests;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainTextAcrivity extends AppCompatActivity{

    Stack userAnswersStack = new Stack();

    int correctAnswers, failedAnsweers = 0;
    List<UserAnswer> userAnswersList = new ArrayList<>();
    Boolean answer1 = false,
            answer2 = false,
            answer3 = false ,
            answer4 = false;
    private Map<Button, Boolean> buttonStates = new HashMap<>();
    private boolean isClickedButton = false;
    Random random = new Random();
    private Button lastClickedButton; // Последняя нажатая кнопка
    Button PreviousQuestion, NextQuestion, Answer1, Answer2, Answer3, Answer4;
    int colorGray2, colorBlue, colorGold;
    int currentQuestionIndex = 47; // Индекс текущего вопроса
    List<Question> questionsList = new ArrayList<>();
    int AllQuestions = 50;

    String USERNAME, USERSURNAME, USERNICKNAME, USERCITY;

    @Override
    public void onBackPressed() {
        showConfirmationDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        boolean isFileDownloaded = false;

        Intent intent = getIntent();
        if (intent != null) {
            USERNAME = intent.getStringExtra("USERNAME");
            USERSURNAME = intent.getStringExtra("USERSURNAME");
            USERNICKNAME = intent.getStringExtra("USERNICKNAME");
            USERCITY = intent.getStringExtra("USERCITY");
        }

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

        // Для эффекта загрузки
        ProgressBar progressBar = findViewById(R.id.progressBar);
        RelativeLayout RelativeQuestions = findViewById(R.id.Question);
        RelativeLayout RelativeButtons = findViewById(R.id.RelativeButtons);

        // Показать ProgressBar при начале загрузки данных
        progressBar.setVisibility(View.VISIBLE);

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

        new FileDownloadTask(new FileDownload.FileDownloadListener() {
            @Override
            public void onDownloadComplete(ResponseBody responseBody) {
                // Обработка завершения загрузки в основном потоке, если необходимо
            }

            @Override
            public void onDownloadFailed() {
                // Обработка ошибок в основном потоке, если необходимо
            }
        }).execute();

        FileDownloadApi fileDownloadApi = RetrofitClient.getClient().create(FileDownloadApi.class);
        Log.d("FileDownloadApi", "fileDownloadApi sucsed");
        Call<ResponseBody> call = fileDownloadApi.downloadFile("https://drive.google.com/uc?id=1W669YuIDmEqhplKJrpyl4tuPdWpL3RKL");
        Log.d("Call<ResponseBody> ", "call = googleDrive");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    // Теперь вы можете использовать responseBody для чтения данных
                    if (responseBody != null) {
                        try {
                            InputStream inputStream = responseBody.byteStream();
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
                                    String[] parts = content.split(", ");

                                    List<String> questionOptions = new ArrayList<>();
                                    List<String> questionCorrectAnswers = new ArrayList<>();

                                    for (String part : parts) {
                                        if (part.startsWith("* ")) {
                                            questionCorrectAnswers.add(part.substring(1));
                                        } else {
                                            questionOptions.add(part);
                                        }
                                    }

                                    options.addAll(questionOptions);
                                } else if (line.startsWith("* ")) {
                                    String content = line.substring(1);
                                    String[] parts = content.split(",");

                                    List<String> questionCorrectAnswers = new ArrayList<>();

                                    for (String part : parts) {
                                        questionCorrectAnswers.add(part.substring(1));
                                    }

                                    correctAnswers.addAll(questionCorrectAnswers);
                                    if (parts.length > 1) {
                                        questionType = QuestionType.MULTIPLE_CHOICE;
                                    } else {
                                        questionType = QuestionType.SINGLE_CHOICE;
                                    }
                                } else if (line.isEmpty()) {
                                    // Пустая строка обозначает конец вопроса
                                    Question question1 = new Question(question, questionType, new ArrayList<>(options), new ArrayList<>(correctAnswers));

                                    questionsList.add(question1);

                                    // Очищаем переменные для следующего вопроса
                                    question = "";
                                    options.clear();
                                    correctAnswers.clear();
                                    questionType = null;
                                }
                            }

                            // Обработка последнего вопроса, если он не завершается пустой строкой
                            if (!question.isEmpty()) {
                                Question question1 = new Question(question, questionType, new ArrayList<>(options), new ArrayList<>(correctAnswers));
                                questionsList.add(question1);
                            }

                            // Закрыть потоки, когда они больше не нужны
                            bufferedReader.close();
                            reader.close();
                            inputStream.close();

                            // Отобразить вопросы
                            if (currentQuestionIndex < questionsList.size()) {
                                Log.d("Questions", "Number of questions: " + questionsList.size());
                                Question question1 = questionsList.get(currentQuestionIndex);
                                questionTextView.setText(question1.GetQuestionText());
                                List<String> correct = question1.getCorrectAnswer();

                                // Создаем строку для вариантов ответов
                                StringBuilder optionsText = new StringBuilder();
                                List<String> options1 = question1.getOptions();
                                for (int i = 0; i < options1.size(); i++) {
                                    optionsText.append((i + 1) + ". " + options1.get(i)); // Нумерация вариантов ответов
                                    Log.d("OptionText", "Text: " + options1);
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    RelativeQuestions.setVisibility(View.VISIBLE);
                                    RelativeButtons.setVisibility(View.VISIBLE);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("FileDownload", "Download failed: " + e.getMessage());
                        }
                    }
                } else {
                    // Обработка неуспешного ответа
                    Log.e("FileDownload", "Unsuccessful response");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Обработка ошибки
                Log.e("FileDownload", "Download failed: " + t.getMessage());
            }
        });

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

                    if (userAnswersStack.pop() == "correctAnswer"){
                        correctAnswers--;
                    } else if (userAnswersStack.pop() == "failedAnswer") {
                        failedAnsweers--;
                    }
                }
                // Если есть ещё вопросы для кнопки Продолжить
                if (currentQuestionIndex < AllQuestions){
                    NextQuestion.setBackgroundColor(colorBlue);
                }
                // Первый вопрос
                if (currentQuestionIndex == 0){
                    PreviousQuestion.setBackgroundColor(colorGray2);
                }

                setDefaultColors(colorBlue);
            }
        });

        NextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход к следующему вопросу
                if (currentQuestionIndex <= AllQuestions) {
                    Log.d("ЦИкл", "ЦИКЛ 1 НАЧАЛ ВЫПОЛНЯТЬСЯ");
                    Question question = questionsList.get(currentQuestionIndex);
                    questionTextView.setText(question.GetQuestionText());
                    Question nextquestion;
                    QuestionType type = question.getQuestionType();

                    Log.d("Answer ", "Answer1: " + answer1);
                    Answer(questionsList, type);
                    answer1 = false;
                    answer2 = false;
                    answer3 = false;
                    answer4 = false;
                    currentQuestionIndex++; // Переключаемся на следующий вопрос
                    if (currentQuestionIndex < AllQuestions) {
                        Log.d("ЦИКЛ", "ЦИКЛ 2 НАЧАЛ ВЫПОЛНЯТЬСЯ");

                        try {
                            nextquestion = questionsList.get(currentQuestionIndex+1);
                            Log.d("Nextquestion", "Text: " + nextquestion.GetQuestionText() + questionsList.toArray().length + "Current question: " + currentQuestionIndex);
                        }
                        catch (IndexOutOfBoundsException exception) {
                            Log.d("Nextquestion", "Text: not found " + "Current question: " + currentQuestionIndex);
                        }
                        StringBuilder optionsText = new StringBuilder();
                        List<String> options = question.getOptions();
                        for (int i = 0; i < options.size(); i++) {
                            optionsText.append((i + 1) + ". " + options.get(i));
                            if (i < options.size() - 1) {
                                optionsText.append("\n");
                            }
                        }
                        Log.d("ЦИКЛ", "ЦИКЛ 2 ЗАКОНЧИЛСЯ");
                        optionTextView.setText(optionsText.toString());
                    } else {
                        Log.d("ЦИКЛ 2 ВЫПОЛНЯЕТ ELSE", "ЦИКЛ 2 ВЫПОЛНЯЕТ ELSE");
                        // Покажите диалоговое окно
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Log.d("Result of testing", "Correct answers: " + correctAnswers + " Failed answers: " + failedAnsweers + " Percent of correct answers: " + Percent());
                    }

                }
                // Последний вопрос
                if (currentQuestionIndex == AllQuestions){
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
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();

                if (type == QuestionType.SINGLE_CHOICE){
                    // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                    if (lastClickedButton != null) {
                        lastClickedButton.setBackgroundColor(colorBlue);
                        answer1 = false;
                        answer2 = false;
                        answer3 = false;
                        answer4 = false;
                    }
                    // Устанавливаем новую последнюю нажатую кнопку
                    lastClickedButton = (Button) view;

                    // Устанавливаем новый цвет для текущей кнопки
                    view.setBackgroundColor(colorGold);
                    answer1 = true; // Обновляем глобальную переменную
                    Log.d(" ", "answer: " + answer1);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    boolean isClicked = getButtonState((Button) view);
                    if (!isClicked) {
                        view.setBackgroundColor(colorGold);
                        setButtonState((Button) view, true);
                        answer1 = true; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to gold" + answer1);
                    } else {
                        view.setBackgroundColor(colorBlue);
                        setButtonState((Button) view, false);
                        answer1 = false; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to blue" + answer1);
                    }
                }
                buttonStates.put(Answer1, answer1); // Сохраняем состояние кнопки в buttonStates
            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();

                if (type == QuestionType.SINGLE_CHOICE){
                    // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                    if (lastClickedButton != null) {
                        lastClickedButton.setBackgroundColor(colorBlue);
                        answer1 = false;
                        answer2 = false;
                        answer3 = false;
                        answer4 = false;
                    }
                    // Устанавливаем новую последнюю нажатую кнопку
                    lastClickedButton = (Button) view;

                    // Устанавливаем новый цвет для текущей кнопки
                    view.setBackgroundColor(colorGold);
                    answer2 = true; // Обновляем глобальную переменную
                    Log.d(" ", "answer: " + answer2);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    boolean isClicked = getButtonState((Button) view);
                    if (!isClicked) {
                        view.setBackgroundColor(colorGold);
                        setButtonState((Button) view, true);
                        answer2 = true; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to gold" + answer2);
                    } else {
                        view.setBackgroundColor(colorBlue);
                        setButtonState((Button) view, false);
                        answer2 = false; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to blue" + answer2);
                    }
                }
                buttonStates.put(Answer2, answer2); // Сохраняем состояние кнопки в buttonStates
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();

                if (type == QuestionType.SINGLE_CHOICE){
                    // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                    if (lastClickedButton != null) {
                        lastClickedButton.setBackgroundColor(colorBlue);
                        answer1 = false;
                        answer2 = false;
                        answer3 = false;
                        answer4 = false;
                    }
                    // Устанавливаем новую последнюю нажатую кнопку
                    lastClickedButton = (Button) view;

                    // Устанавливаем новый цвет для текущей кнопки
                    view.setBackgroundColor(colorGold);
                    answer3 = true; // Обновляем глобальную переменную
                    Log.d(" ", "answer: " + answer3);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    boolean isClicked = getButtonState((Button) view);
                    if (!isClicked) {
                        view.setBackgroundColor(colorGold);
                        setButtonState((Button) view, true);
                        answer3 = true; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to gold" + answer3);
                    } else {
                        view.setBackgroundColor(colorBlue);
                        setButtonState((Button) view, false);
                        answer3 = false; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to blue" + answer3);
                    }
                }
                buttonStates.put(Answer3, answer3); // Сохраняем состояние кнопки в buttonStates
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();

                if (type == QuestionType.SINGLE_CHOICE){
                    // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                    if (lastClickedButton != null) {
                        lastClickedButton.setBackgroundColor(colorBlue);
                        answer1 = false;
                        answer2 = false;
                        answer3 = false;
                        answer4 = false;
                    }
                    // Устанавливаем новую последнюю нажатую кнопку
                    lastClickedButton = (Button) view;

                    // Устанавливаем новый цвет для текущей кнопки
                    view.setBackgroundColor(colorGold);
                    answer4 = true; // Обновляем глобальную переменную
                    Log.d(" ", "answer: " + answer4);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    boolean isClicked = getButtonState((Button) view);
                    if (!isClicked) {
                        view.setBackgroundColor(colorGold);
                        setButtonState((Button) view, true);
                        answer4 = true; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to gold" + answer4);
                    } else {
                        view.setBackgroundColor(colorBlue);
                        setButtonState((Button) view, false);
                        answer4 = false; // Обновляем глобальную переменную
                        Log.d("ButtonState", "Changed to blue" + answer4);
                    }
                }
                buttonStates.put(Answer4, answer4); // Сохраняем состояние кнопки в buttonStates
            }
        });

    }
    private void changeButtonColorSingle (Button button, Boolean answer) {
        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundColor(colorBlue);
            answer = false;
            Log.d(" ", "answer: " + answer);
        }
        // Устанавливаем новую последнюю нажатую кнопку
        lastClickedButton = button;

        // Устанавливаем новый цвет для текущей кнопки
        button.setBackgroundColor(colorGold);
        answer = true;
        Log.d(" ", "answer: " + answer);
    }

    private void changeButtonColorMulti(Button button, Boolean answer) {
        Log.d("ButtonClick", "Button clicked");
        boolean isClicked = getButtonState(button);
        if (!isClicked) {
            button.setBackgroundColor(colorGold);
            setButtonState(button, true);
            answer = true;
            Log.d("ButtonState", "Changed to gold" + answer);
        } else {
            button.setBackgroundColor(colorBlue);
            setButtonState(button, false);
            answer = false;
            Log.d("ButtonState", "Changed to blue" + answer);
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

    private boolean getButtonState(Button button) {
        return buttonStates.getOrDefault(button, false);
    }

    private void setButtonState(Button button, boolean state) {
        buttonStates.put(button, state);
    }


    public class FileDownloadTask extends AsyncTask<Void, Void, Void> {
        private final FileDownload.FileDownloadListener listener;

        public FileDownloadTask(FileDownload.FileDownloadListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FileDownload.downloadFile("download", new FileDownload.FileDownloadListener() {
                @Override
                public void onDownloadComplete(ResponseBody responseBody) {
                    // Чтение файла из responseBody и обработка данных
                    // responseBody.byteStream() предоставляет InputStream для файла
                    Log.d("FileDownload", "FileDownloaded succeeded");
                    if (listener != null) {
                        listener.onDownloadComplete(responseBody);
                    }
                }

                @Override
                public void onDownloadFailed() {
                    // Обработка ошибок при загрузке файла
                    Log.d("FileDownload", "Failed");
                    if (listener != null) {
                        listener.onDownloadFailed();
                    }
                }
            });
            return null;
        }
    }



    // Метод для поиска вопроса по идентификатору
    private Question getQuestionById(int questionId) {
        for (Question question : questionsList) {
            if (question.getId() == questionId) {
                return question;
            }
        }
        return null;
    }

    private UserAnswer Answer(List<Question> questionsList, QuestionType type){

        Question question = questionsList.get(currentQuestionIndex);
        List<String> options = question.getOptions();
        String userSelectedOption;

        UserAnswer answer = new UserAnswer();
        UserAnswer correctAnswer = new UserAnswer(question.getCorrectAnswer());
        List<Boolean> ans = new ArrayList<>();

        ans.add(answer1);
        ans.add(answer2);
        ans.add(answer3);
        ans.add(answer4);

        if (type == QuestionType.SINGLE_CHOICE) {
            try {
                for (int i = 0; i < ans.size(); i++) {
                    boolean answ = ans.get(i);
                    if (answ) {
                        String option = question.getOption(i);
                        userSelectedOption = option;
                        answer.setUserOption(userSelectedOption);
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        if (type == QuestionType.MULTIPLE_CHOICE){
            try {
                for (int i = 0; i < ans.size(); i++) {
                    Boolean answ = ans.get(i);
                    if (answ) {
                        String option = question.getOption(i);
                        userSelectedOption = option;
                        answer.setUserOption(userSelectedOption);
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        if (answer.equals(correctAnswer)){
            correctAnswers++;
            userAnswersStack.push("correctAnswer");
            Log.d("", "correctAnswers++" + correctAnswers);
        } else {
            failedAnsweers++;
            userAnswersStack.push("failedAnswer");
            Log.d("", "failedanswer++" + failedAnsweers);
        }

        Log.d("Stack", "Stack size: " + userAnswersStack.peek() + " " + userAnswersStack.size());
        Log.d("UserAnswer", "User's Answer: " + answer.toString());
        Log.d("Answers", "Answers" + ans.toString());
        Log.d("CorrectAnswer", "Correct Answer: " + correctAnswer.toString());
        return answer;
    }

    private float Percent(){
        float percent = (correctAnswers * 100)/AllQuestions;
        return percent;
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы уверены, что хотите начать тестирование заново?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Пользователь подтвердил, выполните действия для перехода на предыдущий экран
                finish(); // Это закроет текущую активность и вернется на предыдущую
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Пользователь отменил, ничего не делаем
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}