package com.example.handmadestore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    Uri uri;
    String currentUserImage;
    User user;
    boolean normal;

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
        cancel();
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void getData(){

        normal = getIntent().getBooleanExtra("normal",false);
        user = (User) getIntent().getSerializableExtra("user");

        if (!user.getImage().isEmpty()) {
            Glide.with(EditProfileActivity.this).load(user.getImage()).into(binding.image);
        }else {
            binding.delete.setVisibility(View.GONE);
        }
        binding.edtEmail.setText(user.getEmail());
        binding.edtPhone.setText(user.getPhone());
        binding.edtRealName.setText(user.getRealname());
        binding.edtAddress.setText(user.getAddress());
        currentUserImage = user.getImage();
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
                user.setImage("");
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
                    builder.setCancelable(false);
                    builder.setView(R.layout.loading_activity);
                    AlertDialog dialog = builder.create();

                    DatabaseManager databaseManager = new DatabaseManager();

                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setRealname(realname);
                    user.setAddress(address);

                    if (uri != null){
                        user.setImage(uri.toString());
                    }

                    if (binding.changePass.isChecked()){
                        if (password.isEmpty() || cfpassword.isEmpty()){
                            Toast.makeText(EditProfileActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                        }else if (!password.equals(cfpassword)) {
                            Toast.makeText(EditProfileActivity.this,"Mật khẩu xác nhận không trùng khớp",Toast.LENGTH_LONG).show();
                        } else if (password.length() < 6) {
                            Toast.makeText(EditProfileActivity.this,"Mật khẩu phải tối thiểu 6 ký tự",Toast.LENGTH_LONG).show();
                        }else {
                            user.setPassword(password);
                            databaseManager.updateUser(user,uri,EditProfileActivity.this,dialog,normal);
                        }
                    }else {
                        databaseManager.updateUser(user,uri,EditProfileActivity.this,dialog,normal);
                    }
                }

            }
        });
    }

    public void cancel(){
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setImage(currentUserImage);
                finish();
            }
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            user.setImage(currentUserImage);
            finish();
        }
    };
}