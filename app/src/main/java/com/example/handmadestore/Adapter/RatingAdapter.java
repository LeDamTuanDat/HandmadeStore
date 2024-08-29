package com.example.handmadestore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.R;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.ViewholderReviewBinding;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.Viewholder> {
    ArrayList<Rating> items;
    Context context;

    public RatingAdapter(ArrayList<Rating> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        ViewholderReviewBinding binding=ViewholderReviewBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Rating rating = items.get(position);
        holder.binding.name.setText(rating.getUserId());
        holder.binding.review.setText(rating.getReview());
        holder.binding.rating.setText(rating.getRating() + "");

        String image = getUserImage(rating);
        if (image.isEmpty()){
            holder.binding.pic.setImageResource(R.drawable.avatar);
        }else {
            Glide.with(context).load(image).into(holder.binding.pic);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public String getUserImage(Rating rating){
        String userImage = "";
        for (User temp : SplashScreenActivity.users) {
            if (temp.getUsername().equals(rating.getUserId())){
                userImage = temp.getImage();
            }
        }
        return userImage;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderReviewBinding binding;
        public Viewholder(ViewholderReviewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
