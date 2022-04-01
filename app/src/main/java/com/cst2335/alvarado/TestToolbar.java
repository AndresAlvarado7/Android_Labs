package com.cst2335.alvarado;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Needed for the OnNavigationItemSelected interface:
        setContentView(R.layout.activity_test_toolbar);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.settings_item:
                message = getResources().getString(R.string.settings_item);
                break;
            case R.id.search_item:
                message = getResources().getString(R.string.search_item);
                break;
            case R.id.share_item:
                message = getResources().getString(R.string.share_item);
                break;
            case R.id.delete_item:
                message = getResources().getString(R.string.delete_item);
                break;
//            case R.id.call_item:
//                message = "You clicked on call";
//                Intent i = new Intent (getApplicationContext(), SomeActivity.class);
//                startActivity(i);
//                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        if ( message != null ) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.chatPage:
                    Intent goToChat = new Intent(TestToolbar.this, ChatRoomActivity.class);
                    startActivity(goToChat);

                break;
            case R.id.profilePage:
                    Intent goToPro = new Intent(TestToolbar.this, ProfileActivity.class);
                    startActivity(goToPro);

                break;
            case R.id.toLogin:
                    Intent goToLog = new Intent(TestToolbar.this, MainActivity.class);
                    startActivity(goToLog);

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

//        if ( message != null ) {
//            Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
//        }
        return false;
    }
}