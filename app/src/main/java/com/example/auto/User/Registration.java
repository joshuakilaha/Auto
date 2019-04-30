package com.example.auto.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.auto.R;

public class Registration extends AppCompatActivity {

    ImageView userimage;
    static int PRegCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userimage = findViewById(R.id.useriamge);

        ///onclick Image view
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT > 22){
                    
                    checkAndRequestForPermission();

                }
                else{
                    openGallery();
                }
            }
        });
    }


    public void openGallery(){

        Intent galleryopen = new Intent(Intent.ACTION_GET_CONTENT);
        galleryopen.setType("image/*");
        startActivityForResult(galleryopen,REQUESTCODE);



    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.READ_EXTERNAL_STORAGE)

            != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Registration.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Please accept to set photo", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PRegCode);

            }
        }

        else {
                openGallery();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK && requestCode == REQUESTCODE && data !=null){


            pickedImageUri = data.getData();
            userimage.setImageURI(pickedImageUri);
        }

    }
}
