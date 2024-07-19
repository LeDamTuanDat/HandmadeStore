package com.example.handmadestore.Object;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private String ref;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public DatabaseManager() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(ref);
    }

    public DatabaseManager(String ref) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(ref);
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
}
