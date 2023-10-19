package com.example.tests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DownloadTxtFile extends AsyncTask<Void, Void, Void> {
    private Context context;

    public DownloadTxtFile(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String fileURL = "http://www.gutenberg.org/files/11/11-0.txt"; // Замените URL на нужный
        String fileName = "questions.txt"; // Имя файла для сохранения во внутреннем хранилище

        try {
            // Открываем файл для записи во внутреннем хранилище приложения
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            InputStream inputStream = new java.net.URL(fileURL).openStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Вызывается после завершения фоновой задачи, здесь можно выполнить действия после завершения загрузки и сохранения файла
        try {
            FileInputStream inputStream = context.openFileInput("questions.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            String fileContents = sb.toString();

            // Теперь вы можете вывести содержимое файла в лог
            Log.d("FileContents", fileContents);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
