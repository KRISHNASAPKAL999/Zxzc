package com.example.blood_donation;

import static com.example.blood_donation.R.*;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class profile extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-donation-login-default-rtdb.firebaseio.com/");
    CardView calender;
    TextView Pphone,Pemail,Pfullname;
    Dialog dialog;
    Dialog dobandgenderupd;
    Dialog locationUpdate;
    Dialog FitnessInfo;
    EditText nameupd,emailupd,phonenoupd;
    private String date;
    String genderupd;
    String updatedLocation;
    String gmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String recieveText1,recieveText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


       Pphone = findViewById(id.profile_number);
       Pemail = findViewById(id.profile_email);

       dialog = new Dialog(profile.this);
       nameupd = dialog.findViewById(id.nameupd);
       emailupd = dialog.findViewById(id.gmailupd);
       phonenoupd = dialog.findViewById(id.phoneupd);

       final Button bloodgroup = findViewById(id.bloodbutt);
       final TextView gender = findViewById(id.genderprof);
       final TextView lastdon= findViewById(id.lastdate);
       final TextView area = findViewById(id.Area);
       final ImageView editname = findViewById(id.editval);
final ImageView editlastdondate = findViewById(id.lastdondate);
final ImageView editdobandgender = findViewById(id.dobandgenderpen);
final TextView dobp =findViewById(id.upddob);
final ImageView fitnessInfopen = findViewById(id.fitnessinfopen);
final ImageView locationUpdatepen = findViewById(id.LocationUpdate);
final RadioGroup tattoy_n = findViewById(id.tatto_y_n);
final RadioGroup havehiv1 = findViewById(id.HIV_y_n);
final RadioButton yestatto = findViewById(id.yes_tatto);
final RadioButton notatto = findViewById(id.no_tatto);
final RadioButton nohiv = findViewById(id.no_hiv);
final RadioButton yeshiv = findViewById(id.yes_hiv);
gender.setText("noll world");

        Calendar calender1 = Calendar.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // The user is signed in
           recieveText1 = currentUser.getEmail();
             recieveText =recieveText1.replace(".","_");
        } else {
            Toast.makeText(this, "user is not login", Toast.LENGTH_SHORT).show();

        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if(itemid == R.id.home)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                    startActivity(intent);
                }
                if(itemid == R.id.donorlocation)
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                }
                if(itemid == R.id.donateblood)
                {
                    Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
                    startActivity(intent);
                }

                return false;

            }
        });
        Toast.makeText(this, recieveText, Toast.LENGTH_SHORT).show();

        // Dialgo Creation for name and email updation

        dialog.setContentView(layout.content);

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setCancelable(true);
        final Button updatebtn1 = dialog.findViewById(R.id.updatebtn);
 final EditText newusername = dialog.findViewById(id.nameupd);
 final EditText newgmail = dialog.findViewById(id.gmailupd);
 final EditText newphone = dialog.findViewById(id.phoneupd);

        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        updatebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newusername.getText().toString();
                String newGmail = newgmail.getText().toString();
                String newPhone = newphone.getText().toString();


               HashMap user = new HashMap();
               user.put("email",newGmail);
               user.put("fullname",newUsername);
               user.put("pnoneNo",newPhone);
               databaseReference.child("user").child(recieveText).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                   @Override
                   public void onComplete(@NonNull Task task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(profile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                           Pfullname.setText(newUsername);
                           Pemail.setText(newGmail);
                           Pphone.setText(newPhone);
                       }else
                       {
                           Toast.makeText(profile.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                       }
                   }
               });


dialog.dismiss();
            }
        });




//edit lastdonationDate
        editlastdondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("MM-dd-yyy", Locale.getDefault()).format(new Date(selection));
                        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("user").child(recieveText).child("lifesave").setValue(0);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(profile.this, "failed to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
                        lastdon.setText(date);
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(),"tag");
            }
        });


        //Dialog For gender and dob Updation
        dobandgenderupd = new Dialog(profile.this);

        dobandgenderupd.setContentView(layout.genanddob);

        Objects.requireNonNull(dobandgenderupd.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dobandgenderupd.setCancelable(true);

        final CardView newGender = dobandgenderupd.findViewById(id.updgender);
        final RadioGroup genderg = dobandgenderupd.findViewById(id.updategender);
        final CardView newDobsel = dobandgenderupd.findViewById(id.updDob);
        final TextView dob_d = dobandgenderupd.findViewById(id.doblast);
        final Button updatedob = dobandgenderupd.findViewById(id.dobandgenderupd);

        editdobandgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobandgenderupd.show();
            }
        });



        newGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderg.setVisibility(View.VISIBLE);
            }
        });


//dob dialog
        newDobsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                         date = new SimpleDateFormat("MM-dd-yyy", Locale.getDefault()).format(new Date(selection));
                       dob_d.setText(date);
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(),"tag");
            }
        });

        //update database
        updatedob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int SelectedId = genderg.getCheckedRadioButtonId();
                 String dater = date;
               String gender1 = null;
                if(SelectedId == id.updMALE)
                {
                   gender1 = "MALE";
                }
                else if(SelectedId == id.updfemale)
                {
                   gender1 = "Female";
                }
                if(gender1 == null)
                {
                    Toast.makeText(profile.this, "please select gender first", Toast.LENGTH_SHORT).show();
                }

                HashMap user = new HashMap();
                user.put("D-O-B",dater);
                user.put("Gender",gender1);
genderupd = gender1;
                databaseReference.child("user").child(recieveText).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(profile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                         gender.setText(genderupd);
                         dobp.setText(dater);

                        }else
                        {
                            Toast.makeText(profile.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });





        //location Updation Dialog
