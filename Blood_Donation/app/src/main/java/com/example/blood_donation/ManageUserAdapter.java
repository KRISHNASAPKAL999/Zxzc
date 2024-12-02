package com.example.blood_donation;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
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

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

public class ManageUserAdapter extends RecyclerView.Adapter<RecieverlistAdapter.MyViewHolder> {
    Context context;
    ArrayList<Getuser> list;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String name, gmail, phoneno;

    public ManageUserAdapter(Context context, ArrayList<Getuser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecieverlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.managepatientlist, parent, false);
        return new RecieverlistAdapter.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecieverlistAdapter.MyViewHolder holder, int position) {
        Getuser user = list.get(position);
        holder.Name.setText(user.getFullname());
        holder.bloodgroup.setText(user.getBloodgroup());
        holder.units.setText(user.getPhoneNo());
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
                                databaseReference.child("user").child(user.getEmail()).removeValue()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // Successfully deleted user
                                                Toast.makeText(context, "User data delete d  successfully", Toast.LENGTH_SHORT).show();
deleteFirebaseUser(user.getEmail(), user.getPassword());


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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, units, bloodgroup, time;
        Button accept;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.namelist);
            units = itemView.findViewById(R.id.unitsneededlist);
            bloodgroup = itemView.findViewById(R.id.bloodgrouplist);
            time = itemView.findViewById(R.id.TIME);
            accept = itemView.findViewById(R.id.imgsrc);

        }
    }

    private String getFormattedDate() {
        Date currentDate = new Date();

        // Define the date format pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,dd,yyyy", Locale.getDefault());

        // Format the current date and return it as a string
        return dateFormat.format(currentDate);
    }

    private void deleteFirebaseUser(String phoneTxt1, String passwordTxt) {
        // Get the current Firebase user

        String phoneTxt = phoneTxt1.replace("_",".");

        mAuth.signInWithEmailAndPassword(phoneTxt, passwordTxt)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // If the user is authenticated, delete their account
                            user.delete()
                                    .addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            // Account deleted successfully
                                            Toast.makeText(context, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
Intent intent = new Intent(context,ManageUser.class);
context.startActivity(intent);
                                            // Start MainActivity4 and finish the current activity
                                           // Finish the current activity
                                        } else {
                                            // If deleting the account fails, show an error message
                                            Toast.makeText(context, "Account deletion failed: " + deleteTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Authentication failed
                        Toast.makeText(context, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
