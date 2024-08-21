package com.example.handmadestore.AdminCategory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.handmadestore.ClearError;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.ActivityAddCategoryBinding;

public class UploadCategoryActivity extends AppCompatActivity {
    DatabaseManager databaseManager;
    ActivityAddCategoryBinding binding;
    Uri uri;
    Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseManager = new DatabaseManager();
        getData();
        handleEvent();
    }

    public void getData(){
        category = (Category) getIntent().getSerializableExtra("category");
        if (category != null){
            uri = Uri.parse(category.getPicUrl());
            Glide.with(this).load(uri).into(binding.image);
            binding.title.setText(category.getTitle());
            binding.label.setText("Chỉnh sửa danh mục");
        }
    }

    public void handleEvent(){
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImg = new Intent(Intent.ACTION_PICK);
                pickImg.setType("image/*");
                activityResultLauncher.launch(pickImg);
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savaData();
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.title.addTextChangedListener(new ClearError(binding.title,binding.titleLayout));
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {//Nếu đã lấy ảnh
                        Intent data = result.getData();
                        uri = data.getData();
                        binding.image.setImageURI(uri);
                    } else {
                        Toast.makeText(UploadCategoryActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                }
            });

    public void savaData(){
        String title = binding.title.getText().toString().trim();
        if (title.isEmpty()){
//            Toast.makeText(this,"Vui lòng nhập danh mục",Toast.LENGTH_LONG).show();
            binding.titleLayout.setError("Chưa nhập danh mục");
        }else if(uri == null){
            Toast.makeText(this,"Vui lòng chọn ảnh",Toast.LENGTH_LONG).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setCancelable(false);
            builder.setView(R.layout.loading_activity);
            AlertDialog dialog = builder.create();

            if (category == null){
                if (!checkCategoryForAdd(title)){
                    databaseManager.addCategory(title,uri,dialog, UploadCategoryActivity.this);
                }
            }else {
                if (!checkCategoryForMod(title)){
                    databaseManager.modifyCategory(title,category,uri,dialog, UploadCategoryActivity.this);
                }
            }
        }
    }

    public boolean checkCategoryForAdd(String title){
        title = title.toLowerCase();
        for (Category item : LoginActivity.categories){
            if(item.getTitle().toLowerCase().equals(title)){
//                Toast.makeText(this,"Danh mục đã tồn tại",Toast.LENGTH_LONG).show();
                binding.titleLayout.setError("Danh mục đã tồn tại");
                return true;
            }
        }
        return false;
    }

    public boolean checkCategoryForMod(String title){
        title = title.toLowerCase();
        for (Category item : LoginActivity.categories){
            if(item.getTitle().toLowerCase().equals(title) && !category.getTitle().equals(title)){
//                Toast.makeText(this,"Danh mục đã tồn tại",Toast.LENGTH_LONG).show();
                binding.titleLayout.setError("Danh mục đã tồn tại");
                return true;
            }
        }
        return false;
    }
}