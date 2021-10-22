package com.example.quickshop.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quickshop.Adapter.Cus_Order_Adapter;
import com.example.quickshop.Adapter.Sel_Order_Adapter;
import com.example.quickshop.CommonUtil;
import com.example.quickshop.CusOrderExtraDetails;
import com.example.quickshop.OrderSummary;
import com.example.quickshop.R;
import com.example.quickshop.Sel_OrderExtraDetail;
import com.example.quickshop.Sel_home;
import com.example.quickshop.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderFragment extends Fragment {
    View view;

    ListView listView;
    ImageView image;
    String uid;
    List<OrderModel> CorderList;
    ProgressDialog dialog;
    FirebaseAuth auth;
    DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_home,container, false);

        initView();
        loadList();
        return view;
    }

    private void loadList() {
        CorderList.clear();
        dialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CorderList.clear();
                for (DataSnapshot data : snapshot.getChildren())
                {
//                    Toast.makeText(Sel_home.this, orderList+"11", Toast.LENGTH_SHORT).show();
                    OrderModel Order_model = data.getValue(OrderModel.class);
                    if(uid.equals(Order_model.getuID()))
                        CorderList.add(Order_model);
                }
                Collections.reverse(CorderList);

                Cus_Order_Adapter adapter =new Cus_Order_Adapter(getActivity(),android.R.layout.simple_list_item_1, CorderList);
                listView.setAdapter(adapter);
                dialog.dismiss();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CorderList.get(position);
                        startActivity(new Intent(getActivity(), CusOrderExtraDetails.class).putExtra("Order_ID1",CorderList.get(position).getoID()));
                    }
                });

                if(CorderList.size() == 0){
                    listView.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    image.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

    }

    private void initView() {
        listView = view.findViewById(R.id.co_list);
        image = view.findViewById(R.id.no_item);
        image.setVisibility(View.INVISIBLE);
        CorderList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Orders");

        uid = getContext().getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).getString("UID","none");
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
    }
}
