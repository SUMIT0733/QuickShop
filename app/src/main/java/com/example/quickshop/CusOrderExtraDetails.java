package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import in.shadowfax.proswipebutton.ProSwipeButton;

public class CusOrderExtraDetails extends AppCompatActivity {

    String order_id,order_item="";
    TextView cusDetail,selectBranch,branchAddress,orderId,pickupTime,orderTime,orderRupees,orderTotalItems,orderItemList,orderCompletTime;
    CardView o_delete;
    ImageView StatusIMG;
    int i=1,order_status=0,sum = 0;
    int orderStatus;
    ActionBar actionBar;

    DatabaseReference ref,refcus;
    FirebaseAuth auth;
    ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_order_extra_details);

        initView();
        setScreen();

        dialog = new ACProgressFlower.Builder(CusOrderExtraDetails.this)
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderModel model = snapshot.getValue(OrderModel.class);

                cusDetail.setText(""+ model.getcName() + "( "+ model.getcContact() +" )");
                selectBranch.setText(model.getPick_chooseBranch());
                orderId.setText("Order ID ."+ "QS77" +model.getoID());
                pickupTime.setText("" +model.getPickdate() + "( " +model.getPick_chooseTime()+ " )");
                orderTime.setText("Ordered : " + getDate(Long.parseLong(model.getTimeStamp())));

                orderRupees.setText("\u20b9 " + model.getPay_amount());

                order_status = model.getOrderStatus();
                switch(order_status){
                    case 0:
                        StatusIMG.setImageResource(R.drawable.pending);
                        break;
                    case 1:
                        StatusIMG.setImageResource(R.drawable.packing);
                        break;
                    case 2:
                        StatusIMG.setImageResource(R.drawable.pack_giphy);
                        break;
                    case 3:
                        StatusIMG.setImageResource(R.drawable.iconqs);
                        break;
                    case -1:
                        StatusIMG.setImageResource(R.drawable.deleted);
                        break;
                    case -2:
                        StatusIMG.setImageResource(R.drawable.deleted);
                        break;

                }

                if(order_status == 3){
                    orderCompletTime.setVisibility(View.VISIBLE);
                    orderCompletTime.setText("Completed : "+getDate(Long.parseLong(model.getO_C_Time())));
                }else{
                    orderCompletTime.setVisibility(View.GONE);
                }

                if(order_status != 0){
                    o_delete.setVisibility(View.GONE);
                }

                switch(model.getPick_chooseBranch()){
                    case "QuickSHOP ( KATARGAM )":
                        branchAddress.setText(CommonUtil.kat_add);
                        break;
                    case "QuickSHOP ( VARACHHA )":
                        branchAddress.setText(CommonUtil.var_add);
                        break;
                    case "QuickSHOP ( ADAJAN )":
                        branchAddress.setText(CommonUtil.ada_add);
                        break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        o_delete.setOnClickListener(v -> ref.child(order_id).child("orderStatus").setValue(-1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ref.child(order_id).child("O_C_Time").setValue(String.valueOf(Calendar.getInstance().getTimeInMillis())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CusOrderExtraDetails.this, "Order Deleted Successfully and refund initiated!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));

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

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MMM-yyyy hh:mm:ss a", cal).toString();
        return date;
    }


    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("My Orders");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initView() {

        order_id = getIntent().getStringExtra("Order_ID1");
        o_delete = findViewById(R.id.item_delete);
        StatusIMG = findViewById(R.id.coed_status_icon);
        cusDetail = findViewById(R.id.coed_cus_detail);
        selectBranch = findViewById(R.id.coed_shop);
        branchAddress = findViewById(R.id.coed_shop_address);
        orderId = findViewById(R.id.coed_orderid);
        pickupTime = findViewById(R.id.coed_pickuptime);
        orderTime = findViewById(R.id.coed_ordertime);
        orderRupees = findViewById(R.id.coed_orderprice);
        orderTotalItems = findViewById(R.id.coed_totalitems);
        orderItemList = findViewById(R.id.coed_product_name);
        orderCompletTime = findViewById(R.id.coed_order_complete_time);

//        refcus = FirebaseDatabase.getInstance().getReference("customer").child(auth.getCurrentUser().getUid());
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Orders");
    }
}