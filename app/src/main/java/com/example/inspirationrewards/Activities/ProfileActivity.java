package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.example.inspirationrewards.AsyncTasks.GetAllProfilesAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private String TAG = "ProfileActivity";
    private User user = new User();
    private TextView name;
    private TextView userName;
    private TextView location;
    private TextView pointsAwarded;
    private TextView department;
    private TextView position;
    private TextView pointsToAward;
    private TextView story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Your Profile");
        name = findViewById(R.id.tvProfileName);
        userName = findViewById(R.id.tvProfileUserName);
        location = findViewById(R.id.tvLocation);
        pointsAwarded = findViewById(R.id.tvPointsAwarded);
        department = findViewById(R.id.tvDepartment);
        position = findViewById(R.id.tvPosition);
        pointsToAward = findViewById(R.id.tvPointsToAward);
        story = findViewById(R.id.tvStory);
        Intent intent = getIntent();
        if (intent.hasExtra("User Object")) {
            user = (User)intent.getSerializableExtra("User Object");
            Log.d(TAG, "onCreate: " + user.getLastName());
            String sFullName = user.getLastName() + ", " + user.getFirstName();
            name.setText(sFullName);
            userName.setText(user.getUsername());
            location.setText(user.getLocation());
            String sPointsAwarded = Integer.toString(user.getPointsAwarded());
            pointsAwarded.setText(sPointsAwarded);
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            String sPointsToAward = Integer.toString(user.getPointsToAward());
            pointsToAward.setText(sPointsToAward);
            story.setText(user.getStory());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_profile_menu, menu);
        return true;
    }

    public void asyncGetAllProfiles(){
        new GetAllProfilesAPIAsyncTask(this).execute();
    }

    public void sendResults(String result, String json) {
        try {
            Log.d(TAG, "sendResults: " + json);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Log.d(TAG, "sendResults: " + explrObject);
                String sJSONUserName = explrObject.getString("username");
                if (sJSONUserName.equals("user2")){
//                    Log.d(TAG, "sendResults: " + explrObject.getString("firstName"));
                    user.setFirstName(explrObject.getString("firstName"));
                    user.setLastName(explrObject.getString("lastName"));
                    user.setUserName(sJSONUserName);
                    user.setLocation(explrObject.getString("location"));
                    user.setDepartment(explrObject.getString("department"));
                    user.setPosition(explrObject.getString("position"));
                    user.setPointsToAward(explrObject.getInt("pointsToAward"));
                    user.setStory(explrObject.getString("story"));
                    user.setImage(explrObject.getString("imageBytes"));

                    if(explrObject.getString("rewards") == null){
                        user.setPointsAwarded(0);
                    } else {
                        user.setPointsAwarded(explrObject.getInt("rewards"));
                    }
                    break;
                }
            }
            setViews();
        } catch (JSONException e){
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }

    public void setViews(){

    }

}
