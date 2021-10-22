package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quickshop.Adapter.NotificationAdapter;
import com.example.quickshop.model.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Cus_Notification extends AppCompatActivity {

    ListView new_list;
    DatabaseReference ref;
    String uid;
    ArrayList<NotificationModel> newlist;
    ACProgressFlower dialog;
    ActionBar actionBar;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus__notification);

        initView();
        setScreen();
        dialog.show();
        loadList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_notification,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                deleteNotification();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteNotification() {
        ref.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Cus_Notification.this, "Notification cleared...", Toast.LENGTH_SHORT).show();
                edit.putInt(CommonUtil.Pref_count,0).commit();
                Toast.makeText(Cus_Notification.this, "0 done", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadList() {
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newlist.clear();
                count=0;
                for (DataSnapshot data : snapshot.getChildren())
                {
                    NotificationModel model = data.getValue(NotificationModel.class);
                    newlist.add(model);
                    count++;
                }
                Collections.reverse(newlist);
                NotificationAdapter newAdapter = new NotificationAdapter(Cus_Notification.this,newlist);
                new_list.setAdapter(newAdapter);
                dialog.dismiss();
                edit.putInt(CommonUtil.Pref_count,count).commit();
                Toast.makeText(Cus_Notification.this, ""+count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

    }

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("Notifications");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initView() {
        new_list = findViewById(R.id.new_list);
        newlist = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Notification");
        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

        dialog = new ACProgressFlower.Builder(Cus_Notification.this)
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

        preferences = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE);
        edit = preferences.edit();

    }
}