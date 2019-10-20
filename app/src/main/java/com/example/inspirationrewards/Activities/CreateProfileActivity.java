package com.example.inspirationrewards.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspirationrewards.AsyncTasks.CreateProfileAPIAsyncTask;
import com.example.inspirationrewards.Classes.User;
import com.example.inspirationrewards.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateProfileActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView imageView;
    private File currentImageFile;
    private Base64 imageToSave;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText department;
    private EditText position;
    private EditText aboutUser;
    private CheckBox isAdmin;
    User user = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Profile");
        imageView = findViewById(R.id.imageView);
        username = findViewById(R.id.etUserName);
        password = findViewById(R.id.etLoginPassword);
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        department = findViewById(R.id.etDepartment);
        position = findViewById(R.id.etPosition);
        aboutUser = findViewById(R.id.etAboutUser);
        isAdmin = findViewById(R.id.cbIsAdmin);


    }

    private void saveUser(){
        createUserObject();
        asyncCreateProfile();

    }

    public void asyncCreateProfile(){
        new CreateProfileAPIAsyncTask(this, user).execute();
    }

    private void createUserObject(){
        String sUserName = username.getText().toString();
        String sPassword = password.getText().toString();
        String sFirstName = firstName.getText().toString();
        String sLastName = lastName.getText().toString();
        String sDepartment = department.getText().toString();
        String sPosition = position.getText().toString();
        String sStory = aboutUser.getText().toString();
        boolean bIsAdmin = isAdmin.isChecked();


        user.setUserName(sUserName);
        user.setPassword(sPassword);
        user.setFirstName(sFirstName);
        user.setLastName(sLastName);
        user.setDepartment(sDepartment);
        user.setPosition(sPosition);
        user.setStory(sStory);
        user.setAdmin(bIsAdmin);
    }

    public void picClicked(View v){
        if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);

        }
        if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }
        createPictureDialog();
//        PermissionDialog dialog = new PermissionDialog();
//        dialog.show(getSupportFragmentManager(), "Permission Dialog");
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
        try {
            if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                Toast.makeText(this, "If Camera", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Else Camera", Toast.LENGTH_SHORT).show();

                currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
//            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void createPictureDialog(){
        Toast.makeText(this, "IN IT", Toast.LENGTH_SHORT).show();
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
//        makeCustomToast(this,
//                String.format(Locale.getDefault(),
//                        "Gallery Image Size:%n%,d bytes", selectedImage.getByteCount()),
//                Toast.LENGTH_LONG);

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

    public void sendResults(String s) {
//        ((TextView) findViewById(R.id.resultsText)).setText(s);
        Log.d(TAG, "sendResults: " + s);


//        if(s.contains("SUCCESS")) {
//            Intent intent = new Intent(CreateProfileActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        }
    }
}
