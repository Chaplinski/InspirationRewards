package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.RewardsAPIAsyncTask;
import com.example.inspirationrewards.AsyncTasks.UpdateProfileAPIAsyncTask;
import com.example.inspirationrewards.Classes.Reward;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

public class AwardActivity extends AppCompatActivity {
    private String TAG = "AwardActivity";
    private User user = new User();
    private Reward reward = new Reward();
    private TextView name;
    private TextView pointsAwarded;
    private TextView department;
    private TextView position;
    private TextView story;
    private EditText pointsToAward;
    private EditText comment;
    private String[] aLoginData = new String[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        intent.getStringArrayExtra("User Login Data");
        aLoginData = intent.getStringArrayExtra("User Login Data");

        name = findViewById(R.id.aaName);
        pointsAwarded = findViewById(R.id.aaPointsAwarded);
        department = findViewById(R.id.aaDepartment);
        position = findViewById(R.id.aaPosition);
        story = findViewById(R.id.aaStory);
        pointsToAward = findViewById(R.id.aaPointsToSend);
        comment = findViewById(R.id.aaComment);

        if(intent.hasExtra("User Object")){
            user = (User)intent.getSerializableExtra("User Object");

            name.setText(user.getLastName() + ", " + user.getFirstName());
            pointsAwarded.setText("0");
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            story.setText(user.getStory());
            setTitle(user.getFirstName() + " " + user.getLastName());


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add Rewards Points?");
                builder.setMessage("Add rewards points for " + user.getFirstName() + " " + user.getLastName() + "?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String[] aData = getData();
                        asyncAddRewards(user, aData, aLoginData);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                    }
                });

                builder.setIcon(R.drawable.logo);
                AlertDialog alert = builder.create();
                alert.show();

//                Toast.makeText(this, "scoopy", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String[] getData(){

        String sPoints = pointsToAward.getText().toString();
        String sComment = comment.getText().toString();

        String[] aData = {sPoints, sComment};
        return aData;

    }

    public void asyncAddRewards(User updatedUser, String[] aData, String[] aLoginData){
        new RewardsAPIAsyncTask(this, updatedUser, aData, aLoginData).execute();
    }

    public void sendResults(String result, String json) {
        Log.d(TAG, "sendResults: " + result);
        Log.d(TAG, "sendResults: " + json);

    }
}
