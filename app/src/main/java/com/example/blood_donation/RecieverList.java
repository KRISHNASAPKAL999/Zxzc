package com.example.blood_donation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecieverList extends AppCompatActivity {
   RecyclerView recyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    RecieverlistAdapter myadapter;
    ArrayList<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reciever_list);
        recyclerView = findViewById(R.id.listofitem);
        TextView patientlist = findViewById(R.id.patientlist);
        TextView donortlist = findViewById(R.id.donorlist);
        patientlist.setTextColor(Color.WHITE); // Sets text color to blue
        patientlist.setBackgroundResource(R.drawable.downnavigation); // Use a drawable resource

        donortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),donorlistforpatientmain.class);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myadapter = new RecieverlistAdapter(this,list);
        recyclerView.setAdapter(myadapter);
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear(); // Clear the existing list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the user object
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        // Check if the user has an emergency field set to "YES"
                        if ("YES".equals(user.getEmergency())) {
                            // Add the user at the beginning of the list
                            list.add(0, user);
                        } else {
                            // Add the user at the end of the list
                            list.add(user);
                        }
                    }
                }

                // Notify the adapter to update the RecyclerView
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
            }
        });



    }
}
