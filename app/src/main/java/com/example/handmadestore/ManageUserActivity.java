package com.example.handmadestore;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.ItemAdapter;
import com.example.handmadestore.Adapter.UserAdapter;
import com.example.handmadestore.AdminItem.AdminItemActivity;
import com.example.handmadestore.Object.Item;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.databinding.ActivityManageUserBinding;

import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    ActivityManageUserBinding binding;
    UserAdapter adapter;
    String[] options = {"Tất cả người dùng", "Quản trị viên", "Người dùng"};
    ArrayAdapter<String> optionsAdapter;
    ArrayList<User> resultAfterFiltered = new ArrayList<>();
    ArrayList<User> resultAfterSearch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        handleFilter();
        handleSearch();
    }

    private void init(){
        adapter = new UserAdapter(LoginActivity.users);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ManageUserActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.recyclerView.setAdapter(adapter);

        optionsAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,options);
        optionsAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinner.setAdapter(optionsAdapter);
    }

    private void  handleFilter(){
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resultAfterFiltered(i);
                binding.search.setQuery("",false);
                binding.search.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void resultAfterFiltered(int option){
        if (option == 0){
            adapter.setResultAfterFiltered(LoginActivity.users);
        }else {
            resultAfterFiltered.clear();
            boolean priority = option == 1 ? true : false;
            for (User user : LoginActivity.users) {
                if(user.getPriority() == priority){
                    resultAfterFiltered.add(user);
                }
            }
            adapter.setResultAfterFiltered(resultAfterFiltered);
            checkItems(resultAfterFiltered);
        }
    }

    private void handleSearch(){
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resultAfterSearch(newText);
                return true;
            }
        });
    }

    private void resultAfterSearch(String text){
        resultAfterSearch.clear();
        int seleted = binding.spinner.getSelectedItemPosition();
        if(seleted != 0){
            for (User user : resultAfterFiltered) {
                String normalTitle = user.getUsername().toLowerCase();
                String normalText = text.toLowerCase();
                if(normalTitle.contains(normalText)){
                    resultAfterSearch.add(user);
                }
            }
        }else {
            for (User user : LoginActivity.users) {
                String normalTitle = user.getUsername().toLowerCase();
                String normalText = text.toLowerCase();
                if(normalTitle.contains(normalText)){
                    resultAfterSearch.add(user);
                }
            }
        }
        adapter.setResultAfterFiltered(resultAfterSearch);
        checkItems(resultAfterSearch);
    }

    private void checkItems(ArrayList<User> users){
        if (users.size() == 0){
            binding.notification.setVisibility(View.VISIBLE);
        }else {
            binding.notification.setVisibility(View.GONE);
        }
    }
}