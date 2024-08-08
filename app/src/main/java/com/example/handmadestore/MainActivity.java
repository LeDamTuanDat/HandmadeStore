package com.example.handmadestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.handmadestore.Adapter.AdapterViewPager;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        handleBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        currentUser = (User) getIntent().getSerializableExtra("user");
        AdapterViewPager adapterViewPager = new AdapterViewPager(this);
        binding.pageMain.setAdapter(adapterViewPager);
        binding.pageMain.setUserInputEnabled(false);
        binding.pageMain.setOffscreenPageLimit(4);
    }

    private void handleBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itHome:
                        binding.pageMain.setCurrentItem(0);
                        break;
                    case R.id.itCart:
                        binding.pageMain.setCurrentItem(1);
                        break;
                    case R.id.itOrder:
                        binding.pageMain.setCurrentItem(2);
                        break;
                    case R.id.itUser:
                        binding.pageMain.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}