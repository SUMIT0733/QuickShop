package com.example.quickshop.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.quickshop.CusOrderExtraDetails;
import com.example.quickshop.CusShopDetails;
import com.example.quickshop.R;
import com.example.quickshop.model.User_Detail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class ProfileFragment extends Fragment {
    View view;

    TextView name,email,phone;
    DatabaseReference ref;
    CardView shopDetail;
    FirebaseAuth auth;
    ACProgressFlower dialog;

    ActionBar actionBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_home,container, false);
        initView();
        dialog.show();

        ref = FirebaseDatabase.getInstance().getReference("customer").child(auth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Detail detail = snapshot.getValue(User_Detail.class);
                dialog.dismiss();

                if (detail == null){
                    Toast.makeText(getActivity(), "Profile is not available.. Please provide", Toast.LENGTH_SHORT).show();
                }else {
                    name.setText(detail.getName());
                    email.setText(detail.getEmail());
                    phone.setText(detail.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {            }


        });

        return view;


    }

    private void initView() {
        name = (TextView)view.findViewById(R.id.v_name);
        email = (TextView)view.findViewById(R.id.v_email);
        phone = (TextView)view.findViewById(R.id.v_phone);
        shopDetail = view.findViewById(R.id.shop_details);
        auth = FirebaseAuth.getInstance();

        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(R.color.grey_700)
                .bgColor(Color.WHITE)
                .textAlpha(1)
                .text("Please wait...")
                .textColor(Color.BLACK)
                .speed(15)
                .bgAlpha(1)
                .fadeColor(Color.WHITE)
                .build();
    }
}