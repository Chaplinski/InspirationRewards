package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.LoginAPIAsyncTask;
import com.example.inspirationrewards.Classes.Reward;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LoginActivity extends AppCompatActivity {

    private static final int PASS_USER_OBJECT_REQUEST_CODE = 101;
    private String TAG = "LOGINACTIVITY";
    private EditText userName;
    private EditText password;
    private Button login;
    private User user = new User();
    private CheckBox rememberCredentials;
    private List<Reward> aRewards = new ArrayList<>();

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_CHECKBOX = "Checkbox";

    private final String DefaultUnameValue = "";
    private String uNameValue;

    private final boolean DefaultCheckboxValue = false;
    private boolean checkboxValue;

    private final String DefaultPasswordValue = "";
    private String passwordValue;

    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;
    String[] aLoginData = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = findViewById(R.id.etLoginUsername);
        password = findViewById(R.id.etEPLoginPassword);
        login = findViewById(R.id.buttonLogin);
        rememberCredentials = findViewById(R.id.cbRememberCredentials);
        loadPreferences();

        login.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                attemptLogin();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setPowerRequirement(Criteria.POWER_HIGH);

        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();

    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        uNameValue = userName.getText().toString();
        passwordValue = password.getText().toString();
        checkboxValue = rememberCredentials.isChecked();
        if (checkboxValue == true) {
            editor.putString(PREF_UNAME, uNameValue);
            editor.putString(PREF_PASSWORD, passwordValue);
            editor.putBoolean(PREF_CHECKBOX, true);
        } else {
            editor.putString(PREF_UNAME, "");
            editor.putString(PREF_PASSWORD, "");
            editor.putBoolean(PREF_CHECKBOX, false);
        }
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        uNameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        passwordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        checkboxValue = settings.getBoolean(PREF_CHECKBOX, DefaultCheckboxValue);
        userName.setText(uNameValue);
        password.setText(passwordValue);
        if(checkboxValue) {
            rememberCredentials.setChecked(true);
        }
    }

    public void attemptLogin(){
        String sUserName = userName.getText().toString();
        String sPassword = password.getText().toString();
        aLoginData[0] = sUserName;
        aLoginData[1] = sPassword;
        asyncLogin(aLoginData);

    }

    public void asyncLogin(String[] aData){
        new LoginAPIAsyncTask(this, aData).execute();
    }

    public void goProfileCreate(View v){
//        Log.d(TAG, "goProfileCreate: " + aLoginData[0]);
//        Log.d(TAG, "goProfileCreate: " + aLoginData[1]);
        Intent intentCreateProfile = new Intent(LoginActivity.this, CreateProfileActivity.class);
        startActivity(intentCreateProfile);
    }

    public void sendResults(String result, String json) {
//        ((TextView) findViewById(R.id.resultsText)).setText(s);
        Log.d(TAG, "sendResults: " + result);
        Log.d(TAG, "sendResults: " + json);
        if(result.equals("SUCCESS")){
            //TODO show user profile screen
            try {
                Log.d(TAG, "sendResults: top of try block");
                JSONObject jsonObject1 = new JSONObject(json);
                String foo = jsonObject1.getString("firstName");
                user.setFirstName(foo);
//                user.setFirstName(jsonObject.getString("firstName"));
                user.setLastName(jsonObject1.getString("lastName"));
                user.setUserName(jsonObject1.getString("username"));
                user.setLocation(jsonObject1.getString("location"));
                user.setDepartment(jsonObject1.getString("department"));
                user.setPosition(jsonObject1.getString("position"));
                user.setPointsToAward(jsonObject1.getInt("pointsToAward"));
                user.setAdmin(jsonObject1.getBoolean("admin"));
                user.setStory(jsonObject1.getString("story"));
                user.setImage(jsonObject1.getString("imageBytes"));
                user.setRewardRecord(jsonObject1.getString("rewards"));
                Log.d(TAG, "sendResults: end of user variables being set");

                Intent intentProfileView = new Intent(LoginActivity.this, ProfileActivity.class);
                intentProfileView.putExtra("User Login Data", aLoginData);
                intentProfileView.putExtra("User Object", user);
                startActivity(intentProfileView);
                Log.d(TAG, "sendResults: end of try block");
            }catch (JSONException err){
                Log.d("Error", err.toString());
            }
//            Log.d(TAG, "sendResults: " + json);
        }

    }


}
