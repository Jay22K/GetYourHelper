package com.srijan.empmgmt.ui.addEmployee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;

import com.srijan.empmgmt.AppDatabase;
import com.srijan.empmgmt.R;
public class AddWorkerActivity extends AppCompatActivity {

    public static Context context;
    private AppDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        context=getApplicationContext();

        db = Room.databaseBuilder(AddWorkerActivity.context,
                AppDatabase.class, "employee")
                .build();

        AddWorkerFragment AddWorker = new AddWorkerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, AddWorker).commit();




    }
}