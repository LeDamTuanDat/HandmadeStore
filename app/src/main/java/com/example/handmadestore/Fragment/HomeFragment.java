package com.example.handmadestore.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.handmadestore.Adapter.ItemAdapter;
import com.example.handmadestore.Adapter.MiniCategoryAdapter;
import com.example.handmadestore.Category.CategoryActivity;
import com.example.handmadestore.Item.ItemActivity;
import com.example.handmadestore.Order.CartActivity;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.R;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    public static ArrayList<Banner> items;
    private View view;
    ArrayList<Item> bestSelling;

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
        openCategory();
        openItem();
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

    private void initUser(){
        binding.name.setText(MainActivity.currentUser.getRealname());
        if (MainActivity.currentUser.getCarts().size() > 0){
            binding.number.setVisibility(View.VISIBLE);
            binding.cart2.setVisibility(View.VISIBLE);
            binding.number.setText("" + MainActivity.currentUser.getCarts().size());
        }else {
            binding.number.setVisibility(View.GONE);
            binding.cart2.setVisibility(View.GONE);
        }
        if (!MainActivity.currentUser.getImage().isEmpty()){
            Glide.with(getContext()).load(MainActivity.currentUser.getImage()).into(binding.image);
        }else {
            binding.image.setImageResource(R.drawable.avatar);
        }
    }

    private void initBanners(){
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for ( Banner banner : SplashScreenActivity.banners) {
            imageList.add(new SlideModel(banner.getUrl(), ScaleTypes.FIT));
        }
        binding.imageSlider.setImageList(imageList);
    }


    private void initCategories(){
        binding.rycyclerCategory.setLayoutManager(new GridLayoutManager(getContext(),5));
        binding.rycyclerCategory.setAdapter(new MiniCategoryAdapter(SplashScreenActivity.categories));
    }

    private void initItems(){
        getBestSelling();
        binding.rycyclerItems.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rycyclerItems.setAdapter(new ItemAdapter(bestSelling, MainActivity.currentUser));
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

    private void openCategory() {
        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(binding.getRoot().getContext(), CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getBestSelling(){
        ArrayList<Item> temp = new ArrayList<>(SplashScreenActivity.items);
        Collections.sort(temp, new Comparator<Item>() {
            @Override
            public int compare(Item j, Item i) {
                return Integer.compare(i.getSold(), j.getSold());
            }
        });
        bestSelling = new ArrayList();
        for (int i = 0 ; i < 6 ; i++){
            bestSelling.add(temp.get(i));
        }
    }

    private void openItem() {
        binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(binding.getRoot().getContext(), ItemActivity.class);
                startActivity(intent);
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(binding.getRoot().getContext(), ItemActivity.class);
                startActivity(intent);
            }
        });
    }
}