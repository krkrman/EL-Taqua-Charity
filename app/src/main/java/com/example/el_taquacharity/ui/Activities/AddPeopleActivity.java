package com.example.el_taquacharity.ui.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.el_taquacharity.R;
import com.example.el_taquacharity.databinding.ActivityAddPeopleBinding;
import com.example.el_taquacharity.pojo.Family;
import com.example.el_taquacharity.ui.viewmodel.FamiliesModelView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class AddPeopleActivity extends AppCompatActivity {

    Toolbar toolbar;
    Family family;
    ActivityAddPeopleBinding binding;
    FamiliesModelView familiesModelView;
    int numberOfGeneratedEditText = 0;
    ArrayList<EditText> allGeneratedChildNameEditTexts;
    ArrayList<EditText> allGeneratedChildAgeEditTexts ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPeopleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allGeneratedChildAgeEditTexts = new ArrayList<>();
        allGeneratedChildNameEditTexts = new ArrayList<>();
        getDataFromIntent();
        initToolbar();
        clickEvents();
        familiesModelView = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(FamiliesModelView.class); // this is the instance of AndroidViewModel

        familiesModelView.isAdded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(AddPeopleActivity.this, R.string.addedSuccessfully, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPeopleActivity.this, R.string.failed_to_add, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        binding.sonsNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int numberOfChildren;
                if (s.toString().isEmpty()) {
                    for (int i = 0; i < numberOfGeneratedEditText; i++) {
                        removeEditText();
                        allGeneratedChildAgeEditTexts.clear();
                        allGeneratedChildNameEditTexts.clear();
                    }
                    allGeneratedChildNameEditTexts.clear();
                } else {
                    numberOfChildren = Integer.parseInt(s.toString());
                    for (int i = 0; i < numberOfChildren; i++) {
                        addChildrenEditText(String.valueOf(i + 1));
                    }
                    numberOfGeneratedEditText = numberOfChildren;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void addChildrenEditText(String title) {
        numberOfGeneratedEditText++;
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);
        binding.linearLayout.addView(parent);
        EditText childNameEditText = new EditText(this);
        EditText childAgeEditText = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        childNameEditText.setLayoutParams(p);
        childAgeEditText.setLayoutParams(p);
        childNameEditText.setHint("Child " + title + " Name");
        childAgeEditText.setHint("Child " + title + " Age");
        childAgeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        childAgeEditText.setFadingEdgeLength(2);
        childNameEditText.setTextColor(getResources().getColor(R.color.mainActivityTextColor));
        childAgeEditText.setTextColor(getResources().getColor(R.color.mainActivityTextColor));
        parent.addView(childNameEditText);
        parent.addView(childAgeEditText);
        allGeneratedChildNameEditTexts.add(childNameEditText);
        allGeneratedChildAgeEditTexts.add(childAgeEditText);
    }

    public void removeEditText() {
        binding.linearLayout.removeViewAt(binding.linearLayout.getChildCount() - 1);
    }

    void initToolbar() {
        toolbar = findViewById(R.id.add_family_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to put arrow back
        getSupportActionBar().setTitle(R.string.add_family);
    }

    public void getDataFromIntent() {
        Family family = getIntent().getParcelableExtra("Family");
        if (family != null) {
            binding.idEditText.setEnabled(false);
            binding.button.setText(getText(R.string.updateData));
            binding.idEditText.setText(family.getID());
            binding.husbandNameEditText.setText(family.getHusbandName());
            binding.wifeNameEditText.setText(family.getWifeName());
            binding.sonsNumberEditText.setText(family.getSonsNumber());
            binding.workEditText.setText(family.getWork());
            binding.phoneNumberEditText.setText(family.getPhoneNumber());
            binding.adressEditText.setText(family.getAddress());
            binding.husbandNameEditText.setText(family.getHusbandName());
            binding.incomeEditText.setText(String.valueOf(family.getIncome()));
            binding.deptEditText.setText(String.valueOf(family.getDept()));
            setSocialStatus(family.getSocialStatus());
            setStatus(family.getStatus());
        }
    }

    void clickEvents() {
        binding.addFamilyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFamilyToFirestore();
            }
        });
    }

    void addFamilyToFirestore() {
        String id = binding.idEditText.getText().toString();
        if (id.length() != 14) {
            binding.idEditText.setError(getText(R.string.errorIdString));
            return;
        }
        String husbandName = binding.husbandNameEditText.getText().toString();
        String wifeName = binding.wifeNameEditText.getText().toString();
        if (husbandName.isEmpty() && wifeName.isEmpty()) {
            binding.husbandNameEditText.setError(getText(R.string.husbandOrWifeNameRequired));
            binding.wifeNameEditText.setError(getText(R.string.husbandOrWifeNameRequired));
            return;
        }
        String sonsNumber = binding.sonsNumberEditText.getText().toString();
        String work = binding.workEditText.getText().toString();
        String phoneNumber = binding.phoneNumberEditText.getText().toString();
        String address = binding.adressEditText.getText().toString();
        int income = 0;
        if (!binding.incomeEditText.getText().toString().isEmpty()) {
            income = Integer.parseInt(binding.incomeEditText.getText().toString());
        }
        int dept = 0;
        if (!binding.deptEditText.getText().toString().isEmpty()) {
            dept = Integer.parseInt(binding.deptEditText.getText().toString());
        }
        String others = "";
        if (!binding.othersEditText.getText().toString().isEmpty()) {
            others = binding.othersEditText.getText().toString();
        }
        String status = getStatus();
        String socialStatus = getSocialStatus();
        ArrayList<String> childrenNames = new ArrayList<>();
        ArrayList<Integer> childrenAges = new ArrayList<>();
        for (int i = 0; i < numberOfGeneratedEditText; i++) {
            if (allGeneratedChildNameEditTexts.get(i).getText().toString().isEmpty()) {
                childrenNames.add("Not Specified yet");
            } else {
                childrenNames.add(allGeneratedChildNameEditTexts.get(i).getText().toString());
            }
            if (allGeneratedChildAgeEditTexts.get(i).getText().toString().isEmpty()) {
                childrenAges.add(0);
            } else {
                childrenAges.add(Integer.valueOf(allGeneratedChildAgeEditTexts.get(i).getText().toString()));
            }
        }
        boolean unableToEarn = binding.isUnableToEarnCheckbox.isChecked();
        boolean isOld = binding.isOldCheckbox.isChecked();
        boolean isOrphans = binding.isOrphansCheckbox.isChecked();
        boolean hasEndemic = binding.hasEndemicCheckbox.isChecked();

        family = new Family(others, husbandName, wifeName, socialStatus, sonsNumber, work, phoneNumber
                , address, status, income, dept, childrenNames, childrenAges, unableToEarn, isOrphans, isOld, hasEndemic);
        familiesModelView.addFamilyToFirestore(family, id);
    }

    private String getStatus() {
        int radioId = binding.statusRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        if (radioButton != null)
            return radioButton.getText().toString();
        else
            return "0";
    }

    public void setStatus(String status) {
        switch (status) {
            case "1":
                binding.one.setChecked(true);
                break;
            case "2":
                binding.two.setChecked(true);
                break;
            case "3":
                binding.three.setChecked(true);
                break;
            case "4":
                binding.four.setChecked(true);
                break;
        }
    }

    private String getSocialStatus() {
        int radioId = binding.socialStatusRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        if (radioButton != null)
            return radioButton.getText().toString();
        else
            return "0";
    }

    public void setSocialStatus(String socialStatus) {
        switch (socialStatus) {
            case "Married":
                binding.married.setChecked(true);
                binding.sonsNumberEditText.setEnabled(true);
                binding.sonsNumberEditText.setFocusable(true);
                break;
            case "Single":
                binding.single.setChecked(true);
                binding.sonsNumberEditText.setEnabled(false);
                binding.sonsNumberEditText.setFocusable(false);
                break;
            case "Divorced":
                binding.divorced.setChecked(true);
                binding.sonsNumberEditText.setEnabled(true);
                binding.sonsNumberEditText.setFocusable(true);
                break;
            case "Widow":
                binding.widow.setChecked(true);
                binding.sonsNumberEditText.setEnabled(true);
                binding.sonsNumberEditText.setFocusable(true);
                break;
        }
    }
}