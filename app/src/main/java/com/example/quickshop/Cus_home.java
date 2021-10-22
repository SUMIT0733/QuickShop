package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.quickshop.fragments.CusSettingsFragment;
import com.example.quickshop.fragments.HomeFragment;
import com.example.quickshop.fragments.OrderFragment;
import com.example.quickshop.fragments.ProfileFragment;
import com.example.quickshop.model.CartModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.steelkiwi.library.view.BadgeHolderLayout;

public class Cus_home extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    boolean data,wifi;
    BottomNavigationView navigation;
    Fragment fragment;
    FirebaseAuth auth;
    Toolbar toolbar;
    DatabaseReference cart_ref,not_ref;
    BadgeHolderLayout cart,notification;
    int cart_count=0, not_count=0, old_not_count=0;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_home);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        navigation = findViewById(R.id.navigation);

        toolbar= findViewById(R.id.toolbar);
        cart = findViewById(R.id.cart_badge);
        notification = findViewById(R.id.notification_badge);

        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

        cart_ref = FirebaseDatabase.getInstance().getReference("cart");
        not_ref = FirebaseDatabase.getInstance().getReference("Notification");

        setSupportActionBar(toolbar);

        NotCount();
        cartCout();

        fragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack("home1");
        transaction.commit();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, fragment);
                        transaction.commit();
                        break;

                    case R.id.order:
                        fragment = new OrderFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.setting:
                        fragment = new CusSettingsFragment();
                        loadFragment(fragment);
                        break;
//                    case R.id.nav_faq:
//                        fragment = new CusFAQFragment();
//                        loadFragment(fragment);
//                        break;
//                    case R.id.logout:
//                       signOut();
//                       break;
                }
                return true;
            }
        });

        data=getData();
        wifi = getwifi();
        if(!wifi  && !data){
            //Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(Cus_home.this);
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

        cart.setOnClickListener(v -> {
            startActivity(new Intent(Cus_home.this, Cus_Cart.class));
        });

        notification.setOnClickListener(v -> {
            startActivity(new Intent(Cus_home.this, Cus_Notification.class));
        });
    }

//    private void signOut() {
//        auth = FirebaseAuth.getInstance();
//        AlertDialog.Builder builder = new AlertDialog.Builder(Cus_home.this);
//        builder.setTitle("Alert")
//                .setMessage("Do you want to Logout?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", (dialog, which) -> {
//                    auth.signOut();
//                    finish();
//                    startActivity(new Intent(this, User_login.class));
//                })
//                .setNegativeButton("Cancel", (dialogInterface, i) -> {
//                    dialogInterface.dismiss();
//                }).show();
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

    public void cartCout(){
        cart_ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cart_count=0;
                for (DataSnapshot data : snapshot.getChildren())
                {
                    CartModel model = data.getValue(CartModel.class);
                    cart_count += model.getpQuantity();
                }
                cart.setCountWithAnimation(cart_count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void NotCount(){
        not_ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                not_count=0;
                for (DataSnapshot data : snapshot.getChildren())
                {
                    not_count++;
                }
                notification.setCountWithAnimation(not_count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotCount();
        cartCout();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        NotCount();
        cartCout();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(navigation.getSelectedItemId() == R.id.home){
            finish();
        }
        else
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(fm.getBackStackEntryCount() > 1)
            {
                fm.popBackStack("home1",0);
                navigation.setSelectedItemId(R.id.home);
                ft.commit();
            }
            else{
                finish();
            }
        }
    }

}