package com.example.tests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DownloadTxtFile extends AsyncTask<String, Void, Void> {
    private Context context;

    public DownloadTxtFile(Context context) {
        this.context = context;
    }

    public void overwriteQuestionsFileFromUrl() {
        try {
            // URL для загрузки данных из GitHub
            String githubUrl = "https://github.com/MiWanya/AppForTesting/blob/master/app/src/main/assets/questions.txtx";

            // Открываем соединение с URL и получаем входной поток данных
            URL url = new URL(githubUrl);
            InputStream inputStream = url.openStream();

            // Читаем данные из входного потока
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            // Закрываем входной поток
            inputStream.close();

            // Записываем скачанные данные в целевой файл в папке sampledata
            File questionsFile = new File(context.getFilesDir().getParent() + "/sampledata/questions.txt");
            FileOutputStream fos = new FileOutputStream(questionsFile);
            fos.write(stringBuilder.toString().getBytes());
            fos.close();


            // Файл успешно перезаписан
        } catch (IOException e) {
            e.printStackTrace();
            // Возникла ошибка при скачивании и записи файла
        }
    }

    @Override
    protected Void doInBackground(@NonNull String... params) {
        String fileURL = params[0]; // URL-адрес файла для загрузки
        String fileName = "downloaded.txt"; // Имя файла для сохранения во внутреннем хранилище

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URL url = new URL(fileURL);
            connection = (HttpURLConnection) url.openConnection();

            // Следим за перенаправлениями (код HTTP 302)
            while (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
                    || connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER) {
                String newUrl = connection.getHeaderField("Location");
                url = new URL(newUrl);
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } else {
                Log.e("DownloadTxtFile", "Ошибка HTTP: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e("DownloadTxtFile", "Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                Log.e("DownloadTxtFile", "Ошибка при закрытии потоков: " + e.getMessage());
            }
        }

        return null;
    }
}
