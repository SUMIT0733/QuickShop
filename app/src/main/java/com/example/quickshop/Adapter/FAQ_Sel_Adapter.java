package com.example.quickshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.quickshop.R;
import com.example.quickshop.model.FAQ_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.mindorks.editdrawabletext.OnDrawableClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class FAQ_Sel_Adapter  extends ArrayAdapter<FAQ_Model> {

    List<FAQ_Model> faq_list;
    DatabaseReference ref;
    String fid,cName;

    public FAQ_Sel_Adapter(@NonNull Context context, @NonNull List<FAQ_Model> objects) {
        super(context, 0, objects);
        this.faq_list = objects;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_sel_faq, null);

        ref = FirebaseDatabase.getInstance().getReference("FAQ");

        FAQ_Model model = faq_list.get(position);

        CardView Card = view.findViewById(R.id.faq_card1);
        TextView Question = view.findViewById(R.id.question1);
        TextView Answer = view.findViewById(R.id.answer1);
        TextView Username = view.findViewById(R.id.user_name1);
        EditDrawableText TextAnswer = view.findViewById(R.id.edit_text_answer1);

        if(model.isVerified()) {
            Card.setBackgroundColor(getContext().getResources().getColor(R.color.green_100));
//            Answer.setVisibility(View.VISIBLE);
            TextAnswer.setVisibility(View.GONE);
        }
        else {
            Card.setBackgroundColor(getContext().getResources().getColor(R.color.red_100));
            TextAnswer.setVisibility(View.VISIBLE);
        }

        Question.setText(" "+ model.getQuestion());
        Answer.setText("A. "+ model.getAnswer());
        Username.setText(""+model.getUser_name());

        TextAnswer.setDrawableClickListener(drawablePosition -> {
            if(drawablePosition == DrawablePosition.RIGHT){
                String fid = model.getFid();
                String question = Question.getText().toString().trim();
                String answer = TextAnswer.getText().toString().trim();
                String cName = Username.getText().toString().trim();

                ref.child(fid).setValue(new FAQ_Model(fid,question,answer,cName, Calendar.getInstance().getTimeInMillis(),true)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Answered Posted Successfully!", Toast.LENGTH_SHORT).show();

                        InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        //TextAnswer.setText(" ");
                    }
                });
            }
        });

        return view;
    }
}
