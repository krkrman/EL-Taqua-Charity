package com.example.el_taquacharity.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.el_taquacharity.R;
import com.example.el_taquacharity.pojo.Event;
import com.example.el_taquacharity.pojo.Family;
import com.example.el_taquacharity.ui.adapters.FamiliesRecyclerViewAdapter;
import com.example.el_taquacharity.ui.viewmodel.EventsModelView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventDataActivity extends AppCompatActivity {

    EventsModelView eventsModelView;
    RecyclerView recyclerView, takenFamiliesRecyclerView;
    Event currentEvent;
    List<Family> families = new ArrayList<>();
    List<Family> takenFamilies = new ArrayList<>();
    List<Family> emptyList = new ArrayList<>();
    private FamiliesRecyclerViewAdapter mAdapter,tookFamiliesAdapter;
    private RecyclerView.LayoutManager layoutManager , takenLayoutManager;

    FloatingActionButton scanBtn;
    Family scannedFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);
        scanBtn = findViewById(R.id.scan_QR_code_button);
        receiveData();
        initRecyclerView();
        eventsModelView = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(EventsModelView.class); // this is the instance of AndroidViewModel
        eventsModelView.loadEventFamilies(currentEvent);
        eventsModelView.loadEventTakenFamilies(currentEvent);

        eventsModelView.familiesLiveData.observe(this, new Observer<List<Family>>() {
            @Override
            public void onChanged(List<Family> familyList) {
                mAdapter.setList(familyList, emptyList);
                families = familyList;
            }
        });

        eventsModelView.takenFamiliesLiveData.observe(this, new Observer<List<Family>>() {
            @Override
            public void onChanged(List<Family> familyList) {
                takenFamilies = familyList;
                tookFamiliesAdapter.setList(takenFamilies,takenFamilies);
            }
        });

        eventsModelView.isFamilyDeleted.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    eventsModelView.loadEventFamilies(currentEvent);
                }
            }
        });

        eventsModelView.isTakenFamilyAdded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                eventsModelView.loadEventTakenFamilies(currentEvent);
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDataActivity.this,ScanQrCodeActivity.class);
                intent.putExtra("From" , "EventDataActivity");
                startActivityForResult(intent,1);
            }
        });

        eventsModelView.isFamilyExist.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean){
                    eventsModelView.deleteFamilyFromEvent(scannedFamily.getID(), currentEvent);
                    eventsModelView.addFamilyWhichTake(scannedFamily, currentEvent);
                }else {
                    Toast.makeText(EventDataActivity.this, R.string.taken_family_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void receiveData() {
        currentEvent = getIntent().getParcelableExtra("currentEvent");
    }

    void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        // put you list on the constructor
        mAdapter = new FamiliesRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);

        /******************************************************************************************/
        /******************************************************************************************/

        takenFamiliesRecyclerView = findViewById(R.id.took_families_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        takenFamiliesRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        takenLayoutManager = new LinearLayoutManager(this);
        takenFamiliesRecyclerView.setLayoutManager(takenLayoutManager);
        // specify an adapter (see also next example)
        // put you list on the constructor
        tookFamiliesAdapter = new FamiliesRecyclerViewAdapter(this);
        takenFamiliesRecyclerView.setAdapter(tookFamiliesAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scannedFamily = data.getParcelableExtra("scannedFamily");
                eventsModelView.checkIfFamilyFoundInTakenFamilies(currentEvent,scannedFamily);
                if (!takenFamilies.contains(scannedFamily)) {
                }else {
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}