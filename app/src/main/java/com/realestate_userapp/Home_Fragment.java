package com.realestate_userapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Adapters.CategoryAdapter;
import com.realestate_userapp.Adapters.PropertyAdapter;
import com.realestate_userapp.Models.CategoryModel;
import com.realestate_userapp.Models.PropertyModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home_Fragment extends Fragment {

    TextView location, text2, text1;
    ImageView menu;
    String userid, mobile, username, city;
    ProgressDialog progressDialog;
    RecyclerView recyclerView, recyclerView1;
    List<PropertyModel> propertyModels = new ArrayList<>();
    List<CategoryModel> categoryModels = new ArrayList<>();
    PropertyAdapter adapter;
    CategoryAdapter categoryAdapter;
    EditText search;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        location = view.findViewById(R.id.location);
        menu = view.findViewById(R.id.menu);
        text2 = view.findViewById(R.id.text2);
        text1 = view.findViewById(R.id.text1);
        search = view.findViewById(R.id.search);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference saleTypesRef = FirebaseDatabase.getInstance().getReference("sale_types");
                saleTypesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> saleTypes = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String type = snapshot.child("type").getValue(String.class);
                            saleTypes.add(type);
                        }

                        // Convert list to array
                        CharSequence[] options = saleTypes.toArray(new CharSequence[saleTypes.size()]);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Show properties for Type - ")
                                .setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Get the selected type
                                        String selectedType = saleTypes.get(which);

                                        // Pass the selected type to the SaleTypeActivity
                                        Intent intent = new Intent(getContext(), SaleTypeActivity.class);
                                        intent.putExtra("selectedType", selectedType);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });
            }
        });

        // Inside onCreateView() method after initializing views