locationUpdate = new Dialog(profile.this);
        locationUpdate.setContentView(layout.location);

        Objects.requireNonNull(locationUpdate.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        locationUpdate.setCancelable(true);
        final EditText lupdate = locationUpdate.findViewById(id.editTextLocation);
        final Button locationUpdateButton = locationUpdate.findViewById(id.validateButton);
        locationUpdatepen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
locationUpdate.show();
            }
        });
        locationUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationInput = lupdate.getText().toString().trim();
                if((isValidLocationFormat(locationInput)))
                {

                    updatedLocation = locationInput;
                    Toast.makeText(profile.this, updatedLocation, Toast.LENGTH_SHORT).show();

                    HashMap user = new HashMap();
                    user.put("Aera",updatedLocation);

                    databaseReference.child("user").child(recieveText).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(profile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                area.setText(updatedLocation);

                            }else
                            {
                                Toast.makeText(profile.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    locationUpdate.dismiss();
                }
                else
                {
                    Toast.makeText(profile.this, "the input is invalid please enter city/state/country format", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isValidLocationFormat(String locationInput) {
                String regex = "^[a-zA-Z]+/[a-zA-Z]+/[a-zA-Z]+$";
                return locationInput.matches(regex);
            }



        });

        // Dialog box for fitness information

FitnessInfo = new Dialog(profile.this);

        FitnessInfo.setContentView(layout.fitnessinformation);

        Objects.requireNonNull(FitnessInfo.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        FitnessInfo.setCancelable(true);
        final RadioGroup havetatoos = FitnessInfo.findViewById(id.tatto_y_n);
        final RadioGroup havehiv = FitnessInfo.findViewById(id.HIV_y_n);
        final Button updatebtn = FitnessInfo.findViewById(id.fitnessinfobutton);
        fitnessInfopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitnessInfo.show();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int Selectidtatto = havetatoos.getCheckedRadioButtonId();
              int Selectidhiv = havehiv.getCheckedRadioButtonId();
              String havetatto;
              String haveHIV;

              // this is for tatto
              if(Selectidtatto == id.yestatto)
              {
                  havetatto = "YES";
                  yestatto.setChecked(true);
              }
              else if(Selectidtatto == id.notatto)
              {
                  havetatto = "NO";
                  notatto.setChecked(true);
              }
              else
              {
                  havetatto =null;
              }



              //This is for HIV
                if(Selectidhiv == id.yeshiv)
                {
                    haveHIV = "YES";
                    yeshiv.setChecked(true);
                }
                else if(Selectidhiv == id.nohiv)
                {
                    haveHIV ="NO";
                    nohiv.setChecked(true);
                }
                else
                {
                    haveHIV = null;
                }
                if(haveHIV == null && havetatto == null)
                {
                    Toast.makeText(profile.this, "please Select First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(profile.this, havetatto+" "+ haveHIV, Toast.LENGTH_SHORT).show();
                }

                databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("user").child(recieveText).child("HAVE HIV").setValue(haveHIV);
                        databaseReference.child("user").child(recieveText).child("HAVE TATOO").setValue(havetatto);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(profile.this, "failed to add data", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (recieveText == null) {
                    Toast.makeText(profile.this, "null value", Toast.LENGTH_SHORT).show();
                } else {
                    final String get_email = snapshot.child(recieveText).child("email").getValue(String.class);
                    gmail = get_email;
                    final String get_name = snapshot.child(recieveText).child("fullname").getValue(String.class);
                    final String get_gender = snapshot.child(recieveText).child("Gender").getValue(String.class);
                    final String get_blood = snapshot.child(recieveText).child("bloodgroup").getValue(String.class);
                    final String get_area = snapshot.child(recieveText).child("Area").getValue(String.class);
                    final String get_dob = snapshot.child(recieveText).child("D-O-B").getValue(String.class);
                    final String last_donation_date = snapshot.child(recieveText).child("LastDonationDate").getValue(String.class);
                    final String get_tatto_info = snapshot.child(recieveText).child("HAVE TATTO").getValue(String.class);
                    final String get_hiv_info = snapshot.child(recieveText).child("HAVE HIV").getValue(String.class);
                    final String get_phoneno = snapshot.child(recieveText).child("phoneNo").getValue(String.class);

                    Toast.makeText(profile.this, get_dob+get_area+get_blood, Toast.LENGTH_SHORT).show();

                    TextView gender = findViewById(R.id.genderprof);
                    TextView name = findViewById(R.id.profile_name);
                    RadioButton yt = findViewById(R.id.yes_tatto);
                    RadioButton nt = findViewById(R.id.no_tatto);
                    RadioButton yh = findViewById(R.id.yes_hiv);
                    RadioButton nh = findViewById(R.id.no_hiv);

                    // HIV status check
                    if ("NO".equals(get_hiv_info)) {
                        nh.setChecked(true);
                    } else if ("YES".equals(get_hiv_info)) {
                        yh.setChecked(true);
                    } else {
                        yh.setChecked(true);
                        nh.setChecked(true);
                    }

                    // Tattoo status check
                    if ("NO".equals(get_tatto_info)) {
                        nt.setChecked(true);
                    } else if ("YES".equals(get_tatto_info)) {
                        yt.setChecked(true);
                    } else {
                        yt.setChecked(true);
                        nt.setChecked(true);
                    }

                    // Set the values to the UI
                    Pphone.setText(get_phoneno);
                    gender.setText(get_gender);
                    bloodgroup.setText(get_blood);
                    area.setText(get_area);
                    Pemail.setText(get_email);
                    name.setText(get_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
                Toast.makeText(profile.this, "Data load failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });










    }




}