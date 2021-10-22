package com.example.quickshop.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quickshop.R;
import com.example.quickshop.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Cus_Order_Adapter extends ArrayAdapter<OrderModel> {

    DatabaseReference ref;
    Context context;
    List<OrderModel> listView;
    String Orders;
    int orderStatus;

    public Cus_Order_Adapter(@NonNull Context context, int resource, @NonNull List<OrderModel> objects) {
        super(context, 0, objects);
        this.context = context;
        this.listView = objects;

    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_cus_my_order_1,null);
        OrderModel order_model = getItem(position);

        orderStatus = order_model.getOrderStatus();
//        ImageView progress = (ImageView)view.findViewById(R.id.my_order_progress);
        TextView index= view.findViewById(R.id.my_order_index);
        TextView order_cus_id = view.findViewById(R.id.my_order_cus_id);
        TextView order_pDate = view.findViewById(R.id.my_cus_pickup_date);
        TextView order_price = view.findViewById(R.id.my_order_price);
        TextView order_status_text = view.findViewById(R.id.cus_ord_status_text);

        index.setText(""+ (position+1)+".");
        order_cus_id.setText("QS77"+order_model.getoID());
//        order_pDate.setText(""+order_model.getPickdate()+ " -- " +order_model.getPick_chooseTime());
        order_price.setText("\u20b9 "+order_model.getPay_amount());
        order_pDate.setText(getDate(Long.parseLong(order_model.getTimeStamp())));


        if(orderStatus==0){
            order_status_text.setText(" P E N D I N G ");
            order_status_text.setTextColor(context.getResources().getColor(R.color.red_600));
        }
        else if(orderStatus==1){
            order_status_text.setText(" A C C E P T E D ");
            order_status_text.setTextColor(context.getResources().getColor(R.color.yellow_900));
        }
        else if(orderStatus==2){
            order_status_text.setText(" R E A D Y TO P I C K ");
            order_status_text.setTextColor(context.getResources().getColor(R.color.blue_900));
        }
        else if(orderStatus==3){
            order_status_text.setText(" C O M P L E T E D ");
            order_status_text.setTextColor(context.getResources().getColor(R.color.green_600));
        }
        else if(orderStatus==-1){
            order_status_text.setText(" C A N C E L L E D ");
            order_status_text.setTextColor(context.getResources().getColor(R.color.red_900));
        }
        else if(orderStatus==-2){
            order_status_text.setText(" R E J E C T E D ");
            order_status_text.setTextColor(context.getResources().getColor(R.color.red_900));
        }


        return view;
    }

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MMM-yyyy hh:mm:ss a", cal).toString();
        return date;
    }
}
