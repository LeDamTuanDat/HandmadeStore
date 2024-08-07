package com.example.handmadestore;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.handmadestore.Adapter.AdapterViewPagerForItem;
import com.example.handmadestore.Fragment.DescriptionFragment;
import com.example.handmadestore.Fragment.ReviewFragment;
import com.example.handmadestore.Fragment.SoldFragment;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.Cart;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.databinding.ActivityDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

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

    private void initBanners() {
       ArrayList<Banner> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new Banner(object.getPicUrl().get(i)));
        }

        ArrayList<SlideModel> imageList = new ArrayList<>();
        for ( Banner banner : sliderItems) {
            imageList.add(new SlideModel(banner.getUrl(), ScaleTypes.FIT));
        }
        binding.imageSlider.setImageList(imageList);
    }

    private void getBundles(){
        object = (Item) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText(object.getPrice() + "đ");
        binding.ratingBar.setRating(Math.round(calRating() * 10) / 10.0f);
        binding.ratingTxt.setText(Math.round(calRating() * 10) / 10.0f+ "/5");

        binding.addtoCart.setOnClickListener(view -> {
            if(checkCart(object)) {
                ArrayList<Cart> carts = MainActivity.currentUser.getCarts();
                Cart cart = new Cart(object);
                carts.add(cart);
                databaseManager.addCart(MainActivity.currentUser);
                Toast.makeText(DetailActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(DetailActivity.this, "Đã có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
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
            if (temp.getTitle().equals(item.getTitle())) {
                return false;
            }
        }
        return true;
    }

    public float calRating(){
        ArrayList<Rating> ratings = new ArrayList<>();
        float ratingsAvg = 0;
        for (Rating temp: LoginActivity.ratings) {
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