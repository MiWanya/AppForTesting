package com.example.tests;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTxtFile {

    private static final String TAG = "DownloadTxtFile";

    public static void download() {
        new DownloadTask().execute();
    }

    private static class DownloadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String fileURL = "http://www.gutenberg.org/files/11/11-0.txt";
            String savePath = Environment.getExternalStorageDirectory().getPath() + "/question.txt";

            try {
                URL url = new URL(fileURL);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();

                File file = new File(savePath);
                FileOutputStream outputStream = new FileOutputStream(file);

                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                Log.d(TAG, "Файл успешно скачан и сохранен в " + savePath);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при скачивании файла: " + e.getMessage());
            }
            return null;
        }
    }
}
