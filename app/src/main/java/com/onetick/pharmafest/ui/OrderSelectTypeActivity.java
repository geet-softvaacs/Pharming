package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityOrderSelectTypeBinding;

public class OrderSelectTypeActivity extends AppCompatActivity {
    ActivityOrderSelectTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_select_type);

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.lvlOrder.setOnClickListener(view -> {
            Intent i = new Intent(OrderSelectTypeActivity.this, OrderActivity.class);
            i.putExtra("comeFrom", "order");
            startActivity(i);
        });

        binding.lvlMyprescription.setOnClickListener(view -> {
            startActivity(new Intent(OrderSelectTypeActivity.this, PrescriptionOrderActivity.class));
        });
    }
}