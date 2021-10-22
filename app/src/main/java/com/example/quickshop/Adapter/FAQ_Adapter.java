package com.example.quickshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.quickshop.R;
import com.example.quickshop.model.FAQ_Model;

import java.util.List;

public class FAQ_Adapter extends ArrayAdapter<FAQ_Model> {

    List<FAQ_Model> faq_list;

    public FAQ_Adapter(@NonNull Context context, @NonNull List<FAQ_Model> objects) {
        super(context, 0, objects);
        this.faq_list = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_cus_faq, null);

        FAQ_Model model = faq_list.get(position);

        CardView Card = view.findViewById(R.id.faq_card);
        TextView Question = view.findViewById(R.id.question);
        TextView Answer = view.findViewById(R.id.answer);
        TextView Username = view.findViewById(R.id.user_name);

        if(model.isVerified())
            Card.setBackgroundColor(getContext().getResources().getColor(R.color.green_100));
        else
            Card.setBackgroundColor(getContext().getResources().getColor(R.color.red_100));

        Question.setText("Q. "+ model.getQuestion());
        Answer.setText("A. "+ model.getAnswer());
        Username.setText("@ "+model.getUser_name());

        return view;
    }

}
