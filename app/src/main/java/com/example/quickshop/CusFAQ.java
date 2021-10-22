package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quickshop.Adapter.FAQ_Adapter;
import com.example.quickshop.model.FAQ_Model;
import com.example.quickshop.model.User_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.mindorks.editdrawabletext.OnDrawableClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CusFAQ extends AppCompatActivity {

    ListView list;
    EditDrawableText editText;
    DatabaseReference ref, name_ref;
    String uid, cName;
    List<FAQ_Model> faq_list;
    String fid;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_f_a_q);

        initView();
        loadList();
        setScreen();


        name_ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Detail detail = snapshot.getValue(User_Detail.class);
                cName = detail.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        editText.setDrawableClickListener(new OnDrawableClickListener() {
            @Override
            public void onClick(@NotNull DrawablePosition drawablePosition) {

                if (drawablePosition == DrawablePosition.RIGHT) {
                    fid = ref.push().getKey();
                    String question = editText.getText().toString().trim();
                    String answer = "Not Answered by Seller.";
//                    Toast.makeText(CusFAQ.this, cName+"", Toast.LENGTH_SHORT).show();

                    ref.child(fid).setValue(new FAQ_Model(fid, question, answer, cName, Calendar.getInstance().getTimeInMillis(), false)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CusFAQ.this, "Query Posted Successfully!", Toast.LENGTH_SHORT).show();
//                            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            editText.setText("");
                        }
                    });
                }
            }
        });
    }

    private void loadList() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faq_list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FAQ_Model c_model = data.getValue(FAQ_Model.class);
                    faq_list.add(c_model);
                }

                FAQ_Adapter adapter = new FAQ_Adapter(CusFAQ.this, faq_list);
                Collections.reverse(faq_list);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        list = findViewById(R.id.faq_list);
        editText = findViewById(R.id.edit_text_question);

        ref = FirebaseDatabase.getInstance().getReference("FAQ");
        name_ref = FirebaseDatabase.getInstance().getReference("customer");
        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID", "none");

        faq_list = new ArrayList<>();


    }

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("FAQ");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}