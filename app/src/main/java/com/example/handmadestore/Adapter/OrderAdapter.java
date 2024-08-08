package com.example.handmadestore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handmadestore.Object.Order;
import com.example.handmadestore.OrderDetail;
import com.example.handmadestore.databinding.CardOrderItemBinding;
import com.example.handmadestore.databinding.FragmentOrderBinding;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<Order> orders;
    private Context context;

    public OrderAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CardOrderItemBinding binding = CardOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.binding.orderId.setText("ID: " + order.getKeyId());
        holder.binding.name.setText("Người nhận: "+ order.getName());
        holder.binding.phone.setText("SĐT: " + order.getPhone());
        holder.binding.address.setText("Địa chỉ: " + order.getAddress());
        holder.binding.status.setText("Trạng thái: " + order.getStatus());

        String paymentMethod = (order.getZaloPayment() ? "ZaloPay" : "Tiền mặt");
        holder.binding.paymentMethod.setText("Hình thức thanh toán: " + paymentMethod);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetail.class);
                intent.putExtra("order",order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setResultAfterFiltered(ArrayList<Order> orders){
        this.orders = orders;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardOrderItemBinding binding;
        public ViewHolder(CardOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
