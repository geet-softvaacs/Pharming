package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.AddressList;
import com.onetick.pharmafest.model.CartProduct;
import com.onetick.pharmafest.model.GetPincode;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity implements GetResult.MyListener{

    @BindView(R.id.btn_countinue)
    TextView btnCountinue;
    @BindView(R.id.getCurrentLocationp)
    RadioButton getCurrentLocationp;
    //    @BindView(R.id.manualAddress)
//    RadioButton manualAddress;
    @BindView(R.id.ed_country)
    EditText ed_country;
    @BindView(R.id.ed_pincode)
    EditText ed_pincode;
    @BindView(R.id.ed_city)
    EditText ed_city;
    @BindView(R.id.ed_street_address)
    EditText ed_street_address;
    @BindView(R.id.manualAddressData)
    LinearLayout manualAddressData;
    @BindView(R.id.CurrentAddress)
    LinearLayout CurrentAddress;
    @BindView(R.id.txt_atype)
    TextView txtAType;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.ed_firstnamep)
    TextView ed_firstname;
    @BindView(R.id.ed_lastnamep)
    TextView ed_lastname;
    @BindView(R.id.ed_mobilep)
    TextView ed_mobile;
    @BindView(R.id.ed_emailp)
    TextView ed_email;
    @BindView(R.id.btn_countinue_upload)
    TextView btn_countinue_upload;
    @BindView(R.id.ed_address)
    TextView ed_address;
    String phonenumber;
    String phonecode;
    String subtotal;
    String pincode2;
    String total;
    String note;
    String country;
    String pincode;
    String city;
    String getType;
    String isChecked;
    String getHno;
    String getLandmark;
    String getCurrentAddress;
    String streetAddress;
    String mAddress;
    String cAddress;
    String type;
    private List<AddressList> mBanner;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> productName;
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray;
    ArrayList<CartProduct>cartProducts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        phonenumber = getIntent().getStringExtra("phone");
        phonecode = getIntent().getStringExtra("code");
        isChecked = getIntent().getStringExtra("isChecked");
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");


        if (getIntent() == null) {
            CurrentAddress.setVisibility(View.GONE);
        } else if (getIntent() != null) {
            if (isChecked != null) {
                CurrentAddress.setVisibility(View.VISIBLE);
                getType = getIntent().getStringExtra("atype");
                getHno = getIntent().getStringExtra("hno");
                getLandmark = getIntent().getStringExtra("landmark");
                isChecked = getIntent().getStringExtra("true");
                getCurrentAddress = getIntent().getStringExtra("address");
                txtAType.setText(getType);
                txtAddress.setText(getHno + "," + getLandmark + "," + getCurrentAddress);
                ed_firstname.setText(getIntent().getStringExtra("fname"));
                ed_lastname.setText(getIntent().getStringExtra("lname"));
                ed_email.setText(getIntent().getStringExtra("email"));
                ed_mobile.setText(getIntent().getStringExtra("mobile"));
            }
        }




        ed_firstname.setText(user.getFname());
        ed_lastname.setText(user.getLname());
        ed_email.setText(user.getEmail());
        ed_mobile.setText(user.getMobile());
        ed_address.setText(sessionManager.getStringData("pincoded"));


        btn_countinue_upload.setOnClickListener(v->
        {
            startActivity(new Intent(ProfileActivity.this, UploadPrescriptionActivity.class));
        });

        getCurrentLocationp.setOnClickListener(v ->
        {
            Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
            intent.putExtra("fname", ed_firstname.getText().toString());
            intent.putExtra("lname", ed_lastname.getText().toString());
            intent.putExtra("email", ed_email.getText().toString());
            intent.putExtra("mobile", ed_mobile.getText().toString());
            intent.putExtra("subtotal", subtotal);
            intent.putExtra("total", total);
            intent.putExtra("note", note);
            intent.putExtra("pincode", pincode2);
            intent.putExtra("type", "profile");
            intent.putExtra("comeFrom", "profile");
            startActivity(intent);
        });

        Intent intent = getIntent();
        subtotal = intent.getStringExtra("subtotal");
        total = intent.getStringExtra("total");
        note = intent.getStringExtra("note");
        pincode2 = sessionManager.getStringData("pincode");
        cartProducts = intent.getParcelableArrayListExtra("cartProduct");

        cAddress=txtAddress.getText().toString();
        jsonArray= new JSONArray();

        for(int i =0; i<cartProducts.size(); i++)
        {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("title", cartProducts.get(i).getTitle());
                jsonObject.put("med_id", cartProducts.get(i).getProductId());
                jsonObject.put("qty", cartProducts.size());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(!sessionManager.getStringData("pincode").equals(""))
        {
            sendPincode(jsonArray);
        }

        ed_address.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, AddressActivity.class);
            i.putExtra("fname", ed_firstname.getText().toString());
            i.putExtra("lname", ed_lastname.getText().toString());
            i.putExtra("email", ed_email.getText().toString());
            i.putExtra("mobile", ed_mobile.getText().toString());
            i.putExtra("subtotal", subtotal);
            i.putExtra("total", total);
            i.putExtra("note", note);
            i.putExtra("pincode", pincode2);
            i.putExtra("type", "profile");
            i.putExtra("comeFrom", "profile");
            startActivity(i);
        });








    }

    @OnClick(R.id.btn_countinue)
    public void onClick() {
        if (ed_firstname.getText().toString().isEmpty()) {
            ed_firstname.setError("Enter First Name");
        }
        else if (ed_email.getText().toString().isEmpty()) {
            ed_email.setError("Enter Valid Email");
        }
        else if (ed_lastname.getText().toString().isEmpty()) {
            ed_lastname.setError("Enter Last Name");
        }
        else if (ed_mobile.getText().toString().isEmpty()) {
            ed_mobile.setError("Enter Phone Number");
        }
        else if(ed_mobile.getText().length() <10){
            ed_mobile.setError("Enter 10 Digits Number");
        }else if(sessionManager.getStringData("pincoded").equals(""))
        {
            Toast.makeText(this, "Please select address", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(ProfileActivity.this, PaymentOptionActivity.class);
            intent.putExtra("subtotal", subtotal);
            intent.putExtra("total", total);
            intent.putExtra("note", note);
            intent.putExtra("fname", ed_firstname.getText().toString());
            intent.putExtra("manualAddress", sessionManager.getStringData("pincoded"));
            intent.putExtra("currentAddress", cAddress);
            intent.putExtra("lname", ed_lastname.getText().toString());
            intent.putExtra("email", ed_email.getText().toString());
            intent.putExtra("mobile", ed_mobile.getText().toString());
            intent.putExtra("pincode", sessionManager.getStringData("pincode"));
            intent.putExtra("type1", "Medicine");
            startActivity(intent);

        }
        sessionManager.setStringData("fname",ed_firstname.getText().toString());
        sessionManager.setStringData("lname",ed_lastname.getText().toString());
        sessionManager.setStringData("email",ed_email.getText().toString());
        sessionManager.setStringData("mobile",ed_mobile.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ed_address.setText(sessionManager.getStringData("pincoded"));
        if(!sessionManager.getStringData("pincode").equals(""))
        {
            sendPincode(jsonArray);
        }

    }

    private void sendPincode(JSONArray jsonArray2) {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("pincode", sessionManager.getStringData("pincode"));
            jsonObject.put("ProductData", jsonArray2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().checkAvailability(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3", this);

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();

        Gson gson = new Gson();
        GetPincode getPincode = gson.fromJson(result.toString(), GetPincode.class);
        if (getPincode.getUnAvailable().size() != 0) {

            names.clear();
            for (int i = 0; i < getPincode.getUnAvailable().size(); i++) {
                if (getPincode.getUnAvailable().get(i).getStatus().equalsIgnoreCase("0")) {
                    String data = getPincode.getUnAvailable().get(i).getTitle();
                    names.add(data);
                    productName = names;
                }
            }
            if (!names.isEmpty()) {
                sessionManager.setStringData("isAdmin", "1");

            }
        } else {
            sessionManager.setStringData("isAdmin", "0");
        }

    }
}