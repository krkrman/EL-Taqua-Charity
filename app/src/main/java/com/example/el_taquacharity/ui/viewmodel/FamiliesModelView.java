package com.example.el_taquacharity.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.el_taquacharity.pojo.Family;
import com.example.el_taquacharity.ui.repo.FamilyRepo;

import java.util.List;

public class FamiliesModelView extends AndroidViewModel {
    public MutableLiveData<List<Family>> familyLiveData;
    public MutableLiveData<Boolean> isRemoved;
    public MutableLiveData<Boolean> isAdded;
    FamilyRepo familyRepo;

    public FamiliesModelView(@NonNull Application application) {
        super(application);
        familyRepo = new FamilyRepo(application);
        familyLiveData = familyRepo.familyLiveData;
        isRemoved = familyRepo.isRemoved;
        isAdded = familyRepo.isAdded;
        getAllFamiliesFromFirestore();
    }

    public void getAllFamiliesFromFirestore() {
        familyRepo.loadFamiliesFromFireStore();
    }

    public void addFamilyToFirestore(Family family, String id) {
        familyRepo.addFamilyToFireStore(family, id);
    }

    public void deleteFamily(String id) {
        familyRepo.deleteFamily(id);
    }

    public void searchByName(String name, boolean isHusbandSearch, boolean isWifeSearch, boolean isAddressSearch) {
        familyRepo.searchByName(name, isHusbandSearch, isWifeSearch,isAddressSearch);
    }

    public void searchById(String id) {
        familyRepo.searchById(id);
    }
}
