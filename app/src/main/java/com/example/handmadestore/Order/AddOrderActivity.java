package com.example.handmadestore.Order;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handmadestore.Api.CreateOrder;
import com.example.handmadestore.Database.DatabaseManager;
import com.example.handmadestore.MainActivity;
import com.example.handmadestore.Object.Order;
import com.example.handmadestore.databinding.ConfirmOrderBinding;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class AddOrderActivity extends AppCompatActivity {
    ConfirmOrderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleAddInfor();
        handleConfirm();
        handleExit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

    }

    public void handleAddInfor(){
        binding.defaultInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.defaultInfor.isChecked()){
                    binding.name.setText(MainActivity.currentUser.getRealname());
                    binding.phone.setText(MainActivity.currentUser.getPhone());
                    binding.address.setText(MainActivity.currentUser.getAddress());
                }else {
                    binding.name.setText("");
                    binding.phone.setText("");
                    binding.address.setText("");
                }
            }
        });
    }

    public void handleConfirm(){
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String phone = binding.phone.getText().toString();
                String address = binding.address.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()){
                    Toast.makeText(AddOrderActivity.this,"Vui lòng điền đủ thông tin",Toast.LENGTH_LONG).show();
                }else if (phone.length() < 10 || !phone.startsWith("0")) {
                    Toast.makeText(AddOrderActivity.this,"Vui lòng nhập đúng định dạng số điện thoại",Toast.LENGTH_LONG).show();
                } else {

                    String idUser = MainActivity.currentUser.getUsername();
                    Order order = new Order(idUser,name,phone,address,MainActivity.currentUser.getCarts());
                    DatabaseManager databaseManager = new DatabaseManager();

                    if (binding.zalo.isChecked()){
                        CreateOrder orderApi = new CreateOrder();

                        try {
                            JSONObject data = orderApi.createOrder(""+ (order.calTotal() + 15000));
                            String code = data.getString("return_code");

                            if (code.equals("1")) {
                                String token = data.getString("zp_trans_token");
                                ZaloPaySDK.getInstance().payOrder(AddOrderActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(String s, String s1, String s2) {
                                        order.setZaloPayment(true);
                                        databaseManager.addOrder(order, AddOrderActivity.this);
                                        finish();
                                        Toast.makeText(AddOrderActivity.this,"Thanh toán thành công",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onPaymentCanceled(String s, String s1) {
                                        Toast.makeText(AddOrderActivity.this,"Thanh toán thất bại",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                        Toast.makeText(AddOrderActivity.this,"Huỷ bỏ giao dịch",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else {
                        databaseManager.addOrder(order, AddOrderActivity.this);
                        finish();
                    }
                }
            }
        });
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    public void handleExit(){
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}