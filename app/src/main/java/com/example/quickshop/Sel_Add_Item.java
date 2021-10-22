package com.example.quickshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quickshop.model.Item_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class Sel_Add_Item extends DialogFragment {

    Spinner spin;
    View view;
    ProgressDialog dialog;
    StorageReference StorRef;
    DatabaseReference DataRef;
    ImageView img;
    EditText name,weight,price;
    TextView cancel,submit;
    Uri ImageUri;
    String pName="",pWeight="",pPrice="",img_name="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialogue_add_item_detail,null,true);
        initView();
//        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_border));
        return view;

    }

    public void initView() {
        img = (ImageView) view.findViewById(R.id.enter_image);
        name = (EditText) view.findViewById(R.id.enter_name);
        weight = view.findViewById(R.id.enter_weight);
        price = view.findViewById(R.id.enter_amount);
        cancel=view.findViewById(R.id.text_cancel);
        submit=view.findViewById(R.id.text_submit);
        spin=view.findViewById(R.id.spinner);
        dialog= new ProgressDialog(getActivity());
        StorRef = FirebaseStorage.getInstance().getReference("images/");
        DataRef = FirebaseDatabase.getInstance().getReference("Items");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            pName = name.getText().toString().trim();
            pWeight= weight.getText().toString().trim();
            pPrice = price.getText().toString().trim();

            if (ImageUri==null || pName==null || pWeight==null || pPrice==null)
            {
                Toast.makeText(getActivity(),"Please provide proper detail ",Toast.LENGTH_SHORT).show();
            }
            else
            {
                dialog.show();
                StorRef.child(img_name).putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        Log.d("++++++++++",uri.toString());
                        Item_model model = new Item_model(pName,pWeight,pPrice,uri.toString(),pName+"77"+pPrice);
                        Toast.makeText(getContext(), pName+"77"+pPrice, Toast.LENGTH_SHORT).show();
                        DataRef.child(spin.getSelectedItem().toString()).child(pName+"77"+pPrice).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();
                            }
                        });


                    });
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }).addOnProgressListener(snapshot -> {
                double pro = (100 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage((int)pro + "% uploaded");
            });

            }
        }
    });

    cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK && data!=null) {
            ImageUri = data.getData();
            img.setImageURI(ImageUri);
            img_name = data.getData().getLastPathSegment();
        }else{
            Toast.makeText(getActivity(), "Pleasse select image!", Toast.LENGTH_SHORT).show();
        }
    }
}
