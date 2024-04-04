package com.realestate_userapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.BrokerChatPageActivity;
import com.realestate_userapp.BrokerDetailsActivity;
import com.realestate_userapp.Models.PropertyModel;
import com.realestate_userapp.MoreDetailsActivity;
import com.realestate_userapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import jp.wasabeef.glide.transformations.BlurTransformation;


import java.util.List;

public class ChatsDisplayAdapter extends RecyclerView.Adapter<ChatsDisplayAdapter.ViewHolder> {
    private List<PropertyModel> category;
    private Context context;
    private String brokername,imageUrl;

    public ChatsDisplayAdapter(Context context, List<PropertyModel> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public ChatsDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrapper_chat, parent, false);
        return new ChatsDisplayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsDisplayAdapter.ViewHolder holder, int position) {
        PropertyModel destination = category.get(position);
        holder.name.setText(destination.getPropertyName());

        String propertyid = destination.getPropertyId();

        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("Property_images");

        imagesRef.orderByChild("propertyId").equalTo(propertyid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Get the image URL
                            imageUrl = snapshot.child("imageurl").getValue(String.class);
                            Picasso.get().load(imageUrl).into(holder.profileimage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        databaseError.toException().printStackTrace();
                    }
                });

        String brokerid = destination.getBrokerid();

        DatabaseReference brokerRef = FirebaseDatabase.getInstance().getReference("Broker");

        brokerRef.orderByChild("borkerid").equalTo(brokerid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            brokername = snapshot.child("name").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        databaseError.toException().printStackTrace();
                    }
                });

        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("brokerid", brokerid);
                bundle.putString("imageurl", imageUrl);
                bundle.putString("brokername", brokername);
                bundle.putString("propertyid", propertyid);
                bundle.putString("propname", destination.getPropertyName());
                Intent intent = new Intent(context, BrokerChatPageActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileimage;
        TextView name;
        RelativeLayout linear1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage = itemView.findViewById(R.id.profileimage);
            name = itemView.findViewById(R.id.name);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }
}
