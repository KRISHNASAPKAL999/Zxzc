package com.example.blood_donation;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class verification extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText codeInput;
    private MaterialButton verifyButton;
    private String emailLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        codeInput = findViewById(R.id.codeInput);

        verifyButton = findViewById(R.id.verifyButton);

        verifyButton.setOnClickListener(v -> verifyEmailLink());
    }

    private void verifyEmailLink() {
        emailLink = getIntent().getData().toString(); // The email link passed with the Intent

        if (mAuth.isSignInWithEmailLink(emailLink)) {
            String email = getIntent().getStringExtra("userEmail"); // Retrieve the email from the previous step

            mAuth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully signed in with email link");
                            Intent resetIntent = new Intent(verification.this, resetpassword.class);
                            startActivity(resetIntent);
                        } else {
                            Log.e(TAG, "Error signing in with email link", task.getException());
                            Toast.makeText(verification.this, "Error signing in", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
