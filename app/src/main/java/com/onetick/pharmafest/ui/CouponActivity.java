package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;

import com.onetick.pharmafest.adapter.CouponAdp;
import com.onetick.pharmafest.model.RestResponse;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmingo.model.Coupon;
import com.onetick.pharmingo.model.Couponlist;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class CouponActivity extends AppCompatActivity implements GetResult.MyListener, CouponAdp.RecyclerTouchListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    User user;
    SessionManager sessionManager;
    int amount = 0;
    CustPrograssbar custPrograssbar;
    String type;

    @BindView(R.id.lvl_notfound)
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
        amount = getIntent().getIntExtra("amount", 0);
        type = getIntent().getStringExtra("type");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(CouponActivity.this);
        user = sessionManager.getUserDetails("");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getCoupuns();

    }

    private void getCoupuns() {
        try {
            custPrograssbar.prograssCreate(CouponActivity.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",type );
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().getCouponList(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1", this);
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void chalkCoupons(String cid) {
        try {
            custPrograssbar.prograssCreate(CouponActivity.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getId());
            jsonObject.put("cid", cid);
            jsonObject.put("type", type);
            jsonObject.put("o_amount",amount );
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().checkCoupon(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2", this);
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
                com.onetick.pharmingo.model.Coupon coupon = gson.fromJson(result.toString(), Coupon.class);
                if (coupon.getResult().equalsIgnoreCase("true")) {
                    CouponAdp couponAdp = new CouponAdp(this, coupon.getCouponlist(), this, amount);
                    recyclerView.setAdapter(couponAdp);
                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
                if(coupon.getCouponlist().size()==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if(response.getResponseCode().equals("200"))
                {
                    sessionManager.setIntData("coupon", Integer.valueOf(response.getC_value().intValue()));
                    finish();
                }else{
                    Toast.makeText(CouponActivity.this, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            sessionManager.setIntData("coupon", 0);
        }
    }

    @Override
    public void onClickItem(View v, Couponlist coupon) {
        try {
            if (coupon.getMinAmt()<= amount) {
                sessionManager.setIntData("coupon", Integer.parseInt(coupon.getCValue()));
                sessionManager.setIntData("couponid", Integer.parseInt(coupon.getId()));
                chalkCoupons(coupon.getId());
            } else {
                Toast.makeText(CouponActivity.this, "Sorry this coupon code is not applied", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}