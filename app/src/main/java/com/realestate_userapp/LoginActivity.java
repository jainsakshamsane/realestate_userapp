package com.realestate_userapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    TextView login, signup;
    FirebaseDatabase database;
    DatabaseReference reference;
    String emails, passwords;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signuptext);
        ImageView togglePassword = findViewById(R.id.togglePassword);

        togglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                int inputType = (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) ?
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD :
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

                password.setInputType(inputType);
                // Move cursor to the end of the text
                password.setSelection(password.getText().length());

                // Change the visibility toggle icon
                togglePassword.setImageResource(
                        (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) ?
                                R.drawable.visible :
                                R.drawable.hide
                );
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emails = email.getText().toString().trim();
                passwords = password.getText().toString().trim();

                if (emails.isEmpty() || passwords.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(emails)) {
                    // Check for valid email format
                    Toast.makeText(LoginActivity.this, "Incorrect email format. Use @gmail.com at the end.", Toast.LENGTH_SHORT).show();
                } else {
                    Query checkUserDatabase;

                    if (emails.contains("@")) {
                        // The entered value looks like an email
                        checkUserDatabase = reference.orderByChild("email").equalTo(emails);

                        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                                        if (passwordFromDB != null && passwordFromDB.equals(passwords)) {
                                            // Successfully logged in
                                            email.setError(null);

                                            String usernameFromDB = userSnapshot.child("username").getValue(String.class);
                                            String mobileFromDB = userSnapshot.child("phone").getValue(String.class);
                                            String idFromDB = userSnapshot.child("userid").getValue(String.class);
                                            String emailFromDB = userSnapshot.child("email").getValue(String.class);
                                            String cityFromDB = userSnapshot.child("city").getValue(String.class);
                                            String cityidFromDB = userSnapshot.child("city_id").getValue(String.class);
                                            // Store user information in SharedPreferences
                                            storeUserInformation(usernameFromDB, mobileFromDB, idFromDB, emailFromDB, cityidFromDB, cityFromDB);

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        } else {
                                            password.setError("Invalid Credentials");
                                            password.requestFocus();
                                        }
                                    }
                                } else {
                                    email.setError("User does not exist");
                                    email.requestFocus();
                                }
                            }

                            private void storeUserInformation(String username, String mobile, String userId, String email, String cityid, String city) {
                                SharedPreferences sharedPreferencess = getSharedPreferences("user_information", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferencess.edit();
                                editor.putString("username", username);
                                editor.putString("mobile", mobile);
                                editor.putString("userId", userId);
                                editor.putString("city", city);
                                editor.putString("email", email);
                                editor.putString("cityid", cityid);
                                editor.apply();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    }
                }
            }
        });
    }

    // Add a method for email validation
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com");
    }
}
