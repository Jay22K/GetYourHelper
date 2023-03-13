package com.srijan.empmgmt.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.srijan.empmgmt.Login;

import com.srijan.empmgmt.R;
import com.srijan.empmgmt.ui.addEmployee.AddWorkerActivity;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import android.content.Intent;

import android.os.Bundle;


public class SelectActivity extends AppCompatActivity {


    private CardView addworker;
    private CardView loginCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        addworker=findViewById(R.id.addworker);

        loginCon=findViewById(R.id.loginCon);

        loginCon.setOnClickListener(v -> {

            Intent i = new Intent(SelectActivity.this, Login.class);
            startActivity(i);
        });


        addworker.setOnClickListener(v -> {

           /* MainActivity.allAccessNav.navigate(R.id.addEmp);*/
            Intent i = new Intent(SelectActivity.this, AddWorkerActivity.class);
            startActivity(i);

        });



    }
}