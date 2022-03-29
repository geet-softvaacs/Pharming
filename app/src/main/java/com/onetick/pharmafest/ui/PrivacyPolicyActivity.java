package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityPrivacyPolicyBinding;
import com.onetick.pharmafest.utils.SessionManager;

public class PrivacyPolicyActivity extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;
    TextView txtDscirtion;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        txtDscirtion = binding.txtDscirtion;
        sessionManager = new SessionManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDscirtion.setText(Html.fromHtml(sessionManager.getStringData("policy"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtDscirtion.setText(Html.fromHtml(sessionManager.getStringData("policy")));
        }

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });
    }
}