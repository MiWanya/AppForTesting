package com.example.tests;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTxtFile {
    public static void download() {
        String fileURL = "http://www.gutenberg.org/files/11/11-0.txt"; // Замените URL на нужный вам
        String savePath = "src//assets/questions.txt"; // Относительный путь внутри проекта

        try {
            URL url = new URL(fileURL);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            // Создаем файл и поток для записи
            FileOutputStream outputStream = new FileOutputStream(savePath);

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("Файл успешно скачан и сохранен внутри проекта.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
