package com.example.el_taquacharity.ui.repo;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.el_taquacharity.pojo.Family;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FamilyRepo {
    public MutableLiveData<List<Family>> familyLiveData;
    public MutableLiveData<Boolean> isRemoved;
    public MutableLiveData<Boolean> isAdded;
    List<Family> familyList;
    List<Family> searchedFamily;
    DocumentSnapshot lastData;
    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference familyRef = db.collection("Families");

    public FamilyRepo(Application application) {
        // it needs application to be initialized so we need to use AndroidViewModel not ViewModel
        context = application.getApplicationContext();
        familyList = new ArrayList<>();
        searchedFamily = new ArrayList<>();
        familyLiveData = new MutableLiveData<>();
        isRemoved = new MutableLiveData<>();
        isAdded = new MutableLiveData<>();
    }

    public void addFamilyToFireStore(Family family, String id) {
        familyRef.document(id).set(family).addOnCompleteListener(task -> isAdded.setValue(true))
                .addOnFailureListener(e -> isAdded.setValue(false));
    }

    public void loadFamiliesFromFireStore() {
        Query query;
        if (lastData == null) {
            query = familyRef.orderBy("status", Query.Direction.ASCENDING).limit(8);
        } else {
            query = familyRef.orderBy("status", Query.Direction.ASCENDING).limit(8).startAfter(lastData);
        }
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    Family family = documentSnapshot.toObject(Family.class);
                    family.setID(documentSnapshot.getId());
                    familyList.add(family);
                    lastData = documentSnapshot;
                }
                familyLiveData.setValue(familyList);
            }
        });
    }

    public void deleteFamily(String id) {
        familyRef.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isRemoved.setValue(true);
            }
        });
    }

    public void searchByName(String name, boolean isHusbandSearch, boolean isWifeSearch , boolean isAddressSearch) {
        searchedFamily.clear();
        if (isWifeSearch) {
            familyRef.whereGreaterThanOrEqualTo("wifeName", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            Family family = document.toObject(Family.class);
                            family.setID(document.getId());
                            if (!searchedFamily.contains(family))
                                searchedFamily.add(family);
                        }
                        familyLiveData.setValue(searchedFamily);
                    }
                }
            });
        }else if (isHusbandSearch) {
            familyRef.whereGreaterThanOrEqualTo("husbandName", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            Family family = document.toObject(Family.class);
                            family.setID(document.getId());
                            if (!searchedFamily.contains(family))
                                searchedFamily.add(family);
                        }
                        familyLiveData.setValue(searchedFamily);
                    }
                }
            });
        }else if (isAddressSearch){
            familyRef.whereGreaterThanOrEqualTo("address", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            Family family = document.toObject(Family.class);
                            family.setID(document.getId());
                            if (!searchedFamily.contains(family))
                                searchedFamily.add(family);
                        }
                        familyLiveData.setValue(searchedFamily);
                    }
                }
            });
        }
    }

    public void searchById(String id) {
        searchedFamily.clear();
        familyRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Family family = documentSnapshot.toObject(Family.class);
                if (family != null) {
                    family.setID(id);
                    searchedFamily.add(family);
                }
                familyLiveData.setValue(searchedFamily);
            }
        });
    }
}
