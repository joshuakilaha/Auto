package com.example.auto.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.auto.MainActivity;
import com.example.auto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Registration extends AppCompatActivity {
    private static final String TAG = "Registration";

    ImageView userimage;
    static int PRegCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImageUri;


   private EditText name,email,phone,id,password;
   private Button Register;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userimage = (ImageView) findViewById(R.id.useriamge);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);
        Register  = (Button) findViewById(R.id.Register);

        mAuth = FirebaseAuth.getInstance();

        //onclick Register
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();

            }
        });


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



//**************************************************Registration******************************************************************************
    public void Register(){

        final String Name = name.getText().toString();
        final String Email = email.getText().toString();
        final String Id = id.getText().toString();
        final String Phone = phone.getText().toString();
        final String Password = password.getText().toString();

        if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email)|| TextUtils.isEmpty(Id) || TextUtils.isEmpty(Phone)
                || TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Please Fill details on all fields", Toast.LENGTH_SHORT).show();
        }

        final ProgressDialog progressDialog = ProgressDialog.show(Registration.this, "Please wait...","Processing...",true);
        {
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //onSuccess

                                User user = new User(Name,Email,Id,Phone);

                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                                String key = myRef.push().getKey();

                                myRef.child(Name).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Sent to firebase successfully

                                        progressDialog.dismiss();
                                        Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_LONG).show();
                                        UploadUserphoto(Name,pickedImageUri,mAuth.getCurrentUser());

                                        Intent i = new Intent(Registration.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        progressDialog.dismiss();

                                        Toast.makeText(Registration.this, "Failed: " + e.toString() + ". Try again!", Toast.LENGTH_LONG).show();
                                    }
                                });



                            }else{
                                Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });



        }

    }




    private void UploadUserphoto(final String Name,Uri pickedImageUri,final  FirebaseUser currentUser){

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("User Profile Photos");
        final StorageReference imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                       Intent Main = new Intent(Registration.this, MainActivity.class);
                                       startActivity(Main);

                                    }
                                });


                    }
                });

            }
        });



    }


 //***************************************************Image***********************************************************************************
    ///open gallery when image icon is pressed
    public void openGallery(){

        Intent galleryopen = new Intent(Intent.ACTION_GET_CONTENT);
        galleryopen.setType("image/*");
        startActivityForResult(galleryopen,REQUESTCODE);

    }

    ///User permission request

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


    //setting up the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data !=null){


            pickedImageUri = data.getData();
            userimage.setImageURI(pickedImageUri);
        }

    }
}
