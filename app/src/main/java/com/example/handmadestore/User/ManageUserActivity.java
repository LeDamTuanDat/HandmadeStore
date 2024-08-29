package com.example.handmadestore.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.handmadestore.Adapter.UserAdapter;
import com.example.handmadestore.Object.User;
import com.example.handmadestore.R;
import com.example.handmadestore.SplashScreenActivity;
import com.example.handmadestore.databinding.ActivityManageUserBinding;

import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    ActivityManageUserBinding binding;
    UserAdapter adapter;
    String[] options = {"Tất cả người dùng", "Quản trị viên", "Người dùng"};
    ArrayAdapter<String> optionsAdapter;
    ArrayList<User> resultAfterFiltered = new ArrayList<>();
    ArrayList<User> resultAfterSearch = new ArrayList<>();
    int currentSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentSelect = 0;
        init();
        handleFilter();
        handleSearch();
        handleAdd();
        handleBack();
    }

    @Override
    protected void onResume() {
        init();
        binding.search.setQuery("",false);
        binding.search.clearFocus();
        binding.spinner.setSelection(currentSelect);
        super.onResume();
    }

    private void init(){
        adapter = new UserAdapter(SplashScreenActivity.users);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ManageUserActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.recyclerView.setAdapter(adapter);

        optionsAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,options);
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
                currentSelect = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void resultAfterFiltered(int option){
        if (option == 0){
            adapter.setResultAfterFiltered(SplashScreenActivity.users);
        }else {
            resultAfterFiltered.clear();
            boolean priority = option == 1 ? true : false;
            for (User user : SplashScreenActivity.users) {
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
            for (User user : SplashScreenActivity.users) {
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

    private void handleAdd(){
        binding.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(view);
            }
        });
    }

    private void showPopUpMenu(View view){
        PopupMenu popupMenu = new PopupMenu(ManageUserActivity.this, view);
        popupMenu.inflate(R.menu.menu_option_create_user);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ManageUserActivity.this, CreateUserForAdminActivity.class);
                switch (menuItem.getItemId()){
                    case R.id.addAdmin:
                        intent.putExtra("priority",true);
                        startActivity(intent);
                        break;
                    case R.id.addUser:
                        intent.putExtra("priority",false);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void handleBack(){
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}