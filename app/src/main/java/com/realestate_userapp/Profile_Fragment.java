package com.realestate_userapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class Profile_Fragment extends Fragment {

    TextView logout, name, phone, email, address, bio, password, verify, fullname;
    String userid, mobile, username, emails;
    ImageView image, edit, verified;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        edit = view.findViewById(R.id.edit);
        image = view.findViewById(R.id.image);
        address = view.findViewById(R.id.address);
        bio = view.findViewById(R.id.bio);
        password = view.findViewById(R.id.password);
        verify = view.findViewById(R.id.verify);
        fullname = view.findViewById(R.id.fullname);
        verified = view.findViewById(R.id.verified);

        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");
        emails = sharedPreferencess.getString("email", "");

        DatabaseReference purchaseRef = FirebaseDatabase.getInstance().getReference("users");

        purchaseRef.orderByChild("userid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Retrieve data from the "purchase" node
                    String addresss = ds.child("address").getValue(String.class);
                    String city = ds.child("city").getValue(String.class);
                    String cityid = ds.child("city_id").getValue(String.class);
                    String username = ds.child("username").getValue(String.class);
                    String imageurl = ds.child("imageurl").getValue(String.class);
                    String phones = ds.child("phone").getValue(String.class);
                    String emailid = ds.child("email").getValue(String.class);
                    String passwords = ds.child("password").getValue(String.class);
                    String bios = ds.child("bio").getValue(String.class);
                    String firstname = ds.child("firstname").getValue(String.class);
                    String lastname = ds.child("lastname").getValue(String.class);

                    name.setText(username);
                    Picasso.get().load(imageurl).into(image);
                    bio.setText(bios);
                    address.setText(addresss + ", " + city);
                    password.setText(passwords);
                    phone.setText(phones);
                    email.setText(emailid);
                    fullname.setText(firstname + " " + lastname);

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create a Bundle to pass data to the next activity
                            Bundle bundle = new Bundle();
                            bundle.putString("address", addresss);
                            bundle.putString("city", city);
                            bundle.putString("city_id", cityid);
                            bundle.putString("username", username);
                            bundle.putString("imageurl", imageurl);
                            bundle.putString("phone", phones);
                            bundle.putString("email", emailid);
                            bundle.putString("password", passwords);
                            bundle.putString("bio", bios);
                            bundle.putString("firstname", firstname);
                            bundle.putString("lastname", lastname);

                            // Create an intent to start the next activity
                            Intent intent = new Intent(getContext(), EditProfileActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(v -> logoutUser());

        // Get a reference to the "users" node in the Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userid);

// Listen for changes in the "status" value
        usersRef.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the value of the "status" child
                    String status = dataSnapshot.getValue(String.class);
                    // Check if the status is equal to "1"
                    if (status.equals("1")) {
                        // Status is equal to 1, hide the verify view and show the verified view
                        verify.setVisibility(View.GONE);
                        verified.setVisibility(View.VISIBLE);
                    } else {
                        // Status is not equal to 1, show the verify view
                        verify.setVisibility(View.VISIBLE);
                        verified.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(getContext(), "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Define colors
        final int[] colors = {Color.BLACK, Color.RED};

        // Handler to change color every 500 milliseconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int index = 0;

            @Override
            public void run() {
                verify.setTextColor(colors[index]);
                index = (index + 1) % colors.length;
                handler.postDelayed(this, 500); // Change color every 500 milliseconds
            }
        }, 500); // Start changing color after 500 milliseconds

        // Set onClickListener for verify TextView
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerifyUserActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logout());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Clear all preferences, including the switch state
        editor.clear();
        editor.apply();

        // Navigate to the login page
        Intent intent = new Intent(getContext(), LoginActivity.class);
        Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
