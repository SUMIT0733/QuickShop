package com.example.quickshop.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.quickshop.Cus_View_Item_Activity;
import com.example.quickshop.R;

public class HomeFragment extends Fragment {

    View view;
    CardView sgrocery,sdairy,smasala,sdryfruits,spacket,sother;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cus_home,container, false);
        initView();
        return view;
    }

    public void initView() {
        sgrocery = view.findViewById(R.id.sgrocery);
        sdairy = view.findViewById(R.id.sdairy);
        smasala = view.findViewById(R.id.smasala);
        sdryfruits = view.findViewById(R.id.sdryfruits);
        spacket = view.findViewById(R.id.spacket);
        sother = view.findViewById(R.id.sother);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sgrocery.setOnClickListener(v -> newActivity("Grocery"));
        sdairy.setOnClickListener(v -> newActivity("Dairy"));
        smasala.setOnClickListener(v -> newActivity("Masala"));
        sdryfruits.setOnClickListener(v -> newActivity("DryFruits"));
        spacket.setOnClickListener(v -> newActivity("Food Package"));
        sother.setOnClickListener(v -> newActivity("Other"));
    }

    public void newActivity(String value) {
        startActivity(new Intent(getContext(), Cus_View_Item_Activity.class).putExtra("Category",value));
    }
}
