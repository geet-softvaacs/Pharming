package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityBookingTestDetailsBinding;
import com.onetick.pharmafest.fragment.FragmentTestBookingDetail;
import com.onetick.pharmafest.fragment.SlotFragment;
import com.onetick.pharmafest.model.Address;
import com.onetick.pharmafest.model.CartModel;
import com.onetick.pharmafest.model.ProductPrice;
import com.onetick.pharmafest.model.SingleProduct;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class  ActivityBookingTestDetail extends AppCompatActivity implements GetResult.MyListener {
    ActivityBookingTestDetailsBinding bookingTestDetail;
    Context context;
    List<String>images = new ArrayList<>();
    String name,tests,preparation, report;
    double discount =0;
    double productprice=0;
    String short_desc, testId;
    SessionManager sessionManager;
    Double res = 0.0;
    User user;
    CustPrograssbar custPrograssbar;
    boolean isProductAdded = false;
    String qty;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingTestDetail = DataBindingUtil.setContentView(this, R.layout.activity_booking_test_details);
        context = this;
        sessionManager = new SessionManager(this);
        sessionManager.setBooleanData("isSlotSelected", false);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        images =getIntent().getStringArrayListExtra("ImageList");
        if(getIntent().getStringExtra("PriceList")==null)
        {
            productprice = 0;
        }else{
            productprice = Double.parseDouble(getIntent().getStringExtra("PriceList"));
        }
        name =getIntent().getStringExtra("name");
        tests = getIntent().getStringExtra("test");
        preparation = getIntent().getStringExtra("preparation");
        short_desc = getIntent().getStringExtra("short_desc");
        report = getIntent().getStringExtra("report");
        testId = getIntent().getStringExtra("labid");
        if(getIntent().getStringExtra("discount")==null)
        {
            discount =0;
        }else{
            discount = Double.parseDouble(getIntent().getStringExtra("discount"));
        }
        setData();

        bookingTestDetail.tvBookNow.setOnClickListener(view -> {
            if(sessionManager.getBooleanData("isSlotSelected"))
            {
                addtoCart("+", "");
                startActivity(new Intent(this, CartActivity.class).putExtra("type", "Lab"));
            }else if(!sessionManager.getBooleanData("isSlotSelected"))
            {
                if(bookingTestDetail.selectSlot.getCurrentTextColor() == Color.WHITE)
                {
                    Toast.makeText(context, "Please select Available Slot", Toast.LENGTH_SHORT).show();
                }else{
                    activeTab(bookingTestDetail.selectSlot);
                    normalTab(bookingTestDetail.txtAbout,bookingTestDetail.txtPreparation, bookingTestDetail.txtReport);
                    SlotFragment var14 = SlotFragment.newInstance(testId);
                    if (var14 != null) {
                        SlotFragment var4 = var14;

                        this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var4).commit();
                    }
                    Toast.makeText(context, "Please select Available Slot", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bookingTestDetail.txtCountcard.setText(""+sessionManager.getIntData("labCartCount"));
        setJoinPlayrList(bookingTestDetail.lvlAddcart);
        getProductSingleDetail();

        bookingTestDetail.backArrow.setOnClickListener(v -> {
            finish();
        });
        bookingTestDetail.txtActiontitle.setText(name);
    }

    public void setData()
    {
        bookingTestDetail.tvTestBookingName.setText(name);
        res= (double)productprice/100.0f * (double)discount;
        res = (double)productprice - res;
        bookingTestDetail.tvTestBookingPrice.setText("₹"+res);
        bookingTestDetail.tvTestBookingActualPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        bookingTestDetail.tvTestBookingActualPrice.setText(sessionManager.getStringData("currency") + productprice);
        bookingTestDetail.tvTestBookingPrePrerequisite.setText("Fasting|44 Tests");
        if(images!=null)
        {
            Glide.with(context).load(ApiClient.baseUrl+"/"+images.get(0)).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).into(bookingTestDetail.ivTestBookingThumb);
        }

        if(discount==0)
        {
            bookingTestDetail.tvTestBookingActualPrice.setVisibility(View.GONE);
            bookingTestDetail.tvTestBookingOffer.setVisibility(View.GONE);
        }else{
            bookingTestDetail.tvTestBookingOffer.setText(""+discount+" % Offer");
        }

//        setLocation(sessionManager.getStringData(SessionManager.pincoded));
//
//        bookingTestDetail.txtLocation.setOnClickListener(view -> {
//            startActivity(new Intent(ActivityBookingTestDetail.this, AddressActivity.class));
//        });

        bookingTestDetail.rltCart.setOnClickListener(view -> {
            startActivity(new Intent(ActivityBookingTestDetail.this, CartActivity.class).putExtra("type", "Lab"));
        });

        FragmentTestBookingDetail var11 = FragmentTestBookingDetail.newInstance("Overview", this.short_desc);
        if (var11 != null) {
            FragmentTestBookingDetail var1 = var11;
            boolean var2 = false;
            boolean var3 = false;
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var1).commit();
        }

        bookingTestDetail.txtAbout.setOnClickListener(view -> {
            activeTab(bookingTestDetail.txtAbout);
            normalTab(bookingTestDetail.txtPreparation, bookingTestDetail.txtReport, bookingTestDetail.selectSlot);
            FragmentTestBookingDetail var12 = FragmentTestBookingDetail.newInstance("Overview", this.short_desc);
            if (var12 != null) {
                FragmentTestBookingDetail var2 = var12;

                this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var2).commit();
            }

        });

        bookingTestDetail.txtPreparation.setOnClickListener(view -> {
            activeTab(bookingTestDetail.txtPreparation);
            normalTab(bookingTestDetail.txtAbout, bookingTestDetail.txtReport, bookingTestDetail.selectSlot);
            FragmentTestBookingDetail var13 = FragmentTestBookingDetail.newInstance("Preparation", this.preparation);
            if (var13 != null) {
                FragmentTestBookingDetail var3 = var13;
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var3).commit();
            }
        });

        bookingTestDetail.txtReport.setOnClickListener(view -> {
            activeTab(bookingTestDetail.txtReport);
            normalTab(bookingTestDetail.txtAbout,bookingTestDetail.txtPreparation, bookingTestDetail.selectSlot);
            FragmentTestBookingDetail var14 = FragmentTestBookingDetail.newInstance("Report Time estimation", report);
            if (var14 != null) {
                FragmentTestBookingDetail var4 = var14;

                this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var4).commit();
            }
        });


        bookingTestDetail.selectSlot.setOnClickListener(view -> {
            activeTab(bookingTestDetail.selectSlot);
            normalTab(bookingTestDetail.txtAbout,bookingTestDetail.txtPreparation, bookingTestDetail.txtReport);
            SlotFragment var14 = SlotFragment.newInstance(testId);
            if (var14 != null) {
                SlotFragment var4 = var14;

                this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var4).commit();
            }
        });





    }

    public void activeTab(TextView view)
    {
        view.setTextColor(getResources().getColor(R.color.white));
        view.setBackground(getResources().getDrawable(R.drawable.orderbox));
    }

    private void normalTab(TextView view, TextView view1, TextView view2) {
        view.setTextColor(getResources().getColor(R.color.black));
        view.setBackground( getResources().getDrawable(R.drawable.orderbox_white));
        view1.setTextColor(getResources().getColor(R.color.black));
        view1.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
        view2.setTextColor(getResources().getColor(R.color.black));
        view2.setBackground(getResources().getDrawable(R.drawable.orderbox_white));

    }

