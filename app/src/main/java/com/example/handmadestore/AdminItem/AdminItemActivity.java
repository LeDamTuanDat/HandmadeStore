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
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.ActivityAdminItemBinding;
import com.example.handmadestore.databinding.DropdownSearchBinding;

public class AdminItemActivity extends AppCompatActivity {
    ActivityAdminItemBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleEvent();
    }

    public void handleEvent(){
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AdminItemActivity.this);
                DropdownSearchBinding dropdownBinding = DropdownSearchBinding.inflate(LayoutInflater.from(AdminItemActivity.this));
                dialog.setContentView(dropdownBinding.getRoot());
                dialog.getWindow().setLayout(750,900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ArrayAdapter<Category> adapter = new ArrayAdapter<>(AdminItemActivity.this, android.R.layout.simple_list_item_1, LoginActivity.categories);
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
                        binding.category.setText(adapter.getItem(i).toString());
                        dialog.dismiss();
                    }
                });
            }
        });
        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminItemActivity.this, UploadItemActivity.class);
                startActivity(intent);
            }
        });
    }
}