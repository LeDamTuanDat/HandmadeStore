package com.example.handmadestore.AdminItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.handmadestore.Adapter.ImageAdapter;
import com.example.handmadestore.AdminCategory.AdminCategoryActivity;
import com.example.handmadestore.AdminCategory.UploadCategoryActivity;
import com.example.handmadestore.LoginActivity;
import com.example.handmadestore.Object.Category;
import com.example.handmadestore.R;
import com.example.handmadestore.databinding.ActivityUploadItemBinding;
import com.example.handmadestore.databinding.DropdownSearchBinding;

import java.util.ArrayList;

public class UploadItemActivity extends AppCompatActivity implements ImageAdapter.CountOfImagesWhenRemoved {
    ActivityUploadItemBinding binding;
    ArrayList<Uri> uriArrayList = new ArrayList<>();
    ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingCategory();
        setAdapterForImages();
        handleAddImages();
    }

    public void loadingCategory(){
        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(UploadItemActivity.this);
                DropdownSearchBinding dropdownBinding = DropdownSearchBinding.inflate(LayoutInflater.from(UploadItemActivity.this));
                dialog.setContentView(dropdownBinding.getRoot());
                dialog.getWindow().setLayout(750,900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ArrayAdapter<Category> adapter = new ArrayAdapter<>(UploadItemActivity.this, android.R.layout.simple_list_item_1, LoginActivity.categories);
                dropdownBinding.list.setAdapter(adapter);
                dropdownBinding.search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                dropdownBinding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        binding.category.setText(adapter.getItem(i).toString());
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void setAdapterForImages(){
        adapter = new ImageAdapter(uriArrayList,this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(UploadItemActivity.this,3));
        binding.recyclerView.setAdapter(adapter);
    }

    public void handleAddImages(){
        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                activityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh sản phẩm"));
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
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                if (uriArrayList.size() < 6) {
                                    uriArrayList.add(data.getClipData().getItemAt(i).getUri());
                                }else {
                                    Toast.makeText(UploadItemActivity.this,"Đã chọn tối đa 6 ảnh",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            if (uriArrayList.size() < 6) {
                                String imageURL = data.getData().toString();
                                uriArrayList.add(Uri.parse(imageURL));
                            }else {
                                Toast.makeText(UploadItemActivity.this,"Đã chọn tối đa 6 ảnh",Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        binding.addImage.setText("Thêm ảnh (" + uriArrayList.size() + "/6)");
                    }else {
                        Toast.makeText(UploadItemActivity.this,"Chưa chọn ảnh",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void clicked(int getSize) {
        binding.addImage.setText("Thêm ảnh (" + uriArrayList.size() + "/6)");
    }
}