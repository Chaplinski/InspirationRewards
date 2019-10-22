package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

public class EditProfileActivity extends AppCompatActivity {

    private String TAG = "EDITPROFILEACTIVITY";
    private User user = new User();
    private EditText username;
    private EditText password;
    private CheckBox isAdmin;
    private EditText firstName;
    private EditText lastName;
    private EditText department;
    private EditText position;
    private EditText story;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");

        Intent intent = getIntent();
        if (intent.hasExtra("User Object")) {
            user = (User)intent.getSerializableExtra("User Object");
            Log.d(TAG, "onCreate here : " + user.getDepartment());
            username = findViewById(R.id.etEPUserName);
            password = findViewById(R.id.etEPLoginPassword);
            isAdmin = findViewById(R.id.cbEPIsAdmin);
            firstName = findViewById(R.id.etEPFirstName);
            lastName = findViewById(R.id.etEPLastName);
            department = findViewById(R.id.etEPDepartment);
            position = findViewById(R.id.etEPPosition);
            story = findViewById(R.id.etEPAboutUser);
            username.setFocusable(false);
            username.setClickable(false);
            username.setEnabled(false);
            username.setText(user.getUsername());
            password.setText(user.getPassword());
            isAdmin.setChecked(user.getAdmin());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            story.setText(user.getStory());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }
}
