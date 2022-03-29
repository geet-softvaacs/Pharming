package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityHomeBinding;
import com.onetick.pharmafest.fragment.CategoryFragment;
import com.onetick.pharmafest.fragment.HomeFragment;
import com.onetick.pharmafest.fragment.LabFragment;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.LabSearch;
import com.onetick.pharmafest.utils.Constant;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    private int screen = 0;
    BottomNavigationView bottomNavigation;
   public static TextView txtCountCart;
   public static TextView txtLabCountCart;
   public static RelativeLayout rltCart;
    private int  REQUEST_CODE = 102;
    Boolean doubleTapBack = false;


    String userid = "0";
    public static HomeActivity homeActivity = null;

    public static HomeActivity getInstance() {

        return homeActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        callInAppUpdate();

        screen = getIntent().getIntExtra("type", 0);
        userid = getIntent().getStringExtra("uid");
        openFragment(new HomeFragment(), Constant.MEDICINE);
        homeActivity = HomeActivity.this;
        bottomNavigation = binding.bottomNavigation;
        txtCountCart = binding.txtCountcard;
        txtLabCountCart = binding.txtCountcardLab;
        rltCart = binding.rltCart;

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        binding.lvlActionsearch.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchActivity.class).putExtra("comeFrom", "medicine"));
        });


        binding.rltNotification.setOnClickListener(view -> {
            startActivity(new Intent(this, NotificationActivity.class));
        });

        binding.lvlActionsearch2.setOnClickListener(view -> {
            startActivity(new Intent(this, LabSearchActiivity.class).putExtra("comeFrom", "lab"));
        });


    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.ic_home);
                    bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.ic_medicine);
                    bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_prescription);
                    bottomNavigation.getMenu().getItem(3).setIcon(R.drawable.ic_labs);
                    bottomNavigation.getMenu().getItem(4).setIcon(R.drawable.ic_setting);
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            item.setIcon(R.drawable.ic_home_black);
                            openFragment(new HomeFragment(), Constant.MEDICINE);
                            binding.lvlActionsearch.setVisibility(View.VISIBLE);
                            binding.lvlActionsearch2.setVisibility(View.GONE);
                            binding.searchTxt.setText("Search Medicine");
                            binding.rltCart.setVisibility(View.VISIBLE);
                            binding.rltCartLab.setVisibility(View.GONE);
                            return true;
                        case R.id.navigation_medicine:
                            item.setIcon(R.drawable.ic_medicine_black);
                            openFragment(new CategoryFragment(), Constant.MEDICINE);
                            binding.lvlActionsearch.setVisibility(View.VISIBLE);
                            binding.lvlActionsearch2.setVisibility(View.GONE);
                            binding.searchTxt.setText("Search Medicine");

                            binding.rltCart.setVisibility(View.VISIBLE);
                            binding.rltCartLab.setVisibility(View.GONE);
                            return true;
                        case R.id.navigation_prescription:
                            item.setIcon(R.drawable.ic_prescription_black);
                            startActivity(new Intent(HomeActivity.this, UploadPrescriptionActivity.class)
                                    .putExtra("uid", userid));
                            return true;
                        case R.id.navigation_notifications:
                            item.setIcon(R.drawable.ic_labs_black);
                            binding.searchTxt.setText("Search Lab Test");
                            openFragment(new LabFragment(), Constant.LAB);
                            binding.lvlActionsearch.setVisibility(View.GONE);
                            binding.lvlActionsearch2.setVisibility(View.VISIBLE);
                            return true;
                        case R.id.navigation_setting:
                            item.setIcon(R.drawable.ic_setting_black);
                            startActivity(new Intent(HomeActivity.this, SettingsActivity.class)
                                    .putExtra("uid", userid));
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment, String type) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null){
            bundle = new Bundle();
        }
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void callInAppUpdate()
    {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(HomeActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if(result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {
                    try{
                        appUpdateManager.startUpdateFlowForResult(result , AppUpdateType.IMMEDIATE, HomeActivity.this, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
        {
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();

            if(resultCode != RESULT_OK)
            {
                Log.d("update", "Update Flow Failed"+resultCode);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleTapBack) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(HomeActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            this.doubleTapBack = true;
            new Handler().postDelayed(() -> doubleTapBack = false, 2000);
        }
    }
}