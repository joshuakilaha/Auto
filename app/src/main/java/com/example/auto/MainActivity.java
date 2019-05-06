package com.example.auto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.example.auto.User.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //navigation Drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;

    TextView name,email;
    ImageView Userimage;



    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = findViewById(R.id.design_navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){

         //   try{


            Userimage = (ImageView) findViewById(R.id.useriamge);
            name = (TextView) findViewById(R.id.name);
            email = (TextView) findViewById(R.id.email);


            name.setText(currentUser.getDisplayName());
            email.setText(currentUser.getEmail());

            //image using glide library
           // Glide.with(this).load(currentUser.getPhotoUrl()).into(Userimage);

          //  }catch (Exception e){

            //}

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.profile:
                break;

            case R.id.contactus:
                break;

            case R.id.logout:

                Intent Logout = new Intent(MainActivity.this, Login.class);
                startActivity(Logout);
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return false;

    }
}
