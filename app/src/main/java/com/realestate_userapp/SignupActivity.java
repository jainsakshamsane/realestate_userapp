package com.realestate_userapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.realestate_userapp.Models.SignupModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    TextView signup, login;
    EditText username, password, email, phone, address, bio, firstname, lastname;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView uploadimage, imgView;
    Spinner spinnerType;
    FirebaseStorage storage;
    private Uri filePath;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 22;
    String usernames, passwords, emails, phones, imageurl, resto_type_Id, addresss, bios, firstnames, lastnames;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.logintext);
        address = findViewById(R.id.address);
        uploadimage = findViewById(R.id.uploadimage);
        spinnerType = findViewById(R.id.spinnerType);
        imgView = findViewById(R.id.imgView);
        bio = findViewById(R.id.bio);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);

        setUpSpinner();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernames = username.getText().toString();
                emails = email.getText().toString();
                passwords = password.getText().toString();
                phones = phone.getText().toString();
                addresss = address.getText().toString();
                bios = bio.getText().toString();
                firstnames = firstname.getText().toString();
                lastnames = lastname.getText().toString();

                if (usernames.isEmpty() || emails.isEmpty() || passwords.isEmpty() || phones.isEmpty() || addresss.isEmpty() || bios.isEmpty() || firstnames.isEmpty() || lastnames.isEmpty() ) {
                    Toast.makeText(SignupActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else if (!emails.toLowerCase().endsWith("@gmail.com")) {
                    Toast.makeText(SignupActivity.this, "Email must end with @gmail.com", Toast.LENGTH_SHORT).show();
                } else if (imageurl == null || imageurl.isEmpty()) {
                    // If the profile picture is not uploaded, show a message and return
                    Toast.makeText(SignupActivity.this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
                } else {
                        // Check if the email already exists in the database
                        checkEmailExistence(emails);
                    }
                }
        });
    }

    private void checkEmailExistence(final String email) {
        DatabaseReference ownersRef = FirebaseDatabase.getInstance().getReference("users");
        ownersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already exists in the database, show error message
                    Toast.makeText(SignupActivity.this, "Email already exists in the database. Please choose another email.", Toast.LENGTH_SHORT).show();
                } else {
                    checkPhoneExistence(phones);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void checkPhoneExistence(final String phone) {
        DatabaseReference ownersRef = FirebaseDatabase.getInstance().getReference("users");
        ownersRef.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already exists in the database, show error message
                    Toast.makeText(SignupActivity.this, "Phone already exists in the database. Please choose another.", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void registerUser() {
        // Retrieve the selected Spinner item and CheckBox states
        String selectedType = spinnerType.getSelectedItem().toString();

        DatabaseReference idRef = reference.push();
        String id = idRef.getKey();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        reference.child(id).child("userid").setValue(id);
        reference.child(id).child("username").setValue(usernames);
        reference.child(id).child("email").setValue(emails);
        reference.child(id).child("password").setValue(passwords);
        reference.child(id).child("phone").setValue(phones);
        reference.child(id).child("address").setValue(addresss);
        reference.child(id).child("bio").setValue(bios);
        reference.child(id).child("firstname").setValue(firstnames);
        reference.child(id).child("lastname").setValue(lastnames);
        reference.child(id).child("status").setValue("0");
        reference.child(id).child("otp").setValue("0");
        reference.child(id).child("imageurl").setValue(imageurl);
        reference.child(id).child("city_id").setValue(resto_type_Id);
        reference.child(id).child("city").setValue(selectedType);
        reference.child(id).child("timestamp").setValue(timestamp);

        Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);

        username.setText("");
        email.setText("");
        password.setText("");
        phone.setText("");
        bio.setText("");
        address.setText("");
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
                ArrayAdapter<City> adapter = new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_spinner_item, cities);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(adapter);

                // Set a listener to handle item selection
                spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Retrieve the selected City object and store its ID
                        City selectedCity = (City) parent.getItemAtPosition(position);
                        resto_type_Id = selectedCity.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

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

                                    Toast.makeText(SignupActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
