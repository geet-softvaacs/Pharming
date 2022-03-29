package com.onetick.pharmafest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.MyApplication;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.BannerAdapter;
import com.onetick.pharmafest.adapter.CategoryAdapter;
import com.onetick.pharmafest.adapter.LabProductHomeAdapter;
import com.onetick.pharmafest.adapter.UserAdapter;
import com.onetick.pharmafest.databinding.FragmentLabBinding;
import com.onetick.pharmafest.model.Banner;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.Lab;
import com.onetick.pharmafest.model.Tests;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.ui.ActivityBookingTestDetail;
import com.onetick.pharmafest.ui.CartActivity;
import com.onetick.pharmafest.ui.HomeActivity;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LabFragment extends Fragment implements GetResult.MyListener, CategoryAdapter.RecyclerTouchListener, LabProductHomeAdapter.RecyclerTouchListener {
    FragmentLabBinding binding;
    SessionManager sessionManager;
    User user;
    BannerAdapter bannerAdapter;
    List<Banner> bannerList = new ArrayList<>();
    CustPrograssbar custPrograssbar;
    private String type = "";
    LinearLayoutManager layoutManager;
    RecyclerView myRecyclerBanner;
    RecyclerView recyclerCategory;
    RecyclerView recyclerProduct;
    RecyclerView recyclerBrand;

    RecyclerView recyclerUser;
    Timer timer;
    TimerTask timerTask;
    CategoryAdapter categoryAdapter;
    LabProductHomeAdapter productAdapter;
    UserAdapter userAdapter;
    TextView txtviewCategory;
    int position;









    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LabFragment newInstance(String param1, String param2) {
        LabFragment fragment = new LabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lab, container, false);
        sessionManager = new SessionManager(getActivity());
        binding.cvBrand.setVisibility(View.GONE);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        myRecyclerBanner = binding.myRecyclerView;
        recyclerCategory= binding.recyclerCategory;
        recyclerProduct = binding.recyclerProduct;
        recyclerUser = binding.recyclerUser;
        recyclerBrand = binding.recyclerBrand;
        txtviewCategory = binding.txtViewllCategory;
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerBanner.setLayoutManager(layoutManager);

//        setbanner();

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCategory.setLayoutManager(mLayoutManager1);
//        recyclerCategory.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerProduct.setLayoutManager(mLayoutManager2);
//        recyclerProduct.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerBrand.setLayoutManager(mLayoutManager3);
//        recyclerBrand.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity());
        mLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerUser.setLayoutManager(mLayoutManager4);
//        recyclerUser.setItemAnimator(new DefaultItemAnimator());

        binding.txtViewllCategory.setOnClickListener(view -> {
            HomeActivity.getInstance().openFragment(new CategoryFragment(), type);
        });

        binding.txtViewllProduct.setOnClickListener(view -> {
            HomeActivity.getInstance().openFragment(new LabTestsFragment(), type);

        });
        setbanner();

        getHome();

        HomeActivity.rltCart.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), CartActivity.class).putExtra("type", "Lab"));
        });
        return binding.getRoot();
    }

    private void getHome() {
        custPrograssbar.prograssCreate(getActivity());
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("pincode", 1);
            jsonObject.put("type", "Lab");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getLab(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                // Home home = gson.fromJson(labResponse, Home.class);
                Lab lab = gson.fromJson(result.toString(), Lab.class);
                if (lab.getResult().equalsIgnoreCase("true")) {

                    bannerList = lab.getResultData().getBanner();
                    HomeActivity.txtCountCart.setText(""+lab.getResultData().getCartCount());
                    sessionManager.setIntData("labCartCount", lab.getResultData().getCartCount());
//                    sessionManager.setStringData(cu, lab.getResultData().getMainData().getCurrency());
//                    sessionManager.setStringData(terms, lab.getResultData().getMainData().getTerms());
//                    sessionManager.setStringData(contact, lab.getResultData().getMainData().getContact());
//                    sessionManager.setStringData(about, lab.getResultData().getMainData().getAbout());
//                    sessionManager.setStringData(policy, lab.getResultData().getMainData().getPolicy());

                    bannerAdapter = new BannerAdapter(getActivity(), bannerList);
                    myRecyclerBanner.setAdapter(bannerAdapter);

                    if (lab.getResultData().getCatlist().size() > 0) {
                        categoryAdapter = new CategoryAdapter(getActivity(), lab.getResultData().getCatlist(), "single", this);
                        recyclerCategory.setAdapter(categoryAdapter);
                    } else {
                        binding.cvCategory.setVisibility(View.GONE);
                    }

                    if (lab.getResultData().getTests().size() > 0) {
                        productAdapter = new LabProductHomeAdapter(getActivity(), lab.getResultData().getTests(), this::onClickProductItem, "home");
                        recyclerProduct.setAdapter(productAdapter);
                    } else {
                        binding.cvBrand.setVisibility(View.GONE);
                    }

//                    if (lab.getResultData().getTests().size() > 0) {
//                        featureBrandAdapter = new LabsFeatureBrandAdapter(getActivity(), lab.getResultData().getTests(), this, "single");
//                        recyclerBrand.setAdapter(featureBrandAdapter);
//                    } else {
//                        cvBrand.setVisibility(View.GONE);
//                    }

                    if (lab.getResultData().getTestimonial().size() > 0) {
                        userAdapter = new UserAdapter(getActivity(), lab.getResultData().getTestimonial());
                        recyclerUser.setAdapter(userAdapter);
                    } else {
                        binding.cvUsers.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(getActivity(), lab.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }


    private void stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
            position = layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    private void runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (position == myRecyclerBanner.getAdapter().getItemCount() - 1) {
                            position = 0;
                            myRecyclerBanner.smoothScrollBy(5, 0);
                            myRecyclerBanner.smoothScrollToPosition(position);
                        } else {
                            position++;
                            myRecyclerBanner.smoothScrollToPosition(position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            };
            timer.schedule(timerTask, 4000, 4000);
        }

    }

    @Override
    public void onClickCategoryItem(String titel, int position, String id) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("catid", id);
        Fragment fragment = new SubFragmentLab();
        fragment.setArguments(args);
        HomeActivity.getInstance().openFragment(fragment, type);
    }

    @Override
    public void onClickProductItem(String titel, Tests medicine) {
        startActivity(new Intent(getActivity(), ActivityBookingTestDetail.class)
                .putExtra("PriceList", medicine.getTestInfo().getTestPrice())
                .putStringArrayListExtra("ImageList", (ArrayList<String>) medicine.getTestImage())
                .putExtra("name", medicine.getTestName())
                .putExtra("short_desc", medicine.getShortDesc())
                .putExtra("test", medicine.getTests())
                .putExtra("report", medicine.getReport())
                .putExtra("preparation", medicine.getPreparation())
                .putExtra("labid", medicine.getId())
                .putExtra("report", medicine.getReport())
                .putExtra("discount", medicine.getTestInfo().getTestDiscount()));

    }

    private void setbanner() {
        position = 0;
        myRecyclerBanner.scrollToPosition(position);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(myRecyclerBanner);
        myRecyclerBanner.smoothScrollBy(5, 0);

        myRecyclerBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 1) {
                    stopAutoScrollBanner();
                } else if (newState == 0) {
                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner();
                }
            }
        });
    }
}