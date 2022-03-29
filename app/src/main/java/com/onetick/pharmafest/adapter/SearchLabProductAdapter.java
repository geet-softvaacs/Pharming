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
import com.onetick.pharmafest.model.SearchResultItem;
import com.onetick.pharmafest.ui.SearchActivity;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchLabProductAdapter extends RecyclerView.Adapter<SearchLabProductAdapter.MyViewHolder> {
    private Context mContext;
    private List<SearchResultItem> medicineList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public SearchLabProductAdapter(SearchActivity mContext, List<SearchResultItem> searchResult, SearchActivity searchActivity) {
    }

    public interface RecyclerTouchListener {
        public void onClickProductItem(String titel, SearchResultItem medicine);
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
    public SearchLabProductAdapter(Context mContext, List<SearchResultItem> medicineList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.medicineList = medicineList;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SearchResultItem medicine = medicineList.get(position);
        if (medicine.getDiscount()==null|| medicine.getDiscount().equals("0.00")) {
            holder.lvlOffer.setVisibility(View.GONE);
            holder.priceoofer.setVisibility(View.GONE);
            holder.price.setText(sessionManager.getStringData("currency") + medicine.getPrice());
        } else {
            DecimalFormat format = new DecimalFormat("0.#");
            holder.txtOffer.setText(medicine.getDiscount()+ "% OFF");
            double res = (Double.parseDouble(medicine.getPrice()) / 100.0f) * Double.parseDouble(medicine.getDiscount());
            res = Double.parseDouble(medicine.getPrice()) - res;
            holder.price.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            holder.priceoofer.setPaintFlags(holder.priceoofer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceoofer.setText(sessionManager.getStringData("currency") + medicine.getPrice());

        }
        holder.txtTitle.setText(medicine.getTest_name());
        Glide.with(mContext).load(ApiClient.baseUrl +"/"+ medicine.getTest_image().get(0)).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
        holder.lvlClick.setOnClickListener(v -> listener.onClickProductItem("category.getCatname()", medicine));
    }
    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}