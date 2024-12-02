package com.example.blood_donation;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class ManageDonorAdapter extends RecyclerView.Adapter<RecieverlistAdapter.MyViewHolder> {
    Context context;
    ArrayList<GetDonor> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String name,gmail,phoneno;
    public ManageDonorAdapter(Context context,ArrayList<GetDonor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecieverlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.donor_adapter_list,parent,false);
        return new RecieverlistAdapter.MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull RecieverlistAdapter.MyViewHolder holder, int position) {
        GetDonor user = list.get(position);
        holder.Name.setText(user.getFull_name());
        holder.bloodgroup.setText(user.getBlood_group());
        holder.units.setText(user.getPhone_no());
        holder.time.setText(user.getArea());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog to confirm the deletion action
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to delete this user?")
                        .setCancelable(false) // Prevent closing the dialog by touching outside
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If the user clicks "Yes", perform the delete operation
                                databaseReference.child("Donor Detail").child(user.getEmail()).removeValue()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // Successfully deleted user
                                                Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(context,ManageDonor.class);
                                                context.startActivity(intent);
                                                // After deleting, refresh the page (RecyclerView, ListView, etc.)

                                            } else {
                                                // Failed to delete user
                                                Toast.makeText(context, "Error deleting user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If the user clicks "No", dismiss the dialog and do nothing
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();  // Show the dialog
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name,units,bloodgroup,time,gender,phoneno,dob,password,report,reportby;
        Button accept;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.namelist);
            units = itemView.findViewById(R.id.unitsneededlist);
            bloodgroup = itemView.findViewById(R.id.bloodgrouplist);
            time = itemView.findViewById(R.id.TIME);
            accept = itemView.findViewById(R.id.imgsrc);
            gender = itemView.findViewById(R.id.gender);
            phoneno = itemView.findViewById(R.id.Phoneno);
            dob = itemView.findViewById(R.id.dob);
            password = itemView.findViewById(R.id.Password);
            report = itemView.findViewById(R.id.repot);
            reportby = itemView.findViewById(R.id.Reportby);
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
