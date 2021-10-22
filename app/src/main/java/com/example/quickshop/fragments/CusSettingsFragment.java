package com.example.quickshop.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.quickshop.CusFAQ;
import com.example.quickshop.CusShopDetails;
import com.example.quickshop.CusTermsCondition;
import com.example.quickshop.R;
import com.example.quickshop.User_login;
import com.google.firebase.auth.FirebaseAuth;

public class CusSettingsFragment extends Fragment {
    View view;
    CardView shopDetails,Terms,FAQ,LogOut;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cus_setting, container, false);
        initView();

        shopDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CusShopDetails.class));
            }
        });

        Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CusTermsCondition.class));
            }
        });

        FAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CusFAQ.class));
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return view;
    }

    private void signOut() {
        auth = FirebaseAuth.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert")
                .setMessage("Do you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    auth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getContext(), User_login.class));
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    private void initView() {

       shopDetails = view.findViewById(R.id.shop_details);
       Terms = view.findViewById(R.id.cus_terms_conditions);
       FAQ = view.findViewById(R.id.faq);
       LogOut= view.findViewById(R.id.logout);

    }
}
