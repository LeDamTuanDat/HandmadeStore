package com.example.handmadestore.Object;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.handmadestore.MainActivity;
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
import java.util.UUID;

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

    public void updateUser(User user, Uri uri, Context context,AlertDialog dialog){
        dialog.show();
        if (uri == null){
            databaseReference.child("Users").child(user.getUsername()).setValue(user);
            ((Activity) context).finish();
        }else {
            storageReference.child("Users").child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri uriImg = uriTask.getResult();
                    user.setImage(uriImg.toString());
                    databaseReference.child("Users").child(user.getUsername()).setValue(user);
                    dialog.dismiss();
                    Toast.makeText(context,"Cập nhật thông tin thành công",Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
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

    public void deleteCategory(Category category,AlertDialog dialog,Context context){
        databaseReference.child("Category").child(category.getId()).removeValue();
        storageReference = firebaseStorage.getReferenceFromUrl(category.getPicUrl());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.dismiss();
                Toast.makeText(context,"Xoá danh mục thành công",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(context,"Xoá danh mục thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadItem(Item item, ArrayList<Uri> uriArrayList, AlertDialog dialog, Context context, boolean update){
        dialog.show();
        ArrayList<String> picUrl = (item.getPicUrl() == null ? new ArrayList<>() : item.getPicUrl());
        for (int i = 0 ; i < uriArrayList.size(); i++){
            Uri uri = uriArrayList.get(i);
            final String randomName = UUID.randomUUID().toString();
            final int currentIndex = i;
            storageReference.child("Items/" + randomName)
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            if (picUrl.size() > 0 && update){
                                if (currentIndex >= picUrl.size()){
                                    picUrl.add(uriTask.getResult().toString());
                                }else {
                                    picUrl.set(currentIndex,uriTask.getResult().toString());
                                }
                            }else {
                                picUrl.add(uriTask.getResult().toString());
                            }

                            if(currentIndex == uriArrayList.size() - 1){
                                if (!update){
                                    String id = databaseReference.child("Items").push().getKey();
                                    item.setId(id);
                                    item.setPicUrl(picUrl);
                                }
                                databaseReference.child("Items").child(item.getId()).setValue(item);
                                dialog.dismiss();
                                String message = update ? "Cập nhật sản phẩm thành công" : "Thêm sản phẩm thành công";
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                ((Activity) context).finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(currentIndex == uriArrayList.size() - 1){
                                databaseReference.child("Items").child(item.getId()).setValue(item);
                                dialog.dismiss();
                                String message = update ? "Cập nhật sản phẩm thành công" : "Thêm sản phẩm thành công";
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                ((Activity) context).finish();
                            }
                        }
                    });
        }
    }

    public void deleteItem(Item item, AlertDialog dialog, Context context){
        databaseReference.child("Items").child(item.getId()).removeValue();
        for (int i = 0; i < item.getPicUrl().size(); i++) {
            String url = item.getPicUrl().get(i);
            final int currentIndex = i;
            storageReference = firebaseStorage.getReferenceFromUrl(url);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    if (currentIndex == item.getPicUrl().size() - 1){
                        dialog.dismiss();
                        Toast.makeText(context,"Xoá sản phẩm thành công",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void addOrder(Order order, Context context){
        String keyId = databaseReference.push().getKey();
        order.setKeyId(keyId);
        databaseReference.child("Orders").child(order.getIdUser()).child(keyId).setValue(order);

        for (Cart cart: order.getCarts()) {
            Item item = cart.getItem();
            item.setSold(cart.getNumber());
            item.setInventory(item.getInventory() - cart.getNumber());
            databaseReference.child("Items").child(item.getId()).setValue(item);
        }

        MainActivity.currentUser.setCarts(new ArrayList<>());
        databaseReference.child("Users").child(order.getIdUser()).child("carts").setValue(MainActivity.currentUser.getCarts());
        Toast.makeText(context,"Đặt hàng thành công",Toast.LENGTH_LONG).show();
    }

    public void getOrder(String idUser, ArrayList<Order> orders){
        databaseReference.child("Orders").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    orders.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        orders.add(dataSnapshot.getValue(Order.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllOrder(ArrayList<Order> orders){
        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    orders.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot sub : dataSnapshot.getChildren()){
                            orders.add(sub.getValue(Order.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setOrderStatus(Order order){
        databaseReference.child("Orders").child(order.getIdUser()).child(order.getKeyId()).setValue(order);
    }

    public void addRating(Rating rating){
        String id = databaseReference.push().getKey();
        rating.setId(id);
        databaseReference.child("Ratings").child(id).setValue(rating);
    }

    public void getRatings(ArrayList<Rating> ratings){
        databaseReference.child("Ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ratings.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ratings.add(dataSnapshot.getValue(Rating.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
