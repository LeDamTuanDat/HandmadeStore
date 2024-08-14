package com.example.handmadestore;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.ItemAdapter;
import com.example.handmadestore.Adapter.UserAdapter;
import com.example.handmadestore.AdminItem.AdminItemActivity;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.databinding.ActivityManageUserBinding;

import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    ActivityManageUserBinding binding;
    UserAdapter adapter;
    ArrayList<Item> resultAfterFiltered = new ArrayList<>();
    ArrayList<Item> resultAfterSearch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        adapter = new UserAdapter(LoginActivity.users);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ManageUserActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.recyclerView.setAdapter(adapter);
    }
}