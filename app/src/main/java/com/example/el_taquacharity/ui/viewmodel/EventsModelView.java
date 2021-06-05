package com.example.el_taquacharity.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.el_taquacharity.pojo.Event;
import com.example.el_taquacharity.pojo.Family;
import com.example.el_taquacharity.ui.repo.EventRepo;

import java.util.List;

public class EventsModelView extends AndroidViewModel {

    public MutableLiveData<List<Event>> eventsLiveData;
    public MutableLiveData<Boolean> isFamilyAdded;
    public MutableLiveData<Boolean> isTakenFamilyAdded;
    public MutableLiveData<Boolean> isFamilyDeleted;
    public MutableLiveData<Boolean> isFamilyExist;


    public MutableLiveData<List<Family>> familiesLiveData;
    public MutableLiveData<List<Family>> takenFamiliesLiveData;

    EventRepo eventRepo;

    public EventsModelView(@NonNull Application application) {
        super(application);
        eventRepo = new EventRepo(application);
        eventsLiveData = eventRepo.eventsLiveData;
        isFamilyAdded = eventRepo.isFamilyAdded;
        familiesLiveData = eventRepo.familiesLiveData;
        isFamilyDeleted = eventRepo.isFamilyDeleted;
        isTakenFamilyAdded = eventRepo.isTakenFamilyAdded;
        takenFamiliesLiveData = eventRepo.takenFamiliesLiveData;
        isFamilyExist = eventRepo.isFamilyExist;
    }

    public void loadEventsFromFireStore() {
        eventRepo.loadEventsFromFireStore();
    }

    public void addEventToFirebase(Event event) {
        eventRepo.addEventToFirebase(event);
    }

    public void addFamilyToEvent(final Family family, Event event) {
        eventRepo.addFamilyToEvent(family, event);
    }

    public void addFamilyWhichTake(final Family family, Event event) {
        eventRepo.addFamilyWhichTake(family, event);
    }

    public void deleteFamilyFromEvent(String familyID, Event event) {
        eventRepo.deleteFamilyFromEvent(familyID, event);
    }

    public void loadEventFamilies(Event event) {
        eventRepo.loadEventFamilies(event);
    }

    public void loadEventTakenFamilies(Event event) {
        eventRepo.loadEventTakenFamilies(event);
    }

    public void countFamily(Event event, int numberOfFamilyAdded) {
        eventRepo.countFamily(event, numberOfFamilyAdded);
    }

    public void checkIfFamilyFoundInTakenFamilies(Event event, Family family) {
        eventRepo.checkIfFamilyFoundInTakenFamilies(event,family);
    }

}
