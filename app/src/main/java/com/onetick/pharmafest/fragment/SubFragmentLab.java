package com.onetick.pharmafest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.LabProductAdapter;
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.databinding.FragmentSubLabBinding;
import com.onetick.pharmafest.model.Categorywithproduct;
import com.onetick.pharmafest.model.LabCategoryProduct;
import com.onetick.pharmafest.model.LabCategorywithproduct;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.TestlistItem;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.ui.ActivityBookingTestDetail;
import com.onetick.pharmafest.ui.ProductDetailsActivity;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubFragmentLab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubFragmentLab extends Fragment implements GetResult.MyListener, LabProductAdapter.RecyclerTouchListener {
    FragmentSubLabBinding binding;
    TabLayout tabLayout;
    ViewPager vpPager;
    User user;
    SessionManager sessionManager;
    String cat_id;
    int position = 0;
    public List<LabCategoryProduct> categoryProducts;
    List<Medicine>categoryProductsList = new ArrayList<>();

    CustPrograssbar custPrograssbar;
    private String type = "Lab";
    int pageno=1;
    int totalPages;

    public static SubFragmentLab subCategoryFragment = null;

    public static SubFragmentLab getInstance() {

        return subCategoryFragment;
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubFragmentLab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubFragmentLab.
     */
    // TODO: Rename and change types and number of parameters
    public static SubFragmentLab newInstance(String param1, String param2) {
        SubFragmentLab fragment = new SubFragmentLab();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sub_lab, container, false);
        subCategoryFragment=this;
        Bundle b = getArguments();
        position = b.getInt("position");
        cat_id = b.getString("catid");
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        binding.recyclerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        getProductlist(pageno);

        binding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalPages)
                    {
                        pageno++;
                        getProductlist(pageno);
                    }
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            binding.progressbar.setVisibility(View.GONE);
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Categorywithproduct categorywithproduct = gson.fromJson(result.toString(), Categorywithproduct.class);
                totalPages = categorywithproduct.getTotalPages();
                categoryProductsList.addAll(categorywithproduct.getCategoryProduct().get(0).getProductlist());

                com.onetick.pharmafest.adapter.LabProductAdapter productAdapter = new com.onetick.pharmafest.adapter.LabProductAdapter(getActivity(),categoryProductsList, this::onClickProductItem);
                binding.recyclerProduct.setAdapter(productAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public class MyPagerAdapter extends FragmentPagerAdapter {
//
//        List<LabCategoryProduct> catlistList;
//
//        public MyPagerAdapter(FragmentManager fragmentManager, List<LabCategoryProduct> catlistList) {
//            super(fragmentManager);
//            this.catlistList = new ArrayList<>();
//            this.catlistList = catlistList;
//        }
//
//        // Returns total number of pages
//        @Override
//        public int getCount() {
//            return catlistList.size();
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Log.e("ppp", "" + position);
//            return LabProductFragment.newInstance(position, catlistList.get(position).getCategoryName(), type);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return catlistList.get(position).getCategoryName();
//        }
//    }

    private void getProductlist(int pageno) {
        binding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("cat_id", cat_id);
            jsonObject.put("page_no", pageno);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getCatProduct(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());

    }


    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        if (type.equalsIgnoreCase(Constant.MEDICINE))
            startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra("MyClass", String.valueOf(medicine)).putStringArrayListExtra("ImageList", (ArrayList<String>) medicine.getProductImage()));
        else {
            startActivity(new Intent(getActivity(), ActivityBookingTestDetail.class)
                    .putExtra("PriceList", medicine.getProductInfo().get(0).getProductPrice())
                    .putStringArrayListExtra("ImageList", (ArrayList<String>) medicine.getProductImage())
                    .putExtra("name", medicine.getProductName())
                    .putExtra("short_desc", medicine.getShortDesc())
                    .putExtra("test", medicine.getTests())
                    .putExtra("report", medicine.getReport())
                    .putExtra("preparation", medicine.getPreparation())
                    .putExtra("labid", medicine.getId())
                    .putExtra("discount", medicine.getProductInfo().get(0).getProductDiscount()));
        }
    }
}