package com.example.quickshop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PreRegister extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText edittextmobilenumber;
    Button btnlogin;
    EditText edittextotp;
    Button btnverify;
    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;
    ActionBar actionBar;
    CardView otp_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_register);

        initView();
        setScreen();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenumber = ccp.getFullNumberWithPlus().replace(" ","");
                Toast.makeText(PreRegister.this, "OTP sent to Mobile number...", Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                otp_card.setVisibility(View.VISIBLE);
                initiateotp();
            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edittextotp.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_LONG).show();
                else if(edittextotp.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                else
                {
                    startActivity(new Intent(PreRegister.this, Register.class).putExtra("mobile",phonenumber));
                }

            }
        });

    }

    private void initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)

               new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                    }
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        startActivity(new Intent(PreRegister.this, Register.class).putExtra("mobile",phonenumber));
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.e("++++++++++++++++++++",e.getMessage());
                    }
                });
    }

    private void setScreen() {
        actionBar = getSupportActionBar();
        actionBar.setElevation(35);
        actionBar.setTitle("Verification");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }


    private void initView() {
        edittextmobilenumber = findViewById(R.id.edittextmobilenumber);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(edittextmobilenumber);
        btnlogin = findViewById(R.id.btn_send_otp);
        otp_card = findViewById(R.id.card_otp);
        otp_card.setVisibility(View.GONE);

        edittextotp=findViewById(R.id.edittextotp);
        btnverify=findViewById(R.id.btn_verify);
        mAuth=FirebaseAuth.getInstance();

    }
}

