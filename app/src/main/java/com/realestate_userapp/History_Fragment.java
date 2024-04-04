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
import com.realestate_userapp.Adapters.HistoryAdapter;
import com.realestate_userapp.Adapters.PropertyAdapter;
import com.realestate_userapp.Models.CategoryModel;
import com.realestate_userapp.Models.HistoryModel;
import com.realestate_userapp.Models.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class History_Fragment extends Fragment {

    RecyclerView recyclerView;
    List<HistoryModel> historyModels = new ArrayList<>();
    HistoryAdapter adapter;
    String userid, mobile, username;
    TextView text1;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        text1 = view.findViewById(R.id.text1);
        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("payments");

        adapter = new HistoryAdapter(getContext(), historyModels);

        categoryRef.orderByChild("userid").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String brokername = ds.child("broker_name").getValue(String.class);
                    String name = ds.child("propertyName").getValue(String.class);
                    String amount = ds.child("total_amount").getValue(String.class);
                    String imageurl = ds.child("imageurl").getValue(String.class);
                    String propertyid = ds.child("propertyId").getValue(String.class);
                    String timestamp = ds.child("timestamp").getValue(String.class);

                    HistoryModel categoryModel = new HistoryModel(brokername, name, amount, imageurl, propertyid, timestamp);
                    historyModels.add(categoryModel);
                }

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                // Show the "text" TextView if there are no purchased courses
                if (historyModels.isEmpty()) {
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
