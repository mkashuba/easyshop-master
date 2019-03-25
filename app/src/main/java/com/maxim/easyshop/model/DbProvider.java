package com.maxim.easyshop.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbProvider implements IDbProvider {

    private Context context;
    private static final DbProvider ourInstance = new DbProvider();
    private FirebaseFirestore db;
    private List<Item> listItem;

    public static DbProvider getInstance() {
        return ourInstance;
    }

    private DbProvider() {
        db = FirebaseFirestore.getInstance();
        listItem = new ArrayList<>();
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void addUserInDB(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("username", user.getUsername());
        userMap.put("password", user.getPassword());
        userMap.put("uId", user.getuId());

        db.collection("users")
                .add(userMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MY_TAG", "onSuccess: User add successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MY_TAG", "onFailure: Exception - " + e.getMessage());
                    }
                });
    }


    @Override
    public void loadAllItems(final LoadAllItemsFromDbCallback loadAllItemsFromDbCallback) {
        db.collection("items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listItem.add(document.toObject(Item.class));
                            }
                            loadAllItemsFromDbCallback.setShopList(listItem);
                            listItem.clear();
                        } else {
                            loadAllItemsFromDbCallback.errorLoadAllItemsFromDb(task.getException());
                        }
                    }
                });
    }

    @Override
    public void saveListItemInDB(List<Item> list) {
        //TODO save list in DB
    }

    @Override
    public void loadListItemFromDB() {
        //TODO load list from DB (with callback in parameter)
    }
}
