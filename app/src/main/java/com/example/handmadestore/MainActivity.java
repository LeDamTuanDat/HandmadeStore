package com.example.handmadestore;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handmadestore.Adapter.AdapterViewPagerForMain;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

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
        AdapterViewPagerForMain adapterViewPagerForMain = new AdapterViewPagerForMain(this);
        binding.pageMain.setAdapter(adapterViewPagerForMain);
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