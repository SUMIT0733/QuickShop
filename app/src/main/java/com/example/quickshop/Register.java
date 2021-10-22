package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickshop.model.User_Detail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Register extends AppCompatActivity {
    
    EditText name,email,pass;
    TextView mo;
    CardView reg;
    FirebaseAuth auth;
    DatabaseReference ref;
    ProgressDialog dia;

    ProgressBar p_bar;
    boolean data,wifi;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfo1 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            wifi = networkInfo.isConnected();
            data = networkInfo1.isConnected();
        }

            auth = FirebaseAuth.getInstance();
            ref = FirebaseDatabase.getInstance().getReference("customer");

        preferences = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE);
        edit = preferences.edit();

            dia = new ProgressDialog(Register.this);
            dia.setCancelable(false);
            dia.setMessage("Registering...");
            p_bar = (ProgressBar)findViewById(R.id.progress_bar);
            p_bar.setVisibility(View.GONE);

            name = (EditText)findViewById(R.id.full_name);
            mo = (TextView) findViewById(R.id.mobile);
            mo.setText(getIntent().getStringExtra("mobile"));
            email= (EditText) findViewById(R.id.email);
            pass = (EditText) findViewById(R.id.password);
            reg = (CardView) findViewById(R.id.btn_registration);


            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dia.show();
                    p_bar.setVisibility(View.VISIBLE);
                    reg.setVisibility(View.GONE);

                    final String fullname = name.getText().toString();
                    final String mobile = mo.getText().toString();
                    final String id = email.getText().toString();
                    final String pas = pass.getText().toString();


                    if(TextUtils.isEmpty(fullname) ||TextUtils.isEmpty(id)  || TextUtils.isEmpty(pas))
                    {
                        p_bar.setVisibility(View.GONE);
                        reg.setVisibility(View.VISIBLE);
                        //dia.dismiss();
                        Toast.makeText(Register.this, "Please Fill up all Detail", Toast.LENGTH_SHORT).show();
                    }
                    else if(!wifi && !data)
                    {
                        Toast.makeText(Register.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        auth.createUserWithEmailAndPassword(id,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    p_bar.setVisibility(View.VISIBLE);
                                    reg.setVisibility(View.GONE);
                                    auth.signInWithEmailAndPassword(id,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser user = auth.getCurrentUser();
                                                User_Detail user_detail = new User_Detail(id,mobile,fullname);

                                                ref.child(user.getUid()).setValue(user_detail);
                                                Toast.makeText(Register.this, "Successfully Registered...", Toast.LENGTH_SHORT).show();
                                                //dia.dismiss();
                                                FirebaseMessaging.getInstance().subscribeToTopic("customer");
                                                finish();
                                                Intent intent = new Intent(Register.this,Cus_home.class);
                                                startActivity(intent);
                                                edit.putString("UID", auth.getCurrentUser().getUid()).apply();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            p_bar.setVisibility(View.GONE);
                                            reg.setVisibility(View.VISIBLE);
                                            Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else
                                {
                                    p_bar.setVisibility(View.GONE);
                                    reg.setVisibility(View.VISIBLE);
                                    Toast.makeText(Register.this, "Registration error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

