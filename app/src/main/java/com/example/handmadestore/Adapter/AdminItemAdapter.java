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
import com.example.handmadestore.AdminItem.UploadItemActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.CardAdminItemBinding;

import java.util.ArrayList;

public class AdminItemAdapter extends RecyclerView.Adapter<AdminItemAdapter.AdminItemViewHolder>{

    ArrayList<Item> items;
    Context context;

    public AdminItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AdminItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardAdminItemBinding binding = CardAdminItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new AdminItemAdapter.AdminItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminItemViewHolder holder, int position) {
        Item item = items.get(position);
        Glide.with(context).load(item.getPicUrl().get(0)).into(holder.binding.image);
        holder.binding.title.setText(item.getTitle());
        holder.binding.ratingBar.setRating(item.getRating());
        holder.binding.ratingTxt.setText("("+item.getRating()+")");
        holder.binding.price.setText("đ"+item.getPrice());
        holder.binding.sold.setText("Đã bán: "+ item.getSold());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view,item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void showPopupMenu(View view, Item item){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.inflate(R.menu.menu_option);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify:
                        Intent intent = new Intent(context, UploadItemActivity.class);
                        intent.putExtra("item",item);
                        context.startActivity(intent);
                        break;
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setView(R.layout.loading_activity);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        DatabaseManager databaseManager = new DatabaseManager();
                        databaseManager.deleteItem(item,dialog,AdminItemAdapter.this,context);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }


    public void setResultAfterFiltered(ArrayList<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public class AdminItemViewHolder extends RecyclerView.ViewHolder {

        CardAdminItemBinding binding;

        public AdminItemViewHolder(CardAdminItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
