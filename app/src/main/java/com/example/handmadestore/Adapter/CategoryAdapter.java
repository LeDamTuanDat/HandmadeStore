package com.example.handmadestore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.databinding.ViewholderCategoryBinding;


import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private ArrayList<Category> items;
    private Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        if (items.size() <= 5){
            this.items = items;
        }else {
            ArrayList<Category> sub = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                sub.add(items.get(i));
                if (i == 4){
                    break;
                }
            }
            this.items = sub;
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
        holder.binding.title.setText(items.get(position).getTitle());

        Glide.with(context)
                .load(items.get(position).getPicUrl())
                .into(holder.binding.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderCategoryBinding binding;

        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
