                    package com.onetick.pharmafest.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.FeatureBrandAdapter;
import com.onetick.pharmafest.databinding.FragmentBrandBinding;
import com.onetick.pharmafest.model.AllBarand;
import com.onetick.pharmafest.model.Brand;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.ui.HomeActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandFragment extends Fragment implements GetResult.MyListener, FeatureBrandAdapter.RecyclerTouchListener {
    FragmentBrandBinding brandBinding;
    RecyclerView recyclerBrand;
    User user;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    private String type = "";
    List<Brand> brandList = new ArrayList<>();
    int pageno=1;
    int totalPages = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrandFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrandFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandFragment newInstance(String param1, String param2) {
        BrandFragment fragment = new BrandFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            type = getArguments().getString("type");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        brandBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_brand, container, false);
        recyclerBrand = brandBinding.recyclerBrand;
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        recyclerBrand.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        getBrand(1);

        brandBinding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalPages)
                    {
                        pageno++;
                        getBrand(pageno);
                    }
                }
            }
        });
        return  brandBinding.getRoot();
    }

    private void getBrand(int page_no) {
        brandBinding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 136);
            jsonObject.put("type", type);
            jsonObject.put("page_no", page_no);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getBrand(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());

    }

    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            brandBinding.progressbar.setVisibility(View.GONE);
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                AllBarand allBarand = gson.fromJson(result.toString(), AllBarand.class);
                totalPages = allBarand.getTotalPages();
                brandList.addAll(allBarand.getBrandData());
                if (allBarand.getResult().equalsIgnoreCase("true")) {
                    FeatureBrandAdapter featureBrandAdapter = new FeatureBrandAdapter(getActivity(), brandList, "viewall", this);
                    recyclerBrand.setAdapter(featureBrandAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFeaturItem(String titel, String bid) {
        HomeActivity.getInstance().openFragment(new BrandProductFragment().newInstance(titel, bid), type);
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        Toast.makeText(getContext(), "OnResume Called", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Toast.makeText(getContext(), "OnStart Called", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Toast.makeText(getContext(), "OnStop Called", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Toast.makeText(getContext(), "OnPause Called", Toast.LENGTH_SHORT).show();
//    }
}