package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onetick.pharmafest.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompleteOrderActivity extends AppCompatActivity {
    Integer order_id;
    TextView txtOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        ButterKnife.bind(this);
        txtOrder = findViewById(R.id.txtorder);
        order_id = getIntent().getIntExtra("order_id", 0);

        txtOrder.setText("Your Order No. "+ order_id+ " has been placed successfully and will be processed soon."  );



    }

    @OnClick({R.id.img_back, R.id.btn_myorder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_myorder:
                Intent intent1 = new Intent(this, OrderActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("comeFrom", "complete");
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }
}