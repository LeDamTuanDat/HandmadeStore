package com.example.handmadestore;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.CartAdapter;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.databinding.ActivityOrderDetailBinding;

import java.util.Arrays;

public class OrderDetail extends AppCompatActivity {

    ActivityOrderDetailBinding binding ;
    Order order;
    String[] status = {"Chờ xác nhận","Đã xác nhận","Đang giao hàng","Đã giao"};
    ArrayAdapter<String> statusAdapter;
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
    }

    public void getData(){
        order = (Order) getIntent().getSerializableExtra("order");
        statusAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,status);
        statusAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinner.setAdapter(statusAdapter);

        binding.id.setText("ID: " + order.getKeyId());
        binding.name.setText("Người nhận: " + order.getName());
        binding.phone.setText("SĐT: " + order.getPhone());
        binding.address.setText("Địa chỉ: " + order.getAddress());
        binding.status.setText("Trạng thái: " + order.getStatus());
        binding.spinner.setSelection(Arrays.asList(status).indexOf(order.getStatus()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(order.getCarts(),false);
        binding.recyclerView.setAdapter(cartAdapter);

        if (!MainActivity.currentUser.getPriority()){
            binding.layoutSpinner.setVisibility(View.GONE);
            binding.save.setVisibility(View.GONE);
        }
    }
}