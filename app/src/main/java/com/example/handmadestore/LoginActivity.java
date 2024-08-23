package com.example.handmadestore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.Object.Rating;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    public static ArrayList<User> users;
    public static ArrayList<Banner> banners;
    public static ArrayList<Category> categories;
    public static ArrayList<Item> items;
    public static ArrayList<Order> orders;
    public static ArrayList<Rating> ratings;
    User user;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        handleEvent();
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onResume() {
        init();
        super.onResume();
    }

    private void init() {
        users = new ArrayList<>();
        banners = new ArrayList<>();
        categories = new ArrayList<>();
        items = new ArrayList<>();
        orders = new ArrayList<>();
        ratings = new ArrayList<>();
        databaseManager = new DatabaseManager();
        databaseManager.getUsers(users);
        databaseManager.getBanner(banners);
        databaseManager.getCategory(categories);
        databaseManager.getItems(items);
        databaseManager.getRatings(ratings);
    }

    private void handleEvent(){
        disableSpace();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();
                if (check(username,password)){
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    if (user.getPriority()){
                        databaseManager.getAllOrder(orders);
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }else {
                        databaseManager.getOrder(username,orders);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, 14);
            }
        });
        binding.username.addTextChangedListener(new ClearError(binding.username,binding.usernameLayout));
        binding.password.addTextChangedListener(new ClearError(binding.password,binding.passwordLayout));
    }

    private boolean check(String username, String password){
        boolean check = true;
        if (username.isEmpty()){
            binding.usernameLayout.setError("Không được để trống");
            check = false;
        }
        if (password.isEmpty()){
            binding.passwordLayout.setError("Không được để trống");
            check = false;
        }

        if (check){
            boolean isExist = false;
            for (User temp : users) {
                if (temp.getUsername().equals(username)) {
                    isExist = true;
                    if(!temp.getPassword().equals(password)){
                        binding.passwordLayout.setError("Mật khẩu không chính xác");
                        check = false;
                    }else {
                        user = temp;
                    }
                    break;
                }
            }
            if (!isExist){
                binding.usernameLayout.setError("Tài khoản không tồn tại");
                check = false;
            }
        }

        return check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 14){
            if(resultCode == RESULT_OK){
                User user = (User) data.getSerializableExtra("new_user");
                if (user != null){
                    binding.username.setText(user.getUsername());
                    binding.password.setText(user.getPassword());
                }
            }
        }
    }

    private void disableSpace(){
        binding.username.addTextChangedListener(new NoWhitespaceTextWatcher(binding.username));
        binding.password.addTextChangedListener(new NoWhitespaceTextWatcher(binding.password));
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Xác nhận thoát")
                    .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }
    };
}