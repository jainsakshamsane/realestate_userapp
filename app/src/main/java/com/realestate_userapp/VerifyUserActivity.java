package com.realestate_userapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class VerifyUserActivity extends AppCompatActivity {

    TextView submit, resend;
    EditText otp;
    ImageView back;
    String userid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifyuser_activity);

        submit = findViewById(R.id.submit);
        resend = findViewById(R.id.resend);
        otp = findViewById(R.id.otp);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferencess = getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered OTP
                final String enteredOTP = otp.getText().toString().trim();

                if (enteredOTP.isEmpty()) {
                    Toast.makeText(VerifyUserActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // Get a reference to the "users" node in the Firebase Realtime Database
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userid);

                    // Listen for changes in the "otp" value
                    usersRef.child("otp").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Check if the OTP entered by the user matches the OTP stored in the database
                            if (dataSnapshot.exists()) {
                                String storedOTP = dataSnapshot.getValue(String.class);
                                if (storedOTP.equals(enteredOTP)) {
                                    usersRef.child("status").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // OTP matches, user verification successful
                                                Toast.makeText(VerifyUserActivity.this, "User Verification Successful", Toast.LENGTH_SHORT).show();
                                                // Navigate to the Main Activity
                                                Intent intent = new Intent(VerifyUserActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish(); // Finish the current activity to prevent going back to it using the back button
                                            }
                                        }
                                    });
                                } else {
                                    // Wrong OTP entered
                                    Toast.makeText(VerifyUserActivity.this, "Wrong OTP, click on resend to generate once again", Toast.LENGTH_SHORT).show();
                                }
                                if (storedOTP.equals("0")){
                                    // OTP not found (possibly not generated yet)
                                    Toast.makeText(VerifyUserActivity.this, "OTP not generated yet, click on generate OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database error
                            Toast.makeText(VerifyUserActivity.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Set onClickListener for verify TextView
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate 6-digit random number
                Random rand = new Random();
                int otpp = rand.nextInt(900000) + 100000; // Generate a random number between 100000 and 999999

                // Store the generated OTP in the Firebase Realtime Database
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userid);
                usersRef.child("otp").setValue(String.valueOf(otpp));

                // Show a toast message indicating that the OTP has been generated
                Toast.makeText(VerifyUserActivity.this, "OTP generated successfully: " + otpp, Toast.LENGTH_LONG).show();
            }
        });
    }
}
