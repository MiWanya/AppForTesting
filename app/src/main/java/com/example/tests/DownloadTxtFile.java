package com.example.tests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTxtFile extends AsyncTask<String, Void, Void> {
    private Context context;

    public DownloadTxtFile(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
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
