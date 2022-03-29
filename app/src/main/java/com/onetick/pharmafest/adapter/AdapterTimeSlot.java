package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.onetick.pharmafest.model.AvailableSlot;
import com.onetick.pharmafest.model.ModelTimeSlot;

import java.util.List;

public class AdapterTimeSlot extends RecyclerView.Adapter<AdapterTimeSlot.MyViewHolder> {
    Context mContext;
    List<String> slotAvailable;
    RecyclerTouchListener listener;
    int row_index=-1;

    public interface RecyclerTouchListener {
        public void onClickUserItem(String date);
    }


    public AdapterTimeSlot(Context mContext, List<String> slotAvailable, RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.slotAvailable =slotAvailable;
        this.listener = listener;
    }
    @NonNull
    @Override
    public AdapterTimeSlot.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slotdate, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTimeSlot.MyViewHolder holder, int position) {
        String time = slotAvailable.get(position);
        holder.slotDate.setText(time);

        holder.layout.setOnClickListener(view -> {
            row_index=position;
            notifyDataSetChanged();
            listener.onClickUserItem(time);
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
        return slotAvailable.size();
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
