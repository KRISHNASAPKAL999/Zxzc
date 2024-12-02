package com.example.blood_donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;

public class infoRevised extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_revised);
        TextView skipTextView = findViewById(R.id.skip);
        TextView nextTextView = findViewById(R.id.next);

        // Set OnClickListener for the Skip button to navigate to the Login page
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login_Activity.class); // Replace with your Login Activity
                startActivity(intent);
                finish(); // Close the current activity
            }
        });

        // Set OnClickListener for the Next button to navigate to the Info2 page
        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), info2.class); // Replace with your Info2 Activity
                startActivity(intent);
            }
        });
    }
}

