package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import in.shadowfax.proswipebutton.ProSwipeButton;

public class SelCompleteOrderDetails extends AppCompatActivity {

    String order_id,order_item="";
    TextView cusDetail,selectBranch,orderId,pickupTime,orderTime,orderRupees,orderTotalItems,orderItemList,complete_time,soc_cmpltstatus;
    ImageView StatusIMG;
    int i=1, sum = 0;
    ActionBar actionBar;

    DatabaseReference ref;
    FirebaseAuth auth;
    ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_complete_order_details);

        initView();
        setScreen();

        dialog = new ACProgressFlower.Builder(SelCompleteOrderDetails.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(R.color.colorPrimary)
                .bgColor(Color.WHITE)
                .textAlpha(1)
                .text("Please wait...")
                .textColor(R.color.colorPrimary)
                .speed(15)
                .bgAlpha(1)
                .fadeColor(Color.WHITE)
                .build();

        dialog.show();

        ref.child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderModel model = snapshot.getValue(OrderModel.class);

                cusDetail.setText(""+ model.getcName() + "( "+ model.getcContact() +" )");
                selectBranch.setText(model.getPick_chooseBranch());
                orderId.setText("Order ID ."+ "QS77" +model.getoID());
                pickupTime.setText("" +model.getPickdate() + "( " +model.getPick_chooseTime()+ " )");
                orderTime.setText(new Sel_OrderExtraDetail().getDate(Long.parseLong(model.getTimeStamp())));
                orderRupees.setText("\u20b9 " + model.getPay_amount());
                complete_time.setText(""+new Sel_OrderExtraDetail().getDate(Long.parseLong(model.getO_C_Time())));

                if(model.getOrderStatus() == -1){
                    soc_cmpltstatus.setText("Order Cancelled");
                    soc_cmpltstatus.setTextColor(getResources().getColor(R.color.red_900));
                }
                if(model.getOrderStatus() == -2){
                    soc_cmpltstatus.setText("Order Rejected");
                    soc_cmpltstatus.setTextColor(getResources().getColor(R.color.red_900));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child(order_id).child("Order_items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order_item = "";
                int sum = 0;
                for (DataSnapshot data : snapshot.getChildren())
                {
                    CartModel c_model = data.getValue(CartModel.class);
                    order_item += i+". "+c_model.getpName()+" ( "+c_model.getpAmount()+" X "+c_model.getpQuantity()+" )\n";
                    sum += c_model.getpQuantity();
                    i++;

                }
                orderItemList.setText(order_item);
                orderTotalItems.setText("Total Quantity = "+ sum);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("Past Orders");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initView() {
        order_id = getIntent().getStringExtra("Com_Order_ID");

        StatusIMG = findViewById(R.id.status_icon);
        cusDetail = findViewById(R.id.sed_cus_detail);
        selectBranch = findViewById(R.id.sed_branch);
        orderId = findViewById(R.id.sed_orderid);
        pickupTime = findViewById(R.id.sed_pickuptime);
        orderTime = findViewById(R.id.sed_ordertime);
        orderRupees = findViewById(R.id.sed_orderprice);
        orderTotalItems = findViewById(R.id.sed_totalitems);
        orderItemList = findViewById(R.id.sed_product_name);
        complete_time = findViewById(R.id.soc_cmplttime);
        soc_cmpltstatus = findViewById(R.id.soc_cmpltstatus);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Orders");
    }
}