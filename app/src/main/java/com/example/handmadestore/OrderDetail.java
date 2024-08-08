package com.example.handmadestore;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.CartAdapter;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
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
        handleEvent();
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

        calTotal();

        if (MainActivity.currentUser != null) {
            binding.layoutSpinner.setVisibility(View.GONE);
            binding.save.setVisibility(View.GONE);
        }else {
            binding.layoutSpinner.setVisibility(View.VISIBLE);
            binding.save.setVisibility(View.VISIBLE);
        }
    }

    protected void calTotal(){
        long totalCart = 0;
        for (int i = 0 ; i < order.getCarts().size() ; i++){
            Cart cart = order.getCarts().get(i);
            totalCart += cart.calculatePrice();
        }
        binding.total.setText("Tổng thanh toán : " + totalCart + "đ");
    }

    public void handleEvent(){
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                order.setStatus(status[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager databaseManager = new DatabaseManager();
                databaseManager.setOrderStatus(order);
                Toast.makeText(OrderDetail.this,"Cập nhật trạng thái thành công",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}