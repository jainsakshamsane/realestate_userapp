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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.Adapters.DishImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoreDetailsActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView heading1, heading2, heading4, price, parkingg, gym, bedroom, floorss, book;
    String name, rent, rooms, parking, nearby, floors, area, city, propertyid, gymm, brokerid, imageUrl, percentageOfCommission;
    ImageView back, liked, unliked;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moredetails_activity);

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
        book = findViewById(R.id.book);
        liked = findViewById(R.id.liked);
        unliked = findViewById(R.id.unliked);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("propname", "");
            rent = bundle.getString("rent", "");
            rooms = bundle.getString("rooms", "");
            parking = bundle.getString("parking", "");
            nearby = bundle.getString("nearbyplace", "");
            floors = bundle.getString("floors", "");
            area = bundle.getString("area", "");
            city = bundle.getString("city", "");
            propertyid = bundle.getString("propertyid", "");
            gymm = bundle.getString("gym", "");
            brokerid = bundle.getString("brokerid", "");
            percentageOfCommission = bundle.getString("percentageOfCommission", "");

            heading1.setText(name);
            heading2.setText(area + "," + city);
            heading4.setText("You will have nearby: " + nearby);
            price.setText("Rs. " + rent + "/-");
            bedroom.setText("Bedrooms : " + rooms);
            parkingg.setText("Parking : " + parking);
            floorss.setText("Floors : " + floors);
            gym.setText("Gym : " + gymm);
        }

        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("Property");

        statusRef.orderByChild("propertyId").equalTo(propertyid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Get the status as a Long
                            Long statusLong = snapshot.child("liked").getValue(Long.class);

                            // Convert statusLong to String if needed
                            String status = String.valueOf(statusLong);

                            if ("2".equals(status)) { // Compare strings using equals method, not ==
                                liked.setVisibility(View.VISIBLE);
                            } else {
                                unliked.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        databaseError.toException().printStackTrace();
                    }
                });

        unliked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference("Property").child(propertyid);
                    propertyRef.child("liked").setValue(2);
                    Toast.makeText(MoreDetailsActivity.this, "Property marked as favourite", Toast.LENGTH_SHORT).show();
                    liked.setVisibility(View.VISIBLE);
                    unliked.setVisibility(View.GONE);
            }
        });

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference("Property").child(propertyid);
                propertyRef.child("liked").setValue(3);
                Toast.makeText(MoreDetailsActivity.this, "Property marked as unfavourite", Toast.LENGTH_SHORT).show();
                unliked.setVisibility(View.VISIBLE);
                liked.setVisibility(View.GONE);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("propertyid", propertyid);
                bundle.putString("brokerid", brokerid);
                bundle.putString("propname", name);
                bundle.putString("rent", rent);
                bundle.putString("imageurl", imageUrl);
                bundle.putString("area", area);
                bundle.putString("city", city);
                bundle.putString("percentageOfCommission", percentageOfCommission);
                // Start the activity and pass the Bundle
                Log.e("naaaam", name);
                Intent intent = new Intent(MoreDetailsActivity.this, BrokerDetailsActivity.class);
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
                DishImageAdapter adapter = new DishImageAdapter(imageUrls, MoreDetailsActivity.this);
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
