package com.example.handmadestore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityCreateUserForAdminBinding;

public class CreateUserForAdminActivity extends AppCompatActivity {
    ActivityCreateUserForAdminBinding binding;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUserForAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setImage();
        deleteImage();
        disableSpace();
        save();
        back();
    }

    public void init(){
        binding.delete.setVisibility(View.GONE);
    }

    private void setImage(){
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImg = new Intent(Intent.ACTION_PICK);
                pickImg.setType("image/*");
                activityResultLauncher.launch(pickImg);
            }
        });
    }

    private void deleteImage(){
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri = null;
                binding.image.setImageResource(R.drawable.avatar);
                binding.delete.setVisibility(View.GONE);
            }
        });
    }


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {//Nếu đã lấy ảnh
                        Intent data = result.getData();
                        uri = data.getData();
                        binding.image.setImageURI(uri);
                        binding.delete.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(CreateUserForAdminActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void disableSpace(){
        binding.edtEmail.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtEmail));
        binding.edtPassword.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtPassword));
        binding.edtUsername.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtUsername));
    }

    private void save(){
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String realname = binding.edtRealName.getText().toString();
                String username = binding.edtUsername.getText().toString();
                String password = binding.edtPassword.getText().toString();
                String address = binding.edtAddress.getText().toString();
                boolean isExist = false;

                if(email.isEmpty() || phone.isEmpty() || realname.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()){
                    Toast.makeText(CreateUserForAdminActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                } else if (!email.contains("@gmail.com")) {
                    Toast.makeText(CreateUserForAdminActivity.this,"Vui lòng nhập đúng định dạng email",Toast.LENGTH_LONG).show();
                } else if (phone.length() < 10 || !phone.startsWith("0")) {
                    Toast.makeText(CreateUserForAdminActivity.this,"Vui lòng nhập đúng định dạng số điện thoại",Toast.LENGTH_LONG).show();
                } else if (password.length() < 6){
                    Toast.makeText(CreateUserForAdminActivity.this,"Mật khẩu phải tối thiểu 6 ký tự",Toast.LENGTH_LONG).show();
                } else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
                    builder.setCancelable(false);
                    builder.setView(R.layout.loading_activity);
                    AlertDialog dialog = builder.create();

                    DatabaseManager databaseManager = new DatabaseManager();

                    for (User user : LoginActivity.users) {
                        if (user.getUsername().equals(username)) {
                            isExist = true;
                            Toast.makeText(CreateUserForAdminActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_LONG).show();
                        }
                    }

                    if (!isExist){
                        boolean priority = (boolean) getIntent().getBooleanExtra("priority",false);
                        User user = new User(email,phone,username,password,realname,address,priority);

                        if (uri != null){
                            user.setImage(uri.toString());
                        }

                        databaseManager.addUserForAdmin(user,dialog,CreateUserForAdminActivity.this);
                    }
                }

            }
        });
    }

    private void back(){
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}