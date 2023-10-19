package com.example.tests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadTxtFile extends AsyncTask<Void, Void, String> {
    private Context context;

    public ReadTxtFile(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            FileInputStream inputStream = context.openFileInput("downloaded.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            inputStream.close();
            return sb.toString();
        } catch (IOException e) {
            Log.e("ReadTxtFile", "Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String fileContents) {
        if (fileContents != null) {
            // Выводим содержимое файла в лог
            Log.d("FileContents", fileContents);

            // Здесь вы можете также обновить пользовательский интерфейс или выполнить другие операции с данными
        }
    }
}