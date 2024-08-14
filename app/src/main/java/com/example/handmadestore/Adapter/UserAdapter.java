package com.example.handmadestore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.handmadestore.Object.User;
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
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CardUserBinding binding;

        public UserViewHolder(CardUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
