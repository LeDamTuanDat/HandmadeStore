package com.example.handmadestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.databinding.ActivityRatingBinding;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    ActivityRatingBinding binding;
    ArrayList<Item> items;
    ArrayAdapter<Item> adapter;
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getItem();
        setSpinner();
        handleSave();
    }

    public void getItem(){
        items = new ArrayList<>();
        ArrayList<Cart> carts = (ArrayList<Cart>) getIntent().getSerializableExtra("item");
        for (Cart cart : carts) {
            items.add(cart.getItem());
        }
    }

    public void setSpinner(){
        adapter = new ArrayAdapter<>(this,R.layout.spinner_item,items);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();
                item = (Item) adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void handleSave(){
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = binding.review.getText().toString();
                float ratingValue =  binding.ratingBar.getRating();
                if(ratingValue == 0){
                    Toast.makeText(RatingActivity.this, "Vui lòng chọn đánh giá", Toast.LENGTH_SHORT).show();
                }else if(review.isEmpty()){
                    Toast.makeText(RatingActivity.this, "Vui lòng điền nhận xét", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseManager databaseManager = new DatabaseManager();
                    Rating rating = new Rating(item.getId(),MainActivity.currentUser.getUsername(),review,ratingValue);
                    databaseManager.addRating(rating);
                    Toast.makeText(RatingActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    items.remove(item);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}