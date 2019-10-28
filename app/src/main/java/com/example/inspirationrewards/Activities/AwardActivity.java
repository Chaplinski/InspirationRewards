package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.RewardsAPIAsyncTask;
import com.example.inspirationrewards.AsyncTasks.UpdateProfileAPIAsyncTask;
import com.example.inspirationrewards.Classes.Reward;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AwardActivity extends AppCompatActivity {
    private String TAG = "AwardActivity";
    private User user = new User();
    private Reward reward = new Reward();
    private TextView name;
    private TextView pointsAwarded;
    private TextView department;
    private TextView position;
    private TextView story;
    private TextView counter;
    private ImageView image;
    private EditText pointsToAward;
    private EditText comment;
    private Bitmap userBitmap;
    private String[] aLoginData = new String[2];
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        intent.getStringArrayExtra("User Login Data");
        aLoginData = intent.getStringArrayExtra("User Login Data");
        currentUser = intent.getStringExtra("Current User");

        name = findViewById(R.id.aaName);
        pointsAwarded = findViewById(R.id.aaPointsAwarded);
        department = findViewById(R.id.aaDepartment);
        position = findViewById(R.id.aaPosition);
        story = findViewById(R.id.aaStory);
        pointsToAward = findViewById(R.id.aaPointsToSend);
        comment = findViewById(R.id.aaComment);
        counter = findViewById(R.id.aaCharCounter);
        comment.addTextChangedListener(mTextEditorWatcher);
        image = findViewById(R.id.aaImageView);


        if(intent.hasExtra("User Object")){
            user = (User)intent.getSerializableExtra("User Object");

            name.setText(user.getLastName() + ", " + user.getFirstName());
            int iPoints = user.getPointsAwarded();
            String sPoints = Integer.toString(iPoints);
            pointsAwarded.setText(sPoints);
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            story.setText(user.getStory());
            setTitle(user.getFirstName() + " " + user.getLastName());
            userBitmap = StringToBitMap(user.getImage());
            image.setImageBitmap(userBitmap);


        }
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

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            String sCommentLength = String.valueOf(s.length());

            counter.setText("Comment: (" + sCommentLength + " of 80)");
        }

        public void afterTextChanged(Editable s) {
        }
    };

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
//                        if(!aData[0].equals("") && !aData[0].equals("0")) {
                            asyncAddRewards(user, aData, aLoginData);
//                        }


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
        String[] aData = new String[2];

        Log.d(TAG, "getData: points - " + sPoints);
        Log.d(TAG, "getData: comment - " + sComment);
        if(doesStringHaveValue(sPoints) && doesStringHaveValue(sComment)) {
            aData[0] = sPoints;
            aData[1] = sComment;
        } else if (doesStringHaveValue(sPoints)) {
            aData[0] = sPoints;
            aData[1] = "";
        } else if (doesStringHaveValue(sComment)){
            aData[0] = "0";
            aData[1] = sComment;
        }else {
            aData[0] = "0";
            aData[1] = "";
        }
        return aData;

    }

    public boolean doesStringHaveValue(String string){
        if (string != null && !string.isEmpty()) {
            // doSomething
            return true;
        }
        return false;
    }

    public void asyncAddRewards(User updatedUser, String[] aData, String[] aLoginData){
        new RewardsAPIAsyncTask(this, updatedUser, aData, aLoginData, currentUser).execute();
    }

    public void sendResults(String result, String json) {
        Log.d(TAG, "sendResults: " + result);
        Log.d(TAG, "sendResults: " + json);
        if(result.equals("SUCCESS")) {
            makeCustomToast(this, json, Toast.LENGTH_LONG);
            Intent leaderboardIntent = new Intent(AwardActivity.this, LeaderboardActivity.class);
            leaderboardIntent.putExtra("User Login Data", aLoginData);
            leaderboardIntent.putExtra("Current User", currentUser);
            startActivity(leaderboardIntent);
        }else{
            try{
                JSONObject thisJSON = new JSONObject(json);
                JSONObject errordetails = thisJSON.getJSONObject("errordetails");
                String firstMessage = errordetails.getString("message");

                if(firstMessage.equals("Validation error")) {
                    JSONArray subErrors = errordetails.getJSONArray("subErrors");
                    for (int i = 0; i < subErrors.length(); i++) {
                        JSONObject explrObject = subErrors.getJSONObject(i);
                        String sMessage = explrObject.getString("message");
                        makeCustomToast(this, sMessage, Toast.LENGTH_LONG);
                    }
                } else {
                    makeCustomToast(this, firstMessage, Toast.LENGTH_LONG);
                }
//                Log.d(TAG, "sendResults: sub " + subErrors);
            } catch (JSONException e){

            }
        }

    }

    public static void makeCustomToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(50, 25, 50, 25);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }
}
