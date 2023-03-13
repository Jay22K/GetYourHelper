package com.srijan.empmgmt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.widget.EditText;
import android.widget.Toast;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class Login extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    ConstraintLayout constraintLayout;
    CircularProgressImageButton btn;




    EditText username, password;

    AppCompatTextView textViewLinkRegister;

    DBHelper DB;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constraintLayout=findViewById(R.id.containerLogin);
        btn=findViewById(R.id.login);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        DB = new DBHelper(this);

        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Intent i = new Intent(Login.this, RegistrationActivity.class);
                startActivity(i);

            }
        });

    }




    public void doLogin(View v) {
        //todo pass login fields!


        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (user.equals("") || pass.equals("")){
            Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            Boolean checkuserpass = DB.checkusernamepassword(user, pass);

            if (checkuserpass == true) {
                Toast.makeText(Login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);


            } else {
                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }


        btn.startAnimation();
        new Handler().postDelayed(()-> {
            btn.stopAnimation();
            finish();
        },2000);
    }



}
