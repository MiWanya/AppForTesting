package com.example.tests;

import android.content.Context;
import android.os.AsyncTask;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    }
}
