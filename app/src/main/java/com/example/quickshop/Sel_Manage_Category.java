package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.quickshop.extra.model;
//import com.example.quickshop.ViewHolder.Sel_ItemHolder;
import com.example.quickshop.Adapter.Sel_Item_Adapter;
import com.example.quickshop.model.Item_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sel_Manage_Category extends AppCompatActivity {

    ActionBar actionBar;
    ConnectivityManager connectivityManager;
    SwipeRefreshLayout swipe;

    boolean data,wifi;

    FloatingActionButton buton;
    DatabaseReference reference;
    ProgressDialog dialog;
    Spinner spinner;
    ArrayList<Item_model> list;

    ListView listView;
    Sel_Item_Adapter adapter;
    String selected_item = "Grocery";
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel__manage__category);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        actionBar = getSupportActionBar();

        initView();
        setScreen();
        data=getData();
        wifi = getwifi();

        if(!wifi  && !data){
            Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(Sel_Manage_Category.this);
            builder.setTitle("Error")
                    .setMessage("No internet connection.Try again.")
                    .setCancelable(false)
                    .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }else {
            list.clear();
            loadList();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wifi = getwifi();
                data = getData();
                swipe.setRefreshing(false);
                if(!wifi  && !data){
                    Toast.makeText(Sel_Manage_Category.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Sel_Manage_Category.this);
                    builder.setTitle("Error")
                            .setMessage("No internet connection.Try again.")
                            .setCancelable(false)
                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }else {
                    list.clear();
                    loadList();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item = getResources().getStringArray(R.array.category)[position];
                loadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DialogFragment dia = new Sel_Add_Item();
                dia.setCancelable(true);
                dia.show(ft,"Add Item");
            }
        });
    }

    private void loadList() {
        list.clear();
        dialog.show();
        reference.child(selected_item).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    Item_model Item_model = data.getValue(Item_model.class);
                    list.add(Item_model);
                }
                Sel_Item_Adapter adapter = new Sel_Item_Adapter(Sel_Manage_Category.this,list,selected_item);
                listView.setAdapter(adapter);
                dialog.dismiss();
                if(list.size() == 0){
                    listView.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Sel_Manage_Category.this, position+" clicked", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean getData() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        data = networkInfo.isConnected();
        return data;

    }

    public boolean getwifi() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        wifi = networkInfo.isConnected();
        return wifi;
    }

    public void setScreen() {
        actionBar.setElevation(35);
        actionBar.setTitle("Manage Category");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void initView() {
        swipe=(SwipeRefreshLayout) findViewById(R.id.swipe);
        buton =(FloatingActionButton) findViewById(R.id.add_item);
        reference = FirebaseDatabase.getInstance().getReference("Items");
        dialog = new ProgressDialog(Sel_Manage_Category.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        listView = findViewById(R.id.listview);

        list=new ArrayList<>();
        spinner = findViewById(R.id.spinner1);
        text = findViewById(R.id.no_item);
        text.setVisibility(View.INVISIBLE);
    }

}