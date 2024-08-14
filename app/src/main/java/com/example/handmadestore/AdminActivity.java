package com.example.handmadestore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void getData(){

        currentUser = (User) getIntent().getSerializableExtra("user");
        if (!currentUser.getImage().isEmpty()){
            Glide.with(AdminActivity.this).load(currentUser.getImage()).into(binding.image);
        }
        binding.name.setText("Xin chào " + currentUser.getRealname()+ "!");
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
        binding.statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(AdminActivity.this,StatisticActivity.class);
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

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            new AlertDialog.Builder(AdminActivity.this)
                    .setTitle("Xác nhận thoát")
                    .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }
    };
}