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

    List<Question> questionsList = new ArrayList<>();
    QuestionType type = null;
    int CurrentQuestion = 0;
    private Map<Button, Boolean> buttonStates = new HashMap<>();
    private boolean isClickedButton = false;
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
        QuestionType type = null;

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
                                    String[] parts = content.split(",");

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
                                } else if (line.startsWith("* ")) {
                                    String content = line.substring(2);
                                    String[] parts = content.split(",");

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

                            // Закрыть потоки, когда они больше не нужны
                            bufferedReader.close();
                            reader.close();
                            inputStream.close();

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
                if (currentQuestionIndex < 51) {
                    currentQuestionIndex++; // Переключаемся на следующий вопрос

                    if (currentQuestionIndex < questionsList.size()) {
                        Question question = questionsList.get(currentQuestionIndex);
                        questionTextView.setText(question.GetQuestionText());
                        Question nextquestion;
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
                        optionTextView.setText(optionsText.toString());
                    } else {
                        // Покажите диалоговое окно
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                // Последний вопрос
                if (currentQuestionIndex == 50){
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
                    changeButtonColorSingle((Button) view);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    changeButtonColorMulti((Button) view);
                }
            }
        });

        Answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();
                if (type == QuestionType.SINGLE_CHOICE){
                    changeButtonColorSingle((Button) view);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    changeButtonColorMulti((Button) view);
                }
            }
        });

        Answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();
                if (type == QuestionType.SINGLE_CHOICE){
                    changeButtonColorSingle((Button) view);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    changeButtonColorMulti((Button) view);
                }
            }
        });

        Answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question = questionsList.get(currentQuestionIndex);
                QuestionType type = question.getQuestionType();
                if (type == QuestionType.SINGLE_CHOICE){
                    changeButtonColorSingle((Button) view);
                } else if (type == QuestionType.MULTIPLE_CHOICE) {
                    changeButtonColorMulti((Button) view);
                }
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

    private void changeButtonColorMulti(Button button) {

        Log.d("ButtonClick", "Button clicked");
        boolean isClicked = getButtonState(button);
        if (!isClicked) {
            button.setBackgroundColor(colorGold);
            setButtonState(button, true);
            Log.d("ButtonState", "Changed to gold");
        } else {
            button.setBackgroundColor(colorBlue);
            setButtonState(button, false);
            Log.d("ButtonState", "Changed to blue");
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

    private void loadQuestions() {

    }

    //@Override
    //protected void onStart() {
//
  //      if (!questionsList.isEmpty()) {
    //        if (currentQuestionIndex < questionsList.size()) {
      //          Log.d("Questions", "Number of questions: " + questionsList.size());
        //        Question question = questionsList.get(currentQuestionIndex);
          //      questionTextView.setText(question.GetQuestionText());
//
  //              // Создаем строку для вариантов ответов
    //            StringBuilder optionsText = new StringBuilder();
      //          List<String> options = question.getOptions();
        //        for (int i = 0; i < options.size(); i++) {
          //          optionsText.append((i + 1) + ". " + options.get(i)); // Нумерация вариантов ответов
            //        if (i < options.size() - 1) {
              //          optionsText.append("\n"); // Перенос строки между вариантами
                //    }
//                }
  //              // Устанавливаем текст вариантов ответов
    //            optionTextView.setText(optionsText.toString());
      //      } else {
        //        // Покажите диалоговое окно
          //      AlertDialog dialog = builder.create();
            //    dialog.show();
//            }
  //      }
    //    super.onStart();
   // }
}