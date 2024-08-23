package com.example.handmadestore.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.handmadestore.AddOrder;
import com.example.handmadestore.AdminActivity;
import com.example.handmadestore.CartActivity;
import com.example.handmadestore.EditProfileActivity;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.R;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    private View view;

    public UserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        initUser();
        openCart();
        openOrder();
        logout();
        edit();
        return view;
    }

    @Override
    public void onResume() {
        initUser();
        super.onResume();
    }

    private void initUser(){
        binding.txtRealName.setText(MainActivity.currentUser.getRealname());
        binding.txtUsername.setText(MainActivity.currentUser.getUsername());
        binding.cart.setText(MainActivity.currentUser.getCarts().size()+"");
        binding.order.setText(SplashScreenActivity.orders.size() + "");
        binding.username.setText(MainActivity.currentUser.getUsername());
        binding.realName.setText(MainActivity.currentUser.getRealname());
        binding.email.setText(MainActivity.currentUser.getEmail());
        binding.phone.setText(MainActivity.currentUser.getPhone());
        binding.address.setText(MainActivity.currentUser.getAddress());
        binding.password.setText(MainActivity.currentUser.getPassword());
        if (!MainActivity.currentUser.getImage().isEmpty()){
            Glide.with(getContext()).load(MainActivity.currentUser.getImage()).into(binding.image);
        }else {
            binding.image.setImageResource(R.drawable.avatar);
        }
    }

    private void openCart(){
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(binding.getRoot().getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openOrder(){
        binding.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).switchToFragment(2);
                }
            }
        });
    }

    private void edit(){
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("user",MainActivity.currentUser);
                intent.putExtra("normal",true);
                startActivity(intent);
            }
        });
    }

    private void logout(){
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                MainActivity.currentUser = null;
                getActivity().finish();
            }
        });
    }


}