package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inspirationrewards.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
