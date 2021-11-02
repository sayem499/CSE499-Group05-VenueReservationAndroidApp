package com.reservation.app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.reservation.app.MainActivity;
import com.reservation.app.databinding.ActivityLoginBinding;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.ui.venue.VenueActivity;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;

    private static final String TAG = "MAIN_TAG";

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private SharedPrefManager pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharedPrefManager(Login.this);

        if(pref.checkLogin()) {
            startActivity(new Intent(Login.this, Home.class));
            finish();

        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.phoneL1.setVisibility(View.VISIBLE);
        binding.otpL2.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing...");
        progressDialog.setCanceledOnTouchOutside(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull @NotNull String verificationId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent" + verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                progressDialog.dismiss();

                binding.phoneL1.setVisibility(View.GONE);
                binding.otpL2.setVisibility(View.VISIBLE);

                Toast.makeText(Login.this,"Verification code sent...",Toast.LENGTH_SHORT).show();

                binding.codeSentTextView.setText("Please typee the Verification code we sent to\n"+binding.phoneEditText.getText().toString().trim());

            }
        };

        binding.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEditText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(Login.this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
                } else {
                    startPhoneNumberVerification(phone);
                }

            }
        });

        binding.resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEditText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(Login.this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
                } else {
                    resendVerificationCode(phone,forceResendingToken);
                }

            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.otpEditText.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(Login.this,"Please Enter OTP ...",Toast.LENGTH_SHORT).show();
                } else {
                    verifyPhoneNumberWithCode(mVerificationId,code);
                }

            }
        });
        
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        progressDialog.setMessage("Verifying OTP");
        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("Logging In");

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                       progressDialog.dismiss();
                       String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                       Toast.makeText(Login.this,"Logged In as "+phone,Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(getApplicationContext(), VenueActivity.class);
                       pref.userLogin(phone);
                       startActivity(intent);
                       finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
             progressDialog.dismiss();;
             Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Resending Code");
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void startPhoneNumberVerification(String phone) {
        progressDialog.setMessage("Verifying Phone Number");
        progressDialog.show();

        PhoneAuthOptions options =
           PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}