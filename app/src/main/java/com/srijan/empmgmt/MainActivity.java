package com.srijan.empmgmt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.srijan.empmgmt.ui.SelectActivity;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    public static Context context;
    public static SearchView searchView;
    public static MenuItem deleteAll;
    public static NavController allAccessNav;
    private AppDatabase db ;
    EmployeeListAdapter employeeListAdapter;
    public static FragmentManager ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=getApplicationContext();
        FloatingActionButton fab = findViewById(R.id.fab);
        allAccessNav = Navigation.findNavController(this, R.id.nav_host_fragment);
        ft=getSupportFragmentManager();
//        allAccessNav.getGraph().forEach(f-> System.out.println(f.getLabel()+" "+f.getId()));
        fab.setOnClickListener(view -> {
            View.OnClickListener listen = v -> {
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on your worker Management App");
                    intent.putExtra(Intent.EXTRA_TEXT, "oh come on...!\n you need work on this coronavirus tho.");
                    startActivity(intent);
                } catch(Exception e) {
                    Toast.makeText(MainActivity.this, "No suitable email applications found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            };
            Snackbar.make(view, "sent an email!", Snackbar.LENGTH_LONG)
                    .setAction("Compose Mail", listen).show();

        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.displayEmp)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        db = Room.databaseBuilder(MainActivity.context,
                AppDatabase.class, "employee")
                .build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item=menu.findItem(R.id.searchbadge);
        searchView =(SearchView) MenuItemCompat.getActionView(item);
        deleteAll=menu.findItem(R.id.deleteAll);
        return true;
    }
    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        navController.getGraph().forEach(f-> System.out.println(f.getLabel()+" " +f.getId()));

        switch (item.getItemId()) {
            case R.id.action_login:
                Intent i = new Intent(MainActivity.this, SelectActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.searchbadge:
                navController.navigate(R.id.displayEmp);
                return true;
            case R.id.deleteAll:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
