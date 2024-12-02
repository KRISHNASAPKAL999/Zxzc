package com.example.blood_donation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity4 extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    NavigationView navigationView;
    ViewPager nslide,NSLIDE1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");


    ImageButton add_c,Requestblood_c,donateblood_c,list_of_reciever_c,donor_l,hospital_l,blood_B_l;
    TextView profile_c;
    String recieveText,recievetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // The user is signed in
            String recieveText1= currentUser.getEmail();
            recieveText =recieveText1.replace(".","_");
        } else {
            Toast.makeText(this, "not done", Toast.LENGTH_SHORT).show();

        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if(itemid == R.id.donorlocation)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                }
                if(itemid == R.id.recieveblood)
                {
                    Intent intent = new Intent(getApplicationContext(), Request_blood.class);
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
drawerLayout = findViewById(R.id.main);
navigationView = findViewById(R.id.navigationView);
ImageButton menudriver = findViewById(R.id.menu_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if (itemid == R.id.HOME) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.LOG_OUT) {
                    Intent intent = new Intent(getApplicationContext(), login_Activity.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.DONOR_DETAIL) {
                    Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.blood_bank) {
                    Intent intent = new Intent(getApplicationContext(), mapsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.HOSPITAL_LOCATION) {
                    Intent intent = new Intent(getApplicationContext(), Hospital_location.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.DONOR_NEAR_ME) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.REQUEST_BLOOD) {
                    Intent intent = new Intent(getApplicationContext(), Request_blood.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.PROFILE) {
                    Intent intent = new Intent(getApplicationContext(), profile.class);
                    startActivity(intent);
                    finish();
                }
                else if (itemid == R.id.NOTIFICATION) {
                    Intent intent = new Intent(getApplicationContext(), Notification.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
menudriver.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        drawerLayout.open();
    }
});





        final ImageButton notificarion = findViewById(R.id.notification);
        notificarion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                startActivity(intent);
            }
        });

        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (recieveText == null || recieveText.isEmpty()) {
                    Toast.makeText(MainActivity4.this, "RecieveText is null or empty", Toast.LENGTH_SHORT).show();
                    return;  // Exit early if recieveText is invalid
                }

                if (snapshot.exists()) {
                    if (snapshot.hasChild(recieveText)) {
                        String get_email = snapshot.child(recieveText).child("email").getValue(String.class);
                        String get_name = "   " + snapshot.child(recieveText).child("fullname").getValue(String.class);
                        String get_blood = snapshot.child(recieveText).child("bloodgroup").getValue(String.class);

                        // Update the UI with the fetched data
                        Button blood = findViewById(R.id.bgmain);
                        TextView name = findViewById(R.id.namemain);
                        blood.setText(get_blood);
                        name.setText(get_name);

                        // Optionally show the fetched data in a Toast message
                        Toast.makeText(MainActivity4.this, get_name + " " + get_blood, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity4.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity4.this, "Data snapshot is empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log the error for debugging purposes
                Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                Toast.makeText(MainActivity4.this, "Failed to fetch data. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });


        profile_c = findViewById(R.id.profile_card);

        Requestblood_c = findViewById(R.id.requestblood_card);
        donateblood_c = findViewById(R.id.donateblood_card);

        donor_l = findViewById(R.id.nearbydonor);
        hospital_l = findViewById(R.id.nearbyhospital);
        blood_B_l = findViewById(R.id.nearbybloodbank);

        profile_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.putExtra("phone_no",recievetext);
                startActivity(intent);
            }
        });


        Requestblood_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Request_blood.class);
                startActivity(intent);
            }
        });

        donateblood_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DonorLocation.class);
                startActivity(intent);
            }
        });



        donor_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

        hospital_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hospital_location.class);
                startActivity(intent);
            }
        });

        blood_B_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), mapsActivity.class);
                startActivity(intent);
            }
        });


        navigationView = findViewById(R.id.navigationView);


        final ImageButton menu_drawer = findViewById(R.id.menu_drawer);

        NSLIDE1 = findViewById(R.id.slidemeNU1);
        nslide =  findViewById(R.id.slidemenu);
        Viewpageradapter adapter1 = new Viewpageradapter(this);
        NSLIDE1.setAdapter(adapter1);
        adapter1.fetchUserFromFirebase();
        Viewpageradapter1 nun = new Viewpageradapter1(this);
        nslide.setAdapter(nun);










    }
}