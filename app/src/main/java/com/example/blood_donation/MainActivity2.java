package com.example.blood_donation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView mapsearchview;
    private List<Marker> markerList;
    ImageButton menu;
    private ArrayList<Double> latitudes = new ArrayList<>();
    private ArrayList<Double> longitudes = new ArrayList<>();
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> BloodGroup = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    MenuItem disabledItem;
    Menu menu1;
    MenuItem disable,disable1;
    double userLat,userLong;
    LatLng userlocation,destinationlocation;
    private Spinner bloodGroupSpinner;
    String bloodGroup;
    double currlatitude,currlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
       final DrawerLayout drawerLayout = findViewById(R.id.main);
       final ImageButton menu_drawer = findViewById(R.id.menu_drawer);
        final NavigationView navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        menu1 =  bottomNavigationView.getMenu();
        disable = menu1.findItem(R.id.direction);
        disable1 = menu1.findItem(R.id.donateblood);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        setupDropdowns();


        // In your setupDropdowns() method
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected blood group
                String selectedBloodGroup = parentView.getItemAtPosition(position).toString();

                // Fetch data from Firebase based on the selected blood group
                fetchDonorDetails(selectedBloodGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optionally handle case where nothing is selected
            }
        });




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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if(itemid == R.id.HOME)
                {
                    Intent intent = new Intent(getApplicationContext(),MainActivity4.class);
                    startActivity(intent);
                    finish();
                }
                else if(itemid == R.id.LOG_OUT)
                {
                    Intent intent = new Intent(getApplicationContext(), login_Activity.class);
                    startActivity(intent);
                    finish();
                }
                else if(itemid == R.id.DONOR_DETAIL)
                {
                    Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
                    startActivity(intent);
                    finish();
                }
                else if(itemid == R.id.blood_bank)
                {
                    Intent intent = new Intent(getApplicationContext(), mapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(itemid == R.id.HOSPITAL_LOCATION)
                {
                    Intent intent = new Intent(getApplicationContext(), Hospital_location.class);
                    startActivity(intent);
                    finish();
                }
                else if(itemid == R.id.DONOR_NEAR_ME)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                    finish();
                }

                else if(itemid == R.id.REQUEST_BLOOD)
                {
                    Intent intent = new Intent(getApplicationContext(), Request_blood.class);
                    startActivity(intent);
                    finish();
                }
                else if (itemid == R.id.PROFILE) {
                    Intent intent = new Intent(getApplicationContext(), profile.class);
                    startActivity(intent);
                    finish();
                } else if (itemid == R.id.NOTIFICATION) {
                    Intent intent = new Intent(getApplicationContext(), Notification.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
       menu_drawer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MainActivity2.this, "i am here", Toast.LENGTH_SHORT).show();
               drawerLayout.open();
           }
       });










        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
fetchUserlocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Load the style from a raw resource
            InputStream is = getResources().openRawResource(R.raw.map_style);
            String json = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Toast.makeText(this, "map theme added", Toast.LENGTH_SHORT).show();
            }
            boolean success = mMap.setMapStyle(new MapStyleOptions(json));
            if (!success) {
                // Log if the style was not successfully applied
                Log.e("MapActivity", "Style parsing failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap);
                fetchUserlocation();

            }
        }
    }

    private void fetchUserlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

currlatitude= location.getLatitude();
currlongitude = location.getLongitude();

                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            Toast.makeText(MainActivity2.this, location.getLatitude()+" "+ currlatitude, Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity2.this, location.getLongitude()+" "+ currlongitude, Toast.LENGTH_SHORT).show();








                        } else {
                            Toast.makeText(MainActivity2.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public BitmapDescriptor setIcon(Activity context, int Rid)

    {
        Drawable drawable = ActivityCompat.getDrawable(context,Rid);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Double calculateDistance(LatLng start, LatLng end) {
        // Convert LatLng to Location objects
        Location location1 = new Location("start");
        location1.setLatitude(start.latitude);
        location1.setLongitude(start.longitude);

        Location location2 = new Location("end");
        location2.setLatitude(end.latitude);
        location2.setLongitude(end.longitude);

        // Calculate and return the distance in meters
        float v = location1.distanceTo(location2) / 1000;
        DecimalFormat df = new DecimalFormat("#.00");
        Double distance = Double.valueOf(df.format(v));
        return  distance;
    }




    private void setupDropdowns() {
        // Set up blood group dropdown
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.blood_groups));
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);

        // Set up user role dropdown

    }




    private void fetchDonorDetails(final String bloodGroup) {
        // Clear previous data
        latitudes.clear();
        longitudes.clear();
        Names.clear();
        BloodGroup.clear();

        // Clear previous markers on the map
        if (mMap != null) {
            mMap.clear();  // Clears all markers on the map
        }

        // Show loading indicator while fetching data (if needed)


        // Fetch data from Firebase based on the selected blood group
        databaseReference.child("Donor Detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Process the data for the selected blood group
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String latitude1 = snapshot1.child("latitude").getValue(String.class);
                    String longitude1 = snapshot1.child("longitude").getValue(String.class);
                    String fullname1 = snapshot1.child("full_name").getValue(String.class);
                    String bloodgroup1 = snapshot1.child("blood_group").getValue(String.class);
                    String gender = snapshot1.child("gender").getValue(String.class);
                    String bloodgroup2 = "Blood group: " + bloodgroup1;

                    // Check if the selected blood group is "All"
                    if (bloodGroup.equals("All")) {
                        // Fetch all donor details regardless of the blood group
                        // Parse latitude and longitude values to doubles
                        double latitude = Double.parseDouble(latitude1);
                        double longitude = Double.parseDouble(longitude1);

                        // Add matching donor's details to lists (if you need them later)
                        latitudes.add(latitude);
                        longitudes.add(longitude);
                        Names.add(fullname1);
                        BloodGroup.add(bloodgroup1);

                        // Add a marker on the map for this donor
                        LatLng donorLocation = new LatLng(latitude, longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(donorLocation)
                                .icon(setIcon(MainActivity2.this, R.drawable.baseline_3p_24)));

                        LatLng currentLocation = new LatLng(currlatitude, currlongitude);
                        double distance1 = calculateDistance(donorLocation, currentLocation);
                        Toast.makeText(MainActivity2.this, currlatitude + " " + latitude, Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity2.this, currlongitude + " " + longitude, Toast.LENGTH_SHORT).show();
                        String distance3 = "DISTANCE: " + distance1 + " km";

                        MarkerInfo mi = new MarkerInfo(fullname1, bloodgroup2, distance3, "ytfygvf");
                        marker.setTag(mi);

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null; // Use default window
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                MarkerInfo markerInfo = (MarkerInfo) marker.getTag();
                                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window_1, null);
                                TextView nameTextView = infoWindow.findViewById(R.id.name);
                                TextView bloodGroupTextView = infoWindow.findViewById(R.id.blood_group);
                                TextView distanceTextView = infoWindow.findViewById(R.id.distance);
                                TextView phoneTextView = infoWindow.findViewById(R.id.phone_number_1);

                                if (markerInfo != null) {
                                    nameTextView.setText(markerInfo.getName());
                                    bloodGroupTextView.setText(markerInfo.getBloodGroup());
                                    distanceTextView.setText(markerInfo.getDistance());
                                    phoneTextView.setText(markerInfo.getPhoneNumber());
                                }

                                return infoWindow;
                            }
                        });

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                // Create an Intent to open Google Maps with directions to the marker location
                                disable.setVisible(true);
                                disable1.setVisible(false);
                                disable.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                                        LatLng markerLocation = marker.getPosition();
                                        String uri = "google.navigation:q=" + markerLocation.latitude + "," + markerLocation.longitude;

                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        intent.setPackage("com.google.android.apps.maps");  // Specify the Google Maps app

                                        // Check if Google Maps is available and start it
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Google Maps not available", Toast.LENGTH_SHORT).show();
                                        }
                                        return false;
                                    }
                                });

                                return false;  // Allow the default behavior (e.g., info window showing)
                            }
                        });
                    } else if (bloodGroup.equals(bloodgroup1)) {
                        // If bloodGroup matches the donor's blood group, process this donor
                        double latitude = Double.parseDouble(latitude1);
                        double longitude = Double.parseDouble(longitude1);

                        // Add matching donor's details to lists
                        latitudes.add(latitude);
                        longitudes.add(longitude);
                        Names.add(fullname1);
                        BloodGroup.add(bloodgroup1);

                        // Add a marker on the map for this donor
                        LatLng donorLocation = new LatLng(latitude, longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(donorLocation)
                                .icon(setIcon(MainActivity2.this, R.drawable.baseline_3p_24)));

                        LatLng currentLocation = new LatLng(currlatitude, currlongitude);
                        double distance1 = calculateDistance(donorLocation, currentLocation);
                        Toast.makeText(MainActivity2.this, currlatitude + " " + latitude, Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity2.this, currlongitude + " " + longitude, Toast.LENGTH_SHORT).show();
                        String distance3 = "DISTANCE: " + distance1 + " km";

                        MarkerInfo mi = new MarkerInfo(fullname1, bloodgroup2, distance3, "ytfygvf");
                        marker.setTag(mi);

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null; // Use default window
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                MarkerInfo markerInfo = (MarkerInfo) marker.getTag();
                                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window_1, null);
                                TextView nameTextView = infoWindow.findViewById(R.id.name);
                                TextView bloodGroupTextView = infoWindow.findViewById(R.id.blood_group);
                                TextView distanceTextView = infoWindow.findViewById(R.id.distance);
                                TextView phoneTextView = infoWindow.findViewById(R.id.phone_number_1);

                                if (markerInfo != null) {
                                    nameTextView.setText(markerInfo.getName());
                                    bloodGroupTextView.setText(markerInfo.getBloodGroup());
                                    distanceTextView.setText(markerInfo.getDistance());
                                    phoneTextView.setText(markerInfo.getPhoneNumber());
                                }

                                return infoWindow;
                            }
                        });

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                // Create an Intent to open Google Maps with directions to the marker location
                                disable.setVisible(true);
                                disable1.setVisible(false);
                                disable.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                                        LatLng markerLocation = marker.getPosition();
                                        String uri = "google.navigation:q=" + markerLocation.latitude + "," + markerLocation.longitude;

                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        intent.setPackage("com.google.android.apps.maps");  // Specify the Google Maps app

                                        // Check if Google Maps is available and start it
                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Google Maps not available", Toast.LENGTH_SHORT).show();
                                        }
                                        return false;
                                    }
                                });

                                return false;  // Allow the default behavior (e.g., info window showing)
                            }
                        });
                    }
                }

                // Zoom to a default location or the first donor's location if available
                if (!latitudes.isEmpty()) {
                    LatLng firstDonor = new LatLng(latitudes.get(0), longitudes.get(0));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstDonor, 10));  // Adjust zoom level as needed
                }

                // If no data is found, show a message
                if (Names.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "No donors found for " + bloodGroup, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read failure here
                Log.e("DatabaseError", "Failed to read donor details", error.toException());
            }
        });

    }








}