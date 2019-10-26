package com.example.inspirationrewards.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.inspirationrewards.Activities.CreateProfileActivity;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class CreateProfileAPIAsyncTask extends AsyncTask<String, Void, String> {

    private String TAG = "CreateProfileAPIAsyncTask";
    private static final String createProfileEndPoint ="/profiles";
    private final int studentID = 20452745;
    private CreateProfileActivity createProfileActivity;
    private User user;
    private String[] aLoginData = new String[2];


    public CreateProfileAPIAsyncTask(CreateProfileActivity cPA, User user){
        createProfileActivity = cPA;
        this.user = user;
    }

    @Override
    protected void onPostExecute(String connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just return the raw json
        String result;
        if (connectionResult.contains("error")) { // If there is "error" in the results...
            result = "FAILED";
        }
        else {
            result = "SUCCESS";
            aLoginData[0] = user.getUsername();
            aLoginData[1] = user.getPassword();

        }
        createProfileActivity.sendResults(result, connectionResult, user, aLoginData);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", studentID);
            jsonObject.put("username", user.getUsername());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("pointsToAward", 1000);
            jsonObject.put("department", user.getDepartment());
            jsonObject.put("story", user.getStory());
            jsonObject.put("position", user.getPosition());
            jsonObject.put("admin", user.getAdmin());
            jsonObject.put("location", user.getLocation());
            jsonObject.put("imageBytes", user.getImage());
            jsonObject.put("rewardRecords", new JSONArray());

            Log.d(TAG, "doInBackground: image - " + user.getImage());
            Log.d(TAG, "doInBackground: end of try block");
            return doAuth(jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String doAuth(String jsonObjectText) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = createProfileActivity.getResources().getString(R.string.base_url) + createProfileEndPoint;


            Uri.Builder buildURL = Uri.parse(urlString).buildUpon();
            String urlToUse = buildURL.build().toString();
            URL url = new URL(urlToUse);
            Log.d(TAG, "doAuth: " + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObjectText);
            out.close();

            int responseCode = connection.getResponseCode();
            String responseText = connection.getResponseMessage();

            Log.d(TAG, "doAuth: " + responseCode + ": " + responseText);


            StringBuilder result = new StringBuilder();

            if (responseCode == HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();
            } else {

                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();
            }

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: Invalid URL: " + e.getMessage());
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }
        return null;
    }
}
