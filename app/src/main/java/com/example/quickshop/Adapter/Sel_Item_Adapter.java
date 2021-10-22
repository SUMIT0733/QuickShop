package com.example.quickshop.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quickshop.R;
import com.example.quickshop.model.Item_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Sel_Item_Adapter extends ArrayAdapter<Item_model> {

    DatabaseReference ref;
    Activity context;
    List<Item_model> list;
    String category;

    public Sel_Item_Adapter(@NonNull Activity context, @NonNull List<Item_model> objects, String category) {
        super(context, 0, objects);
        this.context = context;
        this.list = objects;
        this.category = category;
        ref  = FirebaseDatabase.getInstance().getReference("Items");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_sel_add_product_ex,null);
        Item_model item_model = getItem(position);

        ImageView pUrl = (ImageView) view.findViewById(R.id.item_photo);
        ImageView del = (ImageView)view.findViewById(R.id.item_remove);
        TextView pName=(TextView)view.findViewById(R.id.item_name);
        TextView pWeight=(TextView)view.findViewById(R.id.item_weight);
        TextView pPrice=(TextView)view.findViewById(R.id.item_price);

        pName.setText(item_model.getpName());
        pWeight.setText(item_model.getpWeight());
        pPrice.setText("\u20b9 "+item_model.getpAmount());
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(category).child(item_model.getpId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Picasso.get().load(item_model.getpUrl()).into(pUrl);

        return view;
    }
}
