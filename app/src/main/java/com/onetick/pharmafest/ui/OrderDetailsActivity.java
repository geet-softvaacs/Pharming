package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityOrderDetailsBinding;
import com.onetick.pharmafest.model.OrderP;
import com.onetick.pharmafest.model.OrderProductDatum;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OrderDetailsActivity extends AppCompatActivity implements GetResult.MyListener {
    ActivityOrderDetailsBinding binding;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActionTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_summary)
    TextView txtSummary;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.txt_orderid)
    TextView txtOrderId;
    @BindView(R.id.txt_orderstatus)
    TextView txtOrderStatus;
    @BindView(R.id.txt_orderdate)
    TextView txtOrderDate;
    @BindView(R.id.txt_orderddate)
    TextView txtOrderDDate;
    @BindView(R.id.txt_paymentmethod)
    TextView txtPaymentMethod;
    @BindView(R.id.txt_qut)
    TextView txtQut;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_deliverycharge)
    TextView txtDeliveryCharge;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.scv_summry)
    ScrollView schSummary;
    @BindView(R.id.scv_item)
    ScrollView scvItem;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    StaggeredGridLayoutManager gridLayoutManager;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    String oid;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_aditionalinfo)
    TextView txtAdditionInfo;
    @BindView(R.id.lvl_aditional)
    LinearLayout lvlAditional;
    @BindView(R.id.txt_discount)
    TextView txtdiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        ButterKnife.bind(this);
        oid = getIntent().getStringExtra("oid");
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(OrderDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        getOrderHistiry();
    }

    private void getOrderHistiry() {
        custPrograssbar.prograssCreate(OrderDetailsActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);
        } catch (Exception e) {
            e.printStackTrace();

        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getOrderHistory(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", this);

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                OrderP orderP = gson.fromJson(result.toString(), OrderP.class);
                if (orderP.getResult().equalsIgnoreCase("true")) {
                    txtOrderId.setText("" + oid);
                    txtOrderStatus.setText("" + orderP.getOrderProductList().get(0).getOrderStatus());
                    txtOrderDate.setText("" + orderP.getOrderProductList().get(0).getOrderDate());
                    txtPaymentMethod.setText("" + orderP.getOrderProductList().get(0).getPMethodName());
                    txtQut.setText(""+orderP.getOrderProductList().get(0).getOrderProductData().size());

                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + orderP.getOrderProductList().get(0).getOrderSubTotal());
                    txtDeliveryCharge.setText(sessionManager.getStringData(SessionManager.currency)  + orderP.getOrderProductList().get(0).getDeliveryCharge());
                    txtTotal.setText(sessionManager.getStringData(SessionManager.currency)  + orderP.getOrderProductList().get(0).getOrderTotal());
                    txtdiscount.setText(sessionManager.getStringData(SessionManager.currency) + orderP.getOrderProductList().get(0).getCouponAmount());

                    txtAddress.setText("" + orderP.getOrderProductList().get(0).getCustomerAddress());
                    if (orderP.getOrderProductList().get(0).getAdditionalNote().equalsIgnoreCase("")) {
                        lvlAditional.setVisibility(View.GONE);
                    }else {
                        lvlAditional.setVisibility(View.VISIBLE);
                    }
                    txtAdditionInfo.setText("" + orderP.getOrderProductList().get(0).getAdditionalNote());

                    ItemAdp itemAdp = new ItemAdp(OrderDetailsActivity.this, orderP.getOrderProductList().get(0).getOrderProductData());
                    myRecyclerView.setAdapter(itemAdp);


                    double DeliveryDouble = Double.parseDouble(orderP.getOrderProductList().get(0).getDeliveryCharge());
                    double CouponAmtDouble= Double.parseDouble(orderP.getOrderProductList().get(0).getCouponAmount());
                    if((DeliveryDouble==0.00||DeliveryDouble==0.0))
                    {
                        binding.DeliveryChargelayout.setVisibility(View.GONE);
                    }
                    if(CouponAmtDouble==0.00||CouponAmtDouble==0.0)
                    {
                        binding.CouponDiscountlayout.setVisibility(View.GONE);
                    }

                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {

        private List<OrderProductDatum> mData;
        private LayoutInflater mInflater;
        Context mContext;


        public ItemAdp(Context context, List<OrderProductDatum> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.mContext = context;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_myitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            OrderProductDatum productDatum = mData.get(i);
            if(productDatum.getProductVariation().equals("Lab"))
            {

                if(mData.get(i).getSlot()!=null)
                {
                    holder.slotDate.setText(mData.get(i).getSlot().getDate());
                    holder.slotDay.setText(mData.get(i).getSlot().getSlot());
                    holder.slotDetailLayout.setVisibility(View.VISIBLE);
                }

            }else{
                holder.slotDetailLayout.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(ApiClient.baseUrl + "/" + productDatum.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgIcon);
            if (productDatum.getProductDiscount().equalsIgnoreCase("0")) {
                holder.txtcount.setText("Qty " + productDatum.getProductQuantity() + "");
                holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + productDatum.getProductTotal());
                holder.txtPtype.setText("" + productDatum.getProductVariation());
                holder.txtTitle.setText("" + productDatum.getProductName());
                holder.txt_proamount.setText(sessionManager.getStringData(SessionManager.currency)+productDatum.getProductPrice());
            } else {
                double res = (Double.parseDouble(productDatum.getProductPrice()) * Double.parseDouble(productDatum.getProductDiscount())) / 100;
                res = Double.parseDouble(productDatum.getProductPrice()) - res;
                holder.txtOffer.setText(productDatum.getProductDiscount() + "% OFF");
                holder.txtcount.setText("Qty " + productDatum.getProductQuantity() + "  ");
                holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + productDatum.getProductTotal());
                holder.txtTitle.setText("" + productDatum.getProductName());
                holder.txtPtype.setText("" + productDatum.getProductVariation());
                holder.txt_proamount.setText(sessionManager.getStringData(SessionManager.currency)+productDatum.getProductPrice());

            }


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_icon)
            ImageView imgIcon;
            @BindView(R.id.txt_title)
            TextView txtTitle;
            @BindView(R.id.txt_ptype)
            TextView txtPtype;
            @BindView(R.id.txt_dscount)
            TextView txtDscount;
            @BindView(R.id.txtcount)
            TextView txtcount;
            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.txt_offer)
            TextView txtOffer;
            @BindView(R.id.lvl_offer)
            LinearLayout lvlOffer;
            @BindView(R.id.txt_proamount)
                    TextView txt_proamount;
            @BindView(R.id.SlotDetailLayout)
            LinearLayout slotDetailLayout;
            @BindView(R.id.txt_slotDate)
            TextView slotDate;
            @BindView(R.id.txt_slotDay)
            TextView slotDay;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }

    @OnClick({R.id.img_back, R.id.txt_summary, R.id.txt_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_summary:
                txtSummary.setTextColor(getResources().getColor(R.color.white));
                txtItem.setTextColor(getResources().getColor(R.color.black));
                txtSummary.setBackground(getResources().getDrawable(R.drawable.orderbox));
                txtItem.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                schSummary.setVisibility(View.VISIBLE);
                scvItem.setVisibility(View.GONE);
                break;
            case R.id.txt_item:
                txtSummary.setTextColor(getResources().getColor(R.color.black));
                txtItem.setTextColor(getResources().getColor(R.color.white));
                txtSummary.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                txtItem.setBackground(getResources().getDrawable(R.drawable.orderbox));
                schSummary.setVisibility(View.GONE);
                scvItem.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}