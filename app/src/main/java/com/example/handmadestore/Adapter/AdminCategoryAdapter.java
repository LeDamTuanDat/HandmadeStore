package com.example.handmadestore.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.AdminCategory.UploadCategoryActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.CardAdminCategoryBinding;

import java.util.ArrayList;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryViewHolder> {

    private ArrayList<Category> items;
    private Context context;

    public AdminCategoryAdapter(ArrayList<Category> items){
        this.items = items;
    }

    @NonNull
    @Override
    public AdminCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardAdminCategoryBinding binding = CardAdminCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new AdminCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryViewHolder holder, int position) {
        holder.binding.title.setText(items.get(position).getTitle());
        Glide.with(context).load(items.get(position).getPicUrl()).into(holder.binding.image);

        Category category = items.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view,category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void showPopupMenu(View view, Category category){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.inflate(R.menu.menu_option);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify:
                        Intent intent = new Intent(context, UploadCategoryActivity.class);
                        intent.putExtra("category",category);
                        context.startActivity(intent);
                        break;
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setView(R.layout.loading_activity);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        DatabaseManager databaseManager = new DatabaseManager();
                        databaseManager.deleteCategory(category,dialog,AdminCategoryAdapter.this,context);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void setFilteredCategory(ArrayList<Category> categories){
        this.items = categories;
        notifyDataSetChanged();
    }

}

class AdminCategoryViewHolder extends RecyclerView.ViewHolder {
    CardAdminCategoryBinding binding;

    public AdminCategoryViewHolder(CardAdminCategoryBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}