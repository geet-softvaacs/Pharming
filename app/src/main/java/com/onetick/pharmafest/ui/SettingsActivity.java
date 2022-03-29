package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivitySettingsBinding;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");


        binding.lvlAbout.setOnClickListener(view -> {
            Intent i = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(i);
        });

        binding.lvlContect.setOnClickListener(view -> {
            Intent i = new Intent(SettingsActivity.this, ContactActivity.class);
            startActivity(i);
        });

        binding.lvlPrivacy.setOnClickListener(view -> {
            Intent i = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(i);
        });

        binding.lvlTeams.setOnClickListener(view -> {
            Intent i = new Intent(SettingsActivity.this, TermConditionActivity.class);
            startActivity(i);
        });




        binding.lvlLogot.setOnClickListener(view -> {
            sessionManager.logoutUser();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        binding.lvlOrder.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, OrderSelectTypeActivity.class));

        });

        binding.lvlEdit.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class).putExtra("comeFrom", "editProfile"));

        });

        binding.lvlAddress.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, AddressActivity.class).putExtra("comeFrom", "settings"));

        });



        if (user.getFname()!=null && !user.getFname().isEmpty()) {
            char first = user.getFname().charAt(0);
            binding.txtfirstl.setText("" + first);
            binding.txtName.setText("" + user.getFname());
        }else{
            binding.txtfirstl.setText("U");
            binding.txtName.setText("User");
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            binding.txtMob.setText("" + user.getMobile());
        } else {
            binding.txtMob.setText("");
        }
    }
}