package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inspirationrewards.R;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LOGINACTIVITY";
    private EditText userName;
    private EditText password;
    private Button login;
    private CheckBox rememberCredentials;

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

    public void sendResults(String s) {
//        ((TextView) findViewById(R.id.resultsText)).setText(s);
        Log.d(TAG, "sendResults: " + s);


//        if(s.contains("SUCCESS")) {
//            Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        }
    }


}
