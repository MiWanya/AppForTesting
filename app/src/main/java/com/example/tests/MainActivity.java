package com.example.tests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    EditText UserName, UserSurname, UserNickName, UserCity;
    Button StartTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        UserName = findViewById(R.id.Namefield);
        UserSurname = findViewById(R.id.surnameField);
        UserNickName = findViewById(R.id.nicknameField);
        UserCity = findViewById(R.id.cityField);
        StartTesting = findViewById(R.id.startButton);

        StartTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserNameText = UserName.getText().toString().trim();
                String UserSurnameText = UserSurname.getText().toString().trim();
                String UserNickNameText = UserNickName.getText().toString().trim();
                String UserCityText = UserCity.getText().toString().trim();

                if (UserNameText.trim().isEmpty() || UserSurnameText.trim().isEmpty() || UserNickNameText.trim().isEmpty() || UserCityText.trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    // Создаем Intent
                    Intent intent = new Intent(MainActivity.this, MainTextAcrivity.class);

                    // Передаем данные в Intent
                    intent.putExtra("USERNAME", UserNameText);
                    intent.putExtra("USERSURNAME", UserSurnameText);
                    intent.putExtra("USERNICKNAME", UserNickNameText);
                    intent.putExtra("USERCITY", UserCityText);

                    // Запускаем новую активность
                    startActivity(intent);
                }
            }
        });

    }
}
