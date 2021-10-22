package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quickshop.Adapter.FAQ_Adapter;
import com.example.quickshop.Adapter.FAQ_Sel_Adapter;
import com.example.quickshop.model.FAQ_Model;
import com.example.quickshop.model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.editdrawabletext.EditDrawableText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sel_Faq extends AppCompatActivity {

    ListView list;
    DatabaseReference ref, name_ref;
    String uid,cName,fid;
    List<FAQ_Model> faq_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel__faq);

        initView();
        loadList();
    }



    private void loadList(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faq_list.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    FAQ_Model c_model = data.getValue(FAQ_Model.class);
                    faq_list.add(c_model);
                }

                Comparator<FAQ_Model> compareById = (FAQ_Model o1, FAQ_Model o2) -> o1.getAnswer().compareTo( o2.getAnswer() );
                Collections.sort(faq_list, compareById);

                FAQ_Sel_Adapter adapter = new FAQ_Sel_Adapter(Sel_Faq.this,faq_list);
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void initView() {
        list=findViewById(R.id.faq_list1);

        ref = FirebaseDatabase.getInstance().getReference("FAQ");
        name_ref = FirebaseDatabase.getInstance().getReference("customer");
        uid =getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

        faq_list = new ArrayList<>();
    }


}