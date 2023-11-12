package com.example.tests;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {

    private static final String BASE_URL = "https://drive.google.com/"; // добавьте символ "/" в конец URL

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        } catch (Exception e){
            Log.d("Start FileDownloadApi", "fileDownloadApi " + e.getMessage());
            return retrofit;
        }
    }
}

