package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

public class AwardActivity extends AppCompatActivity {
    private String TAG = "AwardActivity";
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Franklin Moreno");
        Intent intent = getIntent();
        Log.d(TAG, "onCreate whoop: ");
        if(intent.hasExtra("User Object")){
            user = (User)intent.getSerializableExtra("User Object");
            Log.d(TAG, "onCreate: " + user.getStory());
            Log.d(TAG, "onCreatehere: ");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }
}