// Listen for text changes in the search EditText
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the propertyModels list based on the entered text
                String searchText = charSequence.toString().toLowerCase(Locale.getDefault());
                List<PropertyModel> filteredList = new ArrayList<>();
                for (PropertyModel property : propertyModels) {
                    if (property.getPropertyName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        filteredList.add(property);
                    }
                }
                // Update the adapter with the filtered list
                adapter.filterList(filteredList);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if the text in the search EditText is empty
                if (s.toString().isEmpty()) {
                    // If the text is empty, reload the initial list of properties
                    loadInitialProperties();
                }
            }
        });


        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView1 = view.findViewById(R.id.recyclerview1);
        LinearLayoutManager layoutManagerr = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManagerr);

        // Initialize and show the custom progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.show();

        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");

        DatabaseReference purchaseRef = FirebaseDatabase.getInstance().getReference("users");

        purchaseRef.orderByChild("userid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Retrieve data from the "purchase" node
                    String address = ds.child("address").getValue(String.class);
                    city = ds.child("city").getValue(String.class);
                    String cityid = ds.child("city_id").getValue(String.class);

                    location.setText(address + ", " + city);

                }
                // Dismiss the progress dialog when data is loaded
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Property_Types");

        categoryAdapter = new CategoryAdapter(getContext(), categoryModels);

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Retrieve data from the "purchase" node
                    String id = ds.child("id").getValue(String.class);
                    String imageurl = ds.child("imageUrl").getValue(String.class);
                    String type = ds.child("type").getValue(String.class);

                    CategoryModel categoryModel = new CategoryModel(id, imageurl, type);
                    categoryModels.add(categoryModel);
                }

                categoryAdapter.notifyDataSetChanged();
                recyclerView1.setAdapter(categoryAdapter);

                // Show the "text" TextView if there are no purchased courses
                if (categoryModels.isEmpty()) {
                    text1.setVisibility(View.VISIBLE);
                    recyclerView1.setVisibility(View.GONE);
                }

                // Dismiss the loading dialog once data is loaded
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                progressDialog.dismiss();
            }
        });

        DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference("Property");

        adapter = new PropertyAdapter(getContext(), propertyModels);

        propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Retrieve data from the "purchase" node
                    String propname = ds.child("propertyName").getValue(String.class);
                    String rent = ds.child("rent").getValue(String.class);
                    String saletype = ds.child("saleType").getValue(String.class);
                    String rooms = ds.child("rooms").getValue(String.class);
                    String roomtype = ds.child("roomType").getValue(String.class);
                    String proptype = ds.child("propertyType").getValue(String.class);
                    String parking = ds.child("parking").getValue(String.class);
                    String nearby = ds.child("nearbyPlace").getValue(String.class);
                    String floors = ds.child("floors").getValue(String.class);
                    String deposit = ds.child("deposit").getValue(String.class);
                    String country = ds.child("country").getValue(String.class);
                    String cityy = ds.child("city").getValue(String.class);
                    String brokerid = ds.child("brokerid").getValue(String.class);
                    String area = ds.child("area").getValue(String.class);
                    String propertyid = ds.child("propertyId").getValue(String.class);
                    String gym = ds.child("gym").getValue(String.class);
                    String percentageOfCommission = ds.child("percentageOfCommission").getValue(String.class);

                    if (cityy.equals(city)) {
                        PropertyModel purchaseModel = new PropertyModel(propname, rent, saletype, rooms, roomtype, proptype, parking, nearby, floors, deposit, country, cityy, brokerid, area, propertyid, gym, percentageOfCommission);
                        propertyModels.add(purchaseModel);
                    }
                }

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                // Show the "text" TextView if there are no purchased courses
                if (propertyModels.isEmpty()) {
                    text2.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                // Dismiss the loading dialog once data is loaded
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                progressDialog.dismiss();
            }
        });

        return view;
    }

    // Method to load the initial list of properties
    private void loadInitialProperties() {
        propertyModels.clear(); // Clear the current list of properties

        DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference("Property");

        propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Retrieve data from the "purchase" node
                    String propname = ds.child("propertyName").getValue(String.class);
                    String rent = ds.child("rent").getValue(String.class);
                    String saletype = ds.child("saleType").getValue(String.class);
                    String rooms = ds.child("rooms").getValue(String.class);
                    String roomtype = ds.child("roomType").getValue(String.class);
                    String proptype = ds.child("propertyType").getValue(String.class);
                    String parking = ds.child("parking").getValue(String.class);
                    String nearby = ds.child("nearbyPlace").getValue(String.class);
                    String floors = ds.child("floors").getValue(String.class);
                    String deposit = ds.child("deposit").getValue(String.class);
                    String country = ds.child("country").getValue(String.class);
                    String cityy = ds.child("city").getValue(String.class);
                    String brokerid = ds.child("brokerid").getValue(String.class);
                    String area = ds.child("area").getValue(String.class);
                    String propertyid = ds.child("propertyId").getValue(String.class);
                    String gym = ds.child("gym").getValue(String.class);
                    String percentageOfCommission = ds.child("percentageOfCommission").getValue(String.class);

                    if (cityy.equals(city)) {
                        PropertyModel purchaseModel = new PropertyModel(propname, rent, saletype, rooms, roomtype, proptype, parking, nearby, floors, deposit, country, cityy, brokerid, area, propertyid, gym, percentageOfCommission);
                        propertyModels.add(purchaseModel);
                    }
                }

                adapter.notifyDataSetChanged();

                // Show the "text" TextView if there are no purchased courses
                if (propertyModels.isEmpty()) {
                    text2.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    text2.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                // Dismiss the loading dialog once data is loaded
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                progressDialog.dismiss();
            }
        });
    }

    // Method to open ActivityOne
    private void openActivityOne() {
        // Create an Intent to start ActivityOne
        Intent intent = new Intent(getContext(), RentActivity.class);
        startActivity(intent);
    }

    // Method to open ActivityTwo
    private void openActivityTwo() {
        // Create an Intent to start ActivityTwo
        Intent intent = new Intent(getContext(), PurchasedActivity.class);
        startActivity(intent);
    }
}
