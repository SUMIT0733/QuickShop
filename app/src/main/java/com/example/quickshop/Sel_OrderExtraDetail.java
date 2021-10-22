package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.NotificationModel;
import com.example.quickshop.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import in.shadowfax.proswipebutton.ProSwipeButton;
import pl.droidsonroids.gif.GifImageView;

public class Sel_OrderExtraDetail extends AppCompatActivity {

    String order_id,order_item="";
    ProSwipeButton proSwipeBtn,delete_btn;
    TextView cusDetail,selectBranch,orderId,pickupTime,orderTime,orderRupees,orderTotalItems,orderItemList,sed_payid;
    ImageView pdf;
    GifImageView StatusIMG;
    int i=1, order_status;
    String msg="",uid="";
    DatabaseReference ref;
    DatabaseReference notification_ref,pdf_ref;
    FirebaseAuth auth;
    ACProgressFlower dialog;
    ActionBar actionBar;

//    OrderModel model = new OrderModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel__order_extra_detail);

        initView();
        setScreen();

//        initializePDFView();
        dialog.show();

        ref.child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderModel model = snapshot.getValue(OrderModel.class);

                cusDetail.setText(""+ model.getcName() + "( "+ model.getcContact() +" )");
                selectBranch.setText(model.getPick_chooseBranch());
                orderId.setText("Order ID ."+ "QS77" +model.getoID());
                pickupTime.setText("" +model.getPickdate() + "( " +model.getPick_chooseTime()+ " )");
                orderTime.setText(getDate(Long.parseLong(model.getTimeStamp())));
                orderRupees.setText("\u20b9 " + model.getPay_amount());
                sed_payid.setText(""+model.getPaymentID());
                order_status = model.getOrderStatus();
                uid = model.getuID();
                switch(order_status){
                    case 0:
                        StatusIMG.setImageResource(R.drawable.pending);
                        proSwipeBtn.setText("Start Packaging...");
                        break;
                    case 1:
                        StatusIMG.setImageResource(R.drawable.packing);
                        proSwipeBtn.setText("Ready to PickUp");
                        delete_btn.setVisibility(View.GONE);
                        break;
                    case 2:
                        StatusIMG.setImageResource(R.drawable.pack_giphy);
                        proSwipeBtn.setText("Order Completed");
                        delete_btn.setVisibility(View.GONE);
                        break;
                    case 3:
                        finish();
                        break;
                    case -1:
                        StatusIMG.setImageResource(R.drawable.deleted);
                        proSwipeBtn.setVisibility(View.GONE);
                        delete_btn.setVisibility(View.GONE);
                        break;
                    case -2:
                        StatusIMG.setImageResource(R.drawable.deleted);
                        proSwipeBtn.setVisibility(View.GONE);
                        delete_btn.setVisibility(View.GONE);
                        break;
                }
//              orderTotalItems;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        ref.child(order_id).child("Order_items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order_item = "";
                int total_item=0;
                for (DataSnapshot data : snapshot.getChildren())
                {
                    CartModel c_model = data.getValue(CartModel.class);
                    order_item += i+". "+c_model.getpName()+" ( "+c_model.getpAmount()+" X "+c_model.getpQuantity()+" )\n";
                    i++;
                    total_item += c_model.getpQuantity();
                }
                orderTotalItems.setText("Total Items : "+total_item);
                orderItemList.setText(order_item);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(order_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        printPDF();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { } });
            }
        });

        delete_btn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                ref.child(order_id).child("orderStatus").setValue(-2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        notification_ref.child(uid).push().setValue(new NotificationModel("Order Rejected By Seller",order_id,Calendar.getInstance().getTimeInMillis(),true)).addOnCompleteListener
                                (new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                ref.child(order_id).child("orderStatus").setValue(order_status+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ref.child(order_id).child("O_C_Time").setValue(String.valueOf(Calendar.getInstance().getTimeInMillis())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                proSwipeBtn.showResultIcon(true,true);
                                switch(order_status){
                                    case 1:
                                        msg = "Packaging started.";
                                        break;
                                    case 2:
                                        msg = "order is ready to Pickup.";
                                        break;
                                    case 3:
                                        msg = "Order picked up!!!";
                                        break;
                                }
                                notification_ref.child(uid).push().setValue(new NotificationModel(msg,order_id,Calendar.getInstance().getTimeInMillis(),true)).addOnCompleteListener
                                        (new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.dismiss();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

    }

    private void printPDF() {

        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(150,100,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(7.1f);
        paint.setColor(Color.rgb(  0,  50,  250));
        canvas.drawText (  ""+ selectBranch.getText(),  2,  10, paint);
        canvas.drawText(  "Name: "+ cusDetail.getText() , 2,  30, paint);
        canvas.drawText(  ""+ orderId.getText(), 2,  50, paint);
        canvas.drawText(  ""+orderTotalItems.getText(),2,70,paint);
        canvas.drawText(  ""+orderRupees.getText(),2,90,paint);

        myPdfDocument.finishPage(myPage);
        File file  = new File(this.getExternalFilesDir("/"),(orderId.getText()+"QS.pdf"));

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();
        Toast.makeText(this, "PDF Downloaded", Toast.LENGTH_SHORT).show();
    }

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("Order Detail");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MMM-yyyy hh:mm:ss a", cal).toString();
        return date;
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        order_id = getIntent().getStringExtra("Order_ID");

        proSwipeBtn = findViewById(R.id.awesome_btn);
        StatusIMG = findViewById(R.id.status_icon);
        cusDetail = findViewById(R.id.sed_cus_detail);
        selectBranch = findViewById(R.id.sed_branch);
        orderId = findViewById(R.id.sed_orderid);
        pickupTime = findViewById(R.id.sed_pickuptime);
        orderTime = findViewById(R.id.sed_ordertime);
        orderRupees = findViewById(R.id.sed_orderprice);
        orderTotalItems = findViewById(R.id.sed_totalitems);
        orderItemList = findViewById(R.id.sed_product_name);
        sed_payid=findViewById(R.id.sed_payid);
        delete_btn=findViewById(R.id.delete_btn);

        pdf = findViewById(R.id.download_pdf);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Orders");
        notification_ref = FirebaseDatabase.getInstance().getReference("Notification");

        dialog = new ACProgressFlower.Builder(Sel_OrderExtraDetail.this)
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
    }
}