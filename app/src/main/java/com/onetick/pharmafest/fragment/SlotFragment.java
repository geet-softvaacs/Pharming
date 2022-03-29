package com.onetick.pharmafest.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.AdapterDaySlot;
import com.onetick.pharmafest.adapter.AdapterTimeSlot;
import com.onetick.pharmafest.databinding.FragmentSlotBinding;
import com.onetick.pharmafest.model.AvailableSlot;
import com.onetick.pharmafest.model.CheckSlot;
import com.onetick.pharmafest.model.CustomSlotModel;
import com.onetick.pharmafest.model.GetPincode;
import com.onetick.pharmafest.model.Slot;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;
import com.stripe.android.view.AddPaymentMethodActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlotFragment extends Fragment implements GetResult.MyListener, AdapterTimeSlot.RecyclerTouchListener, AdapterDaySlot.RecyclerTouchListener {
    FragmentSlotBinding binding;
    CustPrograssbar custPrograssbar;
    List<AvailableSlot>availableSlot = new ArrayList<>();
    List<CustomSlotModel>availableTimeSlot = new ArrayList<>();
    SessionManager sessionManager;
    List<String>availableSlotDate = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String proid;
    String pincode;
    private String mParam2;

    public SlotFragment() {
        // Required empty public constructor
    }


    public static SlotFragment newInstance(String proId) {
        SlotFragment fragment = new SlotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, proId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proid = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_slot, container, false);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getContext());
        binding.btnPincode.setOnClickListener(view -> {
            if(binding.etpincodeCheck.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Please enter Pincode", Toast.LENGTH_SHORT).show();
            }else{
                getTestSlotAvailable();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();
        Gson gson = new Gson();
        CheckSlot getPincode = gson.fromJson(result.toString(), CheckSlot.class);
        hideKeyboard(getActivity());
        if(getPincode.getResponseCode().equals("201"))
        {
            binding.slotDateRecycler.setVisibility(View.GONE);
            binding.noSlot.setVisibility(View.VISIBLE);
            binding.noSlot.setText(getPincode.getResponseMsg());
        }else{
            if (getPincode.getStatus1().size() != 0) {
                availableSlot = getPincode.getStatus1().get(0).getAvilableSlot();
                for(int i =0; i<getPincode.getStatus1().get(0).getAvilableSlot().size(); i++)
                {
                    if(!availableSlotDate.contains(getPincode.getStatus1().get(0).getAvilableSlot().get(i).getDate()))
                    {
                        availableSlotDate.add(getPincode.getStatus1().get(0).getAvilableSlot().get(i).getDate());
                    }
                }
                AdapterTimeSlot adapterDaySlot = new AdapterTimeSlot(getContext(), availableSlotDate, this::onClickUserItem);
                binding.slotDateRecycler.setAdapter(adapterDaySlot);
                binding.slotDateRecycler.setVisibility(View.VISIBLE);
                binding.noSlot.setVisibility(View.GONE);
            }else if(getPincode.getStatus0().size()!=0)
            {
                binding.slotDateRecycler.setVisibility(View.GONE);
                binding.noSlot.setVisibility(View.VISIBLE);
                binding.slotTimeDateRecycler.setVisibility(View.GONE);
            }
        }
    }

    public void getTestSlotAvailable()
    {
        custPrograssbar.prograssCreate(getContext());
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("pincode", binding.etpincodeCheck.getText().toString());
            jsonObject.put("type", "Lab");
            jsonObject.put("product_id", proid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getTestAvailable(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", getContext());
    }

    @Override
    public void onClickUserItem(String date) {
        availableTimeSlot.clear();
        for(int i =0; i<availableSlot.size(); i++)
        {
            if(availableSlot.get(i).getDate().contains(date))
            {
                CustomSlotModel customSlotModel = new CustomSlotModel(availableSlot.get(i).getSlot(), availableSlot.get(i).getSlotId(), availableSlot.get(i).getDate());
                availableTimeSlot.add(customSlotModel);
            }
        }

        AdapterDaySlot daySlot = new AdapterDaySlot(getContext(), availableTimeSlot, this::onClickUserItem);
        binding.slotTimeDateRecycler.setAdapter(daySlot);
        binding.slotTimeDateRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickUserItem(String date, String time, String id) {
        pincode = binding.etpincodeCheck.getText().toString();
        sessionManager.setStringData("slotDate", date);
        sessionManager.setStringData("slotTime", time);
        sessionManager.setStringData("labPincode", pincode);
        sessionManager.setBooleanData("isSlotSelected", true);
        sessionManager.setStringData("slotId", id);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}