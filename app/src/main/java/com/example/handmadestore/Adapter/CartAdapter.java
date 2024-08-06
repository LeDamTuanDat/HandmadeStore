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
    private DatabaseManager databaseManager;

    public CartAdapter(ArrayList<Cart> carts, FragmentCartBinding binding) {
        this.carts = carts;
        this.binding = binding;
        databaseManager = new DatabaseManager();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Cart cart = carts.get(position);
        Item item = cart.getItem();
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.freeEachitem.setText(item.getPrice()+"vnd");
        holder.binding.totalEachitem.setText(Math.round(cart.calculatePrice())+"vnd");
        holder.binding.numberItemTxt.setText(cart.getNumber()+"");

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
                holder.binding.totalEachitem.setText(Math.round(cart.calculatePrice())+"vnd");
                databaseManager.addCart(MainActivity.currentUser);
                calculatorCart();
            }
        });

        holder.binding.minusCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getNumber() != 1){
                    cart.decrease();
                    holder.binding.numberItemTxt.setText(cart.getNumber()+"");
                    holder.binding.totalEachitem.setText(Math.round(cart.calculatePrice())+"vnd");
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
        double percentTax = 0.02;
        double delivery = 10;
        double itemTotal = Math.round(getTotalPrice() *100)/100;
        double tax = Math.round((itemTotal * percentTax * 100.0))/100.0;

        double total = itemTotal+tax;

        binding.totalFreeTxt.setText(itemTotal+"vnd");
        binding.taxTxt.setText(tax+"vnd");
        binding.deliveryTxt.setText(delivery+"vnd");
        binding.totalTxt.setText(total+"vnd");
    }

    protected double getTotalPrice(){
        double totalPrice = 0;
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
