package com.example.blood_donation;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecieverAddressREvised extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView mapsearchview;
    private List<Marker> markerList;
    ImageButton menu;
    Double latitude,longitude,distance;
    String name,blood_group,phoneno,recieveText;
   Double distance1;
   String distance3;
    BottomNavigationView bottomNavigationView;
    MenuItem disabledItem;
    Menu menu1;
    MenuItem disable,disable1;
    private static final String DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";
    private  static final String apiKey = "AIzaSyACgXzGVyxphCWzAS7ILVmGC9AumlQoOn4";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciever_address_revised);

        LatLng origin = new LatLng(37.7749, -122.4194); // Example origin
        LatLng destination = new LatLng(34.0522, -118.2437); // Example destination
        getDirections(origin, destination);
        // tool bar open code
        Intent intent = getIntent();
        String recieveText1 = intent.getStringExtra("text_key1");
        String recieveText2 = intent.getStringExtra("g" +
                "mail");
        if(recieveText1 == null)
        {
            recieveText =recieveText2;
        }
        else if(recieveText2 == null)
        {
            recieveText =recieveText1;
        }
        else
        {
            Toast.makeText(this, "recievetext is == null", Toast.LENGTH_SHORT).show();
        }

        final DrawerLayout drawerLayout = findViewById(R.id.main);
        final ImageButton menu_drawer = findViewById(R.id.menu_drawer);
       NavigationView navigationView  = findViewById(R.id.navigationView);

       


        bottomNavigationView = findViewById(R.id.bottomnavigation);
        menu1 =  bottomNavigationView.getMenu();
         disable = menu1.findItem(R.id.direction);
         disable1 = menu1.findItem(R.id.donateblood);


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
                if(itemid == R.id.direction)
                {
                    Toast.makeText(RecieverAddressREvised.this, "first choose the destination address", Toast.LENGTH_SHORT).show();
                }

                return false;

            }
        });
        databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                final String get_lat = snapshot.child(recieveText).child("latitude").getValue(String.class);
                final String get_lon = snapshot.child(recieveText).child("longitude").getValue(String.class);
                final String name1 = snapshot.child(recieveText).child("Name").getValue(String.class);
                final String bg = snapshot.child(recieveText).child("blood_group").getValue(String.class);
                final String ph = snapshot.child(recieveText).child("phone no").getValue(String.class);

                  latitude= Double.parseDouble(get_lat);
                  longitude = Double.parseDouble(get_lon);

                phoneno= "PHONE NO :"+ph;
                name = "NAME   :"+name1;
                blood_group =  "BLOOD G : "+bg;
                Toast.makeText(RecieverAddressREvised.this, latitude+" "+ longitude+ blood_group, Toast.LENGTH_SHORT).show();

                fetchUserlocation();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                }
                return false;
            }
        });
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
                Toast.makeText(RecieverAddressREvised.this, latitude+" "+ longitude+ blood_group, Toast.LENGTH_SHORT).show();
                String location = mapsearchview.getQuery().toString();
                List<Address> addressList = null;
                if(location != null)
                {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try
                    {
                        addressList = geocoder.getFromLocationName(location,6);
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,30));
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
    private void fetchUserlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            // Display current location
                            Toast.makeText(RecieverAddressREvised.this, latitude + " " + longitude + blood_group, Toast.LENGTH_SHORT).show();

                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Set map type to normal
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            if (latitude != null && longitude != null) {


                                LatLng cury = new LatLng(latitude, longitude);

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(cury)
                                        .icon(setIcon(RecieverAddressREvised.this,R.drawable.baseline_person_pin_24)));

                                distance1 = calculateDistance(cury, currentLocation);
                                distance3 = "DISTANCE :" + distance1.toString() + " km";

                                MarkerInfo mi = new MarkerInfo(name, blood_group, distance3, phoneno);
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
                            } else {
                                // Handle the case where latitude or longitude is null
                            }

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
                            Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void displayImageAtBottom() {
        // Find the ImageView from the layout

        // Make the image visible
    }
    private void hideImage() {
        ImageView imageView = findViewById(R.id.imageBottom);
        imageView.setVisibility(View.GONE);  // Hide the image
    }
    public BitmapDescriptor setIcon(Activity context,int Rid)

    {
        Drawable drawable = ActivityCompat.getDrawable(context,Rid);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }




    private void getDirections(LatLng origin, LatLng destination) {
        // Build the Directions API request URL
        String url = buildDirectionsUrl(origin, destination);

        // Create OkHttp client and make the request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Toast.makeText(this, "roite is drawing", Toast.LENGTH_SHORT).show();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Error in directions API request", e);
                runOnUiThread(() -> Toast.makeText(RecieverAddressREvised.this, "Error fetching directions", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    parseDirectionsResponse(jsonResponse);

                } else {
                    Log.e(TAG, "API Response unsuccessful");
                }
            }
        });
    }

    private String buildDirectionsUrl(LatLng origin, LatLng destination) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String key = "key=" + apiKey;
        Toast.makeText(this, "direction is building", Toast.LENGTH_SHORT).show();
        // Build the full URL
        return DIRECTIONS_API_URL + "?" + originStr + "&" + destinationStr + "&" + key;
    }

    private void parseDirectionsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray routes = jsonObject.getJSONArray("routes");

            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONObject polyline = route.getJSONObject("overview_polyline");
                String encodedPolyline = polyline.getString("points");

                // Decode the polyline and draw it on the map
                List<LatLng> decodedPath = decodePolyline(encodedPolyline);
                runOnUiThread(() -> drawPolylineOnMap(decodedPath));
                Toast.makeText(this, "parsing the direction", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing directions response", e);
        }
    }


    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int shift = 0, result = 0;
            while (true) {
                int b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                if (b < 0x20) break;
            }
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            while (true) {
                int b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                if (b < 0x20) break;
            }
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng point = new LatLng((lat / 1E5), (lng / 1E5));
            polyline.add(point);
            Toast.makeText(this, "polyling in designing", Toast.LENGTH_SHORT).show();
        }
        return polyline;
    }


    private void drawPolylineOnMap(List<LatLng> decodedPath) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(decodedPath)
                .width(10)
                .color(ContextCompat.getColor(this, R.color.polyline_color))
                .geodesic(true);

        mMap.addPolyline(polylineOptions);

        // Adjust camera position to show the entire route
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : decodedPath) {
            builder.include(latLng);
        }

        LatLngBounds bounds = builder.build();
        int padding = 100; // padding around the route
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cameraUpdate);
        Toast.makeText(this, "all things is clear", Toast.LENGTH_SHORT).show();
    }


}


