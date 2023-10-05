package com.example.tests;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionConvertor {
    public static String convertTxtToJson(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<JSONObject> questions = new ArrayList<>();
            JSONObject currentQuestion = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("Вопрос ")) {
                        if (currentQuestion != null) {
                            questions.add(currentQuestion);
                        }
                        currentQuestion = new JSONObject();
                        currentQuestion.put("question", line);
                        currentQuestion.put("answers", new JSONArray());
                    } else if (line.startsWith("Правильный ответ: ")) {
                        if (currentQuestion != null) {
                            currentQuestion.put("correctAnswer", line.replace("Правильный ответ: ", ""));
                        }
                    } else {
                        if (currentQuestion != null) {
                            JSONArray answers = currentQuestion.getJSONArray("answers");
                            answers.put(line);
                        }
                    }
                }
            }

            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }

            return new JSONArray(questions).toString(4); // 4 - для красивого форматирования
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
