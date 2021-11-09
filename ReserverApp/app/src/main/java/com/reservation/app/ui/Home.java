package com.reservation.app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reservation.app.R;
import com.reservation.app.adapters.SearchRecyclerAdapter;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.datasource.VenueDataManager;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.util.DialogBuilder;
import com.reservation.app.ui.venue.VenueActivity;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private TextView helloText;
    private SharedPrefManager pref;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private static final String LOG_TAG = Profile.class.getSimpleName();
    private List<Venue> venueList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = new SharedPrefManager(Home.this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.home_nav_menu);
        drawerLayout = findViewById(R.id.home_drawer);

        actionBarDrawerToggle =
                new ActionBarDrawerToggle(Home.this,drawerLayout,toolbar,
                        R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Dialog progressDialog = DialogBuilder.buildProgressDialog(Home.this);
        progressDialog.show();

        recyclerView = findViewById(R.id.search_recycler_view);
        searchRecyclerAdapter = new SearchRecyclerAdapter(Home.this,new ArrayList<>());
        recyclerView.setAdapter(searchRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));

        VenueDataManager.requestVenueList(new RemoteResult<List<Venue>>() {

            @Override
            public void onSuccess(List<Venue> data) {
                if(data != null){
                    Log.d(LOG_TAG,"!INSIDE  IFFF!!!!!!!!!!!!");
                    venueList = new ArrayList<>(data);
                    searchRecyclerAdapter.setVenueList(venueList);
                    progressDialog.dismiss();}

            }

            @Override
            public void onFailure(Exception error) {
                DialogBuilder.buildOkDialog(Home.this, error.getMessage()).show();
                progressDialog.dismiss();
            }
        });

        recyclerView.setAdapter(searchRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));



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

                        case R.id.menu_venue_List:
                        startActivity(new Intent(getApplicationContext(), VenueActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
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





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        venueSearch(searchView);    // Search Filtering Method

        return true;
    }


    private void venueSearch(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}