//    private void setLocation(String location) {
//        bookingTestDetail.txtLocation.setText("Deliver to"+location);
//    }


    private void setJoinPlayrList(LinearLayout lnrView) {
        lnrView.removeAllViews();
        final int[] count = {0};
        LayoutInflater inflater = LayoutInflater.from(ActivityBookingTestDetail.this);
        View view = inflater.inflate(R.layout.custome_prize, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout txtOutstock = view.findViewById(R.id.txt_outstock);
        LinearLayout lvl_addremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvl_addcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout img_mins = view.findViewById(R.id.img_mins);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);

        lvl_addcart.setOnClickListener(v -> {
            if(sessionManager.getBooleanData("isSlotSelected"))
            {
                lvl_addcart.setVisibility(View.GONE);
                lvl_addremove.setVisibility(View.VISIBLE);
                String text = txtcount.getText().toString();
                count[0] = Integer.parseInt(text);
                count[0] = count[0] + 1;
                txtcount.setText("" + count[0]);
                addtoCart("+", "");
            }else if(!sessionManager.getBooleanData("isSlotSelected"))
            {
                activeTab(bookingTestDetail.selectSlot);
                normalTab(bookingTestDetail.txtAbout,bookingTestDetail.txtPreparation, bookingTestDetail.txtReport);
                SlotFragment var14 = SlotFragment.newInstance(testId);
                if (var14 != null) {
                    SlotFragment var4 = var14;

                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_test_detail_booking, (Fragment)var4).commit();
                }
                Toast.makeText(context, "Please select Available Slot", Toast.LENGTH_SHORT).show();
            }

        });

        img_mins.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                txtcount.setText("" + count[0]);
                lvl_addremove.setVisibility(View.GONE);
                lvl_addcart.setVisibility(View.VISIBLE);
                addtoCart("", "0");
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                addtoCart("-", "");
            }
        });
        img_plus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            txtcount.setText("" + count[0]);
            addtoCart("+", "");
        });

        if(isProductAdded)
        {
            lvl_addcart.setVisibility(View.GONE);
            lvl_addremove.setVisibility(View.VISIBLE);
            txtcount.setText(qty);

        }else if(!isProductAdded)
        {
            lvl_addcart.setVisibility(View.VISIBLE);
            lvl_addremove.setVisibility(View.GONE);
        }


        lnrView.addView(view);
    }

    public void addtoCart(String operation, String qty)
    {
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("type", "Lab");
            jsonObject.put("product_id", testId);
            jsonObject.put("prodcuct_qty", qty
            );
            jsonObject.put("qty", operation);
            jsonObject.put("slot_id", sessionManager.getStringData("slotId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().addtoCart(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", this);
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                CartModel cartModel = gson.fromJson(result.toString(), CartModel.class);
                if (cartModel.getResult().equalsIgnoreCase("true") && cartModel.getResponseCode().equals("200")) {
                    Toast.makeText(this, cartModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    sessionManager.setIntData("labCartCount", cartModel.getCardItemCount());

                    bookingTestDetail.txtCountcard.setText(""+ cartModel.getCardItemCount());
                    HomeActivity.txtCountCart.setText(""+cartModel.getCardItemCount());
                }
            }else if(callNo.equalsIgnoreCase("3"))
            {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                SingleProduct singleProduct = gson.fromJson(result.toString(), SingleProduct.class);
                if(singleProduct.getResponseCode().equals("200")){
                    isProductAdded = true;
                    qty = singleProduct.getResultData().getProductQty();
                    setJoinPlayrList(bookingTestDetail.lvlAddcart);
                }else if(singleProduct.getResponseCode().equals("201"))
                {
                    isProductAdded = false;
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    public void getProductSingleDetail()
    {
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("type", "Lab");
            jsonObject.put("product_id", testId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getSingleProductDetail(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3", this);
    }
}