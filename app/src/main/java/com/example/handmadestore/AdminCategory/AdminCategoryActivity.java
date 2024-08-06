package com.example.handmadestore.AdminCategory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.handmadestore.Adapter.AdminCategoryAdapter;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.databinding.ActivityCategoryBinding;

import java.util.ArrayList;

public class AdminCategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    AdminCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new AdminCategoryAdapter(LoginActivity.categories);
        handleEvent();
        initCategory();
        handleSearch();
    }


    public void initCategory(){
        binding.recyclerView.setLayoutManager(new GridLayoutManager(AdminCategoryActivity.this,3));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public void handleEvent(){
        binding.addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, UploadCategoryActivity.class);
                startActivity(intent);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void handleSearch(){
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredCategory(newText);
                return true;
            }
        });
    }

    public void filteredCategory(String text){
        ArrayList<Category> filtered = new ArrayList<>();
        for (Category item : LoginActivity.categories) {
            if(item.getTitle().contains(text)){
                filtered.add(item);
            }
        }
        adapter.setFilteredCategory(filtered);
    }

}