package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityVerifyPhoneBinding;
import com.onetick.pharmafest.model.SendOtpModel;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.model.UserLogin;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class VerifyPhoneActivity extends AppCompatActivity implements GetResult.MyListener {

    private String verificationId;
    private FirebaseAuth mAuth;
    String phonenumber;
    String phonecode;
    String token;
    SessionManager sessionManager;
    ActivityVerifyPhoneBinding binding;
    EditText edOtp1, edOtp2, edOtp3, edOtp4, edOtp5, edOtp6;
    String phone;
    CustPrograssbar custPrograssbar;
    int newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_phone);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        edOtp1 = binding.edOtp1;
        edOtp2 = binding.edOtp2;
        edOtp3 = binding.edOtp3;
        edOtp4 = binding.edOtp4;
        edOtp5 = binding.edOtp5;
        edOtp6 = binding.edOtp6;
        phonenumber = getIntent().getStringExtra("phone");
        binding.txtMob.setText("+91"+phonenumber);
        startTimer();



        binding.btnSend.setOnClickListener(view -> {
            if(edOtp1.getText().toString().equals("") || edOtp2.getText().toString().equals("")|| edOtp3.getText().toString().equals("")|| edOtp4.getText().toString().equals("")|| edOtp5.getText().toString().equals("")
            || edOtp6.getText().toString().equals(""))
            {
                Toast.makeText(this, "Invalid otp", Toast.LENGTH_SHORT).show();
            }else{
                sendVerificationToken();
            }
        });

        edOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);
                Log.e("count", "" + count);
                Log.e("count", "" + count);

                if (s.length() == 1) {
                    edOtp3.requestFocus();
                }else if(count==0){
                    edOtp1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    edOtp4.requestFocus();
                }else if(count==0){
                    edOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp5.requestFocus();
                }else if(count==0){
                    edOtp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp6.requestFocus();
                }else if(count==0){
                    edOtp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    edOtp6.requestFocus();
                }else if(count==0){
                    edOtp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnReenter.setOnClickListener(view -> {
             binding.btnReenter.setVisibility(View.GONE);
                binding.btnTimer.setVisibility(View.VISIBLE);
                try {
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                            binding.btnTimer.setText(seconds + " Secound Wait");
                        }

                        @Override
                        public void onFinish() {
                            binding.btnReenter.setVisibility(View.VISIBLE);
                            binding.btnTimer.setVisibility(View.GONE);
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendVerificationToken();

        });

        binding.editnumberBtn.setOnClickListener(view -> {
            final AlertDialog.Builder mbuilder = new AlertDialog.Builder(VerifyPhoneActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v1 = inflater1.inflate(R.layout.dialog_rateus, null);
            EditText edMobile = v1.findViewById(R.id.ed_mobile);
            TextView btnProceed = v1.findViewById(R.id.btn_proceed);
            edMobile.setText(phonenumber);



            Animation transition_in_view = AnimationUtils.loadAnimation(VerifyPhoneActivity.this, R.anim.dialog_animation);//customer animation appearance
            v1.setAnimation(transition_in_view);
            v1.startAnimation(transition_in_view);
            mbuilder.setView(v1);
            final AlertDialog dialog = mbuilder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            btnProceed.setOnClickListener(view1 -> {
                sendOtpCode(edMobile.getText().toString());
                phonenumber = edMobile.getText().toString();
                binding.txtMob.setText("+91"+edMobile.getText().toString());
                dialog.dismiss();
            });
            dialog.show();
        });
    }


    private void sendVerificationToken() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        String otpCode = edOtp1.getText().toString() + edOtp2.getText().toString()+ edOtp3.getText().toString()+ edOtp4.getText().toString()+ edOtp5.getText().toString()+ edOtp6.getText().toString();
        try {
            jsonObject.put("mobile", phonenumber.toString());
            jsonObject.put("device_id", "");
            jsonObject.put("otp", otpCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().MobileLogin(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", VerifyPhoneActivity.this);
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();
        if (callNo.equalsIgnoreCase("1")) {
            Gson gson = new Gson();
            SendOtpModel sendOtpModel = gson.fromJson(result.toString(), SendOtpModel.class);
            User userLogin = sendOtpModel.getUserLogin();
            if( sendOtpModel.getResult().equals("false"))
            {
                Toast.makeText(this, sendOtpModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
            }else if(sendOtpModel.getResponseCode().equals("200") && sendOtpModel.getResult().equals("true") && sendOtpModel.getResponseMsg().equals("Login successfully!"))
            {
                Toast.makeText(this, "Otp Verified", Toast.LENGTH_SHORT).show();
                sessionManager.setUserDetails("", userLogin);
                sessionManager.setBooleanData(SessionManager.login, true);
                Intent i = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
                startActivity(i);
                finish();

            }else if(sendOtpModel.getResponseCode().equals("204"))
            {
                newUser= sendOtpModel.getNewUser();
                sessionManager.setUserDetails("", userLogin);
                sessionManager.setBooleanData(SessionManager.login, true);
                if(newUser==1){
                    Intent i = new Intent(VerifyPhoneActivity.this, EditProfileActivity.class);
                    i.putExtra("comeFrom", "otpverify");
                    startActivity(i);
                    finish();
                }
            }
        }
    }






    public void sendOtpCode(String mobile)
    {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {   
            jsonObject.put("mobile", mobile);
            jsonObject.put("device_id", "");
            jsonObject.put("otp", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().MobileLogin(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", VerifyPhoneActivity.this);
        startTimer();
    }


    public void startTimer()
    {
        try {
            new CountDownTimer(60000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    binding.btnTimer.setText(seconds + " Secound Wait");
                }

                @Override
                public void onFinish() {
                    binding.btnReenter.setVisibility(View.VISIBLE);
                    binding.btnTimer.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}