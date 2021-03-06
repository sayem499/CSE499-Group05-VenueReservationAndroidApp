package com.reservation.app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reservation.app.R;
import com.reservation.app.datasource.SharedPrefManager;


public class Home extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private TextView helloText;
    private SharedPrefManager pref;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = new SharedPrefManager(Home.this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        helloText = findViewById(R.id.hello_textView);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.home_nav_menu);
        drawerLayout = findViewById(R.id.home_drawer);

        actionBarDrawerToggle =
                new ActionBarDrawerToggle(Home.this,drawerLayout,toolbar,
                        R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){

                        case R.id.menu_home :
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;


                        case R.id.menu_profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                        case R.id.menu_logout:
                        pref.logout();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;


                }

                return true;
            }
        });

        if(firebaseUser != null){
            helloText.setText("Hello !! "+firebaseUser.getPhoneNumber());

        }


    }





}


