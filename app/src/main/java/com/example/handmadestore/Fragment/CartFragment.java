package com.example.handmadestore.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.handmadestore.Adapter.CartAdapter;
import com.example.handmadestore.AddOrder;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.ConfirmOrderBinding;
import com.example.handmadestore.databinding.FragmentCartBinding;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    private View view;
    private CartAdapter cartAdapter;

    public CartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        initCartList();
        confirmOrder();
        return view;
    }

    @Override
    public void onResume() {
        initCartList();
        super.onResume();
    }

    protected long getTotalPrice(){
        MainActivity.currentUser.getCarts();
        long totalPrice = 0;
        for (int i = 0 ; i < MainActivity.currentUser.getCarts().size() ; i++){
            Cart cart = MainActivity.currentUser.getCarts().get(i);
            totalPrice += cart.calculatePrice();
        }
        return totalPrice;
    }

    private void calculatorCart() {
        long delivery = 15000;
        long itemTotal = getTotalPrice();
        long total = itemTotal+delivery;

        binding.totalPriceOfItem.setText(itemTotal+"vnd");
        binding.delivery.setText(delivery+"vnd");
        binding.total.setText(total+"vnd");
    }

    private void initCartList() {
//        calculatorCart();
//        if(MainActivity.currentUser.getCarts().isEmpty()){
//            binding.emptyTxt.setVisibility(View.VISIBLE);
//            binding.acceptOrder.setVisibility(View.GONE);
//        }else{
//            binding.emptyTxt.setVisibility(View.GONE);
//            binding.acceptOrder.setVisibility(View.VISIBLE);
//        }
//        cartAdapter = new CartAdapter(MainActivity.currentUser.getCarts(), binding,true);
//        binding.cartView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        binding.cartView.setAdapter(cartAdapter);
    }

    private void confirmOrder(){
        binding.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddOrder.class);
                startActivity(intent);
            }
        });
    }
}