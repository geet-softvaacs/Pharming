package com.onetick.pharmafest.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.LabProductAdapter;
import com.onetick.pharmafest.databinding.FragmentLabProductBinding;
import com.onetick.pharmafest.model.LabCategoryProduct;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.TestlistItem;
import com.onetick.pharmafest.ui.ActivityBookingTestDetail;
import com.onetick.pharmafest.ui.ProductDetailsActivity;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.CustPrograssbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LabProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LabProductFragment extends Fragment implements LabProductAdapter.RecyclerTouchListener{
    RecyclerView recyclerProduct;
    TextView txtNotfount;
    LinearLayout lvlNotfound;
    private int mPosition;
    LabProductAdapter productAdapter;
    CustPrograssbar custPrograssbar;
    private String type = "";
    FragmentLabProductBinding binding;
    public List<Medicine> categoryProducts;
    public List<LabCategoryProduct>products;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LabProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LabProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LabProductFragment newInstance(int param1, String param2, String type) {
        LabProductFragment fragment = new LabProductFragment();
        Bundle args = new Bundle();
        args.putInt("position", param1);
        args.putString("cid", param2);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }
    public static LabProductFragment newInstance(String param1, String param2) {
        LabProductFragment fragment = new LabProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lab_product, container, false);
        recyclerProduct = binding.recyclerProduct;
        lvlNotfound = binding.lvlNotfound;
        txtNotfount = binding.txtNotfount;
        custPrograssbar = new CustPrograssbar();

        return binding.getRoot();
    }

    private void dataset() {
        recyclerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        products = SubFragmentLab.getInstance().categoryProducts;
//        categoryProducts = SubFragmentLab.getInstance().categoryProducts.get(mPosition).getTestlist();
//        recyclerProduct.setItemAnimator(new DefaultItemAnimator());
        if (SubFragmentLab.getInstance().categoryProducts.get(mPosition).getTestlist().size() != 0) {
            lvlNotfound.setVisibility(View.GONE);
        } else {
            lvlNotfound.setVisibility(View.VISIBLE);
        }
        productAdapter = new LabProductAdapter(getActivity(),categoryProducts, this);
        recyclerProduct.setAdapter(productAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (recyclerProduct != null) {
            dataset();
        }
    }



    @Override
    public void onClickProductItem(String titel, Medicine medicine) {
        if (type.equalsIgnoreCase(Constant.MEDICINE))
            startActivity(new Intent(getActivity(), ProductDetailsActivity.class).putExtra("MyClass", String.valueOf(medicine)).putStringArrayListExtra("ImageList", (ArrayList<String>) medicine.getProductImage()));
        else {
//
//            if (medicine.getLabInfo() != null)
//                MyApplication.Companion.getInstance().setTimeSlot(medicine.getLabInfo().get(0));
////                MyApplication.Companion.getInstance().setTimeSlot(medicine.getLabInfo());
//            else if (medicine.getLabInfo() != null && medicine.getLabInfo().size() > 0)
//                MyApplication.Companion.getInstance().setTimeSlot(medicine.getLabInfo().get(0));
//            else
//                MyApplication.Companion.getInstance().setTimeSlot(null);
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