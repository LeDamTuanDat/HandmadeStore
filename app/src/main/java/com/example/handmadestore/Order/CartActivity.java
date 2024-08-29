package com.example.handmadestore.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.CartAdapter;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.ActivityCartBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCartList();
        confirmOrder();
        back();
    }

    @Override
    protected void onResume() {
        initCartList();
        super.onResume();
    }

    protected long getTotalPrice(){
        long totalPrice = 0;
        for (int i = 0; i < MainActivity.currentUser.getCarts().size() ; i++){
            Cart cart = MainActivity.currentUser.getCarts().get(i);
            totalPrice += cart.calculatePrice();
        }
        return totalPrice;
    }

    public void calculatorCart() {
        long delivery = 15000;
        long itemTotal = getTotalPrice();
        long total = itemTotal+delivery;

        NumberFormat formatVND = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
        binding.totalPriceOfItem.setText(formatVND.format(itemTotal));
        binding.delivery.setText(formatVND.format(delivery));
        binding.total.setText(formatVND.format(total));
    }

    public void initCartList() {
        calculatorCart();
        if(MainActivity.currentUser.getCarts().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.acceptOrder.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.acceptOrder.setVisibility(View.VISIBLE);
        }
        cartAdapter = new CartAdapter(MainActivity.currentUser.getCarts(), binding,true);
        binding.cartView.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(cartAdapter);
    }

    public void confirmOrder(){
        binding.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCarts()){
                    Intent intent = new Intent(CartActivity.this, AddOrderActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(CartActivity.this, "Số lượng nhiều hơn tồn kho", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(){
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean checkCarts(){
        for (Cart cart : MainActivity.currentUser.getCarts()) {
            Item temp = cart.getItem();
            for (Item item : SplashScreenActivity.items) {
                if (item.getId().equals(temp.getId())){
                    if (cart.getNumber() > item.getInventory()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}