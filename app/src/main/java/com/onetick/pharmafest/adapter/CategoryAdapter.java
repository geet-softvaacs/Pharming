package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.CatList;
import com.onetick.retrofit.ApiClient;


import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<CatList> mCatlist;
    private RecyclerTouchListener listener;
    private String typeview;

    public interface RecyclerTouchListener {
        public void onClickCategoryItem(String titel, int position, String id);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);

        }
    }

    public CategoryAdapter(Context mContext, List<CatList> mCatlist, String viewtype, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;
        this.typeview = viewtype;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (typeview.equalsIgnoreCase("viewall")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_categoryviewall, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CatList category = mCatlist.get(position);
        holder.title.setText(category.getCatname());
        Glide.with(mContext).load(ApiClient.baseUrl + "/" + category.getCatimg()).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> {
            if (category.getCount() == 0) {
                Toast.makeText(mContext, "Product Not Found !!!", Toast.LENGTH_SHORT).show();
            } else {
                listener.onClickCategoryItem(category.getCatname(), position, category.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }
}