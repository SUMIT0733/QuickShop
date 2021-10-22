package com.example.quickshop.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quickshop.Adapter.NotificationAdapter;
import com.example.quickshop.CommonUtil;
import com.example.quickshop.R;
import com.example.quickshop.model.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class NotificationFragment extends Fragment {
    ListView new_list;
    DatabaseReference ref;
    String uid;
    ArrayList<NotificationModel> newlist;
    View view;
    ACProgressFlower dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_notification,container,false);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadList();

    }
    private void loadList() {
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.show();
                newlist.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    NotificationModel model = data.getValue(NotificationModel.class);
                    newlist.add(model);
                }
                Collections.reverse(newlist);
                NotificationAdapter newAdapter = new NotificationAdapter(getContext(),newlist);

                new_list.setAdapter(newAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

    }

    private void initView() {
        new_list = view.findViewById(R.id.new_list);
        newlist = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Notification");
        uid = getContext().getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

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
