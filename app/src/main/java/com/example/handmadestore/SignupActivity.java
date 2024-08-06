package com.example.handmadestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivitySignupBinding;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    ArrayList<User> users;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        init();
    }

    private void getData() {
        users = new ArrayList<>();
        databaseManager = new DatabaseManager();
        databaseManager.getUsers(users);
    }

    private void init(){
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String username = binding.edtUsername.getText().toString();
                String password = binding.edtPassword.getText().toString();
                String address = binding.edtAddress.getText().toString();
                boolean isExist = false;
                if(email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                }else {
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            isExist = true;
                            Toast.makeText(SignupActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_LONG).show();
                        }
                    }
                    if (!isExist){
                        User user = new User(email,phone,username,password,address,false);
                        databaseManager.addUser(user);
                        Toast.makeText(SignupActivity.this,"Dang ki thanh cong",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("new_user",user);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}