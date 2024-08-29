package com.example.handmadestore.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.RatingAdapter;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.FragmentReviewBinding;

import java.util.ArrayList;


public class ReviewFragment extends Fragment {
    FragmentReviewBinding binding;
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater,container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void initList(View view) {
        ArrayList<Rating> ratings = new ArrayList<>();
        Item item = (Item) getArguments().getSerializable("item");
        for (Rating temp: SplashScreenActivity.ratings) {
            if(temp.getItemId().equals(item.getId())){
                ratings.add(temp);
            }
        }

        if (ratings.size() > 0){
            binding.reviewView.setAdapter(new RatingAdapter(ratings));
            binding.reviewView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
    }
}