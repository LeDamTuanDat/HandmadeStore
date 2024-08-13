package com.example.handmadestore.Adapter;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.databinding.ActivityCartBinding;
import com.example.handmadestore.databinding.FragmentCartBinding;
import com.example.handmadestore.databinding.ViewholderCartBinding;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private ArrayList<Cart> carts;
    private ActivityCartBinding binding;
    private boolean isCart;

    public CartAdapter(ArrayList<Cart> carts, ActivityCartBinding binding,boolean isCart) {
        this.carts = carts;
        this.binding = binding;
        this.isCart = isCart;
    }

    public CartAdapter(ArrayList<Cart> carts,boolean isCart){
        this.carts = carts;
        this.isCart = isCart;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        DatabaseManager databaseManager = new DatabaseManager();
        Cart cart = carts.get(position);
        Item item = cart.getItem();
        holder.binding.titleTxt.setText(item.getTitle());

        NumberFormat formatVND = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
        holder.binding.freeEachitem.setText(formatVND.format(item.getPrice()));
        holder.binding.totalEachitem.setText("Tổng: " + formatVND.format(cart.calculatePrice()));
        if (isCart){
            holder.binding.amount.setText(cart.getNumber()+"");
        }else {
            holder.binding.plus.setVisibility(View.GONE);
            holder.binding.minus.setVisibility(View.GONE);
            holder.binding.amount.setVisibility(View.GONE);
            holder.binding.amountTxt.setVisibility(View.VISIBLE);
            holder.binding.delete.setVisibility(View.GONE);
            holder.binding.amountTxt.setText("x" + cart.getNumber());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.image);

        holder.binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.increase();
                holder.binding.amount.setText(cart.getNumber()+"");
                holder.binding.freeEachitem.setText(formatVND.format(item.getPrice()));
                holder.binding.totalEachitem.setText("Tổng: " + formatVND.format(cart.calculatePrice()));
                databaseManager.addCart(MainActivity.currentUser);
                calculatorCart();
            }
        });

        holder.binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getNumber() != 1){
                    cart.decrease();
                    holder.binding.amount.setText(cart.getNumber()+"");
                    holder.binding.freeEachitem.setText(formatVND.format(item.getPrice()));
                    holder.binding.totalEachitem.setText("Tổng: " + formatVND.format(cart.calculatePrice()));
                    calculatorCart();
                    databaseManager.addCart(MainActivity.currentUser);
                    notifyDataSetChanged();
                }else {
                    confirmDelete(cart);
                }
            }
        });

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete(cart);
            }
        });
    }

    public void confirmDelete(Cart cart){
        new AlertDialog.Builder(binding.getRoot().getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xoá sản phẩm khỏi giỏ hàng ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        carts.remove(cart);
                        if(carts.size() == 0) {
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.acceptOrder.setVisibility(View.GONE);
                        }else {
                            calculatorCart();
                        }
                        DatabaseManager databaseManager = new DatabaseManager();
                        databaseManager.addCart(MainActivity.currentUser);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void calculatorCart() {
        long delivery = 15000;
        long itemTotal = getTotalPrice();
        long total = itemTotal+delivery;

        NumberFormat formatVND = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
        binding.totalPriceOfItem.setText(formatVND.format(itemTotal));
        binding.delivery.setText(formatVND.format(delivery));
        binding.total.setText(formatVND.format(total));
    }

    protected long getTotalPrice(){
        long totalPrice = 0;
        for (int i = 0 ; i < carts.size() ; i++){
            Cart cart = carts.get(i);
            totalPrice += cart.calculatePrice();
        }
        return totalPrice;
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;
        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
