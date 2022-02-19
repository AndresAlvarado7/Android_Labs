package com.cst2335.alvarado;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {


    public static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        

        Intent fromMain = getIntent();
        String input = fromMain.getStringExtra("email");
        EditText eEmail = findViewById(R.id.editTextEmail);
        eEmail.setText(input);



       ImageButton cam = findViewById( R.id.imageButton);
       cam.setOnClickListener(view -> dispatchTakePictureIntent());
    }

    ActivityResultLauncher<Intent> myPictureTakerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            ,new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    ImageButton imgView = (ImageButton)findViewById(R.id.imageButton);
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Bitmap imgbitmap = (Bitmap) data.getExtras().get("data");
                        imgView.setImageBitmap(imgbitmap);
                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.i(TAG, "User refused to capture a picture.");
                }
            });

    private void dispatchTakePictureIntent(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager())!=null){

            myPictureTakerLauncher.launch(takePicture);
        }
    }

    @Override //screen is visible but not responding
    protected void onStart() {
        super.onStart();

        Log.e(TAG, "In onStart, visible but not responding");
    }

    @Override //screen is visible but not responding
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "In onResume");
    }

    @Override //screen is visible but not responding
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "In onPause");
    }

    @Override //not visible
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "In onStop");
    }

    @Override  //garbage collected
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "In onDestroy");
    }
}