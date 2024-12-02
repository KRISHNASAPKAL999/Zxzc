package com.example.blood_donation;



import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.example.blood_donation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Viewpageradapter extends PagerAdapter {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    private ArrayList<String> BloodGroup = new ArrayList<>();
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> address = new ArrayList<>();
    private ArrayList<String> Units = new ArrayList<>();
    private ArrayList<String> Posted = new ArrayList<>();
    private ArrayList<String> Number = new ArrayList<>();
    private ArrayList<String> gmail = new ArrayList<>();
    private ArrayList<String> emergency1 = new ArrayList<>();
    AlertDialog alertDialog;
    String number;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String name1,bloodgroup,bloodgroup1;
    String gmail1,PhoneNo1;


    private Context context;

    public Viewpageradapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Names.size();  // Ensure this is dynamically updated when data is fetched
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;  // Ensure correct comparison between the view and object
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Get the context from container (ViewGroup) and use it to access the LayoutInflater
        Context context = container.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Use the inflater to inflate your layout
        View view = inflater.inflate(R.layout.slider_layout, container, false);

         Button slidetitleimage = view.findViewById(R.id.stimage);
        TextView slidehead = view.findViewById(R.id.name);
        TextView slidehead1 = view.findViewById(R.id.address);
        TextView slidehead2 = view.findViewById(R.id.units);
        TextView slidehead3 = view.findViewById(R.id.date_posted);
        Button accept = (Button) view.findViewById(R.id.accept);
        Button call = (Button) view.findViewById(R.id.call);
        Button message = (Button) view.findViewById(R.id.message);
        Button emergency = (Button) view.findViewById(R.id.emergency);

      call.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             makeCall(Number.get(position));


          }
      });
       message.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, MessageActivity.class);
               intent.putExtra("email",gmail.get(position));
               context.startActivity(intent);
           }
       });
        slidetitleimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RecieverList.class);
                context.startActivity(intent);


            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog before proceeding
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to proceed? Strict action may be taken against fraud. Be sincere!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Proceed with the code execution if "Yes" is clicked

                                number = gmail.get(position);
                                FirebaseUser currentUser = mAuth.getCurrentUser();

                                if (currentUser != null) {
                                    // The user is signed in
                                } else {
                                    Toast.makeText(context, "user is not signed in", Toast.LENGTH_SHORT).show();
                                    return; // Exit if user is not signed in
                                }

                                String recieveText1 = currentUser.getEmail();
                                String recieveText = recieveText1.replace(".", "_");

                                databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (recieveText == null) {
                                            Toast.makeText(context, "null value", Toast.LENGTH_SHORT).show();
                                        } else {
                                            final String get_email = snapshot.child(recieveText).child("email").getValue(String.class);
                                            final String get_name = snapshot.child(recieveText).child("fullname").getValue(String.class);
                                            final String get_bloodg = snapshot.child(recieveText).child("bloodgroup").getValue(String.class);
                                            bloodgroup = get_bloodg;
                                            final String get_phoneno = snapshot.child(recieveText).child("phoneNo").getValue(String.class);

                                            gmail1 = get_email;
                                            name1 = get_name;
                                            PhoneNo1 = get_phoneno;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error if needed
                                    }
                                });

                                databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        final String get_bloodg1 = snapshot.child(recieveText).child("blood_group").getValue(String.class);
                                        bloodgroup1 = get_bloodg1;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error if needed
                                    }
                                });

                                // Fetch the patient data and perform checks
                                databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // Get the current time
                                        String currentTime = getFormattedDate(); // Assuming you have this method for getting the formatted date
                                        Toast.makeText(context, gmail.get(position), Toast.LENGTH_SHORT).show();

                                        if (snapshot.hasChild(gmail.get(position))) {
                                            // Fetch the existing data for the patient
                                            DataSnapshot userSnapshot = snapshot.child(gmail.get(position));
                                            Toast.makeText(context, bloodgroup + " " + bloodgroup1, Toast.LENGTH_SHORT).show();

                                            if (!bloodgroup.equals(bloodgroup1)) {
                                                Toast.makeText(context, "You can't donate blood", Toast.LENGTH_SHORT).show();
                                            } else if (recieveText.equals(gmail.get(position))) {
                                                Toast.makeText(context, "You cannot donate blood to yourself", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Check if donor1, donor2, and donor3 are already added
                                                boolean hasDonor1 = userSnapshot.hasChild("donor_phone_no") && userSnapshot.hasChild("donor_name");
                                                boolean hasDonor2 = userSnapshot.hasChild("donor2_phone_no") && userSnapshot.hasChild("donor2_name");
                                                boolean hasDonor3 = userSnapshot.hasChild("donor3_phone_no") && userSnapshot.hasChild("donor3_name");

                                                String donor1Name = hasDonor1 ? userSnapshot.child("donor_name").getValue(String.class) : null;
                                                String donor1PhoneNo = hasDonor1 ? userSnapshot.child("donor_phone_no").getValue(String.class) : null;
                                                String donor2Name = hasDonor2 ? userSnapshot.child("donor2_name").getValue(String.class) : null;
                                                String donor2PhoneNo = hasDonor2 ? userSnapshot.child("donor2_phone_no").getValue(String.class) : null;
                                                String donor3Name = hasDonor3 ? userSnapshot.child("donor3_name").getValue(String.class) : null;
                                                String donor3PhoneNo = hasDonor3 ? userSnapshot.child("donor3_phone_no").getValue(String.class) : null;

                                                // Check for name and phone number conflicts
                                                if (name1.equals(donor1Name) && PhoneNo1.equals(donor1PhoneNo)) {
                                                    Toast.makeText(context, "Donor 1 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                    return;  // Exit if the name and phone number match Donor 1
                                                } else if (name1.equals(donor2Name) && PhoneNo1.equals(donor2PhoneNo)) {
                                                    Toast.makeText(context, "Donor 2 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                    return;  // Exit if the name and phone number match Donor 2
                                                } else if (name1.equals(donor3Name) && PhoneNo1.equals(donor3PhoneNo)) {
                                                    Toast.makeText(context, "Donor 3 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                    return;  // Exit if the name and phone number match Donor 3
                                                }

                                                // Proceed with adding the donor data
                                                if (hasDonor3) {
                                                    Toast.makeText(context, "Donor 3 data already exists", Toast.LENGTH_SHORT).show();
                                                } else if (hasDonor1 && hasDonor2) {
                                                    // Add donor3 data
                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor3_name")
                                                            .setValue(name1);

                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor3_phone_no")
                                                            .setValue(PhoneNo1);

                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor3_Acceptence_Time")
                                                            .setValue(currentTime);

                                                    Toast.makeText(context, "Donor 3 data added successfully", Toast.LENGTH_SHORT).show();
                                                } else if (hasDonor1) {
                                                    // Add donor2 data
                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor2_name")
                                                            .setValue(name1);

                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor2_phone_no")
                                                            .setValue(PhoneNo1);

                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor2_Acceptence_Time")
                                                            .setValue(currentTime);

                                                    Toast.makeText(context, "Donor 2 data added successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Add donor1 data
                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor_phone_no")
                                                            .setValue(PhoneNo1);

                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor_name")
                                                            .setValue(name1);

                                                    databaseReference.child("PATIENT Detail")
                                                            .child(gmail.get(position))
                                                            .child("donor_Acceptence_Time")
                                                            .setValue(currentTime);

                                                    Toast.makeText(context, "Donor 1 data added successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error if needed
                                        Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                                        Toast.makeText(context, "Failed to check data, try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Start the next activity after adding the donor data
                                Intent intent = new Intent(context, RecieverAddressREvised.class);
                                intent.putExtra("text_key1", number);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing if "No" is clicked
                                dialog.dismiss();
                            }
                        });
                // Create and show the dialog
                builder.create().show();
            }
        });
        slidehead3.setText(Posted.get(position));
        slidehead.setText(Names.get(position));  // Set the name
        slidehead1.setText(address.get(position));// Set the blood group
        slidehead2.setText(Units.get(position));
        slidetitleimage.setText(BloodGroup.get(position));

        String text = emergency1.get(position);
        if(text.equals("NO"))
        {
            emergency.setVisibility(View.GONE);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public void fetchUserFromFirebase() {
        databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Names.clear();  // Clear existing data to avoid duplicates
                BloodGroup.clear();
                address.clear();
                Units.clear();
                Posted.clear();
                ArrayList<String> emergencyNames = new ArrayList<>();
                ArrayList<String> emergencyBloodGroup = new ArrayList<>();
                ArrayList<String> emergencyAddress = new ArrayList<>();
                ArrayList<String> emergencyUnits = new ArrayList<>();
                ArrayList<String> emergencyPosted = new ArrayList<>();
                ArrayList<String> emergencyNumber = new ArrayList<>();
                ArrayList<String> emergencyGmail = new ArrayList<>();
                ArrayList<String> emergencyEmergency = new ArrayList<>();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String fullname1 = snapshot1.child("Name").getValue(String.class);
                    String bloodgroup1 = snapshot1.child("blood_group").getValue(String.class);
                    String address1 = snapshot1.child("area").getValue(String.class);
                    String units = snapshot1.child("units").getValue(String.class);
                    String posted1 = snapshot1.child("time").getValue(String.class);
                    String number1 = snapshot1.child("phone_no").getValue(String.class);
                    String gmail1 = snapshot1.child("email").getValue(String.class);
                    String EMERGENCY = snapshot1.child("emergency").getValue(String.class);
                    String units1 = units + " (Blood)";
                    if (EMERGENCY != null && EMERGENCY.equals("NO")) {
                        emergencyNames.add(fullname1);
                        emergencyBloodGroup.add(bloodgroup1);
                        emergencyAddress.add(address1);
                        emergencyUnits.add(units1);
                        emergencyPosted.add(posted1);
                        emergencyNumber.add(number1);
                        emergencyGmail.add(gmail1);
                        emergencyEmergency.add(EMERGENCY);
                    } else {
                        Names.add(fullname1 != null ? fullname1 : "No name provided");
                        BloodGroup.add(bloodgroup1 != null ? bloodgroup1 : "No blood group provided");
                        address.add(address1 != null ? address1 : "No address provided");
                        Units.add(units1);
                        Posted.add(posted1);
                        Number.add(number1);
                        gmail.add(gmail1);
                        emergency1.add(EMERGENCY != null ? EMERGENCY : "No blood group provided");
                    }
                }

                // Append the emergency data to the non-emergency data
                Names.addAll(emergencyNames);
                BloodGroup.addAll(emergencyBloodGroup);
                address.addAll(emergencyAddress);
                Units.addAll(emergencyUnits);
                Posted.addAll(emergencyPosted);
                Number.addAll(emergencyNumber);
                gmail.addAll(emergencyGmail);
                emergency1.addAll(emergencyEmergency);






                // Notify the adapter that data has changed so that the ViewPager updates
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFormattedDate() {
        Date currentDate = new Date();

        // Define the date format pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,dd,yyyy", Locale.getDefault());

        // Format the current date and return it as a string
        return dateFormat.format(currentDate);
    }
    private void makeCall(String phoneNumber) {
        // Check if the CALL_PHONE permission is granted
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions((MainActivity4) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
        } else {
            // Permission is granted, initiate the call
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
    }
    // In your Adapter class, assuming you have a context reference:
    // if in ViewHolder or from constructor if you passed it.

    // Use the context directly to check for installed apps

}
