package com.example.handmadestore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.AdminItem.AdminItemActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.databinding.ViewholderCategoryBinding;


import java.util.ArrayList;

public class MiniCategoryAdapter extends RecyclerView.Adapter<MiniCategoryAdapter.Viewholder> {
    private ArrayList<Category> categories;
    private Context context;

    public MiniCategoryAdapter(ArrayList<Category> categories) {
        if (categories.size() <= 5){
            this.categories = categories;
        }else {
            ArrayList<Category> sub = new ArrayList<>();
            for (int i = 0; i < categories.size(); i++) {
                sub.add(categories.get(i));
                if (i == 4){
                    break;
                }
            }
            this.categories = sub;
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Category category = categories.get(position);
        holder.binding.title.setText(categories.get(position).getTitle());

        Glide.with(context)
                .load(categories.get(position).getPicUrl())
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminItemActivity.class);
                intent.putExtra("category",category);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderCategoryBinding binding;

        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
