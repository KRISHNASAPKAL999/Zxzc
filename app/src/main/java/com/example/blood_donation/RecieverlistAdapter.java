package com.example.blood_donation;

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
import android.widget.RadioButton;
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
import java.util.Objects;

public class RecieverlistAdapter extends RecyclerView.Adapter<RecieverlistAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String name,gmail,phoneno;
    AlertDialog alertDialog;
    public RecieverlistAdapter(Context context,ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View v = LayoutInflater.from(context).inflate(R.layout.realative_lay,parent,false);
return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
 User user = list.get(position);
 holder.Name.setText(user.getName());
 holder.bloodgroup.setText(user.getBlood_group());
 holder.units.setText(user.getUnits());
 String yesno = user.getEmergency();
 if(user.getEmergency() != null) {
     if (yesno.equals("NO")) {
         holder.emergency.setVisibility(View.GONE);
         holder.emergency.setVisibility(View.VISIBLE);
         holder.emergency.setBackgroundResource(R.color.blue);
         holder.emergency.setText("No Emergency");
     }
     else
     {

     }
 }
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
                intent.putExtra("emailpatient",user.getEmail());
                context.startActivity(intent);
            }
        });

 holder.time.setText(user.getTime());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    // The user is signed in
                } else {
                    Toast.makeText(context, "User is not signed in", Toast.LENGTH_SHORT).show();
                    return; // Exit if the user is not signed in
                }

                String recieveText1 = currentUser.getEmail();
                String recieveText = recieveText1.replace(".", "_");

                // Create a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Fraud Warning")
                        .setMessage("If you attempt any fraud, strict actions may be taken against you. Please be sincere. Do you wish to proceed?")
                        .setCancelable(false) // Prevent closing by pressing back button
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Proceed with the donor addition logic

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
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle cancellation if needed
                                    }
                                });

                                databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // Get the current time
                                        String currentTime = getFormattedDate();  // Assuming you have this method for getting the formatted date

                                        if (snapshot.hasChild(recieveText)) {
                                            DataSnapshot userSnapshot = snapshot.child(recieveText);

                                            // Check for donor1, donor2, donor3
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
                                            if (name.equals(donor1Name) && phoneno.equals(donor1PhoneNo)) {
                                                Toast.makeText(context, "Donor 1 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (name.equals(donor2Name) && phoneno.equals(donor2PhoneNo)) {
                                                Toast.makeText(context, "Donor 2 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (name.equals(donor3Name) && phoneno.equals(donor3PhoneNo)) {
                                                Toast.makeText(context, "Donor 3 name and phone number already exist", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            // Proceed with adding the donor data
                                            if (hasDonor3) {
                                                Toast.makeText(context, "Donor 3 data already exists", Toast.LENGTH_SHORT).show();
                                            } else if (hasDonor1 && hasDonor2) {
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor3_name")
                                                        .setValue(name);
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor3_phone_no")
                                                        .setValue(phoneno);
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor3_Acceptence_Time")
                                                        .setValue(currentTime);

                                                Toast.makeText(context, "Donor 3 data added successfully", Toast.LENGTH_SHORT).show();
                                            } else if (hasDonor1) {
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor2_name")
                                                        .setValue(name);
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor2_phone_no")
                                                        .setValue(phoneno);
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor2_Acceptence_Time")
                                                        .setValue(currentTime);

                                                Toast.makeText(context, "Donor 2 data added successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor_phone_no")
                                                        .setValue(phoneno);
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor_name")
                                                        .setValue(name);
                                                databaseReference.child("PATIENT Detail")
                                                        .child(recieveText)
                                                        .child("donor_Acceptence_Time")
                                                        .setValue(currentTime);

                                                Toast.makeText(context, "Donor 1 data added successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            databaseReference.child("PATIENT Detail")
                                                    .child(recieveText)
                                                    .child("donor_phone_no")
                                                    .setValue(phoneno);
                                            databaseReference.child("PATIENT Detail")
                                                    .child(recieveText)
                                                    .child("donor_name")
                                                    .setValue(name);
                                            databaseReference.child("PATIENT Detail")
                                                    .child(recieveText)
                                                    .child("donor_Acceptence_Time")
                                                    .setValue(currentTime);

                                            Toast.makeText(context, "Data added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                                        Toast.makeText(context, "Failed to check data, try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Navigate to the next screen
                                Intent intent = new Intent(context, RecieverAddressREvised.class);
                                intent.putExtra("gmail1", currentUser.getEmail());
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing if "No" is clicked
                                dialog.dismiss();
                            }
                        });

                // Show the dialog
                builder.create().show();
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
