package com.example.handmadestore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.Object.ReviewDomain;
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

//        Glide.with(context)
//                .load(items.get(position).getPicUrl())
//                .transform(new GranularRoundedCorners(100, 100, 100, 100))
//                .into(holder.binding.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderReviewBinding binding;
        public Viewholder(ViewholderReviewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
