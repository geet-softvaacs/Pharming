package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.adapter.SearchLabProductAdapter;
import com.onetick.pharmafest.model.LabSearch;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.SearchResultItem;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LabSearchActiivity extends AppCompatActivity implements SearchLabProductAdapter.RecyclerTouchListener ,GetResult.MyListener{
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_notfound)
    ImageView imgNotfound;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.lvl_actionsearch)
    LinearLayout lvlActionsearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    SessionManager sessionManager;
    EditText ed_search;
    String comeFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
//        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LabSearchActiivity.this);
        recyclerProduct.setLayoutManager(new GridLayoutManager(LabSearchActiivity.this, 2));
//        recyclerProduct.setItemAnimator(new DefaultItemAnimator());
        ed_search = findViewById(R.id.ed_search);
        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    getSearch(edSearch.getText().toString());

                    return true;
                }

                return false;
            }
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                getLabSearch(edSearch.getText().toString());
                ed_search.clearFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (!edSearch.getText().toString().isEmpty()) {
//                    getSearch(edSearch.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
        comeFrom = getIntent().getStringExtra("comeFrom");
        ed_search.setHint("Search Lab Test");
    }
    private void getLabSearch(String keyword) {
//        custPrograssbar.prograssCreate(SearchActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("search", keyword);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getLabSearch(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2", LabSearchActiivity.this   );

    }
    @OnClick({R.id.img_back, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_search:
                if (!edSearch.getText().toString().isEmpty()) {
                    getLabSearch(edSearch.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
//            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                LabSearch search1 = gson.fromJson(result.toString(), LabSearch.class);
                if (search1.getResponseCode().equalsIgnoreCase("200")) {
                    if (search1.getResultData().size() != 0) {
                        lvlNotfound.setVisibility(View.GONE);
                        recyclerProduct.setVisibility(View.VISIBLE);
                        SearchLabProductAdapter productAdapter = new SearchLabProductAdapter(this, search1.getResultData(),this);
                        recyclerProduct.setAdapter(productAdapter);
                    } else {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        Toast.makeText(LabSearchActiivity.this, search1.getResponseMsg(), Toast.LENGTH_LONG).show();
                        imgNotfound.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_found));
                    }
                } else {
                    recyclerProduct.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                    imgNotfound.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_found));
                    Toast.makeText(LabSearchActiivity.this, search1.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClickProductItem(String titel, SearchResultItem medicine) {
        String PriceList;
        String short_desc;
        String report;
        String preparation;
        String discount;
        String tests;

        if (medicine.getPrice() !=null){
            PriceList= medicine.getPrice();
        }else{
            PriceList= "0";
        }
        if (medicine.getShort_desc()!=null){
            short_desc= medicine.getShort_desc();
        }else{
            short_desc= "";
        }
        if (medicine.getReport()!=null){
            report= medicine.getReport();
        }else{
            report= "";
        }
        if (medicine.getPreparation()!=null){
            preparation= medicine.getPreparation();
        }else{
            preparation= "";
        }
        if (medicine.getDiscount()!=null){
            discount= medicine.getDiscount();
        }else{
            discount= "0";
        }
        if (medicine.getTest_title()!=null) {
            tests= medicine.getTest_title();
        }else{
            tests= "";
        }
        startActivity(new Intent(LabSearchActiivity.this, ActivityBookingTestDetail.class)
                .putExtra("PriceList", PriceList)
                .putStringArrayListExtra("ImageList", medicine.getTest_image())
                .putExtra("name", medicine.getTest_name())
                .putExtra("short_desc", short_desc)
                .putExtra("test", tests)
                .putExtra("report", report)
                .putExtra("preparation", preparation)
                .putExtra("discount", discount)
                .putExtra("labid", medicine.getId()));

    }
}