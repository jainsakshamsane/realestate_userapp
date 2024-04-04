package com.realestate_userapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Adapters.CategoryAdapter;
import com.realestate_userapp.Adapters.FavouritesAdapter;
import com.realestate_userapp.Adapters.HistoryAdapter;
import com.realestate_userapp.Adapters.PropertyAdapter;
import com.realestate_userapp.Models.CategoryModel;
import com.realestate_userapp.Models.HistoryModel;
import com.realestate_userapp.Models.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class Save_Fragment extends Fragment {

    RecyclerView recyclerView;
    List<PropertyModel> propertyModels = new ArrayList<>();
    FavouritesAdapter adapter;
    String userid, mobile, username;
    TextView text1;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.save_fragment, container, false);

        text1 = view.findViewById(R.id.text1);
        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Property");

        adapter = new FavouritesAdapter(getContext(), propertyModels);

        categoryRef.orderByChild("liked").equalTo(2).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    PropertyModel purchaseModel = new PropertyModel(propname, rent, saletype, rooms, roomtype, proptype, parking, nearby, floors, deposit, country, cityy, brokerid, area, propertyid, gym, percentageOfCommission);
                        propertyModels.add(purchaseModel);
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

        return view;
    }
}
