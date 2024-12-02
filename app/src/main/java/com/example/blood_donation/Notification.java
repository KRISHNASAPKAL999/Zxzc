package com.example.blood_donation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String recieveText,bloodg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // The user is signed in
            String recieveText1 = currentUser.getEmail();
            recieveText = recieveText1.replace(".", "_");
            Toast.makeText(this, recieveText, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "email == null ", Toast.LENGTH_SHORT).show();

        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if(itemid == R.id.home)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                    startActivity(intent);
                }
                if(itemid == R.id.donorlocation)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                }
                if(itemid == R.id.donateblood)
                {
                    Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
                    startActivity(intent);
                }

                return false;

            }
        });

        databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the user exists in the database
                if (snapshot.hasChild(recieveText)) {
                    // Fetch the existing data for the user
                    DataSnapshot userSnapshot = snapshot.child(recieveText);

                    // Check if donor1, donor2, and donor3 are present
                    boolean hasDonor1 = userSnapshot.hasChild("donor_phone_no") && userSnapshot.hasChild("donor_name");
                    boolean hasDonor2 = userSnapshot.hasChild("donor2_phone_no") && userSnapshot.hasChild("donor2_name");
                    boolean hasDonor3 = userSnapshot.hasChild("donor3_phone_no") && userSnapshot.hasChild("donor3_name");

                    // Retrieve donor data if available
                    if (hasDonor1) {

                        String donor1Name = userSnapshot.child("donor_name").getValue(String.class);
                        String donor1PhoneNo = userSnapshot.child("donor_phone_no").getValue(String.class);
                        String donor1units = userSnapshot.child("units").getValue(String.class);
                        String donor1time = userSnapshot.child("donor_Acceptence_Time").getValue(String.class);
                        String bloodgroup=userSnapshot.child("blood_group").getValue(String.class);
                       bloodg = bloodgroup;
                       if(donor1Name != null)
                       {
                           LinearLayout first = findViewById(R.id.firstcontainer);
                           TextView not = findViewById(R.id.nonotification);
                           not.setVisibility(View.GONE);
                           first.setVisibility(View.VISIBLE);
                           TextView name = findViewById(R.id.donorname1);
                           TextView unit = findViewById(R.id.donorunit1);
                           TextView time = findViewById(R.id.donortime1);
                           TextView bloodgroup1 = findViewById(R.id.donorbloodgroup1);
                           Button call = findViewById(R.id.call2);
                           Button report = findViewById(R.id.donorReport2);
                           Button accept = findViewById(R.id.imgsrc2);

                           name.setText(donor1Name);
                           unit.setText(donor1PhoneNo);
                           time.setText(donor1time);
                           bloodgroup1.setText(bloodg);
                       }

                        Toast.makeText(Notification.this, donor1PhoneNo+donor1Name+donor1units+donor1time, Toast.LENGTH_SHORT).show();
                    }

                    if (hasDonor2) {
                        String donor2Name = userSnapshot.child("donor2_name").getValue(String.class);
                        String donor2PhoneNo = userSnapshot.child("donor2_phone_no").getValue(String.class);
                        String donor1units = userSnapshot.child("units").getValue(String.class);
                        String donor1time = userSnapshot.child("donor2_Acceptence_Time").getValue(String.class);
                        Toast.makeText(Notification.this, donor2Name+donor2PhoneNo, Toast.LENGTH_SHORT).show();
                        if(donor2Name != null)
                        {
                            LinearLayout first = findViewById(R.id.secondcontainer);
                            first.setVisibility(View.VISIBLE);
                            TextView name = findViewById(R.id.donorname2);
                            TextView unit = findViewById(R.id.donorunit2);
                            TextView time = findViewById(R.id.donortime2);
                            TextView bloodgroup1 = findViewById(R.id.donorbloodgroup2);
                            Button call = findViewById(R.id.call2);
                            Button report = findViewById(R.id.donorReport2);
                            Button accept = findViewById(R.id.imgsrc2);

                            name.setText(donor2Name);
                            unit.setText(donor2PhoneNo);
                            time.setText(donor1time);
                            bloodgroup1.setText(bloodg);
                        }
                    }

                    if (hasDonor3) {
                        String donor3Name = userSnapshot.child("donor3_name").getValue(String.class);
                        String donor3PhoneNo = userSnapshot.child("donor3_phone_no").getValue(String.class);
                        String donor1units = userSnapshot.child("units").getValue(String.class);
                        String donor1time = userSnapshot.child("donor3_Acceptence_Time").getValue(String.class);
                        if(donor3Name != null)
                        {
                            LinearLayout first = findViewById(R.id.thirdcontainer);
                            first.setVisibility(View.VISIBLE);
                            TextView name = findViewById(R.id.donorname3);
                            TextView unit = findViewById(R.id.donorunit3);
                            TextView time = findViewById(R.id.donortime3);
                            TextView bloodgroup1 = findViewById(R.id.donorbloodgroup3);
                            Button call = findViewById(R.id.call3);
                            Button report = findViewById(R.id.donorReport3);
                            Button accept = findViewById(R.id.imgsrc3);

                            name.setText(donor3Name);
                            unit.setText(donor3PhoneNo);
                            time.setText(donor1time);
                            bloodgroup1.setText(bloodg);
                        }

                    }

                    // Add new donor data if needed

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if there is any issue with fetching data
                Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Failed to check data, try again later", Toast.LENGTH_SHORT).show();
            }
        });



    }

}

