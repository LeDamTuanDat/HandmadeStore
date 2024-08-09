package com.example.handmadestore.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.handmadestore.Fragment.CartFragment;
import com.example.handmadestore.Fragment.HomeFragment;
import com.example.handmadestore.Fragment.OrderFragment;
import com.example.handmadestore.Fragment.UserFragment;

import java.util.ArrayList;

public class AdapterViewPagerForMain extends FragmentStateAdapter {

    ArrayList<Fragment> arr;

    public AdapterViewPagerForMain(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new CartFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new UserFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
