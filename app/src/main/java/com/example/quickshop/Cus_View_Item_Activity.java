package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.Adapter.Cus_Item_Adapter;
import com.example.quickshop.Adapter.Sel_Item_Adapter;
import com.example.quickshop.model.Item_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cus_View_Item_Activity extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    boolean data,wifi;
    ActionBar actionBar;
    SwipeRefreshLayout swipe;
    DatabaseReference reference;
    ProgressDialog dialog;
    ArrayList<Item_model> list;

    ImageView img;
    ListView listView;
    String category = "";

    Cus_Item_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus__view__item_);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        actionBar = getSupportActionBar();

        initView();
        setScreen();
        data=getData();
        wifi = getwifi();

        if(!wifi  && !data){
            Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(Cus_View_Item_Activity.this);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


    private void loadList() {
        list.clear();
        dialog.show();
        reference.child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    Item_model Item_model = data.getValue(Item_model.class);
                    list.add(Item_model);
                }
                adapter = new Cus_Item_Adapter(Cus_View_Item_Activity.this,list);
                listView.setAdapter(adapter);
                dialog.dismiss();

                if(list.size() == 0){
                    listView.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        SearchView mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setQueryHint("Type a product name");
        LinearLayout searchEditFrame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ((LinearLayout.LayoutParams) searchEditFrame.getLayoutParams()).leftMargin = 0;
        ((LinearLayout.LayoutParams) searchEditFrame.getLayoutParams()).rightMargin = 0;
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(Cus_View_Item_Activity.this,Cus_Cart.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void initView() {
        swipe=(SwipeRefreshLayout) findViewById(R.id.swipe);


        reference = FirebaseDatabase.getInstance().getReference("Items");
        dialog = new ProgressDialog(Cus_View_Item_Activity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        listView = findViewById(R.id.listview_c);

        list=new ArrayList<>();
        img = findViewById(R.id.c_no_item);
        img.setVisibility(View.INVISIBLE);

        category = getIntent().getStringExtra("Category");
    }

    private void setScreen() {
        actionBar.setElevation(35);
        actionBar.setTitle(category);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}