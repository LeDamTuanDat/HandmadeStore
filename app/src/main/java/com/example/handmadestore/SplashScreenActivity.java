package com.example.handmadestore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.handmadestore.HelperClass.IntroSliderActivity;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Database.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.User.LoginActivity;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static ArrayList<Banner> banners;
    public static ArrayList<User> users;
    public static ArrayList<Category> categories;
    public static ArrayList<Item> items;
    public static ArrayList<Rating> ratings;
    public static ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initPreferences();
        getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
                if (isFirstTime) {
                    editor.putBoolean("isFirstTime", false);
                    editor.apply();
                    Intent intent = new Intent(SplashScreenActivity.this, IntroSliderActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
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

    private void getData(){
        DatabaseManager databaseManager = new DatabaseManager();
        banners = new ArrayList<>();
        users = new ArrayList<>();
        categories = new ArrayList<>();
        items = new ArrayList<>();
        ratings = new ArrayList<>();
        orders = new ArrayList<>();
        databaseManager.getUsers(users);
        databaseManager.getBanner(banners);
        databaseManager.getCategory(categories);
        databaseManager.getItems(items);
        databaseManager.getRatings(ratings);
    }
}