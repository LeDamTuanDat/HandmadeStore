package com.example.handmadestore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.handmadestore.DetailActivity;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.databinding.ViewholderPopListBinding;


import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Viewholder> {
    ArrayList<Item> items;
    Context context;

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.title.setText(items.get(position).getTitle());
//        holder.binding.reviewTxt.setText(""+items.get(position).getReview());
        holder.binding.PriceTxt.setText("đ"+items.get(position).getPrice());
//        holder.binding.ratingTxt.setText("("+items.get(position).getRating()+")");
        holder.binding.oldPriceTxt.setText("đ"+items.get(position).getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.binding.ratingBar.setRating((float) items.get(position).getRating());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = new RequestOptions().transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pictr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderPopListBinding binding;

        public  Viewholder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
