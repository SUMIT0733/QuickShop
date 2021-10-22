package com.example.quickshop.Adapter;

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

import com.example.quickshop.CommonUtil;
import com.example.quickshop.R;
import com.example.quickshop.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Cart_Adapter extends ArrayAdapter<CartModel> {

    List<CartModel> cartlist;
    DatabaseReference ref;

    public Cart_Adapter(@NonNull Context context, @NonNull List<CartModel> objects) {
        super(context, 0, objects);
        this.cartlist = objects;

        ref = FirebaseDatabase.getInstance().getReference("cart");

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_cart_row, null);
        CartModel cartmodel = cartlist.get(position);

        TextView iNumber = (TextView) view.findViewById(R.id.item_number);
        TextView pName = (TextView) view.findViewById(R.id.product_name);
        TextView pWeight = (TextView) view.findViewById(R.id.product_weight);
        TextView pPrice = (TextView)view.findViewById(R.id.product_price);
        ImageView del = (ImageView) view.findViewById(R.id.remove);

        iNumber.setText(String.valueOf(position+1));
        pName.setText(cartmodel.getpName()+" * "+cartmodel.getpQuantity());
        pWeight.setText(cartmodel.getpWeight());
        pPrice.setText("\u20b9 "+(cartmodel.getpAmount()*cartmodel.getpQuantity()));

        del.setOnClickListener(v -> {
            ref.child(getContext().getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","None")).child(cartmodel.getpName()+"77"+cartmodel.getpAmount()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Item removed from Cart", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

}
