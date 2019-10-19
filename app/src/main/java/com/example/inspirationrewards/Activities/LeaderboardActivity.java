package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inspirationrewards.R;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inspiration Leaderboard");
    }
}
