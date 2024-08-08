package com.example.handmadestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.handmadestore.Object.Category;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.Banner;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.Order;
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
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        init();
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        users = new ArrayList<>();
        banners = new ArrayList<>();
        categories = new ArrayList<>();
        items = new ArrayList<>();
        orders = new ArrayList<>();
        databaseManager = new DatabaseManager();
        databaseManager.getUsers(users);
        databaseManager.getBanner(banners);
        databaseManager.getCategory(categories);
        databaseManager.getItems(items);
    }

    private void init(){
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.edtUsernameLg.getText().toString();
                String password = binding.edtPasswordLg.getText().toString();
                boolean isExist = false;
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        isExist = true;
                        if(user.getPassword().equals(password)){
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
                        }else {
                            Toast.makeText(LoginActivity.this,"Mật khẩu không chính xác",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if (!isExist){
                    Toast.makeText(LoginActivity.this,"Tài khoản không tồn tại",Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 14){
            if(resultCode == RESULT_OK){
                User user = (User) data.getSerializableExtra("new_user");
                if (user != null){
                    binding.edtUsernameLg.setText(user.getUsername());
                    binding.edtPasswordLg.setText(user.getPassword());
                }
            }
        }
    }
}