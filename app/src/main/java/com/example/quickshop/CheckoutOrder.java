package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.OrderModel;
import com.example.quickshop.model.User_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckoutOrder extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = CheckoutOrder.class.getSimpleName();

    TextView contactName,contactNumber,pickAddresstext,pickupdate,total_amt,payable_amt;
    Button fPayment;
    Spinner spinAddress,spinTime;
    List<CartModel> cartList;
    DatabaseReference ref,orderRef,cartRef;
    ProgressDialog dialog;
    String uid;
    int total=0,mYear,mMonth,mDay;
    ActionBar actionBar;
    FirebaseAuth auth;
    Calendar c;

    String pick_chooseBranch,pick_chooseTime,pickdate="",oID,cName,cContact,paymentID,o_c_time;
    int total_amount,pay_amount, orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Checkout.preload(getApplicationContext());
        initView();
        setScreen();

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Detail detail = snapshot.getValue(User_Detail.class);
                cName = detail.getName();
                cContact = detail.getPhone();
                contactName.setText(detail.getName());
                contactNumber.setText(detail.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

       spinAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               pick_chooseBranch = getResources().getStringArray(R.array.choose_shop)[position];

               switch(pick_chooseBranch){
                   case "QuickSHOP ( KATARGAM )":
                       pickAddresstext.setText(CommonUtil.kat_add);
                       break;
                   case "QuickSHOP ( VARACHHA )":
                       pickAddresstext.setText(CommonUtil.var_add);
                       break;
                   case "QuickSHOP ( ADAJAN )":
                       pickAddresstext.setText(CommonUtil.ada_add);
                       break;
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
           }
       });

       pickupdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               c = Calendar.getInstance();
               mYear = c.get(Calendar.YEAR);
               mMonth = c.get(Calendar.MONTH);
               mDay = c.get(Calendar.DAY_OF_MONTH);

               DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutOrder.this,
                       new DatePickerDialog.OnDateSetListener() {
                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {
                               String month="";
                               if((monthOfYear + 1) <= 9){
                                   month = "0"+ (monthOfYear + 1);
                               }else{
                                   month = ""+(monthOfYear + 1);
                               }
                               String day = "";
                               if((dayOfMonth) <= 9){
                                   day = "0"+(dayOfMonth);
                               }
                               else {
                                   day = ""+(dayOfMonth);
                               }
                               pickupdate.setText(day+"-"+month+"-"+year);
                               pickdate = day + "/"+ month+"/" + year;
                           }
                       }, mYear, mMonth, mDay);
               datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
               datePickerDialog.show();
           }
       });

        spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pick_chooseTime = getResources().getStringArray(R.array.choose_time)[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        total_amt.setText("\u20b9 "+total_amount);
        payable_amt.setText("\u20b9 "+String.valueOf(total_amount + 10));
        pay_amount = total_amount + 10;
        fPayment.setText("Proceed to pay amount : \u20b9 "+pay_amount);

        fPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            } 
        });
    }

    private void validateData() {
        if (pickdate.trim().equals(""))
         {
            Toast.makeText(this, "Please Choose Date!", Toast.LENGTH_SHORT).show();
        }
           else {
               startPayment();
                }
        }


    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID(CommonUtil.RAZORPAY_API_KEY);
        checkout.setImage(R.drawable.qs);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "QUICK SHOP");
            options.put("description", "payment for order");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#74C58D");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(pay_amount*100));//pass amount in currency subunits
            options.put("prefill.email","<YOUR EMAIL ADDRESS>"); //TODO
            options.put("prefill.contact", "77777 77777");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            JSONObject ReadOnly = new JSONObject();
            ReadOnly.put("email", "true");
            ReadOnly.put("contact", "true");
            options.put("readonly", ReadOnly);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("CheckoutOrder");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initView() {
        fPayment = findViewById(R.id.payment);
        contactName = findViewById(R.id.user_name_text);
        contactNumber = findViewById(R.id.user_number_text);
        pickAddresstext = findViewById(R.id.pick_address_text);
        pickupdate = findViewById(R.id.date);
        total_amt = findViewById(R.id.total_amt);
        spinAddress = findViewById(R.id.spinner_address);
        spinTime = findViewById(R.id.spinner_time);
        payable_amt = findViewById(R.id.payable_amt);

        ref = FirebaseDatabase.getInstance().getReference("customer" );
        orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        cartRef =  FirebaseDatabase.getInstance().getReference("cart");

        uid = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");
        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(CheckoutOrder.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        cartList = new ArrayList<>();

        total_amount = getIntent().getIntExtra("Total_amount",0);
    }

    @Override
    public void onPaymentSuccess(String s) {
        paymentID = s;
//        dialog.show();
        doOrder();
    }

    private void doOrder() {
        oID = orderRef.push().getKey();
        orderRef.child(oID).setValue(new OrderModel(pick_chooseBranch,
                pick_chooseTime,
                pickdate,
                oID,
                cName,
                cContact,
                uid,
                paymentID,
                total_amount,
                pay_amount,
                String.valueOf(Calendar.getInstance().getTimeInMillis()),
                0,
                "")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                cartRef.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartList.clear();
                        for (DataSnapshot data : snapshot.getChildren())
                        {
                            CartModel c_model = data.getValue(CartModel.class);
                            cartList.add(c_model);
                            orderRef.child(oID).child("Order_items").child(c_model.getpName()+"77"+c_model.getpAmount()).setValue(c_model);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
                cartRef.child(uid).removeValue().addOnCompleteListener(task1 -> {
                    Toast.makeText(CheckoutOrder.this, "Order Successfully...", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                });
            }
        });

        Intent intent = new Intent(CheckoutOrder.this, OrderSummary.class);
        intent.putExtra("OrderId",oID);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, ""+i+" "+s, Toast.LENGTH_SHORT).show();
    }
}


