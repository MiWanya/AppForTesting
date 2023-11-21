package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;
import android.os.AsyncTask;
import android.util.Log;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void StartTesting(View view) {
        Intent intent = new Intent(this, MainTextAcrivity.class);
        startActivity(intent);
    }


}
