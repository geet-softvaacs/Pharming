package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityAboutBinding;
import com.onetick.pharmafest.utils.SessionManager;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;
    SessionManager sessionManager;
    TextView tatDeception;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        sessionManager = new SessionManager(this);
        tatDeception = binding.txtDscirtion;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tatDeception.setText(Html.fromHtml(sessionManager.getStringData("about"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tatDeception.setText(Html.fromHtml(sessionManager.getStringData("about")));
        }

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });
    }
}