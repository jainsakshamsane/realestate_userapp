package com.realestate_userapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.realestate_userapp.LoginActivity;
import com.realestate_userapp.MainActivity;

public class FlashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);

        // Delay execution of login check using Handler
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, SPLASH_SCREEN_DELAY);
    }

    private void checkLoginStatus() {
        // Check if the user is already logged in
        if (isLoggedIn()) {
            Intent intent = new Intent(FlashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // If not logged in, go to LoginActivity
            Intent intent = new Intent(FlashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish(); // Finish the activity to prevent going back to the splash screen
    }

    private boolean isLoggedIn() {
        // Check your login status logic using shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
