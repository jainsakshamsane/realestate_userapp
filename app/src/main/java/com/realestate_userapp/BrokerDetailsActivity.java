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
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Adapters.CategoryAdapter;
import com.realestate_userapp.Adapters.DishImageAdapter;
import com.realestate_userapp.Models.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrokerDetailsActivity extends AppCompatActivity {
    TextView namee, email, phone, booknow, chat;
    String name, rent, imageurl, brokerid, propertyid, names, area, city, imageurls, percentageOfCommission;
    ImageView back, profileimage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brokerdetails_activity);

        namee = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        back = findViewById(R.id.back);
        booknow = findViewById(R.id.booknow);
        chat = findViewById(R.id.chat);
        profileimage = findViewById(R.id.profileimage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("propname", "");
            rent = bundle.getString("rent", "");
            imageurl = bundle.getString("imageurl", "");
            brokerid = bundle.getString("brokerid", "");
            propertyid = bundle.getString("propertyid", "");
            area = bundle.getString("area", "");
            city = bundle.getString("city", "");
            percentageOfCommission = bundle.getString("percentageOfCommission", "");
        }

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Broker");

        categoryRef.orderByChild("borkerid").equalTo(brokerid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Retrieve data from the "purchase" node
                    names = ds.child("name").getValue(String.class);
                    imageurls = ds.child("imageurl").getValue(String.class);
                    String phones = ds.child("phone").getValue(String.class);
                    String emails = ds.child("email").getValue(String.class);

                    namee.setText(names);
                    email.setText(emails);
                    phone.setText(phones);
                    Picasso.get().load(imageurls).into(profileimage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("brokerid", brokerid);
                bundle.putString("imageurl", imageurl);
                bundle.putString("brokername", names);
                bundle.putString("propertyid", propertyid);
                bundle.putString("propname", name);
                Intent intent = new Intent(BrokerDetailsActivity.this, BrokerChatPageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("propertyid", propertyid);
                bundle.putString("brokerid", brokerid);
                bundle.putString("propname", name);
                bundle.putString("rent", rent);
                bundle.putString("imageurl", imageurl);
                bundle.putString("brokername", names);
                bundle.putString("area", area);
                bundle.putString("city", city);
                bundle.putString("percentageOfCommission", percentageOfCommission);
                // Start the activity and pass the Bundle
                Log.e("naaaam", name);
                Intent intent = new Intent(BrokerDetailsActivity.this, PaymentCheckoutActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
