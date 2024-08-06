package com.example.handmadestore.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.handmadestore.Adapter.CartAdapter;
import com.example.handmadestore.Adapter.CategoryAdapter;
import com.example.handmadestore.Adapter.ItemAdapter;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.DatabaseManager;
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
        initBanners();
        initCategories();
        initItems();
        return view;
    }

    public void initBanners(){
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for ( Banner banner : LoginActivity.banners) {
            imageList.add(new SlideModel(banner.getUrl(), ScaleTypes.FIT));
        }
        binding.imageSlider.setImageList(imageList);
    }


    private void initCategories(){
        binding.rycyclerCategory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.rycyclerCategory.setAdapter(new CategoryAdapter(LoginActivity.categories));
    }

    private void initItems(){
        binding.rycyclerItems.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rycyclerItems.setAdapter(new ItemAdapter(LoginActivity.items));
    }
}