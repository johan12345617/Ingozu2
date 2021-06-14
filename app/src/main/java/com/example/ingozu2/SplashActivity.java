package com.example.ingozu2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = new Intent(this,ListaPrincipal.class);
        startActivity(i);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activvity);
    }
}