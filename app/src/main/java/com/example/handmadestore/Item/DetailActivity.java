package com.example.handmadestore.Item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.handmadestore.Adapter.AdapterViewPagerForItem;
import com.example.handmadestore.Order.CartActivity;
import com.example.handmadestore.Fragment.DescriptionFragment;
import com.example.handmadestore.Fragment.ReviewFragment;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Database.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.ActivityDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    DatabaseManager databaseManager;
    ActivityDetailBinding binding;
    private Item object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseManager = new DatabaseManager();
        getBundles();
        initBanners();
        setupViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBundles();
        initBanners();
        setupViewPager();
        binding.amount.setText("1");
    }

    private void initBanners() {
       ArrayList<Banner> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new Banner(object.getPicUrl().get(i)));
        }

        ArrayList<SlideModel> imageList = new ArrayList<>();
        for ( Banner banner : sliderItems) {
            imageList.add(new SlideModel(banner.getUrl(), ScaleTypes.CENTER_CROP));
        }
        binding.imageSlider.setImageList(imageList);
    }

    private void getBundles(){
        object = (Item) getIntent().getSerializableExtra("object");
        binding.title.setText(object.getTitle());
        binding.inventory.setText("Kho: " + object.getInventory());

        NumberFormat formatVND = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
        binding.price.setText(formatVND.format(object.getPrice()));
        binding.sold.setText("Đã bán: " + object.getSold());
        binding.ratingBar.setRating(Math.round(calRating() * 10) / 10.0f);
        binding.ratingTxt.setText(Math.round(calRating() * 10) / 10.0f+ "/5");


        if (!checkCart(object)){
            binding.layout.setVisibility(View.GONE);
            binding.addtoCart.setText("Kiểm tra giỏ hàng");
        }else {
            binding.layout.setVisibility(View.VISIBLE);
            binding.addtoCart.setText("Thêm vào giỏ hàng");
        }
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = binding.amount.getText().toString();
                binding.amount.clearFocus();
                if (amount.isEmpty()){
                    binding.amount.setText("1");
                }else {
                    int i = Integer.parseInt(amount) + 1;
                    binding.amount.setText("" + i);
                }
            }
        });

        binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = binding.amount.getText().toString();
                binding.amount.clearFocus();
                if (amount.isEmpty()){
                    binding.amount.setText("1");
                }else {
                    int i = Integer.parseInt(amount);
                    if (i > 1){
                        binding.amount.setText("" + (i-1));
                    }
                }
            }
        });

        binding.addtoCart.setOnClickListener(view -> {
            if(checkCart(object)) {
                String amount = binding.amount.getText().toString();
                if (amount.isEmpty() || Integer.parseInt(amount) == 0){
                    Toast.makeText(DetailActivity.this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                }else {
                    if (Integer.parseInt(amount) > object.getInventory()){
                        Toast.makeText(DetailActivity.this, "Số lượng nhiều hơn tồn kho", Toast.LENGTH_SHORT).show();
                    }else {
                        ArrayList<Cart> carts = MainActivity.currentUser.getCarts();
                        Cart cart = new Cart(object, Integer.parseInt(amount));
                        carts.add(cart);
                        databaseManager.addCart(MainActivity.currentUser);
                        Toast.makeText(DetailActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                        startActivity(intent);
                    }
                }
            }else {
                Intent intent = new Intent(DetailActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        binding.back.setOnClickListener(view -> finish());
    }

    private void setupViewPager(){
        AdapterViewPagerForItem adapter = new AdapterViewPagerForItem(this);
        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();

        bundle1.putString("description", object.getDescription());
        bundle2.putSerializable("item",object);

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);

        adapter.addFrag(tab1, "Miêu tả");
        adapter.addFrag(tab2, "Đánh giá");

        binding.viewpager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText(adapter.getPageTitle(position));
                        break;
                    case 1:
                        tab.setText(adapter.getPageTitle(position));
                        break;
                }
            }
        }).attach();
    }


    protected boolean checkCart(Item item) {
        ArrayList<Cart> carts = MainActivity.currentUser.getCarts();
        for (int i = 0; i < carts.size(); i++) {
            Item temp = carts.get(i).getItem();
            if (temp.getId().equals(item.getId())) {
                return false;
            }
        }
        return true;
    }

    public float calRating(){
        ArrayList<Rating> ratings = new ArrayList<>();
        float ratingsAvg = 0;
        for (Rating temp: SplashScreenActivity.ratings) {
            if(temp.getItemId().equals(object.getId())){
                ratings.add(temp);
            }
        }

        for (Rating temp: ratings) {
            ratingsAvg += temp.getRating();
        }

        return ratingsAvg / ratings.size();
    }
}