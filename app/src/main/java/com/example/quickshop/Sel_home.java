package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.Adapter.Sel_Item_Adapter;
import com.example.quickshop.Adapter.Sel_Order_Adapter;
import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.Item_model;
import com.example.quickshop.model.OrderModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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

public class Sel_home extends AppCompatActivity {

    ConnectivityManager connectivityManager;
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
    FloatingActionButton category,com_orders,Sel_term,sel_faq,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_home);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        initView();
        setScreen();

        data=getData();
        wifi = getwifi();
        if(!wifi  && !data){
            //Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(Sel_home.this);
            builder.setTitle("Error")
                    .setMessage("No internet connection.Try again.")
                    .setCancelable(false)
                    .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    }).show();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item = getResources().getStringArray(R.array.choose_shop)[position];
                loadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {   }
        });

        category.setOnClickListener(v -> {
            Intent intent = new Intent(Sel_home.this, Sel_Manage_Category.class);
            startActivity(intent); });

        com_orders.setOnClickListener(v -> {
            Intent intent = new Intent(Sel_home.this, Sel_Complete_Order.class);
            startActivity(intent); });

        Sel_term.setOnClickListener(v -> {
            Intent intent = new Intent(Sel_home.this, Sel_terms.class);
            startActivity(intent); });

        sel_faq.setOnClickListener(v -> {
            Intent intent = new Intent(Sel_home.this, Sel_Faq.class);
            startActivity(intent); });

        logout.setOnClickListener(v -> {

            auth = FirebaseAuth.getInstance();
            AlertDialog.Builder builder = new AlertDialog.Builder(Sel_home.this);
            builder.setTitle("Alert")
                    .setMessage("Do you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        auth.signOut();
                        finish();
                        startActivity(new Intent(this, User_login.class));
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
//
//            auth.signOut();
//            finish();
//            Intent intent = new Intent(Sel_home.this, User_login.class);
//            startActivity(intent);

        });
    }

    private void loadList() {
        orderList.clear();
        dialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {

                    OrderModel Order_model = data.getValue(OrderModel.class);

//                    Log.e("+++++++++", Order_model.getPick_chooseBranch()+" "+selected_item+" "+String.valueOf(Order_model.getPick_chooseBranch().equals(selected_item)));

                    if((Order_model.getOrderStatus() == 0 || Order_model.getOrderStatus() == 1 || Order_model.getOrderStatus() == 2)
                            && Order_model.getPick_chooseBranch().equals(selected_item)) {
                        orderList.add(Order_model);
                    }
                }

                Comparator<OrderModel> compareById = (OrderModel o1, OrderModel o2) -> o1.getPickdate().compareTo( o2.getPickdate() );
                Collections.sort(orderList, compareById);

                Sel_Order_Adapter adapter = new Sel_Order_Adapter(Sel_home.this,orderList);
                listView.setAdapter(adapter);
                dialog.dismiss();

                if(orderList.size() == 0){
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Sel_home.this,Sel_OrderExtraDetail.class).putExtra("Order_ID",orderList.get(position).getoID()));
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

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("QuickSHOP SELLER");
    }

    private void initView() {

        category = (FloatingActionButton) findViewById(R.id.sel_edit_category);
        com_orders = (FloatingActionButton) findViewById(R.id.sel_complete_order);
        Sel_term =  (FloatingActionButton) findViewById(R.id.sel_terms);
        sel_faq =  (FloatingActionButton) findViewById(R.id.sel_faq);
        logout = (FloatingActionButton) findViewById(R.id.sel_logout);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Orders");

        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

        dialog = new ProgressDialog(Sel_home.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        img  = findViewById(R.id.no_item2);
        img.setVisibility(View.INVISIBLE);
        spinner= findViewById(R.id.spinnershop);
        listView=findViewById(R.id.olist);
        orderList = new ArrayList<>();
    }
}