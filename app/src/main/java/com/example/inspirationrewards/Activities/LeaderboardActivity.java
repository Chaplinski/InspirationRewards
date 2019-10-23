package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.inspirationrewards.AsyncTasks.GetAllProfilesAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaderboardActivity extends AppCompatActivity {
    private String TAG = "LeaderboardActivity";
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inspiration Leaderboard");

        asyncGetAllProfiles();

    }

    public void asyncGetAllProfiles(){
        new GetAllProfilesAPIAsyncTask(this).execute();
    }

    public void sendResults(String result, String json) {
        Log.d(TAG, "sendResultsLeaderboard: " + result);
        Log.d(TAG, "sendResultsLeaderboard: " + json);
        try {
            Log.d(TAG, "sendResults: " + json);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Log.d(TAG, "sendResultsLeaderboard: " + explrObject);
                String sJSONUserName = explrObject.getString("username");
//                if (sJSONUserName.equals("user2")){
////                    Log.d(TAG, "sendResults: " + explrObject.getString("firstName"));
//                    user.setFirstName(explrObject.getString("firstName"));
//                    user.setLastName(explrObject.getString("lastName"));
//                    user.setUserName(sJSONUserName);
//                    user.setLocation(explrObject.getString("location"));
//                    user.setDepartment(explrObject.getString("department"));
//                    user.setPosition(explrObject.getString("position"));
//                    user.setPointsToAward(explrObject.getInt("pointsToAward"));
//                    user.setStory(explrObject.getString("story"));
//                    user.setImage(explrObject.getString("imageBytes"));
//
//                    if(explrObject.getString("rewards") == null){
//                        user.setPointsAwarded(0);
//                    } else {
//                        user.setPointsAwarded(explrObject.getInt("rewards"));
//                    }
//                    break;
//                }
            }
           // setViews();
        } catch (JSONException e){
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }
}
