package com.realestate_userapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Adapters.DishImageAdapter;
import com.realestate_userapp.Adapters.HistoryAdapter;
import com.realestate_userapp.Adapters.PropertyCategoryAdapter;
import com.realestate_userapp.Adapters.PurchaseAdapter;
import com.realestate_userapp.Models.HistoryModel;
import com.realestate_userapp.Models.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class PurchasedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView text1, typee;
    String proptype, id;
    ImageView back;
    List<PropertyModel> propertyModels = new ArrayList<>();
    PurchaseAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchased_activity);

        back = findViewById(R.id.back);
        text1 = findViewById(R.id.text1);
        typee = findViewById(R.id.type);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PurchasedActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Property");

        adapter = new PurchaseAdapter(this, propertyModels);

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

                    if (saletype.equals("Purchase")) {
                        PropertyModel purchaseModel = new PropertyModel(propname, rent, saletype, rooms, roomtype, proptype, parking, nearby, floors, deposit, country, cityy, brokerid, area, propertyid, gym, percentageOfCommission);
                        propertyModels.add(purchaseModel);
                    }
                }

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                // Show the "text" TextView if there are no purchased courses
                if (propertyModels.isEmpty()) {
                    text1.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
