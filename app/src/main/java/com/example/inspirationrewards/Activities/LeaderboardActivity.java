package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.GetAllProfilesAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.Other.UserAdapter;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LeaderboardActivity";
   // private User user = new User();

    private RecyclerView recyclerView;
    private UserAdapter mAdapter;
    private List<User> aUsers = new ArrayList<>();
    private String[] aLoginData = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inspiration Leaderboard");
        Intent intent = getIntent();
        aLoginData = intent.getStringArrayExtra("User Login Data");

        recyclerView = findViewById(R.id.userRecycler);
        mAdapter = new UserAdapter(aUsers, this);
        recyclerView.setAdapter(mAdapter);

        asyncGetAllProfiles();

    }

    public void asyncGetAllProfiles(){
        new GetAllProfilesAPIAsyncTask(this).execute();
    }

    public void sendResults(String result, String json) {
//        Log.d(TAG, "sendResultsLeaderboard: " + result);
//        Log.d(TAG, "sendResultsLeaderboard: " + json);
        try {
//            Log.d(TAG, "sendResults: " + json);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                User user = new User();
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Log.d(TAG, "sendResultsLeaderboard: " + explrObject);
                String sJSONUserName = explrObject.getString("username");
                user.setFirstName(explrObject.getString("firstName"));
                user.setLastName(explrObject.getString("lastName"));
                user.setUserName(sJSONUserName);
                user.setLocation(explrObject.getString("location"));
                user.setDepartment(explrObject.getString("department"));
                user.setPosition(explrObject.getString("position"));
                user.setPointsToAward(explrObject.getInt("pointsToAward"));
                user.setStory(explrObject.getString("story"));
                Log.d(TAG, "sendResults: " + explrObject.getString("imageBytes"));
                user.setImage(explrObject.getString("imageBytes"));
                user.setPointsAwarded(0);
                aUsers.add(user);




            }
            mAdapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
           // setViews();
        } catch (JSONException e){
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        final int position = recyclerView.getChildLayoutPosition(v);
        Intent intentAwardActivity = new Intent(LeaderboardActivity.this, AwardActivity.class);
        intentAwardActivity.putExtra("User Object", aUsers.get(position));
        intentAwardActivity.putExtra("User Login Data", aLoginData);
        startActivity(intentAwardActivity);
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }


}
