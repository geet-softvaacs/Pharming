package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityTermConditionBinding;
import com.onetick.pharmafest.utils.SessionManager;

public class TermConditionActivity extends AppCompatActivity {
    ActivityTermConditionBinding binding;
    TextView txtDescription;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this, R.layout.activity_term_condition);
       sessionManager = new SessionManager(this);
       txtDescription = binding.txtDscirtion;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtDescription.setText(Html.fromHtml(sessionManager.getStringData("terms"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtDescription.setText(Html.fromHtml(sessionManager.getStringData("terms")));
        }

        binding.imgBack.setOnClickListener(view -> {
            finish();
        });

    }
}