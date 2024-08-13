package com.example.handmadestore.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.AdminItem.UploadItemActivity;
import com.example.handmadestore.DetailActivity;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.CardAdminItemBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.AdminItemViewHolder>{

    User user;
    ArrayList<Item> items;
    Context context;

    public ItemAdapter(ArrayList<Item> items, User user) {
        this.items = items;
        this.user = user;
    }

    @NonNull
    @Override
    public AdminItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardAdminItemBinding binding = CardAdminItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ItemAdapter.AdminItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminItemViewHolder holder, int position) {
        Item item = items.get(position);
        Glide.with(context).load(item.getPicUrl().get(0)).into(holder.binding.image);
        holder.binding.title.setText(item.getTitle());
        holder.binding.ratingBar.setRating(Math.round(calRating(item) * 10) / 10.0f);
        holder.binding.ratingTxt.setText(Math.round(calRating(item) * 10) / 10.0f + "");

        NumberFormat formatVND = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));

        holder.binding.price.setText(formatVND.format(item.getPrice()));
        holder.binding.sold.setText("Đã bán: "+ item.getSold());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getPriority()){
                    showPopupMenu(view,item);
                }else {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("object", item);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void showPopupMenu(View view, Item item){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.inflate(R.menu.menu_option_items);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify_item:
                        Intent intent = new Intent(context, UploadItemActivity.class);
                        intent.putExtra("item",item);
                        context.startActivity(intent);
                        break;
                    case R.id.delete_item:
                        new androidx.appcompat.app.AlertDialog.Builder(context)
                                .setTitle("Xác nhận thoát")
                                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setCancelable(false);
                                        builder.setView(R.layout.loading_activity);
                                        AlertDialog loading = builder.create();
                                        loading.show();
                                        DatabaseManager databaseManager = new DatabaseManager();
                                        checkCart(item.getId(),databaseManager);
                                        databaseManager.deleteItem(item,loading,context);
                                        ItemAdapter.this.items.remove(item);
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Không", null)
                                .show();
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

    public boolean checkCart(String text,DatabaseManager databaseManager){
        for (User user : LoginActivity.users){
            for (Cart cart : user.getCarts()){
                if (cart.getItem().getId().equals(text)){
                    user.getCarts().remove(cart);
                    databaseManager.addCart(user);
                }
            }
        }
        return false;
    }

    public float calRating(Item item){
        ArrayList<Rating> ratings = new ArrayList<>();
        float ratingsAvg = 0;
        for (Rating temp: LoginActivity.ratings) {
            if(temp.getItemId().equals(item.getId())){
                ratings.add(temp);
            }
        }

        for (Rating temp: ratings) {
            ratingsAvg += temp.getRating();
        }

        return ratingsAvg / ratings.size();
    }

    public class AdminItemViewHolder extends RecyclerView.ViewHolder {

        CardAdminItemBinding binding;

        public AdminItemViewHolder(CardAdminItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
