package com.example.handmadestore.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.handmadestore.Adapter.ItemAdapter;
import com.example.handmadestore.Adapter.CategoryAdapter;
import com.example.handmadestore.CartActivity;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    public static ArrayList<Banner> items;
    private View view;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        initUser();
        initBanners();
        initCategories();
        initItems();
        openCart();
        return view;
    }

    @Override
    public void onResume() {
        initUser();
        initBanners();
        initCategories();
        initItems();
        super.onResume();
    }

    public void initUser(){
        binding.name.setText(MainActivity.currentUser.getUsername());
        if (MainActivity.currentUser.getCarts().size() > 0){
            binding.number.setVisibility(View.VISIBLE);
            binding.cart2.setVisibility(View.VISIBLE);
            binding.number.setText("" + MainActivity.currentUser.getCarts().size());
        }else {
            binding.number.setVisibility(View.GONE);
            binding.cart2.setVisibility(View.GONE);
        }
    }

    public void initBanners(){
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for ( Banner banner : LoginActivity.banners) {
            imageList.add(new SlideModel(banner.getUrl(), ScaleTypes.FIT));
        }
        binding.imageSlider.setImageList(imageList);
    }


    private void initCategories(){
        binding.rycyclerCategory.setLayoutManager(new GridLayoutManager(getContext(),5));
        binding.rycyclerCategory.setAdapter(new CategoryAdapter(LoginActivity.categories));
    }

    private void initItems(){
        binding.rycyclerItems.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rycyclerItems.setAdapter(new ItemAdapter(LoginActivity.items, MainActivity.currentUser));
    }

    public void openCart(){
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(binding.getRoot().getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }
}