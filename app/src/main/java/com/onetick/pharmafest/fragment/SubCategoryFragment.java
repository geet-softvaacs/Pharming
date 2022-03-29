package com.onetick.pharmafest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.onetick.pharmafest.MyApplication;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.databinding.FragmentSubCategoryBinding;
import com.onetick.pharmafest.model.CategoryProduct;
import com.onetick.pharmafest.model.Categorywithproduct;
import com.onetick.pharmafest.model.Medicine;
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
 * Use the {@link SubCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCategoryFragment extends Fragment implements ProductAdapter.RecyclerTouchListener, GetResult.MyListener {
    TabLayout tabLayout;
    ViewPager vpPager;
    User user;
    SessionManager sessionManager;
    int position = 0;
    public List<CategoryProduct> categoryProducts;
    CustPrograssbar custPrograssbar;
    private String type = "Medicine";
    public static SubCategoryFragment subCategoryFragment = null;
    FragmentSubCategoryBinding binding;
    int totalPages, pageno=1;
    List<Medicine>categoryProductsList = new ArrayList<>();
    String catid;
    RecyclerView recyclerProduct;



    public static SubCategoryFragment getInstance() {

        return subCategoryFragment;
    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCategoryFragment newInstance(String param1, String param2) {
        SubCategoryFragment fragment = new SubCategoryFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sub_category, container, false);
        subCategoryFragment=this;
        Bundle b = getArguments();
        position = b.getInt("position");
        catid = b.getString("catid");
        recyclerProduct = binding.recyclerProduct;
        recyclerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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

        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        getProductlist(pageno);
        return binding.getRoot();
    }

    public  void getProductlist(int pageno) {
        binding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("page_no", pageno);
            jsonObject.put("cat_id", catid);
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
    public void callback(JsonObject result, String callNo) {
        binding.progressbar.setVisibility(View.GONE);
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Categorywithproduct categorywithproduct = gson.fromJson(result.toString(), Categorywithproduct.class);
                totalPages = categorywithproduct.getTotalPages();
                categoryProductsList.addAll(categorywithproduct.getCategoryProduct().get(0).getProductlist());

                ProductAdapter productAdapter = new ProductAdapter(getActivity(),categoryProductsList, this::onClickProductItem);
                recyclerProduct.setAdapter(productAdapter);

//                MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), categoryProductsList);
//                binding.vpPager.setAdapter(myPagerAdapter);
//                binding.tabLayout.setupWithViewPager(vpPager);
//                binding.vpPager.setCurrentItem(position);
//                for (int i = 0; i <categorywithproduct.getCategoryProduct().size(); i++) {
//                    LinearLayout customTab = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
//                    CircleImageView tabImage = customTab.findViewById(R.id.img_icon);
//                    TextView tabTitle = customTab.findViewById(R.id.txt_titel);
//                    tabTitle.setText(categorywithproduct.getCategoryProduct().get(i).getCategoryName());
//                    Picasso.get()
//                            .load(ApiClient.baseUrl + "/" + categorywithproduct.getCategoryProduct().get(i).getCategoryImg())
//                            .placeholder(R.drawable.ezgifresize)
//                            .into(tabImage);
//                    tabLayout.getTabAt(i).setCustomView(customTab);
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.black));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        if (type.equalsIgnoreCase(Constant.MEDICINE))
            startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra("MyClass", medicine).putParcelableArrayListExtra("PriceList", medicine.getProductInfo()).putStringArrayListExtra("ImageList", medicine.getProductImage()));
        else {

            if (medicine.getLabInfo() != null)
                MyApplication.Companion.getInstance().setTimeSlot(medicine.getLabInfo().get(0));
//                MyApplication.Companion.getInstance().setTimeSlot(medicine.getLabInfo());
            else if (medicine.getLabInfo() != null && medicine.getLabInfo().size() > 0)
                MyApplication.Companion.getInstance().setTimeSlot(medicine.getLabInfo().get(0));
            else

                startActivity(new Intent(getActivity(), ActivityBookingTestDetail.class).putExtra("MyClass", medicine).putParcelableArrayListExtra("PriceList", medicine.getProductInfo()).putStringArrayListExtra("ImageList", medicine.getProductImage()).putExtra("pro_id", medicine.getId()));
        }
    }

   /* public class MyPagerAdapter extends FragmentPagerAdapter {
        List<CategoryProduct> catlistList;

        public MyPagerAdapter(FragmentManager fragmentManager, List<CategoryProduct> catlistList) {
            super(fragmentManager);
            this.catlistList = new ArrayList<>();
            this.catlistList = catlistList;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return catlistList.size();
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("ppp", " " + position);
            return ProductFragment.newInstance(position, catlistList.get(position).getCategoryName(), type);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catlistList.get(position).getCategoryName();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        categoryProducts = new ArrayList<>();
    }*/
}