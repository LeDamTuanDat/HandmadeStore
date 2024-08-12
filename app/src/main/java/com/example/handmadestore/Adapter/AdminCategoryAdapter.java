package com.example.handmadestore.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.AdminCategory.UploadCategoryActivity;
import com.example.handmadestore.AdminItem.AdminItemActivity;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.CardAdminCategoryBinding;

import java.util.ArrayList;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.AdminCategoryViewHolder> {

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
                if (MainActivity.currentUser != null){
                    Intent intent = new Intent(context, AdminItemActivity.class);
                    intent.putExtra("category",category);
                    context.startActivity(intent);
                }else {
                    showPopupMenu(view,category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void showPopupMenu(View view, Category category){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.inflate(R.menu.menu_option_categories);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify_category:
                        Intent intent = new Intent(context, UploadCategoryActivity.class);
                        intent.putExtra("category",category);
                        context.startActivity(intent);
                        break;
                    case R.id.delete_category:
                        if (checkItem(category.getId())){
                            Toast.makeText(context, "Không thể xoá danh mục", Toast.LENGTH_SHORT).show();
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setCancelable(false);
                            builder.setView(R.layout.loading_activity);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            DatabaseManager databaseManager = new DatabaseManager();
                            databaseManager.deleteCategory(category, dialog, context);
                            AdminCategoryAdapter.this.items.remove(category);
                            notifyDataSetChanged();
                        }
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void setReSultAfterSearch(ArrayList<Category> categories){
        this.items = categories;
        notifyDataSetChanged();
    }
    
    public boolean checkItem(String text){
        for (Item item : LoginActivity.items) {
            if(item.getCategoryId().equals(text)){
                return true;
            }
        }
        return false;
    }

    public class AdminCategoryViewHolder extends RecyclerView.ViewHolder {
        CardAdminCategoryBinding binding;

        public AdminCategoryViewHolder(CardAdminCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}