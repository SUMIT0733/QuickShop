package com.example.quickshop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Sel_terms extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_terms);

        actionBar = getSupportActionBar();

        setScreen();

    }

    public void setScreen() {
        actionBar.setElevation(35);
        actionBar.setTitle("Terms & Conditions");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

}