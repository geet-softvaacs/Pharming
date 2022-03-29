package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityOptionMedicineLabBinding;

public class OptionActivityMedicineLab extends AppCompatActivity {
    ActivityOptionMedicineLabBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_option_medicine_lab);
        binding.lab.setOnClickListener(view -> {
            switchHome(1);
        });
        binding.medicine.setOnClickListener(view -> {
            switchHome(2);
        });
    }

    private void switchHome(int type) {

        startActivity(new Intent(OptionActivityMedicineLab.this, HomeActivity.class).putExtra("type", type));
        finish();
    }
}