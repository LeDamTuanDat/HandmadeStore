package com.example.handmadestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.databinding.ConfirmOrderBinding;

import java.util.ArrayList;

public class AddOrder extends AppCompatActivity {
    ConfirmOrderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleAddInfor();
        handleConfirm();
    }

    public void handleAddInfor(){
        binding.defaultInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.defaultInfor.isChecked()){
                    binding.name.setText(MainActivity.currentUser.getUsername());
                    binding.phone.setText(MainActivity.currentUser.getPhone());
                    binding.address.setText(MainActivity.currentUser.getAddress());
                }else {
                    binding.name.setText("");
                    binding.phone.setText("");
                    binding.address.setText("");
                }
            }
        });
    }

    public void handleConfirm(){
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String phone = binding.phone.getText().toString();
                String address = binding.address.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()){
                    Toast.makeText(AddOrder.this,"Vui lòng điền đủ thông tin",Toast.LENGTH_LONG).show();
                }else {
                    String idUser = MainActivity.currentUser.getUsername();
                    Order order = new Order(idUser,name,phone,address,MainActivity.currentUser.getCarts());
                    DatabaseManager databaseManager = new DatabaseManager();
                    databaseManager.addOrder(order,AddOrder.this);
                    finish();
                }
            }
        });
    };
}