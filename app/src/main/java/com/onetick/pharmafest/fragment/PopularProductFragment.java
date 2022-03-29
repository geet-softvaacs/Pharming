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
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.databinding.FragmentPopularProductBinding;
import com.onetick.pharmafest.model.BProduct;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.ui.ActivityBookingTestDetail;
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


public class PopularProductFragment extends Fragment implements GetResult.MyListener,ProductAdapter.RecyclerTouchListener {
    FragmentPopularProductBinding binding;
    RecyclerView recyclerProduct;

    ProductAdapter productAdapter;
    SessionManager sessionManager;
    User user;
    List<Medicine>currentDataList = new ArrayList<>();
    int totalPages, page_no=1;
    CustPrograssbar custPrograssbar;

    String type;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PopularProductFragment() {
        // Required empty public constructor
    }


    public static PopularProductFragment newInstance(String pincode, String bid) {
        PopularProductFragment fragment = new PopularProductFragment();
        Bundle args = new Bundle();
        args.putString("titel", pincode);
        args.putString("bid", bid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_popular_product, container, false);
        recyclerProduct = binding.recyclerProduct;
        custPrograssbar=new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        recyclerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(page_no!=totalPages)
                    {
                        page_no++;
                        getAllProduct(page_no);
                    }
                }
            }
        });
//        recyclerProduct.setItemAnimator(new DefaultItemAnimator());
        getAllProduct(page_no);
        return binding.getRoot();
    }

    private void getAllProduct(int pageno) {
        binding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("type", type);
            jsonObject.put("page_no",pageno);
//            jsonObject.put("pincode", sessionManager.getStringData(pincode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getRandomProduct(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());
    }

    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra("MyClass", medicine).putParcelableArrayListExtra("PriceList", medicine.getProductInfo()).putStringArrayListExtra("ImageList", medicine.getProductImage()).putExtra("pro_id", medicine.getId()));
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                binding.progressbar.setVisibility(View.GONE);
                Gson gson = new Gson();
                BProduct product = gson.fromJson(result.toString(), BProduct.class);
                totalPages = product.getTotalPages();
                currentDataList.addAll(product.getResultData());
                if (product.getResult().equalsIgnoreCase("true")) {
                    productAdapter = new ProductAdapter(getActivity(), currentDataList, this);
                    recyclerProduct.setAdapter(productAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}