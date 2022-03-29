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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.BannerAdapter;
import com.onetick.pharmafest.adapter.CategoryAdapter;
import com.onetick.pharmafest.adapter.FeatureBrandAdapter;
import com.onetick.pharmafest.adapter.ProductHomeAdapter;
import com.onetick.pharmafest.adapter.UserAdapter;
import com.onetick.pharmafest.databinding.FragmentHomeBinding;
import com.onetick.pharmafest.model.Banner;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.ui.CartActivity;
import com.onetick.pharmafest.ui.HomeActivity;
import com.onetick.pharmafest.ui.ProductDetailsActivity;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements GetResult.MyListener, FeatureBrandAdapter.RecyclerTouchListener , CategoryAdapter.RecyclerTouchListener, ProductHomeAdapter.RecyclerTouchListener{
    FragmentHomeBinding binding;
    private String type = "";
    SessionManager sessionManager;
    User user;
    BannerAdapter bannerAdapter;
    List<Banner> bannerList = new ArrayList<>();
    CustPrograssbar custPrograssbar;
    RecyclerView myRecyclerBanner;
    RecyclerView recyclerCategory;
    RecyclerView recyclerProduct;
    RecyclerView recyclerBrand;
    RecyclerView recyclerUser;
    CategoryAdapter categoryAdapter;
    ProductHomeAdapter productAdapter;
    FeatureBrandAdapter featureBrandAdapter;
    UserAdapter userAdapter;
    int position;
    Timer timer;
    TimerTask timerTask;
    LinearLayoutManager layoutManager;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        sessionManager = new SessionManager(getContext());
        myRecyclerBanner = binding.myRecyclerView;
        recyclerCategory =binding.recyclerCategory;
        recyclerProduct =binding.recyclerProduct;
        recyclerBrand = binding.recyclerBrand;
        recyclerUser = binding.recyclerUser;
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myRecyclerBanner.setLayoutManager(layoutManager);
        user = sessionManager.getUserDetails("");

        setbanner();

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCategory.setLayoutManager(mLayoutManager1);

        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerProduct.setLayoutManager(mLayoutManager2);

        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerBrand.setLayoutManager(mLayoutManager3);

        LinearLayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity());
        mLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerUser.setLayoutManager(mLayoutManager4);

        binding.txtViewllBrand.setOnClickListener(view -> {
            HomeActivity.getInstance().openFragment(new BrandFragment(), type);
        });

        binding.txtViewllCategory.setOnClickListener(view -> {
            HomeActivity.getInstance().openFragment(new CategoryFragment(), type);
        });

        binding.txtViewllProduct.setOnClickListener(view -> {
            HomeActivity.getInstance().openFragment(new PopularProductFragment(), type);

        });

        HomeActivity.rltCart.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), CartActivity.class).putExtra("type", "Medicine"));

        });


        getHome();
        return binding.getRoot();
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Home home = gson.fromJson(result.toString(), Home.class);
                Log.e("error", "jkjdkjfd");
                if (home.getResult().equalsIgnoreCase("true"
                ) && home.getResponse_code().equals("200")) {
                    binding.progress.setVisibility(View.GONE);
                    binding.progressTitle.setVisibility(View.GONE);
                    binding.llHome.setVisibility(View.VISIBLE);
                    bannerList = home.getHomeData().getBanner();
                    bannerAdapter = new BannerAdapter(getActivity(), bannerList);
                    myRecyclerBanner.setAdapter(bannerAdapter);
                    HomeActivity.txtCountCart.setText(""+home.getHomeData().getCartItemCount());
                    sessionManager.setIntData("cartCount", home.getHomeData().getCartItemCount());

                    categoryAdapter = new CategoryAdapter(getActivity(), home.getHomeData().getCatlist(), "single", this::onClickCategoryItem);
                    recyclerCategory.setAdapter(categoryAdapter);

                    productAdapter = new ProductHomeAdapter(getActivity(), home.getHomeData().getMedicine(), this::onClickProductItem);
                    recyclerProduct.setAdapter(productAdapter);
//
                    featureBrandAdapter = new FeatureBrandAdapter(getActivity(), home.getHomeData().getBrand(), "single", this);
                    recyclerBrand.setAdapter(featureBrandAdapter);

                    userAdapter = new UserAdapter(getActivity(), home.getHomeData().getTestimonial());
                    recyclerUser.setAdapter(userAdapter);


                    sessionManager.setStringData("currency", home.getHomeData().getMainData().getCurrency());
                    sessionManager.setStringData("terms", home.getHomeData().getMainData().getTerms());
                    sessionManager.setStringData("contact", home.getHomeData().getMainData().getContact());
                    sessionManager.setStringData("about", home.getHomeData().getMainData().getAbout());
                    sessionManager.setStringData("policy", home.getHomeData().getMainData().getPolicy());





                } else {

                    Toast.makeText(getActivity(), home.getResponse_msg(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }

    private void getHome() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.progressTitle.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", type);
            jsonObject.put("uid", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getHome(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());
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
                        if (position == Objects.requireNonNull(myRecyclerBanner.getAdapter()).getItemCount() - 1) {
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
    public void onClickFeaturItem(String titel, String bid) {
        HomeActivity.getInstance().openFragment(new BrandProductFragment().newInstance(titel, bid), type);
    }

    @Override
    public void onClickCategoryItem(String titel, int position, String id) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("catid", id);
        Fragment fragment = new SubCategoryFragment();
        fragment.setArguments(args);
        HomeActivity.getInstance().openFragment(fragment, type);
    }

    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        startActivity(new Intent(getActivity(), ProductDetailsActivity.class).
                putExtra("MyClass", medicine)
                .putParcelableArrayListExtra("PriceList", medicine.getProductInfo())
                .putStringArrayListExtra("ImageList", medicine.getProductImage()));
    }
}