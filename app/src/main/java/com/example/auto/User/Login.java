package com.example.auto.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auto.MainActivity;
import com.example.auto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email,password;
    TextView Register;
    Button login;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email =(EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Register = (TextView) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);


        mAuth = FirebaseAuth.getInstance();



        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this,Registration.class);
                startActivity(register);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////onclick Login////////////////////////////
                login();

            }
        });

    }


    //////////////////////////////////////////////////////////////Login/////////////////////////////////////////////
    private void login(){

        final String Email = email.getText().toString();
        final String Password = password.getText().toString();

        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }else {

        }

        final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "Please wait...","Processing...",true);
        {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                                    Intent toMainActivity = new Intent(Login.this, MainActivity.class);
                                    startActivity(toMainActivity);
                                } else {
                                    Toast.makeText(Login.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            } else {

                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });
        }progressDialog.dismiss();

    }
}
