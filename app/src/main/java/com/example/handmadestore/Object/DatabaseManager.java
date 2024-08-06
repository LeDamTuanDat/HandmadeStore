package com.example.handmadestore.Object;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.handmadestore.Adapter.AdminCategoryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class DatabaseManager {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public DatabaseManager() {
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = database.getReference();
        storageReference = firebaseStorage.getReference();
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void getUsers(ArrayList<User> users) {
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    users.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addUser(User user){
        databaseReference.child("Users").child(user.getUsername()).setValue(user);
    }

    public void getBanner(ArrayList<Banner> banners){
        databaseReference.child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        banners.add(dataSnapshot.getValue(Banner.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCategory(ArrayList<Category> categories){
        databaseReference.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    categories.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        categories.add(dataSnapshot.getValue(Category.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getItems(ArrayList<Item> items){
        databaseReference.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    items.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        items.add(dataSnapshot.getValue(Item.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addCart(User user){
        databaseReference.child("Users").child(user.getUsername()).child("carts").setValue(user.getCarts());
    }

    public void addCategory(String tilte, Uri uri ,AlertDialog dialog, Context context){
        dialog.show();
        storageReference.child("Category").child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uriImg = uriTask.getResult();
                String id = databaseReference.child("Category").push().getKey();
                Category category = new Category(id,tilte,uriImg.toString());
                databaseReference.child("Category").child(id).setValue(category);
                dialog.dismiss();
                Toast.makeText(context,"Thêm danh mục thành công",Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void modifyCategory(String tilte,Category category, Uri uri, AlertDialog dialog,Context context){
        dialog.show();
        storageReference.child("Category").child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uriImg = uriTask.getResult();
                category.setTitle(tilte);
                category.setPicUrl(uriImg.toString());
                databaseReference.child("Category").child(category.getId()).setValue(category);
                dialog.dismiss();
                Toast.makeText(context,"Sửa danh mục thành công",Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                category.setTitle(tilte);
                databaseReference.child("Category").child(category.getId()).setValue(category);
                dialog.dismiss();
                Toast.makeText(context,"Sửa danh mục thành công",Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
        });
    }

    public void deleteCategory(Category category,AlertDialog dialog,AdminCategoryAdapter adapter,Context context){
        storageReference = firebaseStorage.getReferenceFromUrl(category.getPicUrl());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child("Category").child(category.getId()).removeValue();
                dialog.dismiss();
                adapter.notifyDataSetChanged();
                Toast.makeText(context,"Xoá danh mục thành công",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                databaseReference.child("Category").child(category.getId()).removeValue();
                dialog.dismiss();
                adapter.notifyDataSetChanged();
                Toast.makeText(context,"Xoá danh mục thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
