package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityOrderBinding;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.OrderH;
import com.onetick.pharmafest.model.OrderHistory;
import com.onetick.pharmafest.model.RestResponse;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;
import com.stripe.model.Order;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OrderActivity extends AppCompatActivity implements GetResult.MyListener {
    ActivityOrderBinding binding;
    StaggeredGridLayoutManager gridLayoutManager;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    ItemAdp itemAdp;
    User user;
    List<OrderHistory> resent = new ArrayList<>();
    List<OrderHistory> delivery = new ArrayList<>();
    List<OrderHistory>resentOrderList= new ArrayList<>();
    List<OrderHistory>deliveryOrderList= new ArrayList<>();
    int pageno=1;
    int totalPages;
    List<OrderHistory> orderHistories = new ArrayList<>();
    String comeFrom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        sessionManager = new SessionManager(this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        comeFrom = getIntent().getStringExtra("comeFrom");


        binding.txtResent.setOnClickListener(view -> {
            dataset(resentOrderList);
            binding.txtResent.setTextColor(getResources().getColor(R.color.white));
            binding.txtDelivered.setTextColor(getResources().getColor(R.color.black));
            binding.txtResent.setBackground(getResources().getDrawable(R.drawable.orderbox));
            binding.txtDelivered.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
        });

        binding.txtDelivered.setOnClickListener(view -> {
            dataset(deliveryOrderList);
            binding.txtResent.setTextColor(getResources().getColor(R.color.black));
            binding.txtDelivered.setTextColor(getResources().getColor(R.color.white));
            binding.txtResent.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
            binding.txtDelivered.setBackground(getResources().getDrawable(R.drawable.orderbox));
        });

        binding.imgBack.setOnClickListener(view -> {
            if(comeFrom.equals("order"))
            {
                finish();
            }else if(comeFrom.equals("complete"))
            {
                startActivity(new Intent(OrderActivity.this, HomeActivity.class));
                finish();
            }
        });
        getOrder(pageno);

        binding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalPages)
                    {
                        pageno++;
                        getOrder(pageno);
                    }
                }
            }
        });
    }


    public void dataset(List<OrderHistory> s) {

        if (s.size() != 0) {
            binding.lvlNotfound.setVisibility(View.GONE);
            binding.myRecyclerView.setVisibility(View.VISIBLE);
            gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
            binding.myRecyclerView.setLayoutManager(gridLayoutManager);
            itemAdp = new ItemAdp(OrderActivity.this, s);
            binding.myRecyclerView.setAdapter(itemAdp);
        } else {
            binding.myRecyclerView.setVisibility(View.GONE);
            binding.lvlNotfound.setVisibility(View.VISIBLE);
           binding.txtNotfount.setText("Your orders will be displayed hear.");
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            binding.progressbar.setVisibility(View.GONE);
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                OrderH orderH = gson.fromJson(result.toString(), OrderH.class);
                totalPages = orderH.getTotalPages();
                if (orderH.getResult().equalsIgnoreCase("true")) {
                    orderHistories = orderH.getOrderHistory();
                    resent = new ArrayList<>();
                    delivery = new ArrayList<>();
                    for (int i = 0; i < orderHistories.size(); i++) {
                        if (orderHistories.get(i).getStatus().equalsIgnoreCase("Completed")) {
                            delivery.add(orderHistories.get(i));
                        } else {
                            resent.add(orderHistories.get(i));
                        }
                    }
                    resentOrderList.addAll(resent);
                    deliveryOrderList.addAll(delivery);

                    if(binding.txtResent.getCurrentTextColor()== getResources().getColor(R.color.white))
                    {
                        dataset(resentOrderList);
                    }else if(binding.txtDelivered.getCurrentTextColor()== getResources().getColor(R.color.white))
                    {
                        dataset(deliveryOrderList);
                    }
                } else {
                    binding.myRecyclerView.setVisibility(View.GONE);
                    binding.lvlNotfound.setVisibility(View.VISIBLE);
                    binding.txtNotfount.setText("Your orders will be displayed hear.");
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equalsIgnoreCase("true")) {
                    itemAdp.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {


        private LayoutInflater mInflater;
        Context mContext;
        List<OrderHistory> lists;

        public ItemAdp(Context context, List<OrderHistory> s) {
            this.mInflater = LayoutInflater.from(context);
            this.lists = s;
            this.mContext = context;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_orderitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {

            OrderHistory history = lists.get(i);
            holder.txtOrder.setText("" + history.getId());
            holder.txtOrderdate.setText("" + history.getOrderDate());
            holder.txtOrderstatus.setText("" + history.getStatus());
            holder.txtTotal.setText(sessionManager.getStringData("currency") + history.getTotal());
            if (history.getStatus().equalsIgnoreCase("Pending")) {
                holder.txtCancel.setVisibility(View.VISIBLE);
            } else {
                holder.txtCancel.setVisibility(View.INVISIBLE);

            }
            holder.txtInfo.setOnClickListener(v -> startActivity(new Intent(OrderActivity.this, OrderDetailsActivity.class).putExtra("oid", history.getId())));
            holder.txtCancel.setOnClickListener(v -> {
                AlertDialog myDelete = new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("Order Cancel")
                        .setMessage("Are you sure you want to Order Cancel?")
                        .setPositiveButton("Yes", (dialog, whichButton) -> {
                            Log.d("sdj", "" + whichButton);
                            dialog.dismiss();
                            history.setStatus("Cancelled");
                            lists.set(i, history);
                            getCancel(history.getId());
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            Log.d("sdj", "" + which);
                            dialog.dismiss();
                        })
                        .create();
                myDelete.show();

            });


        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_order)
            TextView txtOrder;
            @BindView(R.id.txt_total)
            TextView txtTotal;
            @BindView(R.id.txt_orderstatus)
            TextView txtOrderstatus;
            @BindView(R.id.txt_orderdate)
            TextView txtOrderdate;
            @BindView(R.id.txt_info)
            TextView txtInfo;
            @BindView(R.id.txt_cancel)
            TextView txtCancel;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }

    public void getCancel(String oid) {
        custPrograssbar.prograssCreate(OrderActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);
        } catch (Exception e) {
            e.printStackTrace();

        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getOrderCancel(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2", this);

    }

    private void getOrder(int pageno) {
        binding.progressbar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("page_no", pageno);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getOrder(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", this);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        deliveryOrderList.clear();
        resentOrderList.clear();
        pageno =1;
        getOrder(pageno);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(comeFrom.equals("order"))
        {
            finish();
        }else if(comeFrom.equals("complete"))
        {
            startActivity(new Intent(OrderActivity.this, HomeActivity.class));
            finish();
        }
    }
}