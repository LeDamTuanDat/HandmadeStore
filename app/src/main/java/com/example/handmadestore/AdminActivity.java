package com.example.handmadestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handmadestore.AdminCategory.AdminCategoryActivity;
import com.example.handmadestore.AdminItem.AdminItemActivity;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {
    public static User currentUser;
    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        handleEvent();
    }

    public void getData(){
        currentUser = (User) getIntent().getSerializableExtra("user");
    }

    public void handleEvent(){
        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });
        binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AdminItemActivity.class);
                startActivity(intent);
            }
        });
        binding.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(AdminActivity.this,AdminOrderActivity.class);
                startActivity(itent);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,LoginActivity.class);
                startActivity(intent);
                currentUser = null;
                finish();
            }
        });
    }
}