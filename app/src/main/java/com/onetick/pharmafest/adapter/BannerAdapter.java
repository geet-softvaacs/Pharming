package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.Banner;
import com.onetick.pharmafest.utils.Utility;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private Context context;
    private List<Banner> mBanner;

    public BannerAdapter(Context context, List<Banner> mBanner) {
        this.context = context;
        this.mBanner = mBanner;
    }
    @NonNull
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_banner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(Utility.getUrl(mBanner.get(position).getImg())).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgBanner);

    }

    @Override
    public int getItemCount() {
        return mBanner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBanner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.img_banner);
        }
    }
}
