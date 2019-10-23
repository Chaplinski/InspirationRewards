package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnLongClickListener {
    private String TAG = "LeaderboardActivity";
   // private User user = new User();

    private RecyclerView recyclerView;
    private UserAdapter mAdapter;
    private List<User> aUsers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inspiration Leaderboard");

        recyclerView = findViewById(R.id.userRecycler);
        mAdapter = new UserAdapter(aUsers, this);
        recyclerView.setAdapter(mAdapter);

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
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        final int position = recyclerView.getChildLayoutPosition(v);
        Intent intentAwardActivity = new Intent(LeaderboardActivity.this, AwardActivity.class);
        intentAwardActivity.putExtra("User Object", aUsers.get(position));
        startActivity(intentAwardActivity);
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        Toast.makeText(this, "Long click", Toast.LENGTH_SHORT).show();
//         use this method to delete a stock
//        final TextView textSymbol = v.findViewById(R.id.textSymbol);
//        final String thisSymbol = textSymbol.getText().toString();
//        final int position = recyclerView.getChildLayoutPosition(v);
////        Toast.makeText(this, thisSymbol, Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Delete Stock");
//        builder.setMessage("Delete Stock Symbol " + thisSymbol + "?");
//        builder.setIcon(R.drawable.trashcan);
//
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing but close the dialog
//                databaseHandler.deleteStock(thisSymbol);
//                mAdapter.removeItem(position);
//                aDBLoadedStocks.remove(thisSymbol);
//                aStoredStockSymbols = getStoredStockSymbols();
//            }
//        });
//
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
        return false;

    }
}
