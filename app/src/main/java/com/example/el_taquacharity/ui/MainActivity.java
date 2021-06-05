package com.example.el_taquacharity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.example.el_taquacharity.R;
import com.example.el_taquacharity.databinding.ActivityMainBinding;
import com.example.el_taquacharity.ui.Activities.AddPeopleActivity;
import com.example.el_taquacharity.ui.Activities.AllFamiliesActivity;
import com.example.el_taquacharity.ui.Activities.EventsActivity;
import com.example.el_taquacharity.ui.Activities.ScanQrCodeActivity;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBinding();
        clickEvents();
    }

    void initBinding(){
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    void clickEvents(){
        binding.addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddPeopleActivity.class));
            }
        });

        binding.scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanQrCodeActivity.class));
            }
        });

        binding.allPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllFamiliesActivity.class));
            }
        });

        binding.createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EventsActivity.class));
            }
        });
    }
}