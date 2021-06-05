package com.example.el_taquacharity.ui.repo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.el_taquacharity.pojo.Event;
import com.example.el_taquacharity.pojo.Family;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventRepo {
    public MutableLiveData<List<Event>> eventsLiveData;
    public MutableLiveData<Boolean> isFamilyAdded;
    public MutableLiveData<Boolean> isTakenFamilyAdded;
    public MutableLiveData<Boolean> isFamilyDeleted;
    public MutableLiveData<Boolean> isFamilyExist;

    public MutableLiveData<List<Family>> familiesLiveData;
    public MutableLiveData<List<Family>> takenFamiliesLiveData;

    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventRef = db.collection("Events");

    public EventRepo(Application application) {
        // it needs application to be initialized so we need to use AndroidViewModel not ViewModel
        context = application.getApplicationContext();
        eventsLiveData = new MutableLiveData<>();
        isFamilyAdded = new MutableLiveData<>();
        isTakenFamilyAdded = new MutableLiveData<>();
        familiesLiveData = new MutableLiveData<>();
        takenFamiliesLiveData = new MutableLiveData<>();
        isFamilyDeleted = new MutableLiveData<>();
        isFamilyExist = new MutableLiveData<>();
    }

    public void loadEventsFromFireStore() {
        final List<Event> events = new ArrayList<>();
        eventRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    Event event = documentSnapshot.toObject(Event.class);
                    events.add(event);
                }
                eventsLiveData.setValue(events);
            }
        });
    }

    public void addEventToFirebase(Event event) {
        eventRef.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Addition failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFamilyToEvent(final Family family, Event event) {
        eventRef.whereEqualTo("eventName", event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    final String eventId = documentSnapshot.getId();
                    eventRef.document(eventId).collection("families").document(family.getID())
                            .set(family).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            isFamilyAdded.setValue(true);
                        }
                    });
                }
            }
        });
    }

    public void addFamilyWhichTake(final Family family, Event event){
        eventRef.whereEqualTo("eventName", event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    final String eventId = documentSnapshot.getId();
                    Event currentEvent = documentSnapshot.toObject(Event.class);
                    eventRef.document(eventId).collection("tookFamilies").document(family.getID())
                            .set(family).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            isTakenFamilyAdded.setValue(true);
                            currentEvent.setRemainingFamilies(currentEvent.getRemainingFamilies()-1);
                            eventRef.document(eventId).set(currentEvent);
                        }
                    });
                }
            }
        });
    }

    public void deleteFamilyFromEvent(String familyID, Event event){
        eventRef.whereEqualTo("eventName", event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    final String eventId = documentSnapshot.getId();
                    eventRef.document(eventId).collection("families").document(familyID)
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                                isFamilyDeleted.setValue(true);
                        }
                    });
                }
            }
        });
    }

    public void loadEventFamilies(Event event) {
        final List<Family> families = new ArrayList<>();
        eventRef.whereEqualTo("eventName", event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    final String eventId = documentSnapshot.getId();
                    eventRef.document(eventId).collection("families").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                                Family family = documentSnapshot.toObject(Family.class);
                                family.setID(documentSnapshot.getId());
                                families.add(family);
                            }
                            familiesLiveData.setValue(families);
                        }
                    });
                }
            }
        });
    }

    public void loadEventTakenFamilies(Event event){
        final List<Family> takenFamilies = new ArrayList<>();
        eventRef.whereEqualTo("eventName", event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    final String eventId = documentSnapshot.getId();
                    eventRef.document(eventId).collection("tookFamilies").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                                Family family = documentSnapshot.toObject(Family.class);
                                family.setID(documentSnapshot.getId());
                                takenFamilies.add(family);
                            }
                            takenFamiliesLiveData.setValue(takenFamilies);
                        }
                    });
                }
            }
        });
    }

    public void countFamily(Event event, int numberOfFamilyAdded) {
        eventRef.whereEqualTo("eventName", event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //this to loop all the documents
                    Event currentEvent = documentSnapshot.toObject(Event.class);
                    String eventId = documentSnapshot.getId();
                    currentEvent.setNumberOfFamilies(currentEvent.getNumberOfFamilies() + numberOfFamilyAdded);
                    currentEvent.setRemainingFamilies(currentEvent.getRemainingFamilies() + numberOfFamilyAdded);
                    eventRef.document(eventId).set(currentEvent);
                };
            }
        });
    }

    public void checkIfFamilyFoundInTakenFamilies(Event event , Family family){
        eventRef.whereEqualTo("eventName",event.getEventName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Event currentEvent = documentSnapshot.toObject(Event.class);
                    String eventId = documentSnapshot.getId();
                    eventRef.document(eventId).collection("tookFamilies").document(family.getID())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Family currentFamily = documentSnapshot.toObject(Family.class);
                            if (currentFamily != null){
                                isFamilyExist.setValue(true);
                            }else {
                                isFamilyExist.setValue(false);
                            }
                        }
                    });
                }
            }
        });
    }
}
