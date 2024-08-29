package com.example.handmadestore;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.OrderAdapter;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.databinding.ActivityAdminOrderBinding;

import java.util.ArrayList;

public class AdminOrderActivity extends AppCompatActivity {
    ActivityAdminOrderBinding binding;
    OrderAdapter orderAdapter;
    ArrayList<Order> resultAfterFiltered = new ArrayList<>();
    ArrayList<Order> resultAfterSearch = new ArrayList<>();
    String[] status = {"Tất cả","Chờ xác nhận","Đã xác nhận","Đang giao hàng","Đã giao"};
    ArrayAdapter<String> statusAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initOrderList();
        initSpinner();
        handleFiltered();
        handleSearch();
        handleExit();
    }

    @Override
    protected void onResume() {
        initOrderList();
        int index = Math.toIntExact(binding.spinner.getSelectedItemId());
        resultAfterFiltered(status[index]);
        binding.search.setQuery("",false);
        binding.search.clearFocus();
        super.onResume();
    }

    public void initSpinner(){
        statusAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,status);
        statusAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinner.setAdapter(statusAdapter);
    }

    public void initOrderList(){
        orderAdapter = new OrderAdapter(SplashScreenActivity.orders);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminOrderActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(orderAdapter);
        checkOrders(SplashScreenActivity.orders);
    }

    private void checkOrders(ArrayList<Order> orders){
        if (orders.size() == 0){
            binding.notification.setVisibility(View.VISIBLE);
        }else {
            binding.notification.setVisibility(View.GONE);
        }
    }

    public void handleFiltered(){
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resultAfterFiltered(status[i]);
                binding.search.setQuery("",false);
                binding.search.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void resultAfterFiltered(String text){
        resultAfterFiltered.clear();
        if (text.equals("Tất cả")){
            orderAdapter.setResultAfterFiltered(SplashScreenActivity.orders);
        }else {
            for (Order order : SplashScreenActivity.orders) {
                if(order.getStatus().equals(text)){
                    resultAfterFiltered.add(order);
                }
            }
            orderAdapter.setResultAfterFiltered(resultAfterFiltered);
            checkOrders(resultAfterFiltered);
        }
    }

    public void handleSearch(){
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resultAfterSearch(newText);
                return true;
            }
        });
    }

    public void resultAfterSearch(String text){
        resultAfterSearch.clear();
        ArrayList<Order> temp;
        if (resultAfterFiltered.size() == 0){
            temp = new ArrayList<>(SplashScreenActivity.orders);
        }else {
            temp = new ArrayList<>(resultAfterFiltered);
        }
        for (Order order : temp) {
            String normalId = order.getKeyId().toLowerCase();
            String normalText = text.toLowerCase();
            if(normalId.contains(normalText)){
                resultAfterSearch.add(order);
            }
        }
        orderAdapter.setResultAfterFiltered(resultAfterSearch);
        checkOrders(resultAfterSearch);
    }

    public void handleExit(){
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}