package com.example.inspirationrewards.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.UpdateProfileAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private File currentImageFile;
    private ImageView imageView;
    private String encodedImage;
    private EditText firstName;
    private EditText lastName;
    private EditText department;
    private EditText position;
    private EditText story;
    private TextView charCounter;
    private String location;
    private Bitmap userBitmap;
    private LocationManager locationManager;
    private Criteria criteria;
    private String[] aLoginData = new String[2];
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;




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

        imageView = findViewById(R.id.ivEPImage);

        username = findViewById(R.id.etEPUserName);
        password = findViewById(R.id.etEPLoginPassword);
        isAdmin = findViewById(R.id.cbEPIsAdmin);
        firstName = findViewById(R.id.etEPFirstName);
        lastName = findViewById(R.id.etEPLastName);
        department = findViewById(R.id.etEPDepartment);
        position = findViewById(R.id.etEPPosition);
        story = findViewById(R.id.etEPAboutUser);
        charCounter = findViewById(R.id.tvEPyourStory);
        story.addTextChangedListener(mTextEditorWatcher);

        Intent intent = getIntent();
        if (intent.hasExtra("User Object")) {
            user = (User)intent.getSerializableExtra("User Object");
            aLoginData = intent.getStringArrayExtra("User Login Data");
            Log.d(TAG, "onCreate here : " + user.getUsername());

            username.setFocusable(false);
            username.setClickable(false);
            username.setEnabled(false);
            username.setText(user.getUsername());
            password.setText(aLoginData[1]);
            isAdmin.setChecked(user.getAdmin());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            department.setText(user.getDepartment());
            position.setText(user.getPosition());
            story.setText(user.getStory());
            userBitmap = StringToBitMap(user.getImage());
            imageView.setImageBitmap(userBitmap);

            if(user.getStory() != null) {
                String sStoryLength = String.valueOf(user.getStory().length());
                charCounter.setText("Your Story: (" + sStoryLength + " of 360)");
            }





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
            String sStoryLength = String.valueOf(s.length());

            charCounter.setText("Your Story: (" + sStoryLength + " of 360)");
        }

        public void afterTextChanged(Editable s) {
        }
    };

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
        Log.d(TAG, "getUpdatedUser: image - " + encodedImage);
        if(!isNullOrEmpty(encodedImage)) {
            updatedUser.setImage(encodedImage);
        } else {
            updatedUser.setImage(user.getImage());
        }
        Log.d(TAG, "getUpdatedUser: got user");
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
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

            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void asyncUpdate(User updatedUser){
        Log.d(TAG, "asyncUpdate: in async call");
        new UpdateProfileAPIAsyncTask(this, updatedUser).execute();
    }

    public void sendResults(String result, String json) {
        Log.d(TAG, "sendResults2: " + result);
        Log.d(TAG, "sendResults2: " + json);

        if(result == "SUCCESS") {
            Intent intentEditProfile = new Intent(EditProfileActivity.this, ProfileActivity.class);
            intentEditProfile.putExtra("User Object", updatedUser);
            intentEditProfile.putExtra("User Login Data", aLoginData);
            startActivity(intentEditProfile);
        }

    }

    public void picClicked(View v){
        createPictureDialog();
    }

    public void createPictureDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Profile Picture");
        builder.setMessage("Take picture from:");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                useCamera();

            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

            }
        });

        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                useGallery();
            }
        });

        builder.setIcon(R.drawable.logo);
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void useCamera(){
        currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }

    public void useGallery(){
        try {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);

            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
            }
//            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: in here");

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                processGallery(data);
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                processCamera();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void processCamera() {
        Uri selectedImage = Uri.fromFile(currentImageFile);
        imageView.setImageURI(selectedImage);
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        imageView.setImageBitmap(bm);
        doConvert(20);
        Log.d(TAG, "processCamera: converted");
//        makeCustomToast(this,
//                String.format(Locale.getDefault(),
//                        "Camera Image Size:%n%,d bytes", bm.getByteCount()),
//                Toast.LENGTH_LONG);
    }


    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(selectedImage);
        doConvert(20);

//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        selectedImage.compress(Bitmap.CompressFormat.PNG, 5, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream .toByteArray();
//        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//        Log.d(TAG, "processGallery: " + encodedImage);

    }

    private void doConvert(int jpgQuality) {
        Log.d(TAG, "doConvert: in here");
        if (imageView.getDrawable() == null)
            return;

        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, bitmapAsByteArrayStream);
        byte[] byteArray = bitmapAsByteArrayStream .toByteArray();
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d(TAG, "processGallery: " + encodedImage);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d(TAG, "doConvert: Image in Base64 size: " + imgString.length());

        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        Log.d(TAG, "doConvert: Image byte array length: " + imgString.length());

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Log.d(TAG, "doConvert: Bitmap created from Base 64 text");

        imageView.setImageBitmap(bitmap);

    }
}
