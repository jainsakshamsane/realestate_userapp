package com.realestate_userapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentCheckoutActivity extends AppCompatActivity {
    TextView namee, brokername, amount, pay;
    String name, rent, imageurl, brokerid, propertyid, brokernames, userid, mobile, username, emails, area, city, percentageOfCommission;
    ImageView back, profileimage;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentcheckout_activity);

        namee = findViewById(R.id.name);
        brokername = findViewById(R.id.brokername);
        amount = findViewById(R.id.amount);
        back = findViewById(R.id.back);
        pay = findViewById(R.id.pay);
        profileimage = findViewById(R.id.profileimage);

        SharedPreferences sharedPreferencess = getSharedPreferences("user_information", MODE_PRIVATE);
        userid = sharedPreferencess.getString("userId", "");
        mobile = sharedPreferencess.getString("mobile", "");
        username = sharedPreferencess.getString("username", "");
        emails = sharedPreferencess.getString("email", "");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("propname", "");
            rent = bundle.getString("rent", "");
            imageurl = bundle.getString("imageurl", "");
            brokerid = bundle.getString("brokerid", "");
            propertyid = bundle.getString("propertyid", "");
            brokernames = bundle.getString("brokername", "");
            area = bundle.getString("area", "");
            city = bundle.getString("city", "");
            percentageOfCommission = bundle.getString("percentageOfCommission", "");

            namee.setText(name);
            amount.setText("Rs. " + rent + "/-");
            brokername.setText(brokernames);
            Picasso.get().load(imageurl).into(profileimage);
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("payments");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference idRef = reference.push();
                String id = idRef.getKey();

                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                reference.child(id).child("payment_id").setValue(id);
                reference.child(id).child("userid").setValue(userid);
                reference.child(id).child("propertyName").setValue(name);
                reference.child(id).child("broker_name").setValue(brokernames);
                reference.child(id).child("borkerid").setValue(brokerid);
                reference.child(id).child("imageurl").setValue(imageurl);
                reference.child(id).child("username").setValue(username);
                reference.child(id).child("area").setValue(area);
                reference.child(id).child("city").setValue(city);
                reference.child(id).child("propertyId").setValue(propertyid);
                reference.child(id).child("total_amount").setValue(rent);
                reference.child(id).child("timestamp").setValue(timestamp);
                reference.child(id).child("percentageOfCommission").setValue(percentageOfCommission);

                SweetAlertDialog successDialog = new SweetAlertDialog(PaymentCheckoutActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        .setContentText("Thank you for purchasing")
                        .setConfirmText("Go to Home Page")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                // Dismiss the SweetAlertDialog
                                sweetAlertDialog.dismiss();

                                // Show AlertDialog with loading message
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentCheckoutActivity.this);
                                alertDialogBuilder.setMessage("Redirecting to homepage...");
                                alertDialogBuilder.setCancelable(false);

                                // Create the AlertDialog
                                final AlertDialog alertDialog = alertDialogBuilder.create();

                                // Handler to delay the dismissal of AlertDialog after 3 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Dismiss the AlertDialog
                                        alertDialog.dismiss();

                                        // Handle navigation to the next page here
                                        // For example, start a new activity
                                        startActivity(new Intent(PaymentCheckoutActivity.this, MainActivity.class));

                                        // Finish the current activity
                                        finish();
                                    }
                                }, 1000);

                                // Show the AlertDialog
                                alertDialog.show();
                            }
                        });
                successDialog.show();
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
