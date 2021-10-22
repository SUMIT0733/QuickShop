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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.Adapter.Sel_Order_Adapter;
import com.example.quickshop.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sel_Complete_Order extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    SwipeRefreshLayout swipe;
    FirebaseAuth auth;
    DatabaseReference ref;
    String uid;
    boolean data,wifi;
    ProgressDialog dialog;
    ActionBar actionBar;
    List<OrderModel> orderList;
    ListView listView;
    Spinner spinner;
    String selected_item = "QuickSHOP( Katargam )";
    ImageView img;
    Sel_Order_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel__complete__order);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        initView();
        loadList();
        setScreen();

        data=getData();
        wifi = getwifi();

        if(!wifi  && !data){
            Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(Sel_Complete_Order.this);
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
            orderList.clear();
            loadList();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item = getResources().getStringArray(R.array.choose_shop)[position];
                loadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //loadList();
            }
        });

    }

    private void loadList() {
        orderList.clear();
        dialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
//                    Toast.makeText(Sel_home.this, orderList+"11", Toast.LENGTH_SHORT).show();
                    OrderModel Order_model = data.getValue(OrderModel.class);

                    if((Order_model.getOrderStatus() == 3 || Order_model.getOrderStatus() == -1|| Order_model.getOrderStatus() == -2)
                            && Order_model.getPick_chooseBranch().equals(selected_item)) {
                        orderList.add(Order_model);
                    }
                }

                Comparator<OrderModel> compareById = (OrderModel o1, OrderModel o2) -> o1.getTimeStamp().compareTo( o2.getTimeStamp() );
                Collections.sort(orderList, compareById);
                Collections.reverse(orderList);

                Sel_Order_Adapter adapter = new Sel_Order_Adapter(Sel_Complete_Order.this, orderList);
                listView.setAdapter(adapter);
                dialog.dismiss();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        orderList.get(position);
                        startActivity(new Intent(Sel_Complete_Order.this, SelCompleteOrderDetails.class).putExtra("Com_Order_ID", orderList.get(position).getoID()));
                    }
                });

                if (orderList.size() == 0) {
                    listView.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(Sel_Complete_Order.this, position + " clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.selsearch,menu);
//
//        SearchView mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        mSearchView.setQueryHint("Type a product name");
//        LinearLayout searchEditFrame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
//        ((LinearLayout.LayoutParams) searchEditFrame.getLayoutParams()).leftMargin = 0;
//        ((LinearLayout.LayoutParams) searchEditFrame.getLayoutParams()).rightMargin = 0;
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.filter(newText);
//                return true;
//            }
//        });
//        return true;
//    }

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

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("Past Orders");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initView() {
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Orders");

        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

        dialog = new ProgressDialog(Sel_Complete_Order.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        img  = findViewById(R.id.com_no_item);
        img.setVisibility(View.INVISIBLE);
        spinner= findViewById(R.id.com_spinner);
        listView=findViewById(R.id.com_list);
        orderList = new ArrayList<>();

    }

}