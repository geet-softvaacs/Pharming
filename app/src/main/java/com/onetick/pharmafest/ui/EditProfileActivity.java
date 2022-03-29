package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityEditProfileBinding;
import com.onetick.pharmafest.model.AddressList;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.ProfileUpdate;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;
import com.onetick.utils.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EditProfileActivity extends AppCompatActivity implements GetResult.MyListener {
    ActivityEditProfileBinding binding;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    EditText edFirstName;
    EditText edLastName;
    EditText edEamil;
    String type;

    EditText edMobile;


    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        custPrograssbar = new CustPrograssbar();
        edFirstName = binding.edFirstname;
        edLastName = binding.edLastname;
        edMobile = binding.edMobile;
        edEamil = binding.edEmail;

        if(getIntent().getStringExtra("comeFrom")!=null)
            type = getIntent().getStringExtra("comeFrom");

        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();

        sessionManager = new SessionManager(EditProfileActivity.this);
        user = sessionManager.getUserDetails("");

        if(!type.equals("otpverify"))
        {
            binding.edFirstname.setText(user.getFname());
            binding.edLastname.setText(user.getLname());
            binding.edMobile.setText(user.getMobile());
            binding.edEmail.setText(user.getEmail());
            binding.edMobile.setEnabled(user.getMobile() == null);

        }else if(type.equals("otpverify"))
        {
            binding.edMobile.setText(user.getMobile());
        }

        binding.btnCountinue.setOnClickListener(view -> {
            if(validation())
            {
                updateUser();
            }
        });

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });




    }

    public boolean validation() {
        if (edFirstName.getText().toString().isEmpty()) {
            edFirstName.setError("Enter First Name");
            return false;
        }
        if (edLastName.getText().toString().isEmpty()) {
            edLastName.setError("Enter Last Name");
            return false;
        }
        if (edMobile.getText().toString().isEmpty()) {
            edMobile.setError("Enter Password");
            return false;
        }
        if(edEamil.getText().toString().isEmpty())
        {
            edEamil.setError("Enter Email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edEamil.getText().toString()).matches())
        {
            edEamil.setError("Invalid Email");
            return false;
        }
        return true;
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                ProfileUpdate profileUpdate = gson.fromJson(result.toString(), ProfileUpdate.class);
                Toast.makeText(EditProfileActivity.this, profileUpdate.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (profileUpdate.getResult().equalsIgnoreCase("true")) {

                    sessionManager.setUserDetails("", profileUpdate.getUser());
                    if(type.equals("otpverify"))
                    {
                        Intent i = new Intent(EditProfileActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        custPrograssbar.prograssCreate(EditProfileActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails("").getId());
            jsonObject.put("mobile", edMobile.getText().toString());
            jsonObject.put("fname", edFirstName.getText().toString());
            jsonObject.put("lname", edLastName.getText().toString());
            jsonObject.put("email", edEamil.getText().toString());
//            jsonObject.put("password", edPassword.getText().toString());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().getUpdate(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}