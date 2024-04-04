package com.realestate_userapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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
import com.realestate_userapp.Adapters.ChatsDisplayAdapter;
import com.realestate_userapp.Models.PropertyModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chat_Fragment extends Fragment {

    RecyclerView recyclerView;
    List<PropertyModel> historyModels = new ArrayList<>();
    ChatsDisplayAdapter adapter;
    String userid, mobile, username;
    TextView text1;
    Set<String> processedProperties = new HashSet<>(); // Keep track of processed properties


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        text1 = view.findViewById(R.id.text1);
        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");

        adapter = new ChatsDisplayAdapter(getContext(), historyModels);

        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("message");

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Inside onDataChange method of messageRef
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    // Inside the message node
                    for (DataSnapshot childSnapshot : messageSnapshot.getChildren()) {
                        // Accessing individual message nodes

                        // Fetching the message id
                        String messageId = childSnapshot.getKey();

                        // Fetching the messageText directly from the childSnapshot
                        String messageText = childSnapshot.child("messageText").getValue(String.class);

                        // Fetching senderUserId directly from the childSnapshot
                        String senderUserId = childSnapshot.child("senderUserId").getValue(String.class);

                        // Fetching timestamp directly from the childSnapshot
                        String timestamp = childSnapshot.child("timestamp").getValue(String.class);

                        // Fetching receiverUserId directly from the childSnapshot
                        String receiverUserId = childSnapshot.child("receiverUserId").getValue(String.class);

                        // Splitting receiverUserId to get propertyId
                        String[] ids = receiverUserId.split(" - ");
                        String propertyId = ids[0].trim(); // Extract property ID

                        if (senderUserId.equals(userid) && !processedProperties.contains(propertyId)) {
                            // If the sender is the current user and the property hasn't been processed before

                            // Retrieve property information from the "Property" node
                            DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference("Property").child(propertyId);
                            propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Retrieve property details
                                        String propName = dataSnapshot.child("propertyName").getValue(String.class);
                                        String propId = dataSnapshot.child("propertyId").getValue(String.class);
                                        String brokerId = dataSnapshot.child("brokerid").getValue(String.class);

                                        // Create a PropertyModel object
                                        PropertyModel propertyModel = new PropertyModel(propName, propId, brokerId);

                                        // Add propertyModel to your historyModels list
                                        historyModels.add(propertyModel);

                                        // Notify adapter about data changes
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter); // Set adapter to RecyclerView here
                                    }

                                    // Show or hide text1 based on whether there are messages or not
                                    if (historyModels.isEmpty()) {
                                        text1.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle errors
                                }
                            });

                            // Add the processed property to the set
                            processedProperties.add(propertyId);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        return view;
    }
}
