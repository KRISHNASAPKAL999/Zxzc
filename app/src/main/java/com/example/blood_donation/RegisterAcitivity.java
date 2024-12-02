package com.example.blood_donation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterAcitivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    String gender;
    String bloodGroup;
    String dob1;
    RadioButton type;
    RadioButton type1;
    RadioButton type2;
    String blood_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_acitivity);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");

        final EditText fullname = findViewById(R.id.full_name2);
        final EditText phone = findViewById(R.id.phone_r);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password_r);
        final EditText conpassword = findViewById(R.id.Conpassword_r);
        final Button registerBtn = findViewById(R.id.Register);
        final TextView clickLog = findViewById(R.id.clickLog);
        final EditText area = findViewById(R.id.areareg);
        final RadioButton male = findViewById(R.id.MALE_r);
        final RadioButton female = findViewById(R.id.female_r);
        final TextView DOB = findViewById(R.id.dob);
        RadioGroup bloodgroup1 = findViewById(R.id.radioGroup1);
        RadioGroup bloodgroup2 = findViewById(R.id.radioGroup2);
        RadioGroup bloodgroup3 = findViewById(R.id.radioGroup3);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setChecked(false);
                gender = male.getText().toString();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setChecked(false);
                gender = female.getText().toString();
            }
        });
        RadioGroup.OnCheckedChangeListener listener= new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId != -1)
                {
                    if(group != bloodgroup1)
                        bloodgroup1.clearCheck();
                    if(group != bloodgroup2 )
                        bloodgroup2.clearCheck();
                    if(group != bloodgroup3)
                        bloodgroup3.clearCheck();

                }
            }
        };
        bloodgroup1.setOnCheckedChangeListener(listener);
        bloodgroup2.setOnCheckedChangeListener(listener);
        bloodgroup3.setOnCheckedChangeListener(listener);

        final String[] selectedOption = {""};


//dob doing
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Dob")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Calendar Selecteddate = Calendar.getInstance();
                        Selecteddate.setTimeInMillis(selection);
                        int SelectedYear = Selecteddate.get(Calendar.YEAR);
                        if(SelectedYear >2009)
                        {
                            Toast.makeText(RegisterAcitivity.this, "YOU ARE TOO SMALL \n TO REGISTERED", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String dob = new SimpleDateFormat("MM-dd-yyy", Locale.getDefault()).format(new Date(selection));

                            dob1 =dob;
                            DOB.setText(dob);
                        }
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(),"tag");
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname1 = fullname.getText().toString().trim();
                final String phone1 = phone.getText().toString().trim();
                final String email1 = email.getText().toString().trim();
                final String password1 = password.getText().toString().trim();
                final String conpassword1 = conpassword.getText().toString().trim();
                final String area1 = area.getText().toString().trim();
                // Listener for RadioGroup1

                int Selectid = bloodgroup1.getCheckedRadioButtonId();
                int selectid1 =bloodgroup2.getCheckedRadioButtonId();
                int selectid3 = bloodgroup3.getCheckedRadioButtonId();
                type = findViewById(Selectid);
                type1 = findViewById(selectid1);
                type2 = findViewById(selectid3);
                if(type == findViewById(Selectid) && type1 == null && type2 == null) {
                    blood_group = type.getText().toString();

                }

                if(type1 == findViewById(selectid1) && type == null && type2 == null) {
                    blood_group = type1.getText().toString();

                }

                if(type2 == findViewById(selectid3) && type == null && type1 == null) {
                    blood_group = type2.getText().toString();

                }



                if (TextUtils.isEmpty(fullname1) || TextUtils.isEmpty(phone1) || TextUtils.isEmpty(email1) ||
                        TextUtils.isEmpty(password1) || TextUtils.isEmpty(conpassword1)) {
                    Toast.makeText(RegisterAcitivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(conpassword1)) {
                    Toast.makeText(RegisterAcitivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Firebase authentication with email/password
                    mAuth.createUserWithEmailAndPassword(email1, password1)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // After successful registration
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String modifiedEmail = email1.replace(".", "_");
                                    String currentdate = getFormattedDate();


                                    // Store additional user info in the Realtime Database
                                    databaseReference.child("user").child(modifiedEmail).child("fullname").setValue(fullname1);
                                    databaseReference.child("user").child(modifiedEmail).child("email").setValue(modifiedEmail);
                                    databaseReference.child("user").child(modifiedEmail).child("phoneNo").setValue(phone1);
                                    databaseReference.child("user").child(modifiedEmail).child("Gender").setValue(gender);
                                    databaseReference.child("user").child(modifiedEmail).child("Area").setValue(area1);
                                    databaseReference.child("user").child(modifiedEmail).child("bloodgroup").setValue(blood_group);
                                    databaseReference.child("user").child(modifiedEmail).child("DOB").setValue(dob1);
                                    databaseReference.child("user").child(modifiedEmail).child("password").setValue(password1);
                                    databaseReference.child("user").child(modifiedEmail).child("time").setValue(currentdate);


                                    Toast.makeText(RegisterAcitivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterAcitivity.this, login_Activity.class));
                                    finish();
                                } else {
                                    Toast.makeText(RegisterAcitivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        clickLog.setOnClickListener(v -> {
            startActivity(new Intent(RegisterAcitivity.this, login_Activity.class));
            finish();
        });
    }
    private String getFormattedDate() {
        Date currentDate = new Date();

        // Define the date format pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE,dd,yyyy", Locale.getDefault());

        // Format the current date and return it as a string
        return dateFormat.format(currentDate);
    }
}
