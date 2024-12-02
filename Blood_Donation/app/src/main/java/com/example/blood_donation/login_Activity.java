package com.example.blood_donation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");

        final EditText phone = findViewById(R.id.phone_l);
        final EditText password = findViewById(R.id.password_l);
        final Button loginBtn = findViewById(R.id.login);
        final TextView registerNow = findViewById(R.id.clickreg);
        final TextView forgotbutton = findViewById(R.id.forgotbtn);
        Button admin = findViewById(R.id.loginAdministrator);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Administratorlogin.class);
                startActivity(intent);
            }
        });
        forgotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(v -> {
            final String phoneTxt = phone.getText().toString().trim();
            final String passwordTxt = password.getText().toString().trim();

            if (TextUtils.isEmpty(phoneTxt) || TextUtils.isEmpty(passwordTxt)) {
                Toast.makeText(login_Activity.this, "Please enter phone number and password", Toast.LENGTH_SHORT).show();
            } else {
                // Firebase Authentication to sign in with email/password
                mAuth.signInWithEmailAndPassword(phoneTxt , passwordTxt)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Login successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(login_Activity.this, MainActivity4.class));
                                finish();
                            } else {
                                Toast.makeText(login_Activity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        registerNow.setOnClickListener(v -> {
            startActivity(new Intent(login_Activity.this, RegisterAcitivity.class));
            finish();
        });
    }
}
