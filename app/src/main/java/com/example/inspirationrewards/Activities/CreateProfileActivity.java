package com.example.inspirationrewards.Activities;

import androidx.annotation.NonNull;
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
import android.graphics.Color;
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

import com.example.inspirationrewards.AsyncTasks.CreateProfileAPIAsyncTask;
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

public class CreateProfileActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private Criteria criteria;
    private String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView imageView;
    private File currentImageFile;
    private String encodedImage;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText department;
    private EditText position;
    private EditText aboutUser;
    private TextView charCounter;
    private CheckBox isAdmin;
    private String location;
    User user = new User();
    private String[] aLoginData = new String[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Profile");
        imageView = findViewById(R.id.ivCPImage);
        username = findViewById(R.id.etCPUserName);
        password = findViewById(R.id.etCPLoginPassword);
        firstName = findViewById(R.id.etCPFirstName);
        lastName = findViewById(R.id.etCPLastName);
        department = findViewById(R.id.etCPDepartment);
        position = findViewById(R.id.etCPPosition);
        aboutUser = findViewById(R.id.etCPAboutUser);
        isAdmin = findViewById(R.id.cbCPIsAdmin);
        aboutUser.addTextChangedListener(mTextEditorWatcher);

        charCounter = findViewById(R.id.tvCPyourStory);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setPowerRequirement(Criteria.POWER_HIGH);

        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

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

    private void saveUser(){
        createUserObject();
        asyncCreateProfile();

    }

    public void asyncCreateProfile(){
        new CreateProfileAPIAsyncTask(this, user).execute();
    }

    private void createUserObject(){
        location = getLocation();

        String sUserName = username.getText().toString();
        String sPassword = password.getText().toString();
        String sFirstName = firstName.getText().toString();
        String sLastName = lastName.getText().toString();
        String sDepartment = department.getText().toString();
        String sPosition = position.getText().toString();
        String sStory = aboutUser.getText().toString();
//        location =
        boolean bIsAdmin = isAdmin.isChecked();

        user.setUserName(sUserName);
        user.setPassword(sPassword);
        user.setFirstName(sFirstName);
        user.setLastName(sLastName);
        user.setDepartment(sDepartment);
        user.setPosition(sPosition);
        user.setImage(encodedImage);
        user.setStory(sStory);
        user.setLocation(location);
        user.setAdmin(bIsAdmin);
        Log.d(TAG, "createUserObject: user created");
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

                if(currentLocation != null) {
                    double latitude = currentLocation.getLatitude();
                    double longitude = currentLocation.getLongitude();

                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    for (Address ad : addresses) {
                        return ad.getLocality() + ", " + ad.getAdminArea();
                    }
                }

            } catch (IOException e){

            }
        }
        return "Unknown Location";
    }

    public void picClicked(View v){
        createPictureDialog();
    }

    public void useGallery(){
        try {
            if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);
                Toast.makeText(this, "If", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Else", Toast.LENGTH_SHORT).show();

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

    public void useCamera(){
        currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_IMAGE_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Changes?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        saveUser();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendResults(String result, String json, User user, String[] aLoginData) {
//        ((TextView) findViewById(R.id.resultsText)).setText(s);
        Log.d(TAG, "sendResults: " + result);
        Log.d(TAG, "sendResults: " + json);
        if(result.equals("SUCCESS")){
            this.aLoginData[0] = aLoginData[0];
            this.aLoginData[1] = aLoginData[1];
            makeCustomToast(this, "User Create Successful", Toast.LENGTH_LONG);
            Intent profileIntent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
            profileIntent.putExtra("User Object", user);
            profileIntent.putExtra("User Login Data", this.aLoginData);
            startActivity(profileIntent);

        }


//        if(s.contains("SUCCESS")) {
//            Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        }
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
