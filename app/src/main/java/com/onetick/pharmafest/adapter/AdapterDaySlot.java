package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.CustomSlotModel;
import com.onetick.pharmafest.model.ModelDaySlot;

import java.util.List;

public class AdapterDaySlot extends RecyclerView.Adapter<AdapterDaySlot.MyViewHolder> {
   Context mContext;
   List<CustomSlotModel> mTestimonial;
    RecyclerTouchListener listener;
    int row_index= -1;

    public AdapterDaySlot(Context mContext, List<CustomSlotModel> mTestimonial, RecyclerTouchListener  listener) {
        this.mContext = mContext;
        this.mTestimonial = mTestimonial;
        this.listener = listener;
    }

    public interface RecyclerTouchListener {
        void onClickUserItem(String date, String time, String id);
    }

    @NonNull
    @Override
    public AdapterDaySlot.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slotdate, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDaySlot.MyViewHolder holder, int position) {
        CustomSlotModel time = mTestimonial.get(position);
        holder.slotDate.setText(time.getSlotTime());

        holder.layout.setOnClickListener(view -> {
            row_index=position;
            notifyDataSetChanged();
            listener.onClickUserItem(mTestimonial.get(position).getSlotDate(), mTestimonial.get(position).getSlotTime(), mTestimonial.get(position).getSlotId());
        });

        if(row_index==position)
        {
            Drawable highlight = mContext.getResources().getDrawable( R.drawable.imagehighlight);
            holder.layout.setBackground(highlight);
        }else{
            Drawable highlight = mContext.getResources().getDrawable( R.drawable.custom_slot_itembg);
            holder.layout.setBackground(highlight);
        }
    }

    @Override
    public int getItemCount() {
        return mTestimonial.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView slotDate;
        public LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            slotDate = view.findViewById(R.id.slotDate);
            layout = view.findViewById(R.id.ll_time_slot);
        }
    }
}
