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

import com.example.inspirationrewards.AsyncTasks.CreateProfileAPIAsyncTask;
import com.example.inspirationrewards.AsyncTasks.LoginAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;

public class LoginActivity extends AppCompatActivity {

    private static final int PASS_USER_OBJECT_REQUEST_CODE = 101;
    private String TAG = "LOGINACTIVITY";
    private EditText userName;
    private EditText password;
    private Button login;
    private User user = new User();
    private CheckBox rememberCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = findViewById(R.id.etLoginUsername);
        password = findViewById(R.id.etLoginPassword);
        login = findViewById(R.id.buttonLogin);
        rememberCredentials = findViewById(R.id.cbRememberCredentials);

        login.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                attemptLogin();
            }
        });
    }

    public void attemptLogin(){
        String sUserName = userName.getText().toString();
        String sPassword = password.getText().toString();
        String[] aData = {sUserName, sPassword};
        asyncLogin(aData);

    }

    public void asyncLogin(String[] aData){
        new LoginAPIAsyncTask(this, aData).execute();
    }

    public void goProfileCreate(View v){
        Intent intentCreateProfile = new Intent(LoginActivity.this, CreateProfileActivity.class);
        startActivity(intentCreateProfile);
    }

    public void sendResults(String result, String json) {
//        ((TextView) findViewById(R.id.resultsText)).setText(s);
        Log.d(TAG, "sendResults: " + result);
        Log.d(TAG, "sendResults: " + json);
        if(result.equals("SUCCESS")){
            //TODO show user profile screen
            try {
                JSONObject jsonObject = new JSONObject(json);
                Log.d(TAG, "sendResults: " + jsonObject.getString("firstName"));
                user.setFirstName(jsonObject.getString("firstName"));
                user.setLastName(jsonObject.getString("lastName"));
                user.setUserName(jsonObject.getString("username"));
                user.setLocation(jsonObject.getString("location"));
                user.setDepartment(jsonObject.getString("department"));
                user.setPosition(jsonObject.getString("position"));
                user.setPointsToAward(jsonObject.getInt("pointsToAward"));
                user.setStory(jsonObject.getString("story"));
                user.setImage(jsonObject.getString("imageBytes"));

                Intent intentNoteCreation = new Intent(LoginActivity.this, ProfileActivity.class);
                intentNoteCreation.putExtra("User Object", user);
                startActivityForResult(intentNoteCreation, PASS_USER_OBJECT_REQUEST_CODE);
            }catch (JSONException err){
                Log.d("Error", err.toString());
            }
            Log.d(TAG, "sendResults: " + json);
        } else{

        }


//        if(s.contains("SUCCESS")) {
//            Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        }
    }


}
