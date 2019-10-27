package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.GetAllProfilesAPIAsyncTask;
import com.example.inspirationrewards.Classes.Reward;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.Other.RewardAdapter;
import com.example.inspirationrewards.Other.UserAdapter;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private TextView rewardHistory;
    private ImageView image;
    private Bitmap userBitmap;
    private String[] aLoginData = new String[2];
    private List<Reward> aRewards = new ArrayList<>();
    private RewardAdapter mAdapter;
    private RecyclerView recyclerView;

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
        image = findViewById(R.id.profileImage);
        rewardHistory = findViewById(R.id.tvRewardHistory);
        Intent intent = getIntent();
        aLoginData = intent.getStringArrayExtra("User Login Data");
        Log.d(TAG, "onCreate: " + aLoginData[0]);
        Log.d(TAG, "onCreate: " + aLoginData[1]);
        if (intent.hasExtra("User Object")) {
            user = (User)intent.getSerializableExtra("User Object");
            String sFullName = user.getLastName() + ", " + user.getFirstName();
            name.setText(sFullName);
            userName.setText(user.getUsername());
            location.setText(user.getLocation());
            String sPointsAwarded = Integer.toString(user.getPointsAwarded());
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            String sPointsToAward = Integer.toString(user.getPointsToAward());
            pointsToAward.setText(sPointsToAward);
            story.setText(user.getStory());
            if(intent.hasExtra("BUNDLE")) {
                Bundle args = intent.getBundleExtra("BUNDLE");
                aRewards = (ArrayList<Reward>) args.getSerializable("Rewardlist");

                recyclerView = findViewById(R.id.RewardRecycler);
                mAdapter = new RewardAdapter(aRewards, this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                int count = mAdapter.getItemCount();
                rewardHistory.setText("Reward History(" + count + "):");
                getPointsAwarded();
            }

            Log.d(TAG, "onCreate: " + aRewards.size());

            userBitmap = StringToBitMap(user.getImage());
            image.setImageBitmap(userBitmap);
        }

    }

    private void getPointsAwarded(){
        int iTotalPoints = 0;
        for(int i=0; i < aRewards.size(); i++){
            Reward reward = aRewards.get(i);
            int iPoints = reward.getRewardPoints();
            iTotalPoints += iPoints;
        }
        String sPoints = Integer.toString(iTotalPoints);
        pointsAwarded.setText(sPoints);
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sTitle = item.getTitle().toString();
        switch (item.getItemId()) {
            case R.id.menuEdit:
                if(sTitle.equals("Edit")) {
                    Intent intentEditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intentEditProfile.putExtra("User Object", user);
                    intentEditProfile.putExtra("User Login Data", aLoginData);
                    startActivity(intentEditProfile);
                }
            case R.id.menuViewLeaderboard:
                if(sTitle.equals("Leaderboard")) {
                    Intent intentLeaderBoard = new Intent(ProfileActivity.this, LeaderboardActivity.class);
                    intentLeaderBoard.putExtra("User Login Data", aLoginData);
                    startActivity(intentLeaderBoard);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



//    public void sendResults(String result, String json) {
//        try {
//            Log.d(TAG, "sendResults: " + json);
//            JSONArray jsonArray = new JSONArray(json);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject explrObject = jsonArray.getJSONObject(i);
//                Log.d(TAG, "sendResults: " + explrObject);
//                String sJSONUserName = explrObject.getString("username");
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
//            }
//            setViews();
//        } catch (JSONException e){
//            Log.e("MYAPP", "unexpected JSON exception", e);
//        }
//    }

//    public void setViews(){
//
//    }

}
