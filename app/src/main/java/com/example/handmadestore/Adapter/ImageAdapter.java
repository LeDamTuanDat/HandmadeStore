package com.example.handmadestore.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.databinding.CustomSingleImageBinding;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;
    private Context context;
    CountOfImagesWhenRemoved countOfImagesWhenRemoved;

    public ImageAdapter(ArrayList<Uri> uriArrayList,CountOfImagesWhenRemoved countOfImagesWhenRemoved) {
        this.uriArrayList = uriArrayList;
        this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        CustomSingleImageBinding binding = CustomSingleImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, countOfImagesWhenRemoved);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(uriArrayList.get(position)).into(holder.binding.image);

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                countOfImagesWhenRemoved.clicked(uriArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomSingleImageBinding binding;
        CountOfImagesWhenRemoved countOfImagesWhenRemoved;

        public ViewHolder(CustomSingleImageBinding binding, CountOfImagesWhenRemoved countOfImagesWhenRemoved) {
            super(binding.getRoot());
            this.binding = binding;
            this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
        }
    }

    public interface CountOfImagesWhenRemoved{
        void clicked(int getSize);
    }
}
