package com.example.handmadestore.AdminItem;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.handmadestore.Adapter.ItemAdapter;
import com.example.handmadestore.AdminActivity;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.databinding.ActivityAdminItemBinding;
import com.example.handmadestore.databinding.DropdownSearchBinding;

import java.util.ArrayList;

public class AdminItemActivity extends AppCompatActivity {
    ActivityAdminItemBinding binding;
    DropdownSearchBinding dropdownBinding;
    ItemAdapter adapter;
    Category category;
    ArrayList<Item> resultAfterFiltered = new ArrayList<>();
    ArrayList<Item> resultAfterSearch = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        category = (Category) getIntent().getSerializableExtra("category");

        checkUser();
        initItems();
        handleSelectCategory();
        handleAddItem();
        handleSearch();
        handleBack();
    }

    @Override
    protected void onResume() {
        initItems();
        binding.search.setQuery("",false);
        binding.search.clearFocus();
        if (category != null) {
            resultAfterFiltered(category.getId());
        }
        super.onResume();
    }

    private void checkUser() {
        if (MainActivity.currentUser != null){
            binding.filter.setVisibility(View.GONE);
            binding.addItem.setVisibility(View.GONE);
        }
    }

    private void initItems(){
        if (MainActivity.currentUser != null){
            adapter = new ItemAdapter(LoginActivity.items, MainActivity.currentUser);
        }else {
            adapter = new ItemAdapter(LoginActivity.items, AdminActivity.currentUser);
        }
        binding.recyclerView.setLayoutManager(new GridLayoutManager(AdminItemActivity.this,2));
        binding.recyclerView.setAdapter(adapter);
    }

    private void handleSelectCategory(){
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AdminItemActivity.this);
                dropdownBinding = DropdownSearchBinding.inflate(LayoutInflater.from(AdminItemActivity.this));
                dialog.setContentView(dropdownBinding.getRoot());
                dialog.getWindow().setLayout(750,900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ArrayList<Category> temp = new ArrayList<>(LoginActivity.categories);
                temp.add(0,new Category("Tất cả danh mục"));
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(AdminItemActivity.this, android.R.layout.simple_list_item_1, temp);
                dropdownBinding.list.setAdapter(adapter);
                dropdownBinding.search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                dropdownBinding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        category = adapter.getItem(i);
                        binding.category.setText(category.toString());
                        resultAfterFiltered(category.getId());
                        binding.search.setQuery("",false);
                        binding.search.clearFocus();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void resultAfterFiltered(String text){
        if (text.equals("all")){
            adapter.setResultAfterFiltered(LoginActivity.items);
        }else {
            resultAfterFiltered.clear();
            for (Item item : LoginActivity.items) {
                if(item.getCategoryId().equals(text)){
                    resultAfterFiltered.add(item);
                }
            }
            adapter.setResultAfterFiltered(resultAfterFiltered);
        }
    }

    private void handleAddItem(){
        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminItemActivity.this, UploadItemActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleSearch(){
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resultAfterSearch(newText);
                return true;
            }
        });
    }

    private void resultAfterSearch(String text){
        resultAfterSearch.clear();
        if(category != null && category.getId() != "all"){
            for (Item item : resultAfterFiltered) {
                if(item.getTitle().contains(text)){
                    resultAfterSearch.add(item);
                }
            }
        }else {
            for (Item item : LoginActivity.items) {
                if(item.getTitle().contains(text)){
                    resultAfterSearch.add(item);
                }
            }
        }
        adapter.setResultAfterFiltered(resultAfterSearch);
    }

    private void handleBack(){
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}