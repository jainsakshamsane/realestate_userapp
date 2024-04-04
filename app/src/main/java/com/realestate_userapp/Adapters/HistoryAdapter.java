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
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestate_userapp.HistoryDetailsActivity;
import com.realestate_userapp.Models.HistoryModel;
import com.realestate_userapp.Models.PropertyModel;
import com.realestate_userapp.MoreDetailsActivity;
import com.realestate_userapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import jp.wasabeef.glide.transformations.BlurTransformation;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryModel> category;
    private Context context;

    public HistoryAdapter(Context context, List<HistoryModel> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrapper_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        HistoryModel destination = category.get(position);
        holder.propertyname.setText(destination.getProperty_name());
        holder.brokername.setText("Broker : " + destination.getBroker_name());
        holder.rent.setText("Rs. " + destination.getTotal_amount() + "/-");
        String timestamp = destination.getTimestamp();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy 'at' hh:mma", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            String formattedDate = outputFormat.format(date);
            holder.datee.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.datee.setText("Invalid Date");
        }
        Picasso.get().load(destination.getImageurl()).into(holder.imageView);

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("propertyid", destination.getProperty_id());
                Intent intent = new Intent(context, HistoryDetailsActivity.class);
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
        ImageView imageView;
        TextView propertyname, brokername, rent, datee;
        LinearLayout linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.leftIcon);
            propertyname = itemView.findViewById(R.id.name);
            brokername = itemView.findViewById(R.id.brokername);
            rent = itemView.findViewById(R.id.price);
            linear = itemView.findViewById(R.id.linear);
            datee = itemView.findViewById(R.id.date);
        }
    }
}
