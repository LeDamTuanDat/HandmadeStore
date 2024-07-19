package com.example.handmadestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handmadestore.Object.DatabaseManager;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        getData();
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user",user);
                            startActivity(intent);
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
    }

    private void getData() {
        users = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager("User");
        databaseManager.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}