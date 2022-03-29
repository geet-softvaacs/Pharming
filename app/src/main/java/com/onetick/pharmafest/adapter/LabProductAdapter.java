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
import com.onetick.pharmafest.model.LabCategoryProduct;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.TestlistItem;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;

import org.bouncycastle.util.test.Test;

import java.text.DecimalFormat;
import java.util.List;

public class LabProductAdapter extends RecyclerView.Adapter<LabProductAdapter.ViewHolder> {
    private Context mContext;
    private List<Medicine> medicineList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public LabProductAdapter(Context mContext, List<Medicine>medicineList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.medicineList = medicineList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    public interface RecyclerTouchListener {
        public void onClickProductItem(String titel, Medicine medicine);
    }
    @NonNull
    @Override
    public LabProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LabProductAdapter.ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        String price = "0";
        String discount = "0";

        if (medicine.getProductInfo().get(0).getProductDiscount()==null|| medicine.getProductInfo().get(0).getProductDiscount().equals("0.00")||medicine.getProductInfo().get(0).getProductDiscount().equals("0.0"))
        {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.price.setText(sessionManager.getStringData("currency") + medicine.getProductInfo().get(0).getProductPrice());
        }

        if(medicine.getProductInfo().get(0).getProductPrice()!=null)
        {
            price = medicine.getProductInfo().get(0).getProductPrice();
        }
        if(medicine.getProductInfo().get(0).getProductDiscount()!=null)
            discount = String.valueOf(medicine.getProductInfo().get(0).getProductDiscount());
        if (price.equals("0")) {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.price.setText(sessionManager.getStringData("currency") + price);
        } else {
            DecimalFormat format = new DecimalFormat("0.#");
            holder.txtOffer.setText(discount + "% OFF");
            double res = (Double.parseDouble(price) / 100.0f) * Double.parseDouble(discount);
            res = Double.parseDouble(price) - res;
            holder.price.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceoofer.setText(sessionManager.getStringData("currency") + price);

        }
        if(medicine.getProductName()!=null)
        {
            holder.txtTitle.setText(medicine.getProductName());

        }
        Glide.with(mContext).load(ApiClient.baseUrl + "/" + medicine.getProductImage().get(0)).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
        holder.lvlClick.setOnClickListener(v -> listener.onClickProductItem("category.getCatname()", medicine));
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView price;
        TextView priceoofer;
        LinearLayout lvlClick;
        TextView txtOffer;
        LinearLayout lvlOffer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_icon);
            txtTitle= itemView.findViewById(R.id.txtTitle);
            price = itemView.findViewById(R.id.price);
            priceoofer = itemView.findViewById(R.id.priceoofer);
            lvlClick = itemView.findViewById(R.id.lvl_click);
            txtOffer = itemView.findViewById(R.id.txt_offer);
            lvlOffer= itemView.findViewById(R.id.lvl_offer);
        }
    }
}
