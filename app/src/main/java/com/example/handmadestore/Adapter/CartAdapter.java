package com.example.handmadestore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.databinding.FragmentCartBinding;
import com.example.handmadestore.databinding.ViewholderCartBinding;


import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private ArrayList<Cart> carts;
    private FragmentCartBinding binding;
    private boolean isCart;

    public CartAdapter(ArrayList<Cart> carts, FragmentCartBinding binding,boolean isCart) {
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
        holder.binding.freeEachitem.setText(item.getPrice()+"vnd");
        holder.binding.totalEachitem.setText("Tổng: " + Math.round(cart.calculatePrice())+"vnd");
        if (isCart){
            holder.binding.numberItemTxt.setText(cart.getNumber()+"");
        }else {
            holder.binding.plusCart.setVisibility(View.GONE);
            holder.binding.minusCart.setVisibility(View.GONE);
            holder.binding.numberItemTxt.setText("Số lượng: " + cart.getNumber());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.binding.plusCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.increase();
                holder.binding.numberItemTxt.setText(cart.getNumber()+"");
                holder.binding.totalEachitem.setText("Tổng: " +Math.round(cart.calculatePrice())+"vnd");
                databaseManager.addCart(MainActivity.currentUser);
                calculatorCart();
            }
        });

        holder.binding.minusCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getNumber() != 1){
                    cart.decrease();
                    holder.binding.numberItemTxt.setText(cart.getNumber()+"");
                    holder.binding.totalEachitem.setText("Tổng: " + Math.round(cart.calculatePrice())+"vnd");
                    calculatorCart();
                }else {
                    carts.remove(cart);
                    if(carts.size() == 0) {
                        binding.emptyTxt.setVisibility(View.VISIBLE);
                        binding.acceptOrder.setVisibility(View.GONE);
                    }else {
                        calculatorCart();
                    }
                }
                databaseManager.addCart(MainActivity.currentUser);
                notifyDataSetChanged();
            }
        });
    }

    private void calculatorCart() {
        long delivery = 15000;
        long itemTotal = getTotalPrice();
        long total = itemTotal+delivery;

        binding.totalPriceOfItem.setText(itemTotal+"vnd");
        binding.delivery.setText(delivery+"vnd");
        binding.total.setText(total+"vnd");
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
