package com.cst2335.alvarado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        Button myBtn = findViewById(R.id.myButton);
        myBtn.setOnClickListener((v) -> {
            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.toast_message);
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();});

        Switch mySwitch = findViewById(R.id.mySwitch);
        mySwitch.setOnCheckedChangeListener((cb, b) ->{

            if(b){
                Snackbar.make(cb, getResources().getString(R.string.snackBar_on),Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.snackBar_undo),click -> cb.setChecked(!b)).show();
            }else{
                Snackbar.make(cb, getResources().getString(R.string.snackBar_off),Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.snackBar_undo),click -> cb.setChecked(!b)).show();
            }

        });


    }
}