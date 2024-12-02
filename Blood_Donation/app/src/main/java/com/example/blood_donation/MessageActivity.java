package com.example.blood_donation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//
//public class MessageActivity extends AppCompatActivity {
//
//    private Button btnSendRequest, btnRequestLocation;
//    private String recipientPhoneNumber = ""; // Replace with the recipient's WhatsApp number
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_message);
//
//        btnSendRequest = findViewById(R.id.btnSendRequest);
////        btnRequestLocation = findViewById(R.id.btnRequestLocation);
//
//        // Set up the button to send a blood request message on WhatsApp
//        btnSendRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendBloodRequest(recipientPhoneNumber);
//            }
//        });
//
//        // Set up the button to reply with a hospital location request message on WhatsApp
////        btnRequestLocation.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                requestHospitalLocation(recipientPhoneNumber);
////            }
////        });
//    }
//
//    // Method to send blood request on WhatsApp
//    private void sendBloodRequest(String phoneNumber) {
//        String message = "Hello, I am requesting a blood donation. Can you help?";
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message)));
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//}




public class MessageActivity extends AppCompatActivity {

    private Button btnSendRequest;
    private String recipientPhoneNumber = ""; // Recipient's phone number
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btnSendRequest = findViewById(R.id.btnSendRequest);
        final EditText report = findViewById(R.id.repot);
        Intent intent = new Intent();
        String email1 = getIntent().getStringExtra("email");
        if (email1 != null) {
            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
        } else {
            // Handle case where email is null or not passed
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String recieveText1= currentUser.getEmail();

        String recieveText =recieveText1.replace(".","_");

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String report1 = report.getText().toString().trim();
                databaseReference.child("PATIENT Detail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.child("user").child(email1).child("Report").setValue(report1);
                        databaseReference.child("user").child(email1).child("Report_given_by").setValue(recieveText);
                        databaseReference.child("PATIENT Detail").child(email1).child("Report").setValue(report1);
                        databaseReference.child("PATIENT Detail").child(email1).child("Report_given_by").setValue(recieveText);
                        Toast.makeText(MessageActivity.this, "Reported successfully", Toast.LENGTH_SHORT).show();



                              Intent intent1 = new Intent(getApplicationContext(),MainActivity4.class);
                              startActivity(intent);

                               



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        // Retrieve the phone number from the intent


    }

    // Method to send a blood request on WhatsApp

}


