package com.example.tests;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.tests.EmailSender.*;

import org.w3c.dom.Text;

public class MainTextAcrivity extends AppCompatActivity{
    Stack userAnswersStack = new Stack();
    private RecyclerView questionNavigationRecyclerView1, questionNavigationRecyclerView2, questionNavigationRecyclerView3, questionNavigationRecyclerView4;
    private QuestionNavigationAdapter adapter1;
    private QuestionNavigationAdapter adapter4;
    private QuestionNavigationAdapter adapter2;
    private QuestionNavigationAdapter adapter3;
    private EmailSender emailSender;
    int correctAnswers = 0, failedAnsweers = 0, partialAnswer = 0;
    List<UserAnswer> userAnswersList = new ArrayList<>();
    Boolean answer1 = false,
            answer2 = false,
            answer3 = false,
            answer4 = false;
    private Map<Button, Boolean> buttonStates = new HashMap<>();
    private Button lastClickedButton;
    Button PreviousQuestion, NextQuestion, Answer1, Answer2, Answer3, Answer4;
    int colorGray2, colorBlue, colorGold, colorBlack;
    int currentQuestionIndex = 0;
    List<Question> questionsList = new ArrayList<>();
    int AllQuestions = 49;
    String USERNAME, USERSURNAME, USERNICKNAME, USERCITY;
    int[] array = new int[50];
    ProgressBar progressBar;
    RelativeLayout RelativeButtons, RelativeQuestions;
    QuestionInfo[] AnswersInformation = new QuestionInfo[50];
    int CorrAnsw = 0;
    int FailAnsw = 0;
    int PartAnsw = 0;
    @Override
    public void onBackPressed() {
        showConfirmationDialog();
    }
    private void sendEmail() {
        // Your existing sendEmail method code here
        String toAddress = "dom-mafia@outlook.com";
        String subject = "Данные: " + "Имя: " + USERNAME + " Фамилия: " + USERSURNAME + " Никнейм: " + USERNICKNAME + " Город: " + USERCITY;
        String body = "Ответы: " + "\n Правильные ответы: " + CorrAnsw + "\n Частично правильные ответы: " + PartAnsw + "\n Неправльные ответы: " + FailAnsw;

        EmailCallback emailCallback = new EmailCallback() {
            @Override
            public void onEmailTaskComplete(boolean success) {
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("MainTextActivityTag", "Письмо успешно отправлено");
                            Log.d("", "progress bar status: " + progressBar.getVisibility());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("MainTextActivityTag", "Интерфейс обновляется");
                                    Intent intent = new Intent(MainTextAcrivity.this, TestCompletedActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } else {
                    onEmailFailed();
                }
            }
            @Override
            public void onEmailSent() {
                // Implement if necessary
            }
            @Override
            public void onEmailFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MainTextActivityTag", "Ошибка при отправке письма");
                    }
                });
            }
        };
        emailSender.sendEmail(toAddress, subject, body, emailCallback);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_acrivity);

        List<QuestionItem> questionItems = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            QuestionItem questionItem = new QuestionItem(i, colorGold); // замените colorBlue на ваш цвет
            questionItems.add(questionItem);
        }
        List<QuestionItem> questionItems1 = new ArrayList<>();
        for (int i = 15; i <= 28; i++) {
            QuestionItem questionItem = new QuestionItem(i, colorGold); // замените colorBlue на ваш цвет
            questionItems1.add(questionItem);
        }
        List<QuestionItem> questionItems2 = new ArrayList<>();
        for (int i = 29; i <= 42; i++) {
            QuestionItem questionItem = new QuestionItem(i, colorGold); // замените colorBlue на ваш цвет
            questionItems2.add(questionItem);
        }
        List<QuestionItem> questionItems3 = new ArrayList<>();
        for (int i = 43; i <= 50; i++) {
            QuestionItem questionItem = new QuestionItem(i, colorGold); // замените colorBlue на ваш цвет
            questionItems3.add(questionItem);
            Log.i(" ", "onCreate: " + i);
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        questionNavigationRecyclerView1 = findViewById(R.id.questionNavigationRecyclerView1);
        questionNavigationRecyclerView2 = findViewById(R.id.questionNavigationRecyclerView2);
        questionNavigationRecyclerView3 = findViewById(R.id.questionNavigationRecyclerView3);
        questionNavigationRecyclerView4 = findViewById(R.id.questionNavigationRecyclerView4);

        List<Integer> questionNumbers1 = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            questionNumbers1.add(i);
        }
        List<Integer> questionNumbers2 = new ArrayList<>();
        for (int i = 15; i <= 28; i++) {
            questionNumbers2.add(i);
        }
        List<Integer> questionNumbers3 = new ArrayList<>();
        for (int i = 29; i <= 42; i++) {
            questionNumbers3.add(i);
        }
        List<Integer> questionNumbers4 = new ArrayList<>();
        for (int i = 43; i <= 50; i++) {
            questionNumbers4.add(i);
        }

        emailSender = new EmailSender("dom-mafia@outlook.com", "agcwDgowHHas34f735fgD8643gjfku");

        // Создаем массив с числами от 0 до 49
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        // Перемешиваем массив
        shuffleArray(array);

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
        colorBlack = ContextCompat.getColor(this, R.color.black);

        // Получаем текстовые поля
        TextView questionTextView = findViewById(R.id.QuestionText);
        TextView optionTextView = findViewById(R.id.optionText);
        TextView questionCountTextView = findViewById(R.id.QuestionCount);

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
        progressBar = findViewById(R.id.progressBar);
        RelativeQuestions = findViewById(R.id.Question);
        RelativeButtons = findViewById(R.id.RelativeButtons);

        // Показать ProgressBar при начале загрузки данных
        progressBar.setVisibility(View.VISIBLE);

        //
        // Тут описано что делает каждая кнопка навигации от 1 до 14
        //
        adapter1 = new QuestionNavigationAdapter(questionItems, questionNumber -> {

            Question AnserQuestion = questionsList.get(array[currentQuestionIndex]);
            QuestionType type = AnserQuestion.getQuestionType();
            setDefaultColors(colorBlue, colorGold);

            currentQuestionIndex = questionNumber.getQuestionNumber()-1;
            Question question = questionsList.get(array[currentQuestionIndex]);
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
            questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));

            // Последний вопрос
            if (currentQuestionIndex == AllQuestions){
                NextQuestion.setBackgroundColor(colorGold);
                NextQuestion.setText("Завершить");
            }
            // Стандартный цвет кнопок
            if (currentQuestionIndex > 0) {
                PreviousQuestion.setBackgroundColor(colorBlue);
            }
            if (!AnswersInformation[currentQuestionIndex].isAnswerGiven()){
                SetBlueColors(colorBlue);
            } else {
                setDefaultColors(colorBlue, colorGold);
            }
            // Если есть ещё вопросы для кнопки Продолжить
            if (currentQuestionIndex < AllQuestions){
                NextQuestion.setBackgroundColor(colorBlue);
                NextQuestion.setText("Продолжить");
            }
            // Первый вопрос
            if (currentQuestionIndex == 0){
                PreviousQuestion.setBackgroundColor(colorGray2);
            }
        });

        adapter2 = new QuestionNavigationAdapter(questionItems1, questionNumber1 -> {
            Question AnserQuestion = questionsList.get(array[currentQuestionIndex]);
            QuestionType type = AnserQuestion.getQuestionType();
            setDefaultColors(colorBlue, colorGold);

            currentQuestionIndex = questionNumber1.getQuestionNumber() - 1;
            Question question = questionsList.get(array[currentQuestionIndex]);
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
            questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));

            // Последний вопрос
            if (currentQuestionIndex == AllQuestions){
                NextQuestion.setBackgroundColor(colorGold);
                NextQuestion.setText("Завершить");
            }
            // Стандартный цвет кнопок
            if (currentQuestionIndex > 0) {
                PreviousQuestion.setBackgroundColor(colorBlue);
            }
            if (!AnswersInformation[currentQuestionIndex].isAnswerGiven()){
                SetBlueColors(colorBlue);
            } else {
                setDefaultColors(colorBlue, colorGold);
            }
            // Если есть ещё вопросы для кнопки Продолжить
            if (currentQuestionIndex < AllQuestions){
                NextQuestion.setBackgroundColor(colorBlue);
                NextQuestion.setText("Продолжить");
            }
        });

        adapter3 = new QuestionNavigationAdapter(questionItems2, questionNumber2 -> {
            Question AnserQuestion = questionsList.get(array[currentQuestionIndex]);
            QuestionType type = AnserQuestion.getQuestionType();
            setDefaultColors(colorBlue, colorGold);

            currentQuestionIndex = questionNumber2.getQuestionNumber() - 1;
            Question question = questionsList.get(array[currentQuestionIndex]);
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
            questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));

            // Последний вопрос
            if (currentQuestionIndex == AllQuestions){
                NextQuestion.setBackgroundColor(colorGold);
                NextQuestion.setText("Завершить");
            }
            // Стандартный цвет кнопок
            if (currentQuestionIndex > 0) {
                PreviousQuestion.setBackgroundColor(colorBlue);
            }
            if (!AnswersInformation[currentQuestionIndex].isAnswerGiven()){
                SetBlueColors(colorBlue);
            } else {
                setDefaultColors(colorBlue, colorGold);
            }
            // Если есть ещё вопросы для кнопки Продолжить
            if (currentQuestionIndex < AllQuestions){
                NextQuestion.setBackgroundColor(colorBlue);
                NextQuestion.setText("Продолжить");
            }
        });

        adapter4 = new QuestionNavigationAdapter(questionItems3, questionNumber3 -> {
            Question AnserQuestion = questionsList.get(array[currentQuestionIndex]);
            QuestionType type = AnserQuestion.getQuestionType();
            setDefaultColors(colorBlue, colorGold);

            currentQuestionIndex = questionNumber3.getQuestionNumber() - 1;
            Question question = questionsList.get(array[currentQuestionIndex]);
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
            questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));

            // Последний вопрос
            if (currentQuestionIndex == AllQuestions){
                NextQuestion.setBackgroundColor(colorGold);
                NextQuestion.setText("Завершить");
            }
            // Стандартный цвет кнопок
            if (currentQuestionIndex > 0) {
                PreviousQuestion.setBackgroundColor(colorBlue);
            }
            if (!AnswersInformation[currentQuestionIndex].isAnswerGiven()){
                SetBlueColors(colorBlue);
            } else {
                setDefaultColors(colorBlue, colorGold);
            }
            // Если есть ещё вопросы для кнопки Продолжить
            if (currentQuestionIndex < AllQuestions){
                NextQuestion.setBackgroundColor(colorBlue);
                NextQuestion.setText("Продолжить");
            }
        });

        questionNavigationRecyclerView1.setAdapter(adapter1);
        questionNavigationRecyclerView2.setAdapter(adapter2);
        questionNavigationRecyclerView3.setAdapter(adapter3);
        questionNavigationRecyclerView4.setAdapter(adapter4);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        questionNavigationRecyclerView1.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        questionNavigationRecyclerView2.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        questionNavigationRecyclerView3.setLayoutManager(layoutManager3);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        questionNavigationRecyclerView4.setLayoutManager(layoutManager4);

        // Создайте диалоговое окно с вопросом
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Завершить тестирование?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Log.i("Сообщение", "Сообщение еще не отправлено");

                for (int i = 0; i < AnswersInformation.length; i++){
                    if (AnswersInformation[i].getResult().equals("correctAnswer")){
                        CorrAnsw++;
                    } else if (AnswersInformation[i].getResult().equals("partialAnswer")) {
                        PartAnsw++;
                    } else {
                        FailAnsw++;
                    }
                }

                progressBar.setVisibility(View.VISIBLE);
                RelativeButtons.setVisibility(View.GONE);
                RelativeQuestions.setVisibility(View.GONE);
                questionNavigationRecyclerView1.setVisibility(View.GONE);
                questionNavigationRecyclerView2.setVisibility(View.GONE);
                questionNavigationRecyclerView3.setVisibility(View.GONE);
                questionNavigationRecyclerView4.setVisibility(View.GONE);

                sendEmail();

            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                currentQuestionIndex--;

                String lastAnswer = userAnswersStack.pop();

                if (lastAnswer == "correctAnswer"){
                    correctAnswers--;
                } else if (lastAnswer == "failedAnswer") {
                    failedAnsweers--;
                }
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                currentQuestionIndex--;

                String lastAnswer = userAnswersStack.pop();

                if (lastAnswer == "correctAnswer"){
                    correctAnswers--;
                } else if (lastAnswer == "failedAnswer") {
                    failedAnsweers--;
                }
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
        Call<ResponseBody> call = fileDownloadApi.downloadFile("https://drive.usercontent.google.com/u/0/uc?id=12U_EKI1Lc_nb61IP5bgkvOd5hqRMl7xK&export=download");
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
                            QuestionType questionType = QuestionType.MULTIPLE_CHOICE;

                            String line;

                            while ((line = bufferedReader.readLine()) != null) {

                                if (line.startsWith("$ ")) {
                                    question = line.substring(2);
                                } else if (line.startsWith("^ ")) {
                                    String content = line.substring(2);
                                    String[] parts = content.split(", ");

                                    List<String> questionOptions = new ArrayList<>();
                                    List<String> questionCorrectAnswers = new ArrayList<>();

                                    for (String part : parts){
                                        questionOptions.add(part);
                                    }

                                    options.addAll(questionOptions);
                                    correctAnswers.addAll(questionCorrectAnswers);
                                } else if (line.startsWith("* ")) {
                                    String content = line.substring(2);
                                    String[] parts = content.split(",");

                                    List<String> questionCorrectAnswers = new ArrayList<>();

                                    for (String part : parts) {
                                        // Удаление пробелов в начале и конце подстроки
                                        String trimmedPart = part.trim();
                                        questionCorrectAnswers.add(trimmedPart);
                                    }

                                    correctAnswers.addAll(questionCorrectAnswers);
                                    if (parts.length > 1) {
                                        questionType = QuestionType.MULTIPLE_CHOICE;
                                    } else {
                                        questionType = QuestionType.MULTIPLE_CHOICE;
                                    }
                                } else if (line.isEmpty()) {
                                    // Empty line indicates the end of a question
                                    Question question1 = new Question(question, questionType, new ArrayList<>(options), new ArrayList<>(correctAnswers));
                                    questionsList.add(question1);

                                    // Reset variables for the next question
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
                                Question question1 = questionsList.get(array[currentQuestionIndex]);
                                questionTextView.setText(question1.GetQuestionText());
                                List<String> correct = question1.getCorrectAnswer();

                                Log.d("Текущий вопрос", "Current question: " + question1.GetQuestionText());

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
                                questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));
                            } else {
                                // Покажите диалоговое окно
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            Log.d("Questions count", "Count of questions: " + question.length());

                            for (int i = 0; i <= AllQuestions; i++) {
                                if (i < questionsList.size()){
                                    Question question2 = questionsList.get(array[i]);
                                    boolean given = false;
                                    List<Boolean> options2 = new ArrayList<>();
                                    List<String> answers = new ArrayList<>();
                                    String res = " ";

                                    for (int j = 0; j < 4; j++) {
                                        options2.add(false);
                                    }

                                    QuestionInfo result = new QuestionInfo(question2.GetQuestionText(), options2, question2.getCorrectAnswer(), answers, given, res);
                                    AnswersInformation[i] = result;
                                }
                                else {
                                    Log.d(" ", "i = :" + i + " Text: " + questionsList.size());
                                }
                            }

                            for (int i = 0; i < adapter1.getItemCount(); i++){
                                adapter1.changeColorForItem(i, colorBlack);
                                adapter2.changeColorForItem(i, colorBlack);
                                adapter3.changeColorForItem(i, colorBlack);
                            }
                            for (int i = 0; i < adapter4.getItemCount(); i++){
                                adapter4.changeColorForItem(i, colorBlack);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    RelativeQuestions.setVisibility(View.VISIBLE);
                                    RelativeButtons.setVisibility(View.VISIBLE);
                                    questionNavigationRecyclerView1.setVisibility(View.VISIBLE);
                                    questionNavigationRecyclerView2.setVisibility(View.VISIBLE);
                                    questionNavigationRecyclerView3.setVisibility(View.VISIBLE);
                                    questionNavigationRecyclerView4.setVisibility(View.VISIBLE);

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
                    setDefaultColors(colorBlue, colorGold);

                    Log.i("QuestionList", "Size: " + questionsList.size());
                    Question question = questionsList.get(array[currentQuestionIndex]);

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
                    questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));


                } else {
                    showConfirmationDialog();
                }
                // Если есть ещё вопросы для кнопки Продолжить
                if (currentQuestionIndex < AllQuestions){
                    NextQuestion.setBackgroundColor(colorBlue);
                    NextQuestion.setText("Продолжить");
                }
                // Первый вопрос
                if (currentQuestionIndex == 0){
                    PreviousQuestion.setBackgroundColor(colorGray2);
                }
                Log.i(" ", " " + AnswersInformation[currentQuestionIndex].GetSelectedOptions());

            }
        });

        NextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question AnserQuestion = questionsList.get(array[currentQuestionIndex]);
                QuestionType type = AnserQuestion.getQuestionType();
                Answer(questionsList, type);
                setDefaultColors(colorBlue, colorGold);

                if (AnswersInformation[currentQuestionIndex].isAnswerGiven()){
                    if (currentQuestionIndex<14){
                        adapter1.changeColorForItem(currentQuestionIndex, colorGold);
                    }
                    if (currentQuestionIndex < 28 && currentQuestionIndex >= 14){
                        int newCurr = currentQuestionIndex-14;
                        adapter2.changeColorForItem(newCurr, colorGold);
                    }
                    if (currentQuestionIndex < 42 && currentQuestionIndex >= 28){
                        int newCurr = currentQuestionIndex-28;
                        adapter3.changeColorForItem(newCurr, colorGold);
                    }
                    if (currentQuestionIndex < 48 && currentQuestionIndex >= 42){
                        int newCurr = currentQuestionIndex-42;
                        adapter4.changeColorForItem(newCurr, colorGold);
                        Log.i(" ", "onClick: " + currentQuestionIndex + " " + newCurr);
                    } // 6 7
                    if (currentQuestionIndex == 48){
                        adapter4.changeColorForItem(6, colorGold);
                    }
                }
                currentQuestionIndex++; // Переключаемся на следующий вопрос

                //Переход к следующему вопросу
                    if (currentQuestionIndex <= AllQuestions) {
                        Question question = questionsList.get(array[currentQuestionIndex]);
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
                        questionCountTextView.setText("Вопрос " + (currentQuestionIndex+1));
                        if (!AnswersInformation[currentQuestionIndex].isAnswerGiven()){
                            SetBlueColors(colorBlue);
                        } else {
                            setDefaultColors(colorBlue, colorGold);
                        }
                    } else {
                        // Покажите диалоговое окно
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                // Последний вопрос
                if (currentQuestionIndex == AllQuestions){
                    NextQuestion.setBackgroundColor(colorGold);
                    NextQuestion.setText("Завершить");
                }
                // Стандартный цвет кнопок
                if (currentQuestionIndex > 0) {
                    PreviousQuestion.setBackgroundColor(colorBlue);
                }
                Log.i("log", "Res: " + AnswersInformation[currentQuestionIndex-1].getResult());
                Log.i("log", "Corr: " + AnswersInformation[currentQuestionIndex-1].getCorrectAnswer());
                Log.i("log", "Answ: " + AnswersInformation[currentQuestionIndex-1].getAnswer());

            }

        });

        Answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = null;
                if (currentQuestionIndex <= questionsList.size()) {
                    question = questionsList.get(array[currentQuestionIndex]);
                } else {
                    Log.d("currentQuestionIndex < questionsList.size()", "currentQuestionIndex > questionsList.size()" + " "
                    + currentQuestionIndex + " " + questionsList.size());
                }

                if (question != null) {
                    QuestionType type = question.getQuestionType();

                    if (type == QuestionType.SINGLE_CHOICE){
                        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                        if (lastClickedButton != null) {
                            lastClickedButton.setBackgroundColor(colorBlue);
                            answer2 = false;
                            answer3 = false;
                            answer4 = false;
                        }

                        // Устанавливаем новую последнюю нажатую кнопку
                        lastClickedButton = (Button) view;

                        // Устанавливаем новый цвет для текущей кнопки
                        view.setBackgroundColor(colorGold);

                        answer1 = true; // Обновляем глобальную переменную
                    } else if (type == QuestionType.MULTIPLE_CHOICE) {
                        boolean isClicked = getButtonState((Button) view);
                        if (!isClicked) {
                            view.setBackgroundColor(colorGold);
                            setButtonState((Button) view, true);
                            answer1 = true; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 0;
                            selOpt.set(index, true);
                        } else {
                            view.setBackgroundColor(colorBlue);
                            setButtonState((Button) view, false);
                            answer1 = false; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 0;
                            selOpt.set(index, false);
                        }
                    }
                    buttonStates.put(Answer1, answer1); // Сохраняем состояние кнопки в buttonStates
                }
            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = null;
                if (currentQuestionIndex <= questionsList.size()) {
                    question = questionsList.get(array[currentQuestionIndex]);
                } else {
                    Log.d("currentQuestionIndex < questionsList.size()", "currentQuestionIndex > questionsList.size()" + " "
                            + currentQuestionIndex + " " + questionsList.size());
                }

                if (question != null) {
                    QuestionType type = question.getQuestionType();

                    if (type == QuestionType.SINGLE_CHOICE){
                        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                        if (lastClickedButton != null) {
                            lastClickedButton.setBackgroundColor(colorBlue);
                            answer1 = false;
                            answer3 = false;
                            answer4 = false;
                        }
                        // Устанавливаем новую последнюю нажатую кнопку
                        lastClickedButton = (Button) view;

                        // Устанавливаем новый цвет для текущей кнопки
                        view.setBackgroundColor(colorGold);
                        answer2 = true; // Обновляем глобальную переменную
                    } else if (type == QuestionType.MULTIPLE_CHOICE) {
                        boolean isClicked = getButtonState((Button) view);
                        if (!isClicked) {
                            view.setBackgroundColor(colorGold);
                            setButtonState((Button) view, true);
                            answer2 = true; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 1;
                            selOpt.set(index, true);
                        } else {
                            view.setBackgroundColor(colorBlue);
                            setButtonState((Button) view, false);
                            answer2 = false; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 1;
                            selOpt.set(index, false);
                        }
                    }
                    buttonStates.put(Answer2, answer2); // Сохраняем состояние кнопки в buttonStates
                }
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = null;
                if (currentQuestionIndex <= questionsList.size()) {
                    question = questionsList.get(array[currentQuestionIndex]);
                } else {
                    Log.d("currentQuestionIndex < questionsList.size()", "currentQuestionIndex > questionsList.size()" + " "
                            + currentQuestionIndex + " " + questionsList.size());
                }

                if (question != null) {
                    QuestionType type = question.getQuestionType();

                    if (type == QuestionType.SINGLE_CHOICE){
                        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                        if (lastClickedButton != null) {
                            lastClickedButton.setBackgroundColor(colorBlue);
                            answer2 = false;
                            answer1 = false;
                            answer4 = false;
                        }
                        // Устанавливаем новую последнюю нажатую кнопку
                        lastClickedButton = (Button) view;

                        // Устанавливаем новый цвет для текущей кнопки
                        view.setBackgroundColor(colorGold);
                        answer3 = true; // Обновляем глобальную переменную
                    } else if (type == QuestionType.MULTIPLE_CHOICE) {
                        boolean isClicked = getButtonState((Button) view);
                        if (!isClicked) {
                            view.setBackgroundColor(colorGold);
                            setButtonState((Button) view, true);
                            answer3 = true; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 2;
                            selOpt.set(index, true);
                        } else {
                            view.setBackgroundColor(colorBlue);
                            setButtonState((Button) view, false);
                            answer3 = false; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 2;
                            selOpt.set(index, false);
                        }
                    }
                    buttonStates.put(Answer3, answer3); // Сохраняем состояние кнопки в buttonStates
                }
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = null;
                if (currentQuestionIndex <= questionsList.size()) {
                    question = questionsList.get(array[currentQuestionIndex]);
                } else {
                    Log.d("currentQuestionIndex < questionsList.size()", "currentQuestionIndex > questionsList.size()" + " "
                            + currentQuestionIndex + " " + questionsList.size());
                }

                if (question != null) {
                    QuestionType type = question.getQuestionType();

                    if (type == QuestionType.SINGLE_CHOICE){
                        // Если у нас есть последняя нажатая кнопка, возвращаем ей начальный цвет
                        if (lastClickedButton != null) {
                            lastClickedButton.setBackgroundColor(colorBlue);
                            answer2 = false;
                            answer3 = false;
                            answer1 = false;
                        }
                        // Устанавливаем новую последнюю нажатую кнопку
                        lastClickedButton = (Button) view;

                        // Устанавливаем новый цвет для текущей кнопки
                        view.setBackgroundColor(colorGold);
                        answer4 = true; // Обновляем глобальную переменную
                    } else if (type == QuestionType.MULTIPLE_CHOICE) {
                        boolean isClicked = getButtonState((Button) view);
                        if (!isClicked) {
                            view.setBackgroundColor(colorGold);
                            setButtonState((Button) view, true);
                            answer4 = true; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 3;
                            selOpt.set(index, true);
                        } else {
                            view.setBackgroundColor(colorBlue);
                            setButtonState((Button) view, false);
                            answer4 = false; // Обновляем глобальную переменную
                            List<Boolean> selOpt = AnswersInformation[currentQuestionIndex].GetSelectedOptions();
                            int index = 3;
                            selOpt.set(index, false);
                        }
                    }
                    buttonStates.put(Answer4, answer4); // Сохраняем состояние кнопки в buttonStates
                }
            }
        });

    }

    private void setDefaultColors(int color, int color2){ // Синий, золотой
        List<Boolean> answers = AnswersInformation[currentQuestionIndex].GetSelectedOptions();

        if (answers.get(0).equals(false)){
            Answer1.setBackgroundColor(color);
            setButtonState(Answer1, false);
            answer1 = false;
        }
        else {
            Answer1.setBackgroundColor(color2);
            setButtonState(Answer1, true);
            answer1 = true;
        }
        if (answers.get(1).equals(false)){
            Answer2.setBackgroundColor(color);
            setButtonState(Answer2, false);
            answer2 = false;
        }
        else {
            Answer2.setBackgroundColor(color2);
            setButtonState(Answer2, true);
            answer2 = true;
        }
        if (answers.get(2).equals(false)){
            Answer3.setBackgroundColor(color);
            setButtonState(Answer3, false);
            answer3 = false;
        }
        else {
            Answer3.setBackgroundColor(color2);
            setButtonState(Answer3, true);
            answer3 = true;
        }
        if (answers.get(3).equals(false)){
            Answer4.setBackgroundColor(color);
            setButtonState(Answer4, false);
            answer4 = false;
        }
        else {
            Answer4.setBackgroundColor(color2);
            setButtonState(Answer4, true);
            answer4 = true;
        }
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
            FileDownload.downloadFile(MainTextAcrivity.this, "download", new FileDownload.FileDownloadListener() {
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
                        Log.d("Третий else", "Третий else " + listener.toString());
                    }
                }
            });
            return null;
        }
    }
    private UserAnswer Answer(List<Question> questionsList, QuestionType type){

        Question question = questionsList.get(array[currentQuestionIndex]);

        List<String> options = question.getOptions();
        String userSelectedOption;

        List<Boolean> ans = new ArrayList<>();
        UserAnswer answer = new UserAnswer();
        UserAnswer correctAnswer = new UserAnswer(question.getCorrectAnswer());
        ans.add(answer1);
        ans.add(answer2);
        ans.add(answer3);
        ans.add(answer4);

        List<String> userAnswers = new ArrayList<>();

        if (type == QuestionType.SINGLE_CHOICE) {
            try {
                for (int i = 0; i < ans.size(); i++) {
                    boolean answ = ans.get(i);
                    if (answ) {
                        String option = question.getOption(i);
                        userSelectedOption = option;
                        userAnswers.add(userSelectedOption);
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (type == QuestionType.MULTIPLE_CHOICE) {
            try {
                for (int i = 0; i < ans.size(); i++) {
                    Boolean answ = ans.get(i);
                    if (answ) {
                        String option = question.getOption(i);
                        userSelectedOption = option;
                        userAnswers.add(userSelectedOption);
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        // Устанавливаем значения в AnswersInformation
        AnswersInformation[currentQuestionIndex].setSelectedOptions(ans);
        AnswersInformation[currentQuestionIndex].setAnswer(userAnswers);
        if (!AnswersInformation[currentQuestionIndex].getAnswer().isEmpty()){
            AnswersInformation[currentQuestionIndex].setAnswerGiven(true); // Устанавливаем флаг, что ответ был дан
        }

        if (AnswersInformation[currentQuestionIndex].isAnswerCorrect()){

            AnswersInformation[currentQuestionIndex].setResult("correctAnswer");

            Log.d("", "correctAnswers++" + correctAnswers);
        } else if (AnswersInformation[currentQuestionIndex].isPartialAnswerCorrect()){
            AnswersInformation[currentQuestionIndex].setResult("partialAnswer");
            Log.d("", "partialAnswer++" + partialAnswer);
        } else {
            Log.d("", "failedanswer++" + failedAnsweers);
            AnswersInformation[currentQuestionIndex].setResult("failedAnswer");
        }

        Log.d("Stack", "Stack size: " + userAnswersStack.peek() + " " + userAnswersStack.size());
        Log.d("UserAnswer", "User's Answer: " + answer.toString());
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

    // Метод для перемешивания массива
    private static void shuffleArray(int[] array) {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void SetBlueColors(int color){
        Answer1.setBackgroundColor(color);
        setButtonState(Answer1, false);
        answer1 = false;
        Answer2.setBackgroundColor(color);
        setButtonState(Answer2, false);
        answer2 = false;
        Answer3.setBackgroundColor(color);
        setButtonState(Answer3, false);
        answer3 = false;
        Answer4.setBackgroundColor(color);
        setButtonState(Answer4, false);
        answer4 = false;
    }

}