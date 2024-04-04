package com.realestate_userapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Adapters.MessageAdapter;
import com.realestate_userapp.Models.MessageModel;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BrokerChatPageActivity extends AppCompatActivity {

    ImageView profileimage, back, sendbutton;
    TextView name;
    EditText message;
    String userid, namee, imageurl, brokerid, mobile, username, propertyid, propname;

    RecyclerView recyclerView;
    MessageAdapter senderAdapter;

    FirebaseDatabase database;
    DatabaseReference reference;

    List<MessageModel> senderMessages = new ArrayList<>();
    //  List<MessageModel> receiverMessages = new ArrayList<>();

    DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brokerchatpage_activity);

        profileimage = findViewById(R.id.profileimage);
        back = findViewById(R.id.back);
        message = findViewById(R.id.message);
        name = findViewById(R.id.name);
        sendbutton = findViewById(R.id.sendbutton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView); // Add your RecyclerView ID
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferencess = getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");

        senderAdapter = new MessageAdapter(senderMessages,userid, this); // Create your custom sender adapter
        recyclerView.setAdapter(senderAdapter); // Set sender adapter by default

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            namee = bundle.getString("brokername", "");
            imageurl = bundle.getString("imageurl", "");
            brokerid = bundle.getString("brokerid", "");
            propertyid = bundle.getString("propertyid", "");
            propname = bundle.getString("propname", "");

            name.setText(propname + " - " +  namee);
            Picasso.get().load(imageurl).into(profileimage);
        }

        // Initialize your Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(message.getText().toString());
            }
        });
        //   senderMessages.clear();
        showsendermessage();
        senderAdapter.notifyDataSetChanged();


        Log.e("oncerate","test");




    }


    //private boolean isFirstMessageSent = false; // Flag to track if the first message has been sent


    private void sendMessage(String messageText) {
        Log.e("sendmessage","my test");

        String senderUserId = userid;
        String receiverUserId = propertyid + " - " + brokerid;

        // Create a unique key for the message
        String messageId = databaseReference.child("message").child(senderUserId + "_" + receiverUserId).push().getKey();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Construct the message data
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("messageText", messageText);
        messageData.put("timestamp", timestamp);
        messageData.put("senderUserId", senderUserId);
        messageData.put("messageid", messageId);
        messageData.put("receiverUserId", receiverUserId);

        // Push the message data to the appropriate location
        databaseReference.child("message").child(senderUserId + "_" + receiverUserId).child(messageId).setValue(messageData);

        showsendermessage();
        scrollToLastItem();
        // Clear the EditText after sending the message
        message.setText("");

        TocheckEnquiry();
    }

    private void sendFirstMessage() {
        // Check if this is the first message
//        if (!isFirstMessageSent) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("enquired");
            DatabaseReference idRef = reference.push();
            String id = idRef.getKey();
            String timestamps = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            reference.child(id).child("enquire_id").setValue(id);
            reference.child(id).child("username").setValue(username);
            reference.child(id).child("propertyId").setValue(propertyid);
            reference.child(id).child("userid").setValue(userid);
            reference.child(id).child("enquire").setValue("Yes");
            reference.child(id).child("propertyName").setValue(propname);
            reference.child(id).child("imageurl").setValue(imageurl);
            reference.child(id).child("timestamp").setValue(timestamps);

            //isFirstMessageSent = true; // Set the flag to indicate that the first message has been sent
        //}

        // Call sendMessage to send the message
       // sendMessage(messageText);
    }

    private void showsendermessage() {
        Log.e("showsendermessage","my test");
        //senderMessages.clear();
        String path1 = userid + "_" + propertyid + " - " + brokerid;
        String path2 = brokerid + "_" + propertyid + "_" + userid;

        databaseReference.child("message").child(path1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                senderMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel message = snapshot.getValue(MessageModel.class);

                    if (message.getSenderUserId().equals(userid)) {
                        senderMessages.add(message);
                    }
                }
                //updateMessages();
                //senderAdapter.notifyDataSetChanged();
                //senderAdapter.setMessages(senderMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        databaseReference.child("message").child(path2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //senderMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel message = snapshot.getValue(MessageModel.class);

                    if (message.getSenderUserId().equals(brokerid)) {
                        // receiverMessages.add(message);
                        senderMessages.add(message);
                    }
                }
                //receiverAdapter.setMessages(receiverMessages);
                updateMessages();
                scrollToLastItem();
                //senderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
        //updateMessages();
    }



    private void updateMessages() {
        Log.e("update","my test");
        // Sort the messages based on timestamp
        Collections.sort(senderMessages, new Comparator<MessageModel>() {
            @Override
            public int compare(MessageModel message1, MessageModel message2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    Date date1 = format.parse(message1.getTimestamp());
                    Date date2 = format.parse(message2.getTimestamp());
                    if (date1 != null && date2 != null) {
                        return date1.compareTo(date2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        // Update the RecyclerView with the sorted list

        //    senderAdapter.notifyDataSetChanged();
    }

    private void scrollToLastItem() {
        // Scroll to the last item in the RecyclerView
        recyclerView.scrollToPosition(senderMessages.size() - 1);
    }



    private void TocheckEnquiry(){
        //check if already enquired or not

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("enquired");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot alldata : snapshot.getChildren()) {
                        Log.e("for", "for");
                        if ((alldata.child("propertyId").getValue(String.class)).equals(propertyid) && (alldata.child("userid").getValue(String.class)).equals(userid)) {
                            String isFirstMessageSent = alldata.child("enquire").getValue(String.class);
                            // Check if this is the first message
                            if (isFirstMessageSent.equals("Yes")) {
                                return;
                            } else {
                                sendFirstMessage();
                                return; // Return after sending the first message to prevent duplicate message sending
                            }
                        } else {
                            Log.e("false", "false");
                            sendFirstMessage();
                            return;
                        }
                    }
                }else{
                    sendFirstMessage();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
