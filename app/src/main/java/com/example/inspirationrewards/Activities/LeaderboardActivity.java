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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LeaderboardActivity";
   // private User user = new User();

    private RecyclerView recyclerView;
    private UserAdapter mAdapter;
    private List<User> aUsers = new ArrayList<>();
    private String[] aLoginData = new String[2];
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inspiration Leaderboard");
        Intent intent = getIntent();
        aLoginData = intent.getStringArrayExtra("User Login Data");
        currentUser = intent.getStringExtra("Current User");

        recyclerView = findViewById(R.id.userRecycler);
        recyclerView.setHasFixedSize(true);
        mAdapter = new UserAdapter(aUsers, this);
        recyclerView.setAdapter(mAdapter);

        asyncGetAllProfiles();

    }

    public void asyncGetAllProfiles(){
        new GetAllProfilesAPIAsyncTask(this, aLoginData).execute();
    }

    public void sendResults(String result, String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                User user = new User();
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Log.d(TAG, "sendResultsLeaderboard: " + explrObject.getString("rewards"));
                String sJSONUserName = explrObject.getString("username");
                user.setFirstName(explrObject.getString("firstName"));
                user.setLastName(explrObject.getString("lastName"));
                user.setUserName(sJSONUserName);
                user.setLocation(explrObject.getString("location"));
                user.setDepartment(explrObject.getString("department"));
                user.setPosition(explrObject.getString("position"));
                user.setPointsToAward(explrObject.getInt("pointsToAward"));
                user.setStory(explrObject.getString("story"));
                user.setImage(explrObject.getString("imageBytes"));

                try {
                    String sAwards = explrObject.getString("rewards");
                    JSONArray jsonaRewards = new JSONArray(sAwards);
                    int iTotalReward = 0;

                    for(int j=0; j < jsonaRewards.length(); j++){
                        JSONObject jsonoReward = jsonaRewards.getJSONObject(j);
                        int iValue = jsonoReward.getInt("value");
                        iTotalReward += iValue;
                    }

                    user.setPointsAwarded(iTotalReward);

                }catch(JSONException e){

                }
                aUsers.add(user);
            }
            sortUserList();
            mAdapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
           // setViews();
        } catch (JSONException e){
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }

    public void sortUserList(){
        Collections.sort(aUsers, new Comparator<User>() {
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getPointsAwarded(), u1.getPointsAwarded());
            }
        });
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
        intentAwardActivity.putExtra("Current User", currentUser);
        startActivity(intentAwardActivity);
    }


}
