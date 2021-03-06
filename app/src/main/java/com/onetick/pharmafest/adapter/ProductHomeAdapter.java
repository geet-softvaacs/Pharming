package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Medicine> mMedicine;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onClickProductItem(String titel, Medicine medicine);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ProductHomeAdapter(Context mContext, List<Medicine> mMedicine, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mMedicine = mMedicine;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_home_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Medicine medicine = mMedicine.get(position);
        if (medicine.getProductInfo().get(0).getProductDiscount().equals("0.00")) {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.price.setText(sessionManager.getStringData("currency") + medicine.getProductInfo().get(0).getProductPrice());
        } else {
            DecimalFormat format = new DecimalFormat("0.#");
            holder.txtOffer.setText(format.format(medicine.getProductInfo().get(0).getProductDiscount()) + "% OFF");
            double res = (Double.parseDouble(medicine.getProductInfo().get(0).getProductPrice()) / 100.0f) * Double.parseDouble(medicine.getProductInfo().get(0).getProductDiscount());
            res = Double.parseDouble(medicine.getProductInfo().get(0).getProductPrice()) - res;
            holder.price.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceoofer.setText(sessionManager.getStringData("currency") + medicine.getProductInfo().get(0).getProductPrice());

        }
        holder.txtTitle.setText(medicine.getProductName());
        Glide.with(mContext).load(ApiClient.baseUrl + "/" + medicine.getProductImage().get(0)).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
        holder.lvlClick.setOnClickListener(v -> listener.onClickProductItem("category.getCatname()", medicine));
    }

    @Override
    public int getItemCount() {
        return mMedicine.size();
    }

}