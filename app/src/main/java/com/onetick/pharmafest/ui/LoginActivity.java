package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityLoginBinding;
import com.onetick.pharmafest.model.Codes;
import com.onetick.pharmafest.model.CountryCode;
import com.onetick.pharmafest.model.SendOtpModel;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Utility;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {
    ActivityLoginBinding binding;
    CustPrograssbar custPrograssbar;
    List<CountryCode> cCodes = new ArrayList<>();
    boolean isNewuser = true;
    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        sendOtpCode();
        onClick();
    }



    public void sendOtpCode()
    {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", binding.edMobile.getText().toString());
            jsonObject.put("device_id", "");
            jsonObject.put("otp", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().MobileLogin(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", LoginActivity.this);
    }


    public void onClick()
    {
        binding.btnProceed.setOnClickListener(view -> {
            if (isNewuser) {
                if(binding.edMobile.getText().toString().length()<10)
                {
                    binding.edMobile.setError("Enter Valid Mobile Number");

                }else if(TextUtils.isEmpty(binding.edMobile.getText().toString()))
                {
                    binding.edMobile.setError("Enter Mobile Number");

                }else{
                    sendOtpCode();

                }
            } else {
                if (TextUtils.isEmpty(binding.edEmail.getText().toString())) {
                    binding.edEmail.setError("Enter Mobile / Email");
                } else if (TextUtils.isEmpty(binding.edPassword.getText().toString())) {
                    binding.edPassword.setError("Enter Password");
                } else {

                }
            }
        });
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();
        if (callNo.equalsIgnoreCase("1")) {
            Gson gson = new Gson();
            SendOtpModel sendOtpModel = gson.fromJson(result.toString(), SendOtpModel.class);
            if(sendOtpModel.getResponseCode().equals("200") && sendOtpModel.getResult().equals("true"))
            {
                Intent i = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                i.putExtra("phone" ,binding.edMobile.getText().toString());
                startActivity(i);
                finish();
                Toast.makeText(LoginActivity.this, sendOtpModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}