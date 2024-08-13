package com.example.handmadestore;

import android.content.Intent;
import android.graphics.Paint;
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
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.databinding.ActivityOrderDetailBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderDetail extends AppCompatActivity {

    ActivityOrderDetailBinding binding ;
    Order order;
    ArrayList<Item> items;
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String formattedDate = dateFormat.format(order.getOrderTime());
        binding.timeOrder.setText("Thời gian đặt hàng: " + formattedDate);

        String paymentMethod = (order.getZaloPayment() ? "ZaloPay" : "Tiền mặt");
        binding.paymentMethod.setText("Hình thức thanh toán: " + paymentMethod);
        binding.spinner.setSelection(Arrays.asList(status).indexOf(order.getStatus()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(order.getCarts(),false);
        binding.recyclerView.setAdapter(cartAdapter);

        if (order.getZaloPayment()){
            binding.total.setText("Tổng thanh toán : 0đ");
            binding.zaloPrice.setText(order.calTotal() + "đ");
            binding.zaloPrice.setPaintFlags(binding.zaloPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            binding.total.setText("Tổng thanh toán : " + order.calTotal() + "đ");
            binding.zaloPrice.setVisibility(View.GONE);
        }

        if (MainActivity.currentUser != null) {
            binding.layoutSpinner.setVisibility(View.GONE);
            binding.save.setVisibility(View.GONE);
            if (!order.getStatus().equals("Đã giao")){
                binding.rate.setVisibility(View.GONE);
            }
        }else {
            binding.layoutSpinner.setVisibility(View.VISIBLE);
            binding.save.setVisibility(View.VISIBLE);
            binding.rate.setVisibility(View.GONE);
        }
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
        binding.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRating()){
                    Intent intent = new Intent(OrderDetail.this,RatingActivity.class);
                    intent.putExtra("items",items);
                    intent.putExtra("orderId",order.getKeyId());
                    startActivity(intent);
                }else {
                    Toast.makeText(OrderDetail.this, "Đơn hàng đã được đánh giá", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkRating(){

        ArrayList<Item> check = new ArrayList<>();
        for (Cart cart : order.getCarts()) {
            check.add(cart.getItem());
        }

        items = new ArrayList<>(check);

        for (Item item : check) {
            for (Rating temp : LoginActivity.ratings) {
                if (temp.getOrderId().equals(order.getKeyId())
                        && temp.getItemId().equals(item.getId())){
                    items.remove(item);
                    break;
                }
            }
        }

        if (items.size() > 0){
            return true;
        }
        return false;
    }
    
    
}