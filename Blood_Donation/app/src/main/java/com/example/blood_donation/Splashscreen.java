package com.example.blood_donation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splashscreen extends AppCompatActivity {

    // Set duration for the splash screen
       private static final int SPLASH_SCREEN_TIMEOUT = 3000; // 3 seconds

    private ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        // Initialize the ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Set the maximum duration for the progress bar
        progressBar.setMax(SPLASH_SCREEN_TIMEOUT);
        progressBar.setProgress(0);

        // Use Handler with Looper for main thread to avoid deprecated API warning
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // After the timeout, start the next activity
            if(currentUser!=null)
            {
                Intent intent = new Intent(Splashscreen.this, MainActivity4.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(Splashscreen.this, infoRevised.class);
                startActivity(intent);
                Toast.makeText(this, "curent user == null", Toast.LENGTH_SHORT).show();
            }
            finish(); // Close this activity so the user cannot go back to it
        }, SPLASH_SCREEN_TIMEOUT);


    }
}
