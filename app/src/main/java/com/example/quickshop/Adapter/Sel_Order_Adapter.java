package com.example.quickshop.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.quickshop.R;
import com.example.quickshop.Sel_OrderExtraDetail;
import com.example.quickshop.Sel_home;
import com.example.quickshop.model.Item_model;
import com.example.quickshop.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Sel_Order_Adapter extends ArrayAdapter<OrderModel> {

    DatabaseReference ref;
    Activity context;
    List<OrderModel> listView;
    FirebaseAuth auth;
    int orderStatus;

    public Sel_Order_Adapter(@NonNull Activity context, @NonNull List<OrderModel> objects) {
        super(context, 0, objects);
        this.context = context;
        this.listView = objects;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_sel_order_1,null);
            OrderModel order_model = getItem(position);

            CardView Card=view.findViewById(R.id.card);
            orderStatus = order_model.getOrderStatus();
            ImageView progress = (ImageView)view.findViewById(R.id.order_progress);
            TextView index= (TextView)view.findViewById(R.id.order_index);
            TextView order_cus_name = (TextView) view.findViewById(R.id.order_cus_name);
            TextView order_pDate = (TextView) view.findViewById(R.id.sel_pickup_date);
            TextView order_price = (TextView) view.findViewById(R.id.order_price);

            index.setText(""+ (position + 1)+".");
            order_cus_name.setText(""+order_model.getcName());
            order_pDate.setText(""+order_model.getPickdate()+" -- "+order_model.getPick_chooseTime());
//            order_pDate.setText(""+new Sel_OrderExtraDetail().getDate(Long.parseLong("" +order_model.getPickdate() + "( " +order_model.getPick_chooseTime()+ " )")));
            order_price.setText("\u20b9 "+order_model.getPay_amount());

            if(orderStatus==0){
                Picasso.get().load(R.drawable.red_circle).placeholder(R.drawable.red_circle).into(progress);
            }
            else if(orderStatus==1){
                Picasso.get().load(R.drawable.yellow_circle).placeholder(R.drawable.yellow_circle).into(progress);
            }
            else if(orderStatus==2){
                Picasso.get().load(R.drawable.green_circle).placeholder(R.drawable.green_circle).into(progress);
            }
            else if(orderStatus==3){
                Picasso.get().load(R.drawable.icon_done).placeholder(R.drawable.icon_done).into(progress);
            }
            else if(orderStatus==-1){
                Picasso.get().load(R.drawable.cancel).placeholder(R.drawable.cancel).into(progress);
            }
            else if(orderStatus==-2){
                Picasso.get().load(R.drawable.cancel).placeholder(R.drawable.cancel).into(progress);
//                Card.setBackgroundColor(getContext().getResources().getColor(R.color.red_100));
//                Card.setRadius(20);
            }
            return view;
        }
    }
