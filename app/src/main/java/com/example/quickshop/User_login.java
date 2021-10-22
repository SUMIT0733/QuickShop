package com.example.quickshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class User_login extends AppCompatActivity {

    TextView textView4;
    TextView frgt,regis,cus_login,sel_login;
    CardView btn_login;
    EditText login,password;
    FirebaseAuth auth;
    ProgressDialog dia;
    boolean wifi,data;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        getSupportActionBar().hide();

        cus_login=(TextView)findViewById(R.id.cus_login);
        sel_login=(TextView)findViewById(R.id.sel_login);

        preferences = getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE);
        edit = preferences.edit();

        textView4=(TextView)findViewById(R.id.textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this, Sel_home.class);
                startActivity(intent);
            }
        });




        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfo1 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            //connectivityManager.getActNetworks();
            wifi = networkInfo.isConnected();
            data = networkInfo1.isConnected();
        }


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            //Toast.makeText(this, ""+auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            String email = auth.getCurrentUser().getEmail();
            finish();
            if(email.equals("quickshop@gmail.com"))
                startActivity(new Intent(this, Sel_home.class));
            else
                startActivity(new Intent(this, Cus_home.class));
                getSharedPreferences(CommonUtil.MyPREFERENCE, Context.MODE_PRIVATE).edit().putString("UID", auth.getCurrentUser().getUid()).apply();
        }

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);


        regis = (TextView) findViewById(R.id.text_registration);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this, PreRegister.class);
                startActivity(intent);
            }
        });
        frgt = (TextView) findViewById(R.id.frgt);
        frgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this, Forget.class);
                startActivity(intent);
            }
        });

        dia = new ProgressDialog(User_login.this);

        cus_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setMessage("Authenticating...");
                dia.show();
                String id = login.getText().toString();
                String pas = password.getText().toString();
                if(TextUtils.isEmpty(id)  || TextUtils.isEmpty(pas))
                {
                    dia.dismiss();
                    Toast.makeText(User_login.this, "Provide Email And Password", Toast.LENGTH_SHORT).show();
                }
                else if(!wifi && !data)
                {
                    dia.dismiss();
                    Toast.makeText(User_login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                else if(id.startsWith("quickshop")){
                    dia.dismiss();
                    Toast.makeText(User_login.this, "You are Seller\nPlease login in Seller.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(id,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                dia.dismiss();
                                finish();
                                Intent intent = new Intent(User_login.this,Cus_home.class);
                                startActivity(intent);
                                edit.putString("UID", auth.getCurrentUser().getUid()).apply();
                            }
                            else
                            {
                                dia.dismiss();
                                Toast.makeText(User_login.this, "Invalid Credentials or Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }

        });
        sel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setMessage("Authenticating...");
                dia.show();
                String id = login.getText().toString();
                String pas = password.getText().toString();
                if(TextUtils.isEmpty(id)  || TextUtils.isEmpty(pas) )
                {
                    dia.dismiss();
                    Toast.makeText(User_login.this, "Provide Email And Password", Toast.LENGTH_SHORT).show();
                }
                else if(!wifi && !data)
                {
                    dia.dismiss();
                    Toast.makeText(User_login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                else if(!id.startsWith("quickshop")){
                    dia.dismiss();
                    Toast.makeText(User_login.this, "Please sign in Customer.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(id,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                dia.dismiss();
                                finish();
                                Intent intent = new Intent(User_login.this,Sel_home.class);
                                startActivity(intent);
                            }
                            else
                            {
                                dia.dismiss();
                                Toast.makeText(User_login.this, "Invalid Credentials or Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(User_login.this);
        builder.setTitle("Alert");
        builder.setMessage("do you want to Exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
    }
}

