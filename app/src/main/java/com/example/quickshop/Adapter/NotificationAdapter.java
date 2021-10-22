package com.example.quickshop.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quickshop.R;
import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.NotificationModel;
import com.example.quickshop.model.SearchModel;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {
    View view;
    TextView product_status_txt,product_oid,time;

    List<NotificationModel> notification_list;

    public NotificationAdapter(@NonNull Context context, @NonNull List<NotificationModel> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.notification_row,null);
        NotificationModel model = getItem(position);

        product_status_txt = (TextView) view.findViewById(R.id.product_status_txt);
        product_oid = (TextView) view.findViewById(R.id.product_oid);
        time = (TextView) view.findViewById(R.id.product_time);



        product_status_txt.setText(model.getText_msg());
        product_oid.setText(model.getOrder_id());
        time.setText(getDate(model.getTimestamp()));

        return view;
    }

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MMM-yyyy hh:mm:ss a", cal).toString();
        return date;
    }

}
