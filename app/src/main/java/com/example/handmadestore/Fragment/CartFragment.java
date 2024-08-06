package com.example.handmadestore.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handmadestore.Adapter.CartAdapter;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.databinding.FragmentCartBinding;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    private View view;
    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        cartAdapter = new CartAdapter(MainActivity.currentUser.getCarts(), binding);
        initCartList();
        return view;
    }

    @Override
    public void onResume() {
        cartAdapter.notifyDataSetChanged();
        initCartList();
        super.onResume();
    }

    @Override
    public void onPause() {
        initCartList();
        super.onPause();
    }

    protected double getTotalPrice(){
        ArrayList<Cart> carts = MainActivity.currentUser.getCarts();
        double totalPrice = 0;
        for (int i = 0 ; i < carts.size() ; i++){
            Cart cart = carts.get(i);
            totalPrice += cart.calculatePrice();
        }
        return totalPrice;
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        double itemTotal = Math.round(getTotalPrice() *100)/100;
        double tax = Math.round((itemTotal * percentTax * 100.0))/100.0;

        double total = itemTotal+tax;

        binding.totalFreeTxt.setText(itemTotal+"vnd");
        binding.taxTxt.setText(tax+"vnd");
        binding.deliveryTxt.setText(delivery+"vnd");
        binding.totalTxt.setText(total+"vnd");
    }

    private void initCartList() {
        calculatorCart();
        if(MainActivity.currentUser.getCarts().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.acceptOrder.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.acceptOrder.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(cartAdapter);
    }
}