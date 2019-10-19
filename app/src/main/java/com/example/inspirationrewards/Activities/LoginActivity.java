package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inspirationrewards.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void goProfileCreate(View v){
        Intent intentCreateProfile = new Intent(LoginActivity.this, CreateProfileActivity.class);
        startActivity(intentCreateProfile);
    }
}
