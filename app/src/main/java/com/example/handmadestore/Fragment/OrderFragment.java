package com.example.handmadestore.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handmadestore.Adapter.OrderAdapter;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.FragmentOrderBinding;

public class OrderFragment extends Fragment {

    FragmentOrderBinding binding;
    private OrderAdapter orderAdapter;
    private View view;

    public OrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        initOrderList();
        return view;
    }

    @Override
    public void onResume() {
        initOrderList();
        super.onResume();
    }

    public void initOrderList(){
        if (SplashScreenActivity.orders.size() == 0){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.order.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.order.setVisibility(View.VISIBLE);
        }
        orderAdapter = new OrderAdapter(SplashScreenActivity.orders);
        binding.cartView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(orderAdapter);
    }
}