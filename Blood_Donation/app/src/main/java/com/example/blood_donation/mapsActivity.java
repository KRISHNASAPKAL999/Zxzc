package com.example.blood_donation;

import android.Manifest;
import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class mapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView mapsearchview;
    private List<Marker> markerList;
    ImageButton menu;
    BottomNavigationView bottomNavigationView;
    MenuItem disabledItem;
    Menu menu1;
    MenuItem disable,disable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // tool bar open code

        final DrawerLayout drawerLayout = findViewById(R.id.main);
       
        final NavigationView navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        menu1 =  bottomNavigationView.getMenu();
        disable = menu1.findItem(R.id.direction);
        disable1 = menu1.findItem(R.id.donateblood);

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
        final ImageButton notificarion = findViewById(R.id.notification);
        notificarion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                startActivity(intent);
            }
        });

        final ImageButton menu_drawer = findViewById(R.id.thisis);
        menu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.open();
            }
        });

        //malpsearch view code
        mapsearchview = findViewById(R.id.map_search);
        mapsearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapsearchview.getQuery().toString();
                List<Address> addressList = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(mapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 6);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });

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

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 100));
                            LatLng Sion_blood_component = new LatLng(19.043494, 72.864576);
                            String distance1 = "Distance :"+String.valueOf(calculateDistance(Sion_blood_component, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(Sion_blood_component).title("Sion_blood_component")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance1));


                            LatLng a1 = new LatLng(19.001364, 72.840541);
                            String distance2 = "Distance :"+String.valueOf(calculateDistance(a1, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a1).title("TAJ MEMORIAL HOSPITAL BLOOD BANK")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance2));

                            LatLng a2 = new LatLng(19.000824, 72.841688);
                            String distance3 = "Distance :"+String.valueOf(calculateDistance(a2, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a2).title("BLOOD BANK KEM")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance3));

                            LatLng a3 = new LatLng(19.033963, 72.837956);
                            String distance4 = "Distance :"+String.valueOf(calculateDistance(a3, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a3).title("PD hinduja national hospital")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance4));

                            LatLng a4 = new LatLng(19.043364, 72.842808);
                            String distance5 = "Distance :"+String.valueOf(calculateDistance(a4, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a4).title("WEST COAST BLOOD BANK")
                                            .snippet(distance5)
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24)));

                            LatLng a5 = new LatLng(19.059660, 72.888574);
                            String distance6 = "Distance :"+String.valueOf(calculateDistance(a5, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a5).title("RAM SURAT MAURYA")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance6));

                            LatLng a6 = new LatLng(19.043494, 72.911076);
                            String distance7 = "Distance :"+String.valueOf(calculateDistance(a6, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a6).title("PALLAVI BLOOD BANK")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance7));

                            LatLng a7 = new LatLng(19.082418, 72.904511);
                            String distance8 = "Distance :"+String.valueOf(calculateDistance(a7, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a7).title("ANVIKSHA PATHOLOGY LABORATORY")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance8));

                            LatLng a8 = new LatLng(19.083548, 72.915203);
                            String distance9 = "Distance :"+String.valueOf(calculateDistance(a8, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(a8).title("NORTH EAST ZONE CHEMISTS")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance9));

                            LatLng A9 = new LatLng(19.051192, 72.837858);
                            String distance10 = "Distance :"+String.valueOf(calculateDistance(A9, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(A9).title("SHANTI NURSHING HOME")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance10));

                            LatLng A10 = new LatLng(19.050965, 72.829242);
                            String distance11 = "Distance :"+String.valueOf(calculateDistance(A10, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(A10).title("LILAVATI HOSPITAL AND REASEARCH CENTRE")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance11));

                            LatLng A11 = new LatLng(19.055244, 72.830189);
                            String distance12 = "Distance :"+String.valueOf(calculateDistance(A11, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(A11).title("NIZAMIYA BLOOD DEPO")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance12));

                            LatLng A12 = new LatLng(19.0576287, 72.8336283);
                            String distance13 = "Distance :"+String.valueOf(calculateDistance(A12, currentLocation))+"km";
                            mMap.addMarker(new MarkerOptions().position(A12).title("KB BHABHA GENRAL HOSPITAL BLOOD BANK")
                                    .icon(setIcon(mapsActivity.this,R.drawable.baseline_beenhere_24))
                                    .snippet(distance13));
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

                        } else {
                            Toast.makeText(mapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
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


}
