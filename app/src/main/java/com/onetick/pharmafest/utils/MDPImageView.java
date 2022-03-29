package com.onetick.pharmafest.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class MDPImageView extends AppCompatImageView {

    public MDPImageView(Context context) {
        super(context);
    }

    public MDPImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MDPImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); // This is the key that will make the height equivalent to its width
        setMeasuredDimension(getMeasuredWidth(), (int) ((getMeasuredWidth() * 9) / 16)); //Snap to width
    }
}