package com.onetick.pharmafest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.BrandProductAdapter;
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.databinding.FragmentBrandProductBinding;
import com.onetick.pharmafest.model.BProduct;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.ui.ProductDetailsActivity;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class BrandProductFragment extends Fragment implements GetResult.MyListener ,BrandProductAdapter.RecyclerTouchListener{
    private String bid;
    private String type;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    FragmentBrandProductBinding brandProductBinding;
    RecyclerView recyclerProduct;
    User user;
    BrandProductAdapter productAdapter;
    int totalpages, pageno=1;
    List<Medicine>brandProductList = new ArrayList<>();





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bid = getArguments().getString("bid");
        type = getArguments().getString("type");
    }

    public static BrandProductFragment newInstance(String pincode, String bid) {
        BrandProductFragment fragment = new BrandProductFragment();
        Bundle args = new Bundle();
        args.putString("titel", pincode);
        args.putString("bid", bid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        brandProductBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_brand_product, container, false);
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        recyclerProduct = brandProductBinding.recyclerProduct;
        user = sessionManager.getUserDetails("");
        recyclerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getBrandProduct(pageno);

        brandProductBinding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalpages)
                    {
                        pageno++;
                        getBrandProduct(pageno);
                    }
                }
            }
        });
        return brandProductBinding.getRoot();
    }

    private void getBrandProduct(int pageno) {
        brandProductBinding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("type", type);
            jsonObject.put("bid", bid);
            jsonObject.put("page_no", pageno);
//            jsonObject.put("pincode", sessionManager.getStringData(pincode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getBrandProduct(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            brandProductBinding.progressbar.setVisibility(View.GONE);
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                BProduct product = gson.fromJson(result.toString(), BProduct.class);
                totalpages =product.getTotalPages();
                brandProductList.addAll(product.getResultData());
                if (product.getResult().equalsIgnoreCase("true")) {
                    productAdapter = new BrandProductAdapter(getActivity(), brandProductList, this);
                    recyclerProduct.setAdapter(productAdapter);
                } else {
                    brandProductBinding.lvlNotfound.setVisibility(View.VISIBLE);
                    brandProductBinding.txtNotfount.setText("" + product.getResponseMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClickProductItem(String titel, Medicine medicine, int position) {
        startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra("MyClass", medicine).putParcelableArrayListExtra("PriceList", medicine.getProductInfo()).putStringArrayListExtra("ImageList", medicine.getProductImage()).putExtra("comeFrom", "brand").putExtra("position", position));
    }
}