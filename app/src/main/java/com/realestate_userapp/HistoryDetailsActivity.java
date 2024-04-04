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
import com.realestate_userapp.Adapters.DishImageAdapter;
import com.realestate_userapp.Adapters.PropertyAdapter;
import com.realestate_userapp.Models.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetailsActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView heading1, heading2, heading4, price, parkingg, gym, bedroom, floorss;
    String propertyid, imageUrl;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historydetails_actitivity);

        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        heading1 = findViewById(R.id.name);
        heading2 = findViewById(R.id.location);
        heading4 = findViewById(R.id.nearby);
        back = findViewById(R.id.back);
        price = findViewById(R.id.price);
        parkingg = findViewById(R.id.parking);
        gym = findViewById(R.id.gym);
        bedroom = findViewById(R.id.bedroom);
        floorss = findViewById(R.id.floors);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            propertyid = bundle.getString("propertyid", "");
        }

        DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference("Property");

        propertyRef.orderByChild("propertyId").equalTo(propertyid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    String gymm = ds.child("gym").getValue(String.class);

                    heading1.setText(propname);
                    heading2.setText(area + "," + cityy);
                    heading4.setText("You will have nearby: " + nearby);
                    price.setText("Rs. " + rent);
                    bedroom.setText("Bedrooms : " + rooms);
                    parkingg.setText("Parking : " + parking);
                    floorss.setText("Floors : " + floors);
                    gym.setText("Gym : " + gymm);
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

        // Fetch image URLs from the database and set up ViewPager adapter
        fetchImageUrlsAndSetupViewPager();
    }

    private void fetchImageUrlsAndSetupViewPager() {
        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("Property_images");
        imagesRef.orderByChild("propertyId").equalTo(propertyid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> imageUrls = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Fetch image URLs from database
                    imageUrl = snapshot.child("imageurl").getValue(String.class);
                    imageUrls.add(imageUrl);
                }
                // Set up ViewPager adapter with fetched image URLs
                DishImageAdapter adapter = new DishImageAdapter(imageUrls, HistoryDetailsActivity.this);
                viewPager.setAdapter(adapter);

                // Update the dot indicators
                setupDotIndicators(imageUrls.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void setupDotIndicators(int count) {
        LinearLayout dotsLayout = findViewById(R.id.dotsLayout);
        ImageView[] dots = new ImageView[count];

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.ic_dot_inactive);

            final int position = i;
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position);
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }

        // Highlight the first dot initially
        if (dots.length > 0) {
            dots[0].setImageResource(R.drawable.ic_dot_active);
        }

        // Set up ViewPager listener to update dot indicators when page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < count; i++) {
                    dots[i].setImageResource(R.drawable.ic_dot_inactive);
                }
                dots[position].setImageResource(R.drawable.ic_dot_active);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
