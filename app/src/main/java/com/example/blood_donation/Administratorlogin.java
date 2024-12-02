package com.example.blood_donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Administratorlogin extends AppCompatActivity {
    EditText username,password;
    Button login,userlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administratorlogin);
        username = findViewById(R.id.phone_l);
        password = findViewById(R.id.password_l);
        login = findViewById(R.id.login);
        userlogin = findViewById(R.id.loginuser);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneTxt = username.getText().toString().trim();
                final String passwordTxt = password.getText().toString().trim();
                if(phoneTxt.equals("faizal") && passwordTxt.equals("123"))
                {
                   Intent intent = new Intent(Administratorlogin.this, AdministerHomeAcitvity.class);
                   startActivity(intent);
                }
                else
                {
                    Toast.makeText(Administratorlogin.this, "wrong password or name", Toast.LENGTH_SHORT).show();
                }
            }

        });
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(Administratorlogin.this,login_Activity.class);
                startActivity(intent);
            }
        });



    }
}