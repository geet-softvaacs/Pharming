package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
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

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.databinding.ActivitySearchBinding;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.Search;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements GetResult.MyListener, ProductAdapter.RecyclerTouchListener {
    ActivitySearchBinding binding;
    ImageView imgBack;
    ImageView imgNotfound;
    EditText edSearch;
    ImageView imgSearch;
    LinearLayout lvlActionsearch;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    RecyclerView recyclerProduct;
    LinearLayout lvlNotfound;
    SessionManager sessionManager;
    EditText ed_search;
    int totalPages;
    int pageno=1;
    List<Medicine> searchDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        sessionManager = new SessionManager(this);
        lvlNotfound = binding.lvlNotfound;
        recyclerProduct = binding.recyclerProduct;
        recyclerProduct.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        binding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalPages)
                    {
                        pageno++;
                        getSearch(ed_search.getText().toString(),pageno);
                    }
                }
            }
        });
        ed_search = binding.edSearch;
        ed_search.setHint("Search Medicine");
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
                getSearch(ed_search.getText().toString(), pageno);
                ed_search.clearFocus();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void getSearch(String keyword, int pageno) {
        binding.progressbar.setVisibility(View.VISIBLE);
//        custPrograssbar.prograssCreate(SearchActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keyword", keyword);
            jsonObject.put("pincode", 1);
            jsonObject.put("page_no",pageno);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getSearch(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", SearchActivity.this);

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        binding.progressbar.setVisibility(View.GONE);
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Search search = gson.fromJson(result.toString(), Search.class);
                totalPages = search.getTotalPages();
                if (search.getResult().equalsIgnoreCase("true")) {
                    if (search.getSearchData().size() != 0) {
                        lvlNotfound.setVisibility(View.GONE);
                        binding.nestedscrollview.setVisibility(View.VISIBLE);
                        searchDataList.addAll(search.getSearchData());
                        ProductAdapter productAdapter = new ProductAdapter(SearchActivity.this, searchDataList, this);
                        recyclerProduct.setAdapter(productAdapter);
                    } else {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        Toast.makeText(SearchActivity.this, search.getResponseMsg(), Toast.LENGTH_LONG).show();
                        imgNotfound.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_found));
                    }
                } else {
                    recyclerProduct.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                    imgNotfound.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_found));
                    Toast.makeText(SearchActivity.this, search.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        startActivity(new Intent(SearchActivity.this, ProductDetailsActivity.class).putExtra("MyClass", medicine).putParcelableArrayListExtra("PriceList", medicine.getProductInfo()).putStringArrayListExtra("ImageList", medicine.getProductImage()).putExtra("pro_id", medicine.getId()));

    }
}