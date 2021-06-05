package com.example.el_taquacharity.ui.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.el_taquacharity.R;
import com.example.el_taquacharity.general.RecyclerItemClickListener;
import com.example.el_taquacharity.pojo.Event;
import com.example.el_taquacharity.pojo.Family;
import com.example.el_taquacharity.ui.adapters.EventRecyclerViewAdapter;
import com.example.el_taquacharity.ui.adapters.FamiliesRecyclerViewAdapter;
import com.example.el_taquacharity.ui.viewmodel.EventsModelView;
import com.example.el_taquacharity.ui.viewmodel.FamiliesModelView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllFamiliesActivity extends AppCompatActivity {
    public List<Family> selectedFamilyList = new ArrayList<>();
    public List<Event> eventList = new ArrayList<>();
    public boolean selectionModeEnabled = false;
    boolean searchedModeEnabled = false;
    boolean isHusbandSearch = false;
    boolean isWifeSearch = true;
    boolean isIdSearch = false;
    boolean isAddressSearch = false;
    FamiliesModelView familiesModelView;
    EventsModelView eventsModelView;
    BottomSheetDialog familiesBottomSheetDialog;
    View familiesBottomSheetView;
    BottomSheetDialog eventsBottomSheetDialog;
    View eventsBottomSheetView;
    TextView idTextView, husbandNameTextView, wifeNameTextView, statusTextView, phoneNumberTextView,
            socialStatusTextView, sonsNumberTextView, workTextView, addressTextView, incomeTextView,
            deptTextView;
    LinearLayout childrenLinearLayout;
    MaterialRippleLayout updateButton;
    FloatingActionButton floatingActionButton;
    Toolbar toolbar;
    ProgressBar progressBar;
    private FamiliesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EventRecyclerViewAdapter eventsAdapter;
    private RecyclerView.LayoutManager eventsLayoutManager;
    private int menuToChoose = R.menu.all_families_menu;
    private List<Family> families = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_families);
        initRecyclerView();
        initViews();
        initToolbar();
        initBottomSheets();
        initObjects();
        observations();
        clickEvents();
    }

    void clickEvents(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllFamiliesActivity.this, AddPeopleActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void initViews(){
        progressBar = findViewById(R.id.progress_circular);
        floatingActionButton = findViewById(R.id.add_family_action_button);
    }

    void initObjects(){
        familiesModelView = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(FamiliesModelView.class); // this is the instance of AndroidViewModel

        eventsModelView = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(EventsModelView.class); // this is the instance of AndroidViewModel
    }

    void observations(){
        eventsModelView.loadEventsFromFireStore();
        familiesModelView.familyLiveData.observe(this, new Observer<List<Family>>() {
            @Override
            public void onChanged(final List<Family> familyList) {
                // update RecyclerView
                families = familyList;
                mAdapter.setList(familyList, selectedFamilyList);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        familiesModelView.isRemoved.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(AllFamiliesActivity.this, R.string.removed_successfully, Toast.LENGTH_SHORT).show();
                }
            }
        });

        eventsModelView.isFamilyAdded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(AllFamiliesActivity.this, getString(R.string.addedSuccessfully), Toast.LENGTH_SHORT).show();
                eventsBottomSheetDialog.dismiss();
            }
        });

        eventsModelView.eventsLiveData.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventList = events;
                eventsAdapter.setList(eventList);
            }
        });

    }

    void initToolbar() {
        toolbar = findViewById(R.id.all_people_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to put arrow back
        getSupportActionBar().setTitle(R.string.all_family);
    }

    void initBottomSheets() {
        familiesBottomSheetDialog = new BottomSheetDialog(AllFamiliesActivity.this, R.style.BottomSheetDialogTheme);
        familiesBottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.family_bottom_sheet,
                (LinearLayout) findViewById(R.id.bottom_sheet_container));//here write the container id

        childrenLinearLayout = familiesBottomSheetView.findViewById(R.id.childrenLinearLayout);
        idTextView = familiesBottomSheetView.findViewById(R.id.id_text_view);
        husbandNameTextView = familiesBottomSheetView.findViewById(R.id.husband_name_text_view);
        wifeNameTextView = familiesBottomSheetView.findViewById(R.id.wife_name_text_view);
        statusTextView = familiesBottomSheetView.findViewById(R.id.status_text_view);
        phoneNumberTextView = familiesBottomSheetView.findViewById(R.id.phone_number_text_view);
        socialStatusTextView = familiesBottomSheetView.findViewById(R.id.social_status_text_view);
        sonsNumberTextView = familiesBottomSheetView.findViewById(R.id.sons_number_text_view);
        workTextView = familiesBottomSheetView.findViewById(R.id.work_text_view);
        addressTextView = familiesBottomSheetView.findViewById(R.id.adress_text_view);
        incomeTextView = familiesBottomSheetView.findViewById(R.id.income_text_view);
        deptTextView = familiesBottomSheetView.findViewById(R.id.dept_text_view);
        updateButton = familiesBottomSheetView.findViewById(R.id.update_family_button);
        /******************************************************************************************/

        eventsBottomSheetDialog = new BottomSheetDialog(AllFamiliesActivity.this, R.style.BottomSheetDialogTheme);
        eventsBottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.events_bottom_sheet,
                 findViewById(R.id.events_container));//here write the container id
        initBottomSheetRecyclerView();
    }

    void initBottomSheetRecyclerView() {
        RecyclerView eventsRecyclerView = eventsBottomSheetView.findViewById(R.id.events_recyclerView);
        eventsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        eventsLayoutManager = new LinearLayoutManager(this);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);

        // specify an adapter (see also next example)
        // put you list on the constructor
        eventsAdapter = new EventRecyclerViewAdapter(this);
        eventsRecyclerView.setAdapter(eventsAdapter);

        eventsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, eventsRecyclerView
                , new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                addFamiliesToEvent(eventList.get(position));
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void addFamiliesToEvent(Event event) {
        for (int i = 0; i<selectedFamilyList.size();i++) {
            eventsModelView.addFamilyToEvent(selectedFamilyList.get(i),event);
        }
        eventsModelView.countFamily(event,selectedFamilyList.size());
    }

    @SuppressLint("SetTextI18n")
    public void setBottomSheetData(final Family family) {
        idTextView.setText(this.getResources().getString(R.string.id_number) + " : " + family.getID());
        husbandNameTextView.setText(this.getResources().getString(R.string.husband_name) + " : " + family.getHusbandName());
        wifeNameTextView.setText(this.getResources().getString(R.string.wife_name) + " : " + family.getWifeName());
        statusTextView.setText(this.getResources().getString(R.string.status) + " : " + family.getStatus());
        phoneNumberTextView.setText(this.getResources().getString(R.string.phone_number) + " : " + family.getPhoneNumber());
        socialStatusTextView.setText(this.getResources().getString(R.string.social_status) + " : " + family.getSocialStatus());
        sonsNumberTextView.setText(this.getResources().getString(R.string.sons_number) + " : " + family.getSonsNumber());
        workTextView.setText(this.getResources().getString(R.string.work) + " : " + family.getWork());
        addressTextView.setText(this.getResources().getString(R.string.address) + " : " + family.getAddress());
        incomeTextView.setText(this.getResources().getString(R.string.income) + " : " + family.getIncome());
        deptTextView.setText(this.getResources().getString(R.string.dept) + " : " + family.getDept());
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllFamiliesActivity.this, AddPeopleActivity.class);
                intent.putExtra("Family", family);
                startActivity(intent);
            }
        });

        childrenLinearLayout.removeAllViews();
        for (int i = 0; i < family.getChildrenNames().size();i++){
            addChildrenData(family.getChildrenNames().get(i), String.valueOf(family.getChildrenAges().get(i)));
        }

        startFamilyBottomSheet();
    }

    void addChildrenData(String name , String age){
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);
        childrenLinearLayout.addView(parent);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) deptTextView.getLayoutParams();
        params.setMargins((layoutParams.leftMargin+60) ,(layoutParams.topMargin-30),layoutParams.leftMargin,0);
        TextView childNameTextView = new TextView(this);
        TextView childAgeTextView = new TextView(this);
        childAgeTextView.setLayoutParams(params);
        childNameTextView.setLayoutParams(params);
        childNameTextView.setTextColor(getResources().getColor(R.color.mainActivityTextColor));
        childAgeTextView.setTextColor(getResources().getColor(R.color.mainActivityTextColor));
        childAgeTextView.setText(age);
        childNameTextView.setText(name);
        childAgeTextView.setTextSize(17);
        childNameTextView.setTextSize(17);
        parent.addView(childNameTextView);
        parent.addView(childAgeTextView);
    }

    void startFamilyBottomSheet() {
        familiesBottomSheetDialog.setContentView(familiesBottomSheetView);
        familiesBottomSheetDialog.show();
    }

    void startEventsBottomSheet(){
        eventsBottomSheetDialog.setContentView(eventsBottomSheetView);
        eventsBottomSheetDialog.show();
    }

    public void loadMoreData() {
        if (!searchedModeEnabled)
            familiesModelView.getAllFamiliesFromFirestore();
    }

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.all_families_recycler_view);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplayed(recyclerView)) {
                    loadMoreData();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!selectionModeEnabled) {
                    setBottomSheetData(families.get(position));
                } else {
                    if (selectedFamilyList.contains(families.get(position))) {
                        selectedFamilyList.remove(families.get(position));
                    } else {
                        selectedFamilyList.add(families.get(position));
                    }
                    if (selectedFamilyList.isEmpty()) {
                        selectionModeEnabled = false;
                        deactivateSelectionModeMenu();
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                selectedFamilyList.add(families.get(position));
                mAdapter.notifyDataSetChanged();
                selectionModeEnabled = true;
                activateSelectionModeMenu();
            }
        }));
    }

    private boolean isLastItemDisplayed(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();

            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    public void activateSelectionModeMenu() {
        menuToChoose = R.menu.all_families_menu_selection_mode;
        invalidateOptionsMenu();
    }

    public void deactivateSelectionModeMenu() {
        menuToChoose = R.menu.all_families_menu;
        invalidateOptionsMenu();
    }

    Menu optionMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuToChoose, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchedModeEnabled = false;
                loadMoreData();
                return true;
            }
        };
        if (menuToChoose == R.menu.all_families_menu) {
            // Search bar
            menu.findItem(R.id.search_bar).setOnActionExpandListener(onActionExpandListener);
            SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
            searchView.setQueryHint(getString(R.string.search_here));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String searchString) {
                    if (Character.isDigit(searchString.charAt(0))){
                        familiesModelView.searchById(searchString);
                    }else {
                        familiesModelView.searchByName(searchString , isHusbandSearch ,isWifeSearch , isAddressSearch);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.equals(""))
                        searchedModeEnabled = false;
                    else
                        searchedModeEnabled = true;
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.husband_search || item.getItemId() == R.id.wife_search ||
                item.getItemId() == R.id.address_search || item.getItemId() == R.id.id_search) {
            isAddressSearch = false;
            isHusbandSearch = false;
            isWifeSearch = false;
            isIdSearch = false;
            for (int i = 0; i < optionMenu.size(); i++) {
                optionMenu.getItem(i).setChecked(false);
            }
        }
        switch (item.getItemId()) {
            case R.id.add_item:
                startEventsBottomSheet();
                return true;
            case R.id.delete_item:
                deleteSelectedItems();
                return true;
            case R.id.husband_search:
                item.setChecked(true);
                isHusbandSearch = true;
                return true;
            case R.id.wife_search:
                item.setChecked(true);
                isWifeSearch = true;
                return true;
            case R.id.id_search:
                item.setChecked(true);
                isIdSearch = true;
                return true;
            case R.id.address_search:
                item.setChecked(true);
                isAddressSearch = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void deleteSelectedItems() {
        while (!selectedFamilyList.isEmpty()) {
            familiesModelView.deleteFamily(selectedFamilyList.get(0).getID());
            families.remove(selectedFamilyList.get(0));
            selectedFamilyList.remove(selectedFamilyList.get(0));
        }
        mAdapter.notifyDataSetChanged();
    }
}