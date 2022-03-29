package com.onetick.pharmafest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.LabProductHomeAdapter;
import com.onetick.pharmafest.databinding.FragmentLabTestsBinding;
import com.onetick.pharmafest.model.Lab;
import com.onetick.pharmafest.model.LabTestModel;
import com.onetick.pharmafest.model.Tests;
import com.onetick.pharmafest.ui.ActivityBookingTestDetail;
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
 * Use the {@link LabTestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LabTestsFragment extends Fragment implements GetResult.MyListener,LabProductHomeAdapter.RecyclerTouchListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentLabTestsBinding binding;
    List<Tests>testsList= new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int pageno=1;
    int totalPages;
    public LabTestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LabTestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LabTestsFragment newInstance(String param1, String param2) {
        LabTestsFragment fragment = new LabTestsFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lab_tests, container, false);
        getLabTests(pageno);
        binding.recyclerLabtest.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        binding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalPages)
                    {
                        pageno++;
                        getLabTests(pageno);
                    }
                }
            }
        });
        return binding.getRoot();
    }


    public void getLabTests(int pageno)
    {
        binding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page_no", pageno);
            jsonObject.put("type", "Lab");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getLabTests(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        if(callNo.equalsIgnoreCase("1"))
        {
            Gson gson = new Gson();
            LabTestModel lab = gson.fromJson(result.toString(), LabTestModel.class);
            totalPages = lab.getTotalPages();
            testsList.addAll(lab.getProducts().get(0).getTestlist());
            LabProductHomeAdapter productAdapter = new LabProductHomeAdapter(getActivity(),testsList, this::onClickProductItem, "viewall");
            binding.recyclerLabtest.setAdapter(productAdapter);
            binding.progressbar.setVisibility(View.GONE);
        }
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
}