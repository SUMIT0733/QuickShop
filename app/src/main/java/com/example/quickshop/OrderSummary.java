package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderSummary extends AppCompatActivity {

    DatabaseReference ref;

    TextView OID, OitemDetail, TA, PA, ShopName, ShopAddress, pickupdate, pickupTime, uName, uContact;
    Button next_btn;
    String order_id,order_item="";
    ProgressDialog dialog;
    int order_status=0,sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);

        initView();
        ref.child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderModel model = snapshot.getValue(OrderModel.class);

                OID.setText("QS77"+model.getoID());
                TA.setText(String.valueOf(model.getTotal_amount()));
                PA.setText(String.valueOf(model.getPay_amount()));
                ShopName.setText(model.getPick_chooseBranch());
                pickupdate.setText(model.getPickdate());
                pickupTime.setText(model.getPick_chooseTime());
                uName.setText(model.getcName());
                uContact.setText(model.getcContact());

                switch(model.getPick_chooseBranch()){
                    case "QuickSHOP ( KATARGAM )":
                        ShopAddress.setText(CommonUtil.kat_add);
                        break;
                    case "QuickSHOP ( VARACHHA )":
                        ShopAddress.setText(CommonUtil.var_add);
                        break;
                    case "QuickSHOP ( ADAJAN )":
                        ShopAddress.setText(CommonUtil.ada_add);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        ref.child(order_id).child("Order_items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order_item = "";
                int sum = 0,i=1;

                for (DataSnapshot data : snapshot.getChildren())
                {
                    CartModel c_model = data.getValue(CartModel.class);
                    order_item += i+". "+c_model.getpName()+" ( "+c_model.getpAmount()+" X "+c_model.getpQuantity()+" )\n";
                    sum += c_model.getpQuantity();
                    i++;
                }
                OitemDetail.setText(order_item);
//                orderTotalItems.setText("Total Quantity = "+ sum);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        next_btn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(OrderSummary.this, Cus_home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });
    }

    private void initView() {
        OID = findViewById(R.id.sum_order_id);
        OitemDetail = findViewById(R.id.sum_item_detail);
        TA = findViewById(R.id.sum_total_amt);
        PA = findViewById(R.id.sum_payable_amt);
        ShopName = findViewById(R.id.sum_shop_name);
        ShopAddress = findViewById(R.id.sum_shop_address);
        pickupdate = findViewById(R.id.sum_pickup_date);
        pickupTime = findViewById(R.id.sum_pickup_time);
        uName = findViewById(R.id.sum_user_name);
        uContact = findViewById(R.id.sum_user_contact);
        next_btn = findViewById(R.id.sum_next_btn);

        ref = FirebaseDatabase.getInstance().getReference("Orders");

        order_id = getIntent().getStringExtra("OrderId");

        dialog = new ProgressDialog(OrderSummary.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, Cus_home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}