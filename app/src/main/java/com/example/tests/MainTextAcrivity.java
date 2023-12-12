package com.example.tests;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class MainTextAcrivity extends AppCompatActivity{
    Stack userAnswersStack = new Stack();
    private EmailSender emailSender;
    int correctAnswers = 0, failedAnsweers = 0, partialAnswer = 0;
    List<UserAnswer> userAnswersList = new ArrayList<>();
    Boolean answer1 = false,
            answer2 = false,
            answer3 = false,
            answer4 = false;
    private Map<Button, Boolean> buttonStates = new HashMap<>();
    private boolean isClickedButton = false;
    Random random = new Random();
    private Button lastClickedButton; // Последняя нажатая кнопка
    Button PreviousQuestion, NextQuestion, Answer1, Answer2, Answer3, Answer4;
    int colorGray2, colorBlue, colorGold;
    int currentQuestionIndex = 48; // Индекс текущего вопроса
    List<Question> questionsList = new ArrayList<>();
    int AllQuestions = 49;
    String USERNAME, USERSURNAME, USERNICKNAME, USERCITY;
    int[] array = new int[50];
    String toAddress;
    String subject;
    String body;

    ProgressBar progressBar;
    RelativeLayout RelativeButtons, RelativeQuestions;
    @Override
    public void onBackPressed() {
        showConfirmationDialog();
    }
    private void sendEmail() {
        // Your existing sendEmail method code here
        String toAddress = "dommafiatest@outlook.com";
        String subject = "Данные: " + USERNAME + " " + USERSURNAME + " " + USERCITY + " " + USERNICKNAME;
        String body = "Ответы: " + "\n Привальные ответы: " + correctAnswers + "\n Частично правильные ответы: " + partialAnswer + "\n Неправльные ответы: " + failedAnsweers;

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
                                    Intent intent = new Intent(MainTextAcrivity.this, MainActivity.class);
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

        emailSender = new EmailSender("dommafiatest@outlook.com", "ongtsdpjggyemjqe");

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
        progressBar = findViewById(R.id.progressBar);
        RelativeQuestions = findViewById(R.id.Question);
        RelativeButtons = findViewById(R.id.RelativeButtons);

        // Показать ProgressBar при начале загрузки данных
        progressBar.setVisibility(View.VISIBLE);

        // Создайте диалоговое окно с вопросом
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Завершить тестирование?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Log.i("Сообщение", "Сообщение еще не отправлено");

                progressBar.setVisibility(View.VISIBLE);
                RelativeButtons.setVisibility(View.GONE);
                RelativeQuestions.setVisibility(View.GONE);

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
        Log.d("FileDownloadApi", "fileDownloadApi succsed");

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
                                    // This is a line with the question text
                                    question = line.substring(2);
                                } else if (line.startsWith("^ ")) {
                                    // This is a line with options and correct answers
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
                                    // This is a line with correct answers for multiple choice questions
                                    String content = line.substring(2);
                                    String[] parts = content.split(",");

                                    List<String> questionCorrectAnswers = new ArrayList<>();

                                    for (String part : parts) {
                                        questionCorrectAnswers.add(part);
                                    }

                                    correctAnswers.addAll(questionCorrectAnswers);
                                    if (parts.length > 1) {
                                        questionType = QuestionType.MULTIPLE_CHOICE;
                                    } else {
                                        questionType = QuestionType.SINGLE_CHOICE;
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
                            } else {
                                // Покажите диалоговое окно
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            Log.d("Questions count", "Count of questions: " + question.length());

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

                    String lastAnswer = userAnswersStack.pop();

                    if (lastAnswer == "correctAnswer"){
                        correctAnswers--;
                    } else if (lastAnswer == "failedAnswer") {
                        failedAnsweers--;
                    } else if (lastAnswer == "partialAnswer"){
                        partialAnswer--;
                    }
                } else {
                    showConfirmationDialog();
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
                answer1 = false;
                answer2 = false;
                answer3 = false;
                answer4 = false;
                setButtonState(Answer1, false);
                setButtonState(Answer2, false);
                setButtonState(Answer3, false);
                setButtonState(Answer4, false);
            }
        });

        NextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question AnserQuestion = questionsList.get(array[currentQuestionIndex]);
                QuestionType type = AnserQuestion.getQuestionType();

                Log.d("QuestionList size", "Size: " + questionsList.size());

                Log.d("Answer ", "Answer1: " + answer1);
                Answer(questionsList, type);
                answer1 = false;
                answer2 = false;
                answer3 = false;
                answer4 = false;
                setButtonState(Answer1, false);
                setButtonState(Answer2, false);
                setButtonState(Answer3, false);
                setButtonState(Answer4, false);

                currentQuestionIndex++; // Переключаемся на следующий вопрос

                Log.d("", " " + currentQuestionIndex + questionsList.size());

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
                        Log.d("Questions", "Question type: " + type + " Options: " + options);
                    } else {
                        // Покажите диалоговое окно
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Log.d("Result of testing", "Correct answers: " + correctAnswers + " Failed answers: " + failedAnsweers + " Parial answers: " + partialAnswer + " Percent of correct answers: " + Percent());
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
                        Log.d(" ", "answer: " + answer1);
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
                        Log.d(" ", "answer: " + answer1);
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
            }
        });

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

        Question question = questionsList.get(array[currentQuestionIndex]);

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
        } else if (UserAnswer.isPartialMatch(answer, correctAnswer)){
            partialAnswer++;
            userAnswersStack.push("partialAnswer");
            Log.d("", "partialAnswer++" + partialAnswer);
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

    private void delayedSendEmail() {
        // Delay for 10 seconds before calling sendEmail
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendEmail();
            }
        }, 10000); // 10000 milliseconds = 10 seconds
    }

}