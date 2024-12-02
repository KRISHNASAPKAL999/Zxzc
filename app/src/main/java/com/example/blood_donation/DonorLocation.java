package com.example.blood_donation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DonorLocation extends FragmentActivity implements OnMapReadyCallback {
    RadioButton type;
    RadioButton type1;
    RadioButton type2;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView mapsearchview;
    private List<Marker> markerList;
    String longitude1;
    String latitude1;
    EditText fullName,phoneno,email,area;
     String blood_group;
     String gender;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String recieveText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_location);
fullName =findViewById(R.id.full_name);
phoneno = findViewById(R.id.phone_D);
email = findViewById(R.id.email_D);
area = findViewById(R.id.AREA_D);
        final RadioButton male = findViewById(R.id.MALE);
        final RadioButton female = findViewById(R.id.female);
        final RadioGroup bloodgroup1 = findViewById(R.id.bloodGroup1);
        final RadioGroup bloodgroup2 = findViewById(R.id.bloodGroup4);
        final RadioGroup bloodgroup3 = findViewById(R.id.bloodGroup3);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // The user is signed in
            String recieveText1= currentUser.getEmail();
            recieveText =recieveText1.replace(".","_");
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
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setChecked(false);
                gender = male.getText().toString();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setChecked(false);
                gender = female.getText().toString();
            }
        });
        // To combine the bloodgroup radiobutton;
       RadioGroup.OnCheckedChangeListener listener= new RadioGroup.OnCheckedChangeListener()
       {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               if(checkedId != -1)
               {
                   if(group != bloodgroup1)
                       bloodgroup1.clearCheck();
                   if(group != bloodgroup2 )
                       bloodgroup2.clearCheck();
                   if(group != bloodgroup3)
                       bloodgroup3.clearCheck();

               }
           }
       };
       bloodgroup1.setOnCheckedChangeListener(listener);
       bloodgroup2.setOnCheckedChangeListener(listener);
       bloodgroup3.setOnCheckedChangeListener(listener);

        final Button getValue = findViewById(R.id.SUBMIT);
        getValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int Selectid = bloodgroup1.getCheckedRadioButtonId();
 int selectid1 =bloodgroup2.getCheckedRadioButtonId();
 int selectid3 = bloodgroup3.getCheckedRadioButtonId();
                type = findViewById(Selectid);
                type1 = findViewById(selectid1);
                type2 = findViewById(selectid3);
                if(type == findViewById(Selectid) && type1 == null && type2 == null) {
                    blood_group = type.getText().toString();

                }

                if(type1 == findViewById(selectid1) && type == null && type2 == null) {
                    blood_group = type1.getText().toString();

                }

                if(type2 == findViewById(selectid3) && type == null && type1 == null) {
                    blood_group = type2.getText().toString();

                }


           databaseReference.child("Donor Detail").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   String fullname1 = fullName.getText().toString();
                   String phoneno2 = phoneno.getText().toString();
                   String email12 = email.getText().toString();
                   String email1 = email12.replace(".","_");
                   String area1 = area.getText().toString();
                   Toast.makeText(DonorLocation.this, recieveText+" "+email1, Toast.LENGTH_SHORT).show();
                   String currentdate = getFormattedDate();

                   if(email1.trim().equals(recieveText.trim())) {

                       if(snapshot.hasChild(email1))
                       {
                           Toast.makeText(DonorLocation.this, "YOU HAS ALREADY REGESTERED AS DONOR", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           databaseReference.child("Donor Detail").child(email1).child("email").setValue(email1);
                           databaseReference.child("Donor Detail").child(email1).child("phone_no").setValue(phoneno2);
                           databaseReference.child("Donor Detail").child(email1).child("full_name").setValue(fullname1);
                           databaseReference.child("Donor Detail").child(email1).child("area").setValue(area1);
                           databaseReference.child("Donor Detail").child(email1).child("blood_group").setValue(blood_group);
                           databaseReference.child("Donor Detail").child(email1).child("latitude").setValue(latitude1);
                           databaseReference.child("Donor Detail").child(email1).child("longitude").setValue(longitude1);
                           databaseReference.child("Donor Detail").child(email1).child("gender").setValue(gender);
                           databaseReference.child("Donor Detail").child(email1).child("time").setValue(currentdate);
                           Toast.makeText(DonorLocation.this, "Donor details added to database", Toast.LENGTH_SHORT).show();

                           Intent intent = new Intent(getApplicationContext(),MainActivity4.class);
                           startActivity(intent);
                           finish();
                       }
                   } else {
                       Toast.makeText(DonorLocation.this, "USE THE LOGIN EMAIL", Toast.LENGTH_SHORT).show();
                   }


               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
            }
        });

        // tool bar open code


        //malpsearch view code



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {



                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                             mMap.addMarker(new MarkerOptions().position(currentLocation).title("MeraLocation"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 100));
                            String message = "updated locaation"+" " +
                                    Double.toString(location.getLatitude()) +" " +
                                    Double.toString(location.getLongitude());
                            Toast.makeText(DonorLocation.this, message, Toast.LENGTH_SHORT).show();
                            latitude1 = String.valueOf(location.getLatitude());
                             longitude1=String.valueOf(location.getLongitude());







                        } else {
                            Toast.makeText(DonorLocation.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap);
            }
        }
    }



    private String getFormattedDate() {
        Date currentDate = new Date();

        // Define the date format pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,dd,yyyy", Locale.getDefault());

        // Format the current date and return it as a string
        return dateFormat.format(currentDate);
    }


}
