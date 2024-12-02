package com.example.blood_donation;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

public class donorlistforpatientadapter extends RecyclerView.Adapter<donorlistforpatientadapter.MyViewHolder> {
    Context context;
    ArrayList<donorforpatientdata> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String name,gmail,phoneno;
    AlertDialog alertDialog;
    public donorlistforpatientadapter(Context context,ArrayList<donorforpatientdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public donorlistforpatientadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.donorlistforpatient,parent,false);
        return new MyViewHolder(v);
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        donorforpatientdata user = list.get(position);
        holder.Name.setText(user.getFull_name());
        holder.bloodgroup.setText(user.getBlood_group());
        holder.units.setText(user.getArea());


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(user.getPhone_no());
            }
        });

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("emaildonor",user.getEmail());
                context.startActivity(intent);
            }
        });
        holder.time.setText(user.getTime());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog to ask for confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("If you want to do some fraud, strict action may be taken on you, so be sincere. Do you want to proceed?")
                        .setCancelable(false) // Prevent the dialog from being dismissed by clicking outside
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Proceed with the code if "Yes" is clicked
                                FirebaseUser currentUser = mAuth.getCurrentUser();

                                if (currentUser != null) {
                                    // The user is signed in
                                } else {
                                    Toast.makeText(context, "User is not signed in", Toast.LENGTH_SHORT).show();
                                }

                                String recieveText1 = currentUser.getEmail();
                                String recieveText = recieveText1.replace(".", "_");

                                databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (recieveText == null) {
                                            Toast.makeText(context, "Null value", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Data is preparing", Toast.LENGTH_SHORT).show();
                                            final String get_email = snapshot.child(recieveText).child("email").getValue(String.class);
                                            final String get_name = snapshot.child(recieveText).child("fullname").getValue(String.class);
                                            final String get_phoneno = snapshot.child(recieveText).child("phoneNo").getValue(String.class);

                                            gmail = get_email;
                                            name = get_name;
                                            phoneno = get_phoneno;

                                            Intent intent = new Intent(context, RecieverAddressREvised.class);
                                            intent.putExtra("gmail2", user.getEmail());
                                            context.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error
                                    }
                                });

                                databaseReference.child("Donor Detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // Get the current time
                                        String currentTime = getFormattedDate(); // Assuming you have this method for getting the formatted date
                                        Toast.makeText(context, "Executing", Toast.LENGTH_SHORT).show();

                                        if (snapshot.hasChild(user.getEmail())) {
                                            DataSnapshot userSnapshot = snapshot.child(user.getEmail());

                                            // Check if donor1, donor2, and donor3 are already added
                                            boolean hasDonor1 = userSnapshot.hasChild("patient_phone_no") && userSnapshot.hasChild("patient_name");
                                            boolean hasDonor2 = userSnapshot.hasChild("patient2_phone_no") && userSnapshot.hasChild("patient2_name");
                                            boolean hasDonor3 = userSnapshot.hasChild("patient3_phone_no") && userSnapshot.hasChild("patient3_name");

                                            // Fetch existing donor data from the database
                                            String donor1Name = hasDonor1 ? userSnapshot.child("patient_name").getValue(String.class) : null;
                                            String donor1PhoneNo = hasDonor1 ? userSnapshot.child("patient_phone_no").getValue(String.class) : null;
                                            String donor2Name = hasDonor2 ? userSnapshot.child("patient2_name").getValue(String.class) : null;
                                            String donor2PhoneNo = hasDonor2 ? userSnapshot.child("patient2_phone_no").getValue(String.class) : null;
                                            String donor3Name = hasDonor3 ? userSnapshot.child("patient3_name").getValue(String.class) : null;
                                            String donor3PhoneNo = hasDonor3 ? userSnapshot.child("patient3_phone_no").getValue(String.class) : null;

                                            // Check for name and phone number conflicts
                                            if (name.equals(donor1Name) && phoneno.equals(donor1PhoneNo)) {
                                                Toast.makeText(context, "Donor 1 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                return;  // Exit if match found
                                            } else if (name.equals(donor2Name) && phoneno.equals(donor2PhoneNo)) {
                                                Toast.makeText(context, "Donor 2 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                return;  // Exit if match found
                                            } else if (name.equals(donor3Name) && phoneno.equals(donor3PhoneNo)) {
                                                Toast.makeText(context, "Donor 3 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                return;  // Exit if match found
                                            }

                                            // Proceed with adding donor data
                                            if (hasDonor3) {
                                                Toast.makeText(context, "Donor 3 data already exists", Toast.LENGTH_SHORT).show();
                                            } else if (hasDonor1 && hasDonor2) {
                                                // Add donor3 if both donor1 and donor2 are present
                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient3_name")
                                                        .setValue(name);

                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient3_phone_no")
                                                        .setValue(phoneno);

                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient3_Acceptence_Time")
                                                        .setValue(currentTime);

                                                Toast.makeText(context, "Donor 3 data added successfully", Toast.LENGTH_SHORT).show();
                                            } else if (hasDonor1) {
                                                // Add donor2 if donor1 is present
                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient2_name")
                                                        .setValue(name);

                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient2_phone_no")
                                                        .setValue(phoneno);

                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient2_Acceptence_Time")
                                                        .setValue(currentTime);

                                                Toast.makeText(context, "Donor 2 data added successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Add donor1 data
                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient_phone_no")
                                                        .setValue(phoneno);

                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient_name")
                                                        .setValue(name);

                                                databaseReference.child("Donor Detail")
                                                        .child(user.getEmail())
                                                        .child("patient_Acceptence_Time")
                                                        .setValue(currentTime);

                                                Toast.makeText(context, "Donor 1 data added successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // Create new user data with donor1 if user does not exist
                                            Toast.makeText(context, "Data added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                                        Toast.makeText(context, "Failed to check data, try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing if "No" is clicked
                                dialog.dismiss();  // Dismiss the dialog
                            }
                        });

                // Show the dialog
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name,units,bloodgroup,time,emergency;
        Button accept,report,call;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.namelist);
            units = itemView.findViewById(R.id.unitsneededlist);
            bloodgroup = itemView.findViewById(R.id.bloodgrouplist);
            time = itemView.findViewById(R.id.TIME);
            accept = itemView.findViewById(R.id.imgsrc);
            emergency = itemView.findViewById(R.id.emergency);
            report = itemView.findViewById(R.id.messageButton);
            call    = itemView.findViewById(R.id.call);

        }
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
}
