package com.example.inspirationrewards.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.inspirationrewards.Activities.AwardActivity;
import com.example.inspirationrewards.Activities.EditProfileActivity;
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

public class RewardsAPIAsyncTask extends AsyncTask<String, Void, String> {

    private String TAG = "AwardAPIAsyncTask";
    private static final String rewardEndPoint ="/rewards";
    private final int studentID = 20452745;
    private AwardActivity AwardActivity;
    private User userToAward;
    private String[] aData;

    public RewardsAPIAsyncTask(AwardActivity aa, User updatedUser, String[] aData){

        AwardActivity = aa;
        userToAward = updatedUser;
        this.aData = aData;
    }

    @Override
    protected void onPostExecute(String connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just return the raw json
        Log.d(TAG, "onPostExecute: " + connectionResult);
        String result;
        if (connectionResult.contains("error")) // If there is "error" in the results...
            result = "FAILED";
        else
            result = "SUCCESS";
        AwardActivity.sendResults(result, connectionResult);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonTarget = new JSONObject();
            JSONObject jsonSource = new JSONObject();

            jsonTarget.put("studentId", studentID);
            jsonTarget.put("username", userToAward.getUsername());
            jsonTarget.put("name", userToAward.getFirstName() + " " + userToAward.getLastName());
            //TODO add real date
            jsonTarget.put("date", "03/10/2019");
            //TODO add notes and value to json Target
            String sPoints = aData[0];

            int iPoints = Integer.parseInt(sPoints);
            //todo add points
            jsonTarget.put("value", iPoints);
            jsonTarget.put("notes", aData[1]);

            jsonSource.put("studentId", studentID);
            jsonSource.put("username", "user3");
            jsonSource.put("password", "pw3");

            jsonObject.put("target", jsonTarget);
            jsonObject.put("source", jsonSource);

            Log.d(TAG, "doInBackground: " + jsonObject.toString());
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

            String urlString = AwardActivity.getResources().getString(R.string.base_url) + rewardEndPoint;


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
