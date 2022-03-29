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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetick.pharmafest.MyApplication;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.ProductAdapter;
import com.onetick.pharmafest.databinding.FragmentBrandProductBinding;
import com.onetick.pharmafest.databinding.FragmentProductBinding;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.ui.ActivityBookingTestDetail;
import com.onetick.pharmafest.ui.ProductDetailsActivity;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.CustPrograssbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements ProductAdapter.RecyclerTouchListener {
    RecyclerView recyclerProduct;
    TextView txtNotfount;
    LinearLayout lvlNotfound;
    private int mPosition;
    ProductAdapter productAdapter;
    CustPrograssbar custPrograssbar;
    private String type = "";
    FragmentProductBinding brandProductBinding;
    int totalpages =0;
    int pageno=1;





    public ProductFragment() {
        // Required empty public constructor
    }


    public static ProductFragment newInstance(int param1, String param2, String type) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt("position", param1);
        args.putString("cid", param2);
        args.putString("type", type);
        fragment.setArguments(args);
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
        brandProductBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product, container, false);
        recyclerProduct = brandProductBinding.recyclerProduct;
        custPrograssbar = new CustPrograssbar();
        dataset();

        brandProductBinding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalpages)
                    {
                        pageno++;
                        SubCategoryFragment.getInstance().getProductlist(pageno);
                    }
                }
            }
        });
        return brandProductBinding.getRoot();
    }

    private void dataset() {
        recyclerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        recyclerProduct.setItemAnimator(new DefaultItemAnimator());
        if (SubCategoryFragment.getInstance().categoryProducts.get(mPosition).getProductlist().size() != 0) {
            brandProductBinding.lvlNotfound.setVisibility(View.GONE);
        } else {
            brandProductBinding.lvlNotfound.setVisibility(View.VISIBLE);
        }
        productAdapter = new ProductAdapter(getActivity(), SubCategoryFragment.getInstance().categoryProducts.get(mPosition).getProductlist(), this);
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
}