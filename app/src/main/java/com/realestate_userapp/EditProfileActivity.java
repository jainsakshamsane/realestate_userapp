package com.realestate_userapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    EditText username, password, email, phone, address, bio, firstname, lastname;
    FirebaseDatabase database;
    TextView savechanges;
    DatabaseReference reference;
    ImageView uploadimage, imgView, back;
    Spinner spinnerType;
    FirebaseStorage storage;
    private Uri filePath;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 22;
    String imageurl, userid, mobile, usernames, resto_type_Id;
    // Declare adapter variable
    ArrayAdapter<City> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_activity);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        uploadimage = findViewById(R.id.uploadimage);
        imgView = findViewById(R.id.imgView);
        bio = findViewById(R.id.bio);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        back = findViewById(R.id.back);
        savechanges = findViewById(R.id.savechanges);
        spinnerType = findViewById(R.id.spinnerType);

        SharedPreferences sharedPreferencess = getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        usernames = sharedPreferencess.getString("username", "");

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes to the database
                saveChanges();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Retrieve the user ID from the intent
        Bundle bundle = getIntent().getExtras();

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Set initial values from the Bundle
        if (bundle != null) {
            String addresss = bundle.getString("address");
            String citys = bundle.getString("city");
            String cityIds = bundle.getString("city_id");
            String usernames = bundle.getString("username");
            String imageUrls = bundle.getString("imageurl");
            String phones = bundle.getString("phone");
            String emails = bundle.getString("email");
            String passwords = bundle.getString("password");
            String bios = bundle.getString("bio");
            String firstnames = bundle.getString("firstname");
            String lastnames = bundle.getString("lastname");

            username.setText(usernames);
            firstname.setText(firstnames);
            lastname.setText(lastnames);
            Picasso.get().load(imageUrls).into(uploadimage);
            password.setText(passwords);
            email.setText(emails);
            bio.setText(bios);
            phone.setText(phones);
            address.setText(addresss);

            // Set the selected city and its ID to the spinner
            if (cityIds != null && citys != null) {
                // Create a City object with the selected city and its ID
                City selectedCity = new City(citys, cityIds);
                // Set the selected city to the spinner
                spinnerType.setSelection(adapter.getPosition(selectedCity));

                resto_type_Id = bundle.getString("city_id");
                spinnerType.setAdapter(adapter); // Set adapter to spinner
                setUpSpinner(); // Call setUpSpinner() after setting adapter
            }
        }

        // Initialize Firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Add click listener for uploading image
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        // Add TextChangedListeners for EditText fields to update database in real-time
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("username").setValue(s.toString());
            }
        });

        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("firstname").setValue(s.toString());
            }
        });

        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("lastname").setValue(s.toString());
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("password").setValue(s.toString());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("email").setValue(s.toString());
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("phone").setValue(s.toString());
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("address").setValue(s.toString());
            }
        });

        bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                reference.child(userid).child("bio").setValue(s.toString());
            }
        });
    }

    private void saveChanges() {
        // Update the values in the database
        reference.child(userid).child("username").setValue(username.getText().toString());
        reference.child(userid).child("firstname").setValue(firstname.getText().toString());
        reference.child(userid).child("lastname").setValue(lastname.getText().toString());
        reference.child(userid).child("password").setValue(password.getText().toString());
        reference.child(userid).child("email").setValue(email.getText().toString());
        reference.child(userid).child("phone").setValue(phone.getText().toString());
        reference.child(userid).child("address").setValue(address.getText().toString());
        reference.child(userid).child("bio").setValue(bio.getText().toString());

        // Navigate to the MainActivity
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back to it when pressing back button from MainActivity
    }

    private void setUpSpinner() {
        DatabaseReference dishCategoryRef = FirebaseDatabase.getInstance().getReference("cities");

        // Add a listener to fetch data from the "cities" node
        dishCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<City> cities = new ArrayList<>();

                // Iterate through the dataSnapshot to retrieve city names and ids
                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                    String cityName = citySnapshot.child("name").getValue(String.class);
                    String cityId = citySnapshot.child("id").getValue(String.class);

                    // Create a City object and add it to the list
                    City city = new City(cityName, cityId);
                    cities.add(city);
                }

                // Create an ArrayAdapter using the list of City objects and set it to the spinner
                adapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_spinner_item, cities);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(adapter);

                // Set the selected city and its ID if available
                if (resto_type_Id != null) {
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).getId().equals(resto_type_Id)) {
                            spinnerType.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        // Add an OnItemSelectedListener to the spinner
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected City object and store its ID
                City selectedCity = (City) parent.getItemAtPosition(position);
                resto_type_Id = selectedCity.getId();

                // Update the selected city and its ID in the database
                updateCityAndId(selectedCity.getName(), selectedCity.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updateCityAndId(String cityName, String cityId) {
        reference.child(userid).child("city").setValue(cityName);
        reference.child(userid).child("city_id").setValue(cityId);
    }


    // Method to select an image from gallery
    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    // Method to handle result after selecting an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadimage.setImageBitmap(bitmap);
                imgView.setVisibility(View.GONE);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to upload the selected image to Firebase Storage
    private void uploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    progressDialog.dismiss();
                                    imageurl = downloadUri.toString();

                                    // Update the "imageurl" field in the database
                                    reference.child(userid).child("imageurl").setValue(imageurl);

                                    Toast.makeText(EditProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
