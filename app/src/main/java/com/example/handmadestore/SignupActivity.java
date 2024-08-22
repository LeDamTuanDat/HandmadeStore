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
        init();
        handleEvent();
        clearError();
    }

    private void init() {
        users = new ArrayList<>();
        databaseManager = new DatabaseManager();
        databaseManager.getUsers(users);
    }

    private void handleEvent(){
        disableSpace();
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String phone = binding.phone.getText().toString();
                String realname = binding.realname.getText().toString();
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();
                String address = binding.address.getText().toString();
                if (check(email,phone,realname,username,password,address)){
                    User user = new User(email,phone,username,password,realname,address,false);
                    databaseManager.addUser(user);
                    Toast.makeText(SignupActivity.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("new_user",user);
                    setResult(RESULT_OK, intent);
                    finish();
                }
//                boolean isExist = false;
//                if(email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty() || realname.isEmpty()){
//                    Toast.makeText(SignupActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
//                } else if (!email.contains("@gmail.com")) {
//                    Toast.makeText(SignupActivity.this,"Vui lòng nhập đúng định dạng email",Toast.LENGTH_LONG).show();
//                } else if (phone.length() < 10 || !phone.startsWith("0")) {
//                    Toast.makeText(SignupActivity.this,"Vui lòng nhập đúng định dạng số điện thoại",Toast.LENGTH_LONG).show();
//                } else if (password.length() < 6) {
//                    Toast.makeText(SignupActivity.this,"Mật khẩu phải tối thiểu 6 ký tự",Toast.LENGTH_LONG).show();
//                }
//                else {
//                    for (User user : users) {
//                        if (user.getUsername().equals(username)) {
//                            isExist = true;
//                            Toast.makeText(SignupActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    if (!isExist){
//                        User user = new User(email,phone,username,password,realname,address,false);
//                        databaseManager.addUser(user);
//                        Toast.makeText(SignupActivity.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent();
//                        intent.putExtra("new_user",user);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                }
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

    private boolean check(String email,String phone,String realname,String username,String password,String address){
        boolean check = true;

        if (email.isEmpty()){
            binding.emailLayout.setError("Không được để trống");
            check = false;
        }else if (!email.contains("@gmail.com")){
            binding.emailLayout.setError("Chưa đúng định dạng");
            check = false;
        }

        if (phone.isEmpty()){
            binding.phoneLayout.setError("Không được để trống");
            check = false;
        }else if (phone.length() < 10 || !phone.startsWith("0")){
            binding.phoneLayout.setError("Chưa đúng định dạng");
            check = false;
        }

        if (realname.isEmpty()){
            binding.realnameLayout.setError("Không được để trống");
            check = false;
        }

        if (username.isEmpty()){
            binding.usernameLayout.setError("Không được để trống");
            check = false;
        }

        if (password.isEmpty()){
            binding.passwordLayout.setError("Không được để trống");
            check = false;
        } else if (password.length() < 6) {
            binding.passwordLayout.setError("Tối thiểu 6 ký tự");
            check = false;
        }

        if (address.isEmpty()){
            binding.addressLayout.setError("Không được để trống");
            check = false;
        }

        if (check){
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    binding.usernameLayout.setError("Tài khoản đã tồn tại");
                    check = false;
                    break;
                }
            }
        }

        return check;
    }

    private void clearError(){
        binding.email.addTextChangedListener(new ClearError(binding.email,binding.emailLayout));
        binding.phone.addTextChangedListener(new ClearError(binding.phone,binding.phoneLayout));
        binding.realname.addTextChangedListener(new ClearError(binding.realname,binding.realnameLayout));
        binding.username.addTextChangedListener(new ClearError(binding.username,binding.usernameLayout));
        binding.password.addTextChangedListener(new ClearError(binding.password,binding.passwordLayout));
        binding.address.addTextChangedListener(new ClearError(binding.address,binding.addressLayout));
    }

    private void disableSpace(){
        binding.email.addTextChangedListener(new NoWhitespaceTextWatcher(binding.email));
        binding.phone.addTextChangedListener(new NoWhitespaceTextWatcher(binding.phone));
        binding.username.addTextChangedListener(new NoWhitespaceTextWatcher(binding.username));
        binding.password.addTextChangedListener(new NoWhitespaceTextWatcher(binding.password));
    }

}