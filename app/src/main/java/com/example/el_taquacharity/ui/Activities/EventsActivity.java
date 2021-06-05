package com.example.el_taquacharity.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.el_taquacharity.R;
import com.example.el_taquacharity.general.RecyclerItemClickListener;
import com.example.el_taquacharity.pojo.Event;
import com.example.el_taquacharity.ui.adapters.EventRecyclerViewAdapter;
import com.example.el_taquacharity.ui.viewmodel.EventsModelView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    EventsModelView eventsModelView;

    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;

    EditText eventNameEditText , eventDescriptionEditText;
    MaterialRippleLayout addEventButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initToolbar();
        initRecyclerView();
        initBottomSheet();
        clickEvents();
        eventsModelView = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(EventsModelView.class); // this is the instance of AndroidViewModel
        eventsModelView.loadEventsFromFireStore();
        eventsModelView.eventsLiveData.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                mAdapter.setList(events);
            }
        });
    }

    void initToolbar() {
        toolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to put arrow back
        getSupportActionBar().setTitle(R.string.Events);
    }

    void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        // put you list on the constructor
        mAdapter = new EventRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(EventsActivity.this,EventDataActivity.class);
                intent.putExtra("currentEvent",mAdapter.events.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    void clickEvents(){
        FloatingActionButton addButton = findViewById(R.id.add_event_action_button);
        addButton.setOnClickListener(v -> startBottomSheet());
    }

    void initBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(EventsActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_event_bottom_sheet,
                (LinearLayout) findViewById(R.id.bottom_sheet_container));//here write the container id
        eventNameEditText = bottomSheetView.findViewById(R.id.event_name_edit_text);
        eventDescriptionEditText = bottomSheetView.findViewById(R.id.event_description_edit_text);
        addEventButton = bottomSheetView.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(v -> {
            Event event = new Event(eventNameEditText.getText().toString(),eventDescriptionEditText.getText().toString());
            eventsModelView.addEventToFirebase(event);
            eventsModelView.loadEventsFromFireStore();
            addEventButton.setEnabled(false);
        });
    }
    void startBottomSheet() {
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_event:
            startBottomSheet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

}