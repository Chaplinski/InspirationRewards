package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.inspirationrewards.AsyncTasks.GetAllProfilesAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private String TAG = "ProfileActivity";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Your Profile");
        asyncGetAllProfiles();

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
                        user.setRewards(0);
                    } else {
                        user.setRewards(explrObject.getInt("rewards"));
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
