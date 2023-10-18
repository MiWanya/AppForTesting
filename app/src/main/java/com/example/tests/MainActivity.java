package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void StartTesting(View view) {
        // Переход на тестирование
        new DownloadTxtFile(this).execute();
        Intent intent = new Intent(this, MainTextAcrivity.class);
        startActivity(intent);
    }
}
