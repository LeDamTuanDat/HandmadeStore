package com.example.handmadestore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initPreferences();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
                if (isFirstTime) {
                    editor.putBoolean("isFirstTime", false);
                    editor.apply();
                    Intent intent = new Intent(SplashScreenActivity.this,IntroSliderActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 2000);
    }

    private void initPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }
}