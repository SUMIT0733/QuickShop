package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickshop.Adapter.Cart_Adapter;
import com.example.quickshop.model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Cus_Cart extends AppCompatActivity {

    LinearLayout back,next;
    String uid;
    ListView listView;
    TextView fAmount,checkout;
    DatabaseReference ref;
    ActionBar actionBar;
    List<CartModel> cartlist;
    ProgressDialog dialog;
    ImageView img;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus__cart);

        initView();
        setScreen();
        loadList();


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cus_Cart.this, CheckoutOrder.class).putExtra("Total_amount",total));
            }
        });
    }

    private void loadList() {
        cartlist.clear();
        dialog.show();
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total=0;
                cartlist.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    CartModel c_model = data.getValue(CartModel.class);
                    cartlist.add(c_model);
                    total += (c_model.getpAmount() * c_model.getpQuantity());
                }
                Cart_Adapter adapter = new Cart_Adapter(Cus_Cart.this,cartlist);
                listView.setAdapter(adapter);
                dialog.dismiss();
                fAmount.setText("\u20b9 "+total);

                if(cartlist.size() == 0){
                    listView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    checkout.setVisibility(View.GONE);
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

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("My Cart");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initView() {
        img  = findViewById(R.id.no_item);
        listView = findViewById(R.id.cart_items);
        fAmount = findViewById(R.id.final_amount);
        checkout = findViewById(R.id.check_out);
        ref = FirebaseDatabase.getInstance().getReference("cart");
        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");

        dialog = new ProgressDialog(Cus_Cart.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        cartlist = new ArrayList<>();

    }


}