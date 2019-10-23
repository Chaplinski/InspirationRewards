package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.inspirationrewards.AsyncTasks.LoginAPIAsyncTask;
import com.example.inspirationrewards.AsyncTasks.UpdateProfileAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class EditProfileActivity extends AppCompatActivity {

    private String TAG = "EDITPROFILEACTIVITY";
    private User user = new User();
    private User updatedUser = new User();
    private EditText username;
    private EditText password;
    private CheckBox isAdmin;
    private EditText firstName;
    private EditText lastName;
    private EditText department;
    private EditText position;
    private EditText story;
    private String location;
    private LocationManager locationManager;
    private Criteria criteria;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setPowerRequirement(Criteria.POWER_HIGH);

        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        username = findViewById(R.id.etEPUserName);
        password = findViewById(R.id.etEPLoginPassword);
        isAdmin = findViewById(R.id.cbEPIsAdmin);
        firstName = findViewById(R.id.etEPFirstName);
        lastName = findViewById(R.id.etEPLastName);
        department = findViewById(R.id.etEPDepartment);
        position = findViewById(R.id.etEPPosition);
        story = findViewById(R.id.etEPAboutUser);

        Intent intent = getIntent();
        if (intent.hasExtra("User Object")) {
            user = (User)intent.getSerializableExtra("User Object");
            Log.d(TAG, "onCreate here : " + user.getDepartment());

            username.setFocusable(false);
            username.setClickable(false);
            username.setEnabled(false);
            username.setText(user.getUsername());
            password.setText(user.getPassword());
            isAdmin.setChecked(user.getAdmin());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            story.setText(user.getStory());
        }

    }

    private void getUpdatedUser(){
        location = getLocation();
        updatedUser.setUserName(user.getUsername());
        updatedUser.setPassword(password.getText().toString());
        updatedUser.setAdmin(isAdmin.isChecked());
        updatedUser.setFirstName(firstName.getText().toString());
        updatedUser.setLastName(lastName.getText().toString());
        updatedUser.setDepartment(department.getText().toString());
        updatedUser.setPosition(position.getText().toString());
        updatedUser.setStory(story.getText().toString());
        updatedUser.setLocation(location);
    }

    public String getLocation(){
        Log.d(TAG, "getLocation: in method");
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: permission granted");
            try {
                Log.d(TAG, "getLocation: in try block");
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                Location currentLocation = locationManager.getLastKnownLocation(bestProvider);

                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                for (Address ad : addresses) {
                    Log.d(TAG, "getLocation: in for loop");

//                    String a = String.format("%s %s %s %s %s %s",
//                            (ad.getSubThoroughfare() == null ? "" : ad.getSubThoroughfare()),
//                            (ad.getThoroughfare() == null ? "" : ad.getThoroughfare()),
//                            (ad.getLocality() == null ? "" : ad.getLocality()),
//                            (ad.getAdminArea() == null ? "" : ad.getAdminArea()),
//                            (ad.getPostalCode() == null ? "" : ad.getPostalCode()),
//                            (ad.getCountryName() == null ? "" : ad.getCountryName()));

                    Log.d(TAG, "getLocation: " + ad.getLocality() + ", " + ad.getAdminArea());
                    return ad.getLocality() + ", " + ad.getAdminArea();
                }


            } catch (IOException e){

            }
        }
        return "Unknown Location";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                //get updated user
                getUpdatedUser();
                //run async task updating user info
                asyncUpdate(updatedUser);
                //open user profile activity
                Intent intentEditProfile = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intentEditProfile.putExtra("User Object", updatedUser);
                startActivity(intentEditProfile);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void asyncUpdate(User updatedUser){
        new UpdateProfileAPIAsyncTask(this, updatedUser).execute();
    }

    public void sendResults(String result, String json) {
        Log.d(TAG, "sendResults2: " + result);
        Log.d(TAG, "sendResults2: " + json);
////        ((TextView) findViewById(R.id.resultsText)).setText(s);
//        Log.d(TAG, "sendResults: " + result);
//        Log.d(TAG, "sendResults: " + json);
//        if(result.equals("SUCCESS")){
//            //TODO show user profile screen
//            try {
//                JSONObject jsonObject = new JSONObject(json);
//                Log.d(TAG, "sendResults: " + jsonObject.getString("firstName"));
//                user.setFirstName(jsonObject.getString("firstName"));
//                user.setLastName(jsonObject.getString("lastName"));
//                user.setUserName(jsonObject.getString("username"));
//                user.setLocation(jsonObject.getString("location"));
//                user.setDepartment(jsonObject.getString("department"));
//                user.setPosition(jsonObject.getString("position"));
//                user.setPointsToAward(jsonObject.getInt("pointsToAward"));
//                user.setStory(jsonObject.getString("story"));
//                user.setImage(jsonObject.getString("imageBytes"));
//
//                Intent intentNoteCreation = new Intent(LoginActivity.this, ProfileActivity.class);
//                intentNoteCreation.putExtra("User Object", user);
//                startActivityForResult(intentNoteCreation, PASS_USER_OBJECT_REQUEST_CODE);
//            }catch (JSONException err){
//                Log.d("Error", err.toString());
//            }
//            Log.d(TAG, "sendResults: " + json);
//        } else{
//
//        }
//
//
////        if(s.contains("SUCCESS")) {
////            Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
////            startActivity(intent);
////        }
    }
}
