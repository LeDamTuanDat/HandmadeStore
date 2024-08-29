package com.example.handmadestore.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.handmadestore.Database.DatabaseManager;
import com.example.handmadestore.HelperClass.ClearError;
import com.example.handmadestore.HelperClass.NoWhitespaceTextWatcher;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    Uri uri;
//    String currentUserImage;
    User user;
    boolean normal;
    boolean isCurrentImg;

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
        clearError();
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
        binding.email.setText(user.getEmail());
        binding.phone.setText(user.getPhone());
        binding.realname.setText(user.getRealname());
        binding.address.setText(user.getAddress());
//        currentUserImage = user.getImage();
        isCurrentImg = true;
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
                isCurrentImg = false;
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
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = data.getData();
                        isCurrentImg = false;
                        binding.image.setImageURI(uri);
                        binding.delete.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(EditProfileActivity.this, "No Image Selected", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void disableSpace(){
        binding.email.addTextChangedListener(new NoWhitespaceTextWatcher(binding.email));
        binding.phone.addTextChangedListener(new NoWhitespaceTextWatcher(binding.phone));
        binding.password.addTextChangedListener(new NoWhitespaceTextWatcher(binding.password));
        binding.cfPassword.addTextChangedListener(new NoWhitespaceTextWatcher(binding.cfPassword));
    }

    public void hideChangePass(){
        binding.changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.changePass.isChecked()){
                    binding.hideLayout.setVisibility(View.VISIBLE);
                }else {
                    binding.hideLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void save(){
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String phone = binding.phone.getText().toString();
                String realname = binding.realname.getText().toString();
                String address = binding.address.getText().toString();
                String password = binding.password.getText().toString();
                String cfpassword = binding.cfPassword.getText().toString();

                if (check(email,phone,realname,address,password,cfpassword)){
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
                        user.setPassword(password);
                        MainActivity.currentUser.setPassword(password);
                    }
                    if (normal){
                        MainActivity.currentUser.setEmail(email);
                        MainActivity.currentUser.setPhone(phone);
                        MainActivity.currentUser.setRealname(realname);
                        MainActivity.currentUser.setAddress(address);
                    }
                    databaseManager.updateUser(user,uri,EditProfileActivity.this,dialog,normal,isCurrentImg);
                }
            }
        });
    }

    private boolean check(String email,String phone,String realname,String address,String password, String cfpassword){
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

        if (address.isEmpty()){
            binding.addressLayout.setError("Không được để trống");
            check = false;
        }

        if (binding.changePass.isChecked()){
            if (password.isEmpty()){
                binding.passwordLayout.setError("Không được để trống");
                check = false;
            }

            if (cfpassword.isEmpty()){
                binding.cfPasswordLayout.setError("Không được để trống");
                check = false;
            }

            if (!password.equals(cfpassword) && !password.isEmpty() && !cfpassword.isEmpty()){
                binding.cfPasswordLayout.setError("Không trùng khớp");
                check = false;
            }else if (password.length() < 6 && !password.isEmpty()) {
                binding.passwordLayout.setError("Tối thiểu 6 ký tự");
                if (!cfpassword.isEmpty()){
                    binding.cfPasswordLayout.setError("");
                }
                check = false;
            }

        }

        return check;
    }

    private void clearError(){
        binding.email.addTextChangedListener(new ClearError(binding.email,binding.emailLayout));
        binding.phone.addTextChangedListener(new ClearError(binding.phone,binding.phoneLayout));
        binding.realname.addTextChangedListener(new ClearError(binding.realname,binding.realnameLayout));
        binding.password.addTextChangedListener(new ClearError(binding.password,binding.passwordLayout));
        binding.cfPassword.addTextChangedListener(new ClearError(binding.cfPassword,binding.cfPasswordLayout));
        binding.address.addTextChangedListener(new ClearError(binding.address,binding.addressLayout));
    }

    public void cancel(){
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                user.setImage(currentUserImage);
                finish();
            }
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
//            user.setImage(currentUserImage);
            finish();
        }
    };
}