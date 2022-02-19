package com.cst2335.alvarado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public final static String PREFERENCES_FILE = "MyData";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        EditText editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);

        Button myButton = findViewById(R.id.myButton);
        myButton.setOnClickListener((click) -> {
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
            goToProfile.putExtra("email",editTextTextEmailAddress.getText().toString());
            startActivity(goToProfile);
        });


        SharedPreferences emailAdd = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        String s = emailAdd.getString("email","");
        editTextTextEmailAddress.setText(s);

    }

    @Override
    protected void onPause(){
        super.onPause();

        EditText editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);

        SharedPreferences emailAdd = getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = emailAdd.edit();
        myEditor.putString("email", editTextTextEmailAddress.getText().toString());
        myEditor.apply();
    }
}