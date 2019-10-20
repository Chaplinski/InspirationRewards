package com.example.inspirationrewards.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.inspirationrewards.Activities.CreateProfileActivity;
import com.example.inspirationrewards.Classes.User;

public class CreateProfileAPIAsyncTask extends AsyncTask<String, Void, String> {

    private String TAG = "CreateProfileAPIAsyncTask";
    private CreateProfileActivity createProfileActivity;
    private User user;

    public CreateProfileAPIAsyncTask(CreateProfileActivity cPA, User user){
        createProfileActivity = cPA;
        this.user = user;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: " + user.getAboutUser());
        return "";
    }
}
