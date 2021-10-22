package com.example.quickshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quickshop.R;
import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.CheckoutModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CheckoutAdapter extends ArrayAdapter<CheckoutModel> {
    List<CheckoutModel> checkoutList;
    DatabaseReference ref;

    public CheckoutAdapter(@NonNull Context context,  @NonNull List<CheckoutModel> objects) {
        super(context, 0, objects);
        this.checkoutList = objects;

        ref = FirebaseDatabase.getInstance().getReference("cart");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_checkout_order_row, null);
        CheckoutModel checkoutModel = checkoutList.get(position);

        TextView iNumber = (TextView) view.findViewById(R.id.item_number);
        TextView pName = (TextView) view.findViewById(R.id.product_name);
        TextView pPrice = (TextView)view.findViewById(R.id.product_price);

        iNumber.setText(String.valueOf(position+1));

        pName.setText(checkoutModel.getpName()+" * "+checkoutModel.getpQuantity());
        pPrice.setText("\u20b9 "+(checkoutModel.getpAmount()*checkoutModel.getpQuantity()));

        return view;
    }
}
