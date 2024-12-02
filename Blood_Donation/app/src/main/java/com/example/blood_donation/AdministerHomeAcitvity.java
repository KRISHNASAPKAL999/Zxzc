package com.example.blood_donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ImageDecoderKt;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdministerHomeAcitvity extends AppCompatActivity {
    ImageButton managepatient,managedonor,manageuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administer_home_acitvity);
        managepatient = findViewById(R.id.requestblood_card);
        managedonor = findViewById(R.id.donateblood_card);
        manageuser = findViewById(R.id.donateblood_card1);

        manageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageUser.class);
                startActivity(intent);
            }
        });

        managepatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagePatient.class);
                startActivity(intent);
            }
        });

        managedonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageDonor.class);
                startActivity(intent);
            }
        });

    }
}