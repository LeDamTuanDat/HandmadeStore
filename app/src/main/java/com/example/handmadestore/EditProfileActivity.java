package com.example.handmadestore;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.handmadestore.AdminCategory.UploadCategoryActivity;
import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        setImage();
        deleteImage();
        disableSpace();
        hideChangePass();
        save();
    }

    private void getData(){
        if (!MainActivity.currentUser.getImage().isEmpty()) {
            Glide.with(EditProfileActivity.this).load(MainActivity.currentUser.getImage()).into(binding.image);
        }else {
            binding.delete.setVisibility(View.GONE);
        }
        binding.edtEmail.setText(MainActivity.currentUser.getEmail());
        binding.edtPhone.setText(MainActivity.currentUser.getPhone());
        binding.edtRealName.setText(MainActivity.currentUser.getRealname());
        binding.edtAddress.setText(MainActivity.currentUser.getAddress());
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
                        Toast.makeText(EditProfileActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void disableSpace(){
        binding.edtEmail.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtEmail));
        binding.edtPhone.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtPhone));
        binding.edtPassword.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtPassword));
        binding.edtCfPassword.addTextChangedListener(new NoWhitespaceTextWatcher(binding.edtCfPassword));
    }

    public void hideChangePass(){
        binding.changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.changePass.isChecked()){
                    binding.passwordLayout.setVisibility(View.VISIBLE);
                }else {
                    binding.passwordLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void save(){
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString();
                String phone = binding.edtPhone.getText().toString();
                String password = binding.edtPassword.getText().toString();
                String cfpassword = binding.edtCfPassword.getText().toString();
                String realname = binding.edtRealName.getText().toString();
                String address = binding.edtAddress.getText().toString();

                if(email.isEmpty() || phone.isEmpty() || address.isEmpty() || realname.isEmpty()){
                    Toast.makeText(EditProfileActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                } else if (!email.contains("@gmail.com")) {
                    Toast.makeText(EditProfileActivity.this,"Vui lòng nhập đúng định dạng email",Toast.LENGTH_LONG).show();
                } else if (phone.length() < 10 || !phone.startsWith("0")) {
                    Toast.makeText(EditProfileActivity.this,"Vui lòng nhập đúng định dạng số điện thoại",Toast.LENGTH_LONG).show();
                } else {
                    if (binding.changePass.isChecked()){
                        if (password.isEmpty() || cfpassword.isEmpty()){
                            Toast.makeText(EditProfileActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                        }else if (!password.equals(cfpassword)) {
                            Toast.makeText(EditProfileActivity.this,"Mật khẩu xác nhận không trùng khớp",Toast.LENGTH_LONG).show();
                        } else if (password.length() < 6) {
                            Toast.makeText(EditProfileActivity.this,"Mật khẩu phải tối thiểu 6 ký tự",Toast.LENGTH_LONG).show();
                        }else {
                            MainActivity.currentUser.setPassword(password);
                        }
                    }else {
                        if (uri != null){
                            MainActivity.currentUser.setImage(uri.toString());
                        }else {
                            MainActivity.currentUser.setImage("");
                        }
                    }
                    MainActivity.currentUser.setEmail(email);
                    MainActivity.currentUser.setPhone(phone);
                    MainActivity.currentUser.setRealname(realname);
                    MainActivity.currentUser.setAddress(address);
                    DatabaseManager databaseManager = new DatabaseManager();
                    databaseManager.updateUser(MainActivity.currentUser,uri,EditProfileActivity.this);
                }

            }
        });
    }
}