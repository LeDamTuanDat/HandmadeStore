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
import com.example.handmadestore.User.EditProfileActivity;
import com.example.handmadestore.Database.DatabaseManager;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.CardUserBinding;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    ArrayList<User> users;
    Context context;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardUserBinding binding = CardUserBinding.inflate(LayoutInflater.from(context),parent,false);
        return new UserAdapter.UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = users.get(position);
        if (!user.getImage().isEmpty()){
            Glide.with(context).load(user.getImage()).into(holder.binding.image);
        }else {
            holder.binding.image.setImageResource(R.drawable.avatar);
        }
        holder.binding.username.setText("Tài khoản: " + user.getUsername());
        holder.binding.realName.setText("Họ tên: " + user.getRealname());
        holder.binding.email.setText("Email: " + user.getEmail());
        holder.binding.phone.setText("Điện thoại: " + user.getPhone());
        holder.binding.address.setText("Địa chỉ: " +user.getAddress());
        if(user.getPriority()){
            holder.binding.priority.setText("Loại tài khoản: Quản trị viên");
        }else {
            holder.binding.priority.setText("Loại tài khoản: Người dùng");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view,user);
            }
        });
    }

    public void showPopupMenu(View view, User user){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.inflate(R.menu.menu_option_user);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify_user:
                        Intent intent = new Intent(context, EditProfileActivity.class);
                        intent.putExtra("user",user);
                        context.startActivity(intent);
                        break;
                    case R.id.delete_user:
                        new AlertDialog.Builder(context)
                                .setTitle("Xác nhận xóa")
                                .setMessage("Bạn có chắc chắn muốn xóa người dùng không?")
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setCancelable(false);
                                        builder.setView(R.layout.loading_activity);
                                        AlertDialog loading = builder.create();

                                        DatabaseManager databaseManager = new DatabaseManager();

                                        databaseManager.deleteUser(user,loading,context);
                                        UserAdapter.this.users.remove(user);
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


    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setResultAfterFiltered(ArrayList<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CardUserBinding binding;

        public UserViewHolder(CardUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
