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

import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    User user;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleEvent();
        this.getOnBackPressedDispatcher().addCallback(this, callback);
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
                    databaseManager = new DatabaseManager();
                    if (user.getPriority()){
                        databaseManager.getAllOrder(SplashScreenActivity.orders);
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }else {
                        databaseManager.getOrder(username,SplashScreenActivity.orders);
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
        clearError();
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
            for (User temp : SplashScreenActivity.users) {
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

    private void clearError(){
        binding.username.addTextChangedListener(new ClearError(binding.username,binding.usernameLayout));
        binding.password.addTextChangedListener(new ClearError(binding.password,binding.passwordLayout));
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