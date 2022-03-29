package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.Brand;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandProductAdapter extends RecyclerView.Adapter<BrandProductAdapter.Viewholder> {
    private Context mContext;
    private List<Medicine> medicineList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onClickProductItem(String titel, Medicine medicine, int position);
    }

    public BrandProductAdapter(Context mContext, List<Medicine> medicineList, RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.medicineList = medicineList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @NonNull
    @Override
    public BrandProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandProductAdapter.Viewholder holder, int position) {
        Medicine medicine = medicineList.get(position);
        if (medicine.getProductInfo().get(0).getProductDiscount().equals("0.00")) {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.price.setText(sessionManager.getStringData("currency") + medicine.getProductInfo().get(0).getProductPrice());
        } else {
            DecimalFormat format = new DecimalFormat("0.#");
            holder.txtOffer.setText(format.format(Double.parseDouble(medicine.getProductInfo().get(0).getProductDiscount()))+ "% OFF");
            double res = (Double.parseDouble(medicine.getProductInfo().get(0).getProductPrice()) / 100.0f) * Double.parseDouble(medicine.getProductInfo().get(0).getProductDiscount());
            res = Double.parseDouble(medicine.getProductInfo().get(0).getProductPrice()) - res;
            holder.price.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceoofer.setText(sessionManager.getStringData("currency") + medicine.getProductInfo().get(0).getProductPrice());

        }
        holder.txtTitle.setText(medicine.getProductName());
        Glide.with(mContext).load(ApiClient.baseUrl + "/" + medicine.getProductImage().get(0)).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
        holder.lvlClick.setOnClickListener(v ->            listener.onClickProductItem("category.getCatname()", medicine, position));

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.priceoofer)
        TextView priceoofer;
        @BindView(R.id.lvl_click)
        LinearLayout lvlClick;
        @BindView(R.id.txt_offer)
        TextView txtOffer;
        @BindView(R.id.lvl_offer)
        LinearLayout lvlOffer;


        public Viewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
