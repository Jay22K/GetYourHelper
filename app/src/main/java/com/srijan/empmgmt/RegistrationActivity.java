package com.srijan.empmgmt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class RegistrationActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;
    CircularProgressImageButton btnsingup;

    EditText username, password, repassword;

    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        username = (EditText) findViewById(R.id.username2);
        password = (EditText) findViewById(R.id.password2);
        repassword = (EditText) findViewById(R.id.repassword);

        DB = new DBHelper(this);
        btnsingup = findViewById(R.id.singup);

        String user = username.getText().toString();
        String pass = password.getText().toString();
        String repass = repassword.getText().toString();








    }




    public void doSingup(View v) {
        //todo pass login fields!

        String user = username.getText().toString();
        String pass = password.getText().toString();
        String repass = repassword.getText().toString();

        if (user.equals("") || pass.equals("") || repass.equals(""))
            Toast.makeText(RegistrationActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        else {
            if (pass.equals(repass)) {
                Boolean checkuser = DB.checkusername(user);
                if (checkuser == false) {
                    Boolean insert = DB.insertData(user, pass);
                    if (insert == true) {
                        Toast.makeText(RegistrationActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegistrationActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
            }


            btnsingup.startAnimation();
            new Handler().postDelayed(() -> {
                btnsingup.stopAnimation();
                finish();


            }, 2000);

        }

    }
}
