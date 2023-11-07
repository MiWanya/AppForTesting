package com.example.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import android.os.AsyncTask;

public class FileDownloader {
    private final String url;
    private final String targetFilePath;

    public FileDownloader(String url, String targetFilePath) {
        this.url = url;
        this.targetFilePath = targetFilePath;
    }

    public void startDownload() {
        new DownloadFileTask().execute(url);
    }

    private class DownloadFileTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                URL fileUrl = new URL(urls[0]);
                InputStream inputStream = fileUrl.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();

                File questionsFile = new File(targetFilePath);
                FileOutputStream fos = new FileOutputStream(questionsFile);
                fos.write(stringBuilder.toString().getBytes());
                fos.close();

                return true; // Успешное завершение загрузки
            } catch (IOException e) {
                e.printStackTrace();
                return false; // Произошла ошибка при загрузке
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // Загрузка завершена успешно
            } else {
                // Произошла ошибка при загрузке
            }
        }
    }
}
