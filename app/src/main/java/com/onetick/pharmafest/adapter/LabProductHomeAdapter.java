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
import com.onetick.pharmafest.model.Tests;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Utility;

import java.text.DecimalFormat;
import java.util.List;

public class LabProductHomeAdapter extends RecyclerView.Adapter<LabProductHomeAdapter.ViewHolder> {
    private Context mContext;
    private List<Tests> mMedicine;
    private RecyclerTouchListener listener;
    String type;


    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onClickProductItem(String titel, Tests medicine);
    }

    public LabProductHomeAdapter(Context mContext, List<Tests> mMedicine, final RecyclerTouchListener listener, String type) {
        this.mContext = mContext;
        this.listener = listener;
        this.mMedicine = mMedicine;
        this.type = type;
        sessionManager = new SessionManager(mContext);
    }

    @NonNull
    @Override
    public LabProductHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_home_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LabProductHomeAdapter.ViewHolder holder, int position) {
        Tests medicine = mMedicine.get(position);
        if (medicine.getTestInfo().getTestDiscount()!=null && medicine.getTestInfo().getTestDiscount().equals("0.00")||medicine.getTestInfo().getTestDiscount()==null) {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.price.setText(sessionManager.getStringData("currency") + medicine.getTestInfo().getTestPrice());
        } else {
            DecimalFormat format = new DecimalFormat("0.#");
            if (medicine.getTestInfo().getTestDiscount()!=null) {
                holder.txtOffer.setText(medicine.getTestInfo().getTestDiscount() + "% OFF");
            }else if(medicine.getTestInfo().getTestDiscount()==null){
                holder.lvlOffer.setVisibility(View.GONE);
            }
            if (medicine.getTestInfo().getTestDiscount()!=null) {
                double res = (Double.parseDouble(medicine.getTestInfo().getTestPrice()) / 100.0f) * Double.parseDouble(medicine.getTestInfo().getTestDiscount());
                res = Double.parseDouble(medicine.getTestInfo().getTestPrice()) - res;
                holder.price.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            }else{
                holder.price.setText(sessionManager.getStringData("currency") + medicine.getTestInfo().getTestPrice());
            }
            holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceoofer.setText(sessionManager.getStringData("currency") + medicine.getTestInfo().getTestPrice());

        }
        holder.txtTitle.setText(medicine.getTestName());
        if(medicine.getTestImage().get(0)!=null){
            Glide.with(mContext).load(Utility.getUrl(medicine.getTestImage().get(0))).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
        }
        holder.lvlClick.setOnClickListener(v -> listener.onClickProductItem("category.getCatname()", medicine));
    }

    @Override
    public int getItemCount() {
        if(type.equals("home"))
        {
            if(mMedicine.size()>6)
            {
                return 6;
            }else{
                return mMedicine.size();
            }
        }else{
            return mMedicine.size();
        }
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
            txtTitle = itemView.findViewById(R.id.txtTitle);
            price = itemView.findViewById(R.id.price);
            priceoofer = itemView.findViewById(R.id.priceoofer);
            lvlClick = itemView.findViewById(R.id.lvl_click);
            txtOffer = itemView.findViewById(R.id.txt_offer);
            lvlOffer = itemView.findViewById(R.id.lvl_offer);
        }
    }
}
