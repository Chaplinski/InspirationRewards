package com.example.inspirationrewards.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inspirationrewards.Dialogs.GeneralDialog;
import com.example.inspirationrewards.Dialogs.PermissionDialog;
import com.example.inspirationrewards.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import static android.Manifest.permission_group.CAMERA;
import static android.graphics.Color.GREEN;

public class CreateProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView selectAPhoto;
    private ImageView imageView;
    private Uri imageUri;
    private File currentImageFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_with_logo);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Profile");
        imageView = findViewById(R.id.imageView);


    }

    public void picClicked(View v){
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
//            try {
//                processCamera();
//            } catch (Exception e) {
//                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
        }
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
                GeneralDialog dialog = new GeneralDialog();
                dialog.show(getSupportFragmentManager(), "Save Dialog");
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
