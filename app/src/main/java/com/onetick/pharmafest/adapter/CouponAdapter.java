package com.onetick.pharmafest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmingo.model.Couponlist;
import com.onetick.retrofit.ApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    private Context mContext;
    private List<Couponlist> couponsList;
    private RecyclerTouchListener listener;
    private int amount;
    SessionManager sessionManager;

    public CouponAdapter(Context mContext, List<Couponlist> couponsList, RecyclerTouchListener listener, int amount) {
        this.mContext = mContext;
        this.couponsList = couponsList;
        this.listener = listener;
        this.amount = amount;
        sessionManager = new SessionManager(mContext);
    }

    public interface RecyclerTouchListener {
        public void onClickItem(View v, Couponlist coupon);
    }

    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        Couponlist coupon = couponsList.get(position);
        Glide.with(mContext).load(ApiClient.baseUrl + "/" + coupon.getCImg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgCode);

        if (amount < coupon.getMinAmt()) {
            holder.txtApply.setTextColor(mContext.getResources().getColor(R.color.colorgrey));
            holder.txtApply.setEnabled(false);
        } else {
            holder.txtApply.setEnabled(true);
            holder.txtApply.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        holder.txtCoupon.setText("" + coupon.getCouponCode());
        holder.txtTitel.setText("" + coupon.getCouponTitle());
        holder.txtAmount.setText(sessionManager.getStringData("currency") + coupon.getCValue());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtDesc.setText(Html.fromHtml(coupon.getCDesc(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.txtDesc.setText(Html.fromHtml(coupon.getCDesc()));
        }
        makeTextViewResizable(holder.txtDesc, 3, "See More", true);
        holder.txtApply.setOnClickListener(v -> listener.onClickItem(v, coupon));

    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_coupon)
        TextView txtCoupon;
        @BindView(R.id.txt_apply)
        TextView txtApply;
        @BindView(R.id.txt_titel)
        TextView txtTitel;
        @BindView(R.id.txt_amount)
        TextView txtAmount;
        @BindView(R.id.txt_desc)
        TextView txtDesc;
        @BindView(R.id.img_code)
        CircleImageView imgCode;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#F28021"));
        }

        @Override
        public void onClick(View widget) {
        }
    }
}
