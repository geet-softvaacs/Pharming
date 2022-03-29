package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.CartProduct;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.PatientDetails;
import com.onetick.pharmafest.model.Payment;
import com.onetick.pharmafest.model.RestResponse;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Singleton;
import com.onetick.pharmafest.utils.Utility;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RazerPayActivity extends AppCompatActivity implements PaymentResultListener, GetResult.MyListener {
    SessionManager sessionManager;
    double amount = 0;
    CustPrograssbar custPrograssbar;
    User user;
    PatientDetails details;
    Medicine medicine;
    String subtotal;
    String note;
    String total;
    String mAddress;
    String cAddress;
    String typetosent;
    boolean type = false;
    List<CartProduct> cartProducts = new ArrayList<>();
    String type1, pincode;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razer_pay);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        amount = getIntent().getIntExtra("amount", 0);
        type1 = getIntent().getStringExtra("type1");


        if (getIntent() != null) {
            subtotal = getIntent().getStringExtra("subtotal");
            total = getIntent().getStringExtra("total");
            note = getIntent().getStringExtra("note");
            mAddress = getIntent().getStringExtra("manualAddress");
            cAddress = getIntent().getStringExtra("currentAddress");
            pincode =getIntent().getStringExtra("pincode");

        }

        if (getIntent().getParcelableExtra(Constant.PATIENT_DETAILS) != null) {
            details = getIntent().getParcelableExtra(Constant.PATIENT_DETAILS);
            medicine = getIntent().getParcelableExtra("MyClass");
            type = getIntent().getBooleanExtra("type", false);
        }


        if(type1.equals("Lab"))
        {
            cartProducts = Singleton.getConstant().getLabCartData();

        }else if(type1.equals("Medicine"))
        {
            cartProducts = Singleton.getConstant().getCartData();
        }
        startPayment(String.valueOf(amount));
    }

    @Override
    public void onPaymentSuccess(String s) {
        Utility.tragectionID = s;
        Utility.paymentsucsses = 1;
        new AsyncTaskRunner().execute("");

        finish();

    }

    @Override
    public void onPaymentError(int i, String s) {
        finish();
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show();
    }

    public void startPayment(String amount) {
        final Activity activity = this;
        final Checkout co = new Checkout();
            co.setKeyID("rzp_test_DyKk7boze1RPG8");
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("theme.color", "#60a35f");
            options.put("currency", "INR");
            double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getMobile());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void orderPlace(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        try {

            if (type1.equals("Lab")) {
                jsonObject.put("pincode", pincode);
                jsonObject.put("uid", sessionManager.getUserDetails("").getId());
                jsonObject.put("fname", getIntent().getStringExtra("fname"));
                jsonObject.put("lname", getIntent().getStringExtra("lname"));
                jsonObject.put("email", getIntent().getStringExtra("email"));
                jsonObject.put("mobile", getIntent().getStringExtra("mobile"));
                jsonObject.put("p_method_id", "2");
                if (mAddress != null) {
                    jsonObject.put("full_address", mAddress);
                } else if (cAddress != null) {
                    jsonObject.put("full_address", cAddress);
                } else {
                    jsonObject.put("full_address", sessionManager.getStringData("pincoded"));
                }


                jsonObject.put("d_charge", "0");
                jsonObject.put("cou_id", sessionManager.getIntData("couponid"));
                jsonObject.put("cou_amt", sessionManager.getIntData("coupon"));
                jsonObject.put("transaction_id",  Utility.tragectionID);
                jsonObject.put("product_total", total);
                jsonObject.put("product_subtotal", subtotal);
                jsonObject.put("a_note", note);
                jsonObject.put("test_patient_name", details.getName());
                jsonObject.put("test_patient_mobile", details.getMobile());
                jsonObject.put("test_patient_gender", details.getGender());
                jsonObject.put("test_patient_age", details.getAge());
                jsonObject.put("test_patient_address", details.getAddress());
                jsonObject.put("test_date", details.getDate());
                jsonObject.put("test_slot", details.getTime());
                jsonObject.put("order_type", type1);
                jsonObject.put("date",sessionManager.getStringData("slotDate"));
                jsonObject.put("slot", sessionManager.getStringData("slotTime"));
                jsonObject.put("ProductData", jsonArray);
                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<JsonObject> call = ApiClient.getInterface().getLabORderNow(bodyRequest);
                GetResult getResult = new GetResult();
                getResult.setMyListener(this);
                getResult.callForLogin(call, "2", this);
            }else if(type1.equals("Medicine"))
            {
                jsonObject.put("pincode", pincode);
                jsonObject.put("uid", sessionManager.getUserDetails("").getId());
                jsonObject.put("p_method_id","2");
                jsonObject.put("full_address", sessionManager.getStringData("pincoded"));
                jsonObject.put("d_charge", 0);
                jsonObject.put("cou_id", sessionManager.getIntData("couponid"));
                jsonObject.put("cou_amt", sessionManager.getIntData("coupon"));
                jsonObject.put("transaction_id", Utility.tragectionID);
                jsonObject.put("product_total", total);
                jsonObject.put("product_subtotal", subtotal);
                jsonObject.put("a_note", note);
                jsonObject.put("order_type", type1);
                jsonObject.put("is_admin", sessionManager.getStringData("isAdmin"));
                jsonObject.put("ProductData", jsonArray);
                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<JsonObject> call = ApiClient.getInterface().getOrderNow(bodyRequest);
                GetResult getResult = new GetResult();
                getResult.setMyListener(this);
                getResult.callForLogin(call, "2", this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    Intent intent = new Intent(this, CompleteOrderActivity.class);
                    startActivity(intent);
                    finish();
                    HomeActivity.txtCountCart.setText("0");
                    HomeActivity.txtLabCountCart.setText("0");
                } else if (result==null){
                    Toast.makeText(this, "Unable to book lab test", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            JSONObject object = new JSONObject();

            if (details != null && type) {
                try {

                    object.put("title", medicine.getProductName());
                    object.put("image", "");
                    object.put("type", "");
                    object.put("cost", total);
                    object.put("qty", "1");
                    object.put("discount", "");
                    object.put("attribute_id", 1);
                    jsonArray.put(object);
                    return jsonArray;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for(int i =0; i<cartProducts.size(); i++)
            {
                if(type1.equals("Lab"))
                {
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("title", cartProducts.get(i).getTitle());
                        jsonObject.put("image", cartProducts.get(i).getImage());
                        jsonObject.put("type", cartProducts.get(i).getType());
                        jsonObject.put("cost", cartProducts.get(i).getCost());
                        jsonObject.put("qty", cartProducts.get(i).getQty());
                        jsonObject.put("discount", cartProducts.get(i).getDiscount()    );
                        jsonObject.put("product_amt", cartProducts.get(i).getProductAmt());
                        jsonObject.put("product_id", cartProducts.get(i).getProductId());
                        jsonObject.put("slot_id", sessionManager.getStringData("slotId"));
                        jsonObject.put("mrp", cartProducts.get(i).getMrp());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(type1.equals("Medicine"))
                {
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("title", cartProducts.get(i).getTitle());
                        jsonObject.put("image", cartProducts.get(i).getImage());
                        jsonObject.put("type", cartProducts.get(i).getType());
                        jsonObject.put("cost", cartProducts.get(i).getCost());
                        jsonObject.put("qty", cartProducts.get(i).getQty());
                        jsonObject.put("discount", cartProducts.get(i).getDiscount());
                        jsonObject.put("product_amt", cartProducts.get(i).getProductAmt());
                        jsonObject.put("product_id", cartProducts.get(i).getProductId());
                        jsonObject.put("mrp", cartProducts.get(i).getMrp());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            // execution of result of Long time consuming operation
            orderPlace(jsonArray);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}