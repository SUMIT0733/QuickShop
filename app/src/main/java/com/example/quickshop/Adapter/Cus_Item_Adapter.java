package com.example.quickshop.Adapter;

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

import com.example.quickshop.CommonUtil;
import com.example.quickshop.R;
import com.example.quickshop.model.CartModel;
import com.example.quickshop.model.Item_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.quantityview.QuantityView;

public class Cus_Item_Adapter extends ArrayAdapter<Item_model> {
    Activity context;

    QuantityView unit;
    FirebaseAuth auth;
    DatabaseReference ref;

    List<CartModel> cart_list;

    private List<Item_model> list;
    private List<Item_model> filter_list;


    public Cus_Item_Adapter(@NonNull Context context, @NonNull List<Item_model> objects) {
        super(context, 0, objects);
        this.list = objects;
        filter_list = new ArrayList<>();
        filter_list.addAll(list);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("cart");

        cart_list = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_cus_show_product, null);
        Item_model item_model = list.get(position);

        ImageView pUrl = (ImageView) view.findViewById(R.id.item_photo);
        TextView pName = (TextView) view.findViewById(R.id.item_name);
        TextView pWeight = (TextView) view.findViewById(R.id.item_weight);
        TextView pPrice = (TextView) view.findViewById(R.id.item_price);
        unit = view.findViewById(R.id.unit);

        cart_list.add(new CartModel(item_model.getpName() + "77" + item_model.getpAmount(), item_model.getpName(), item_model.getpWeight(), 1, Integer.parseInt(item_model.getpAmount())));
        ImageView add_cart = view.findViewById(R.id.add_cart);

        pName.setText(item_model.getpName());
        pWeight.setText(item_model.getpWeight());
        pPrice.setText("\u20b9 " + item_model.getpAmount());

        Picasso.get().load(item_model.getpUrl()).into(pUrl);


        unit.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                int newprice = Integer.parseInt(item_model.getpAmount()) * newQuantity;
                pPrice.setText("\u20b9 " + newprice);
                cart_list.get(position).setpQuantity(newQuantity);
                //cart_list.get(position).setpAmount(newprice);
            }

            @Override
            public void onLimitReached() {
            }
        });


        add_cart.setOnClickListener(v -> {
            ref.child(getContext().getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","None")).child(item_model.getpName() + "77" + item_model.getpAmount()).setValue(new CartModel(item_model.getpName() + "77" + item_model.getpAmount(), item_model.getpName(), item_model.getpWeight(),cart_list.get(position).getpQuantity() , cart_list.get(position).getpAmount())).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(filter_list);
        } else {
            for (Item_model wp : filter_list) {
                if (wp.getpName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getpWeight().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
