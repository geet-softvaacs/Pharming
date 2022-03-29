package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.CartProduct;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.PatientDetails;
import com.onetick.pharmafest.model.Payment;
import com.onetick.pharmafest.model.PaymentItem;
import com.onetick.pharmafest.model.RestResponse;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Singleton;
import com.onetick.pharmafest.utils.Utility;
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

public class PaymentOptionActivity extends AppCompatActivity implements GetResult.MyListener{
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.lvl_paymentlist)
    LinearLayout lvlPaymentList;
    @BindView(R.id.tv_razorpay)
    TextView razorPay;
    @BindView(R.id.tv_cod)
    TextView cod;
    String subtotal;
    String note;
    String total;
    User user;
    String mAddress;
    String cAddress;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    PatientDetails details;
    Medicine medicine;
    boolean type = false;
    String type1;
    String typetosent;
    String addresses;
    String pincode;
    List<CartProduct>cartProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(PaymentOptionActivity.this);
        type1 = getIntent().getStringExtra("type1");
        if(type1.equals("Lab"))
        {
            cartProducts = Singleton.getConstant().getLabCartData();

        }else if(type1.equals("Medicine"))
        {
            cartProducts = Singleton.getConstant().getCartData();
        }
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        if (getIntent() != null) {
            subtotal = getIntent().getStringExtra("subtotal");
            total = getIntent().getStringExtra("total");
            note = getIntent().getStringExtra("note");
            mAddress = getIntent().getStringExtra("manualAddress");
            cAddress = getIntent().getStringExtra("currentAddress");
            pincode = getIntent().getStringExtra("pincode");
        }



        if (mAddress != null) {
            addresses = mAddress;
        } else if (cAddress != null) {
            addresses = cAddress;
        } else {
            addresses = "abcd";
        }

        if (getIntent().getParcelableExtra(Constant.PATIENT_DETAILS) != null) {
            details = getIntent().getParcelableExtra(Constant.PATIENT_DETAILS);
            medicine = getIntent().getParcelableExtra("MyClass");
            type = getIntent().getBooleanExtra("type", false);
        }



    }

    @OnClick({R.id.tv_razorpay, R.id.tv_cod, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_razorpay:
                int temtoal = (int) Math.round(Double.parseDouble(total));
                startActivity(new Intent(PaymentOptionActivity.this, RazerPayActivity.class).putExtra("amount", temtoal).putExtra("pincode", pincode)
                .putExtra("type1", type1).putExtra("subtotal", subtotal).putExtra("total", total).putExtra("note", note).putExtra("manualAddress", mAddress)
                .putExtra("currentAddress", cAddress).putExtra("type", false) .putExtra(Constant.PATIENT_DETAILS, details)
);
                break;
            case R.id.tv_cod:
                new AsyncTaskRunner().execute("");
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }


    public void orderPlace(JSONArray jsonArray) {
        custPrograssbar.prograssCreate(PaymentOptionActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {

            if (details != null) {
                jsonObject.put("pincode", pincode);
                jsonObject.put("uid", sessionManager.getUserDetails("").getId());
                jsonObject.put("fname", getIntent().getStringExtra("fname"));
                jsonObject.put("lname", getIntent().getStringExtra("lname"));
                jsonObject.put("email", getIntent().getStringExtra("email"));
                jsonObject.put("mobile", getIntent().getStringExtra("mobile"));
                jsonObject.put("p_method_id", "1");
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
            } else {
                jsonObject.put("pincode", pincode);
                jsonObject.put("uid", sessionManager.getUserDetails("").getId());
                jsonObject.put("p_method_id","1");
                jsonObject.put("full_address", addresses);
                jsonObject.put("d_charge", 0);
                jsonObject.put("cou_id", sessionManager.getIntData("couponid"));
                jsonObject.put("cou_amt", sessionManager.getIntData("coupon"));
                jsonObject.put("transaction_id", "");
                jsonObject.put("product_total", total);
                jsonObject.put("product_subtotal", subtotal);
                jsonObject.put("a_note", note);
                jsonObject.put("order_type", type1);
                jsonObject.put("is_admin", sessionManager.getStringData("isAdmin"));
            }
            jsonObject.put("ProductData", jsonArray);
            if(type)
            {
                RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<JsonObject> call = ApiClient.getInterface().getLabORderNow(bodyRequest);
                GetResult getResult = new GetResult();
                getResult.setMyListener(this);
                getResult.callForLogin(call, "2", this);
            }else{
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
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                setJoinPlayrList(lvlPaymentList, payment.getData());
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    Intent intent = new Intent(this, CompleteOrderActivity.class);
                    intent.putExtra("order_id", restResponse.getOrder_id());
                    startActivity(intent);
                    finish();
                    HomeActivity.txtCountCart.setText("0");
                    HomeActivity.txtLabCountCart.setText("0");
                }else if (result==null){
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
                        jsonObject.put("discount", cartProducts.get(i).getDiscount());
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

    private void setJoinPlayrList(LinearLayout lnrView, List<PaymentItem> paymentList) {
        lnrView.removeAllViews();
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(PaymentOptionActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_paymen, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txt_title = view.findViewById(R.id.txt_title);
            TextView txt_subtitel = view.findViewById(R.id.txt_subtitel);
            txt_title.setText("" + paymentList.get(i).getmTitle());
            txt_subtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(PaymentOptionActivity.this).load(ApiClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(PaymentOptionActivity.this).load(R.drawable.ezgifresize)).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {
                Utility.paymentID = paymentList.get(finalI).getmId();
                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(Double.parseDouble(total));
                            startActivity(new Intent(PaymentOptionActivity.this, RazerPayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem).putExtra("pincode", pincode));
                            break;
                        case "Cash On Delivery":
                            new AsyncTaskRunner().execute("");
                            break;
                        case "Paypal":
                            break;
                        case "Stripe":
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            lnrView.addView(view);
        }
    }

}