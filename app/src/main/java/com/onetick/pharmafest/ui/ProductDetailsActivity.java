package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.adapter.BannerAdapter;
import com.onetick.pharmafest.databinding.ActivityProductDetailsBinding;
import com.onetick.pharmafest.model.CartModel;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.ProductPrice;
import com.onetick.pharmafest.model.SingleProduct;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailsActivity extends AppCompatActivity implements GetResult.MyListener {
    ActivityProductDetailsBinding binding;
    Medicine medicine;
    ArrayList<String> productImage = new ArrayList<>();
    private ArrayList<ProductPrice> productInfo = new ArrayList<>();
    SessionManager sessionManager;
    String comeFrom = "";
    ProductPrice productPrice;
    int position;
    User user;
    String pro_id;
    boolean isProductAdded = false;
    String qty;
    CustPrograssbar custPrograssbar;
    public static TextView txtCountCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_details);
        sessionManager = new SessionManager(ProductDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        txtCountCart = binding.txtCountcard;

        medicine = (Medicine) getIntent().getParcelableExtra("MyClass");
        productInfo = getIntent().getParcelableArrayListExtra("PriceList");
        productImage = getIntent().getStringArrayListExtra("ImageList");
        pro_id = getIntent().getStringExtra("pro_id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtDesc.setText(Html.fromHtml(medicine.getShortDesc(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            binding.txtDesc.setText(Html.fromHtml(medicine.getShortDesc()));
        }
        binding.txtTitle.setText("" + medicine.getProductName());
        binding.txtBrandname.setText("" + medicine.getBrandName());
        if(medicine.getmSalt()!=null)
        {
            binding.txtMsalt.setHtml(""+medicine.getmSalt());
            binding.mSaltLayout.setVisibility(View.VISIBLE);
        }else if(medicine.getmSalt()==null){
            binding.mSaltLayout.setVisibility(View.GONE);
        }
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(ProductDetailsActivity.this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.myRecyclerView.setLayoutManager(mLayoutManager1);
        binding.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        BannerAdapter bannerAdapter = new BannerAdapter(ProductDetailsActivity.this, productImage);
        binding.myRecyclerView.setAdapter(bannerAdapter);
        binding.txtCountcard.setText(""+sessionManager.getIntData("cartCount"));
        if(getIntent().getStringExtra("comeFrom")!=null)
        {
            comeFrom = getIntent().getStringExtra("comeFrom");
        }

        if(comeFrom.equals("brand"))
        {
            position = getIntent().getIntExtra("position", 0);
            productPrice = productInfo.get(0);
        }else{
            productPrice = productInfo.get(0);
        }

        if (productPrice.getProductDiscount().equals("0.00")) {
            binding.lvlOffer.setVisibility(View.GONE);
            binding.txtPrice.setText(sessionManager.getStringData("currency") + productPrice.getProductPrice());
            binding.txtItemOffer.setVisibility(View.GONE);
        } else {
            binding.lvlOffer.setVisibility(View.VISIBLE);
            binding.txtItemOffer.setVisibility(View.VISIBLE);
            DecimalFormat format = new DecimalFormat("0.#");
            binding.txtOffer.setText(format.format(Double.parseDouble(productPrice.getProductDiscount())) + "% OFF");
            double res = (Double.parseDouble(productPrice.getProductPrice()) / 100.0f) * Double.parseDouble(productPrice.getProductDiscount());
            res = Double.parseDouble(productPrice.getProductPrice()) - res;
            binding.txtPrice.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            binding.txtItemOffer.setText(sessionManager.getStringData("currency") + productPrice.getProductPrice());
            binding.txtItemOffer.setPaintFlags(binding.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        List<String> Arealist = new ArrayList<>();
        if (productInfo.size() == 1) {
            binding.txtPriceone.setText("" + productInfo.get(0).getProductPrice());
            binding.lvlSpineer.setVisibility(View.GONE);

        } else {
            for (int i = 0; i < productInfo.size(); i++) {
                Arealist.add(productInfo.get(i).getProductPrice());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Arealist);
            binding.spinner.setAdapter(dataAdapter);
            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positontype = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });


        binding.btnAddtocart.setOnClickListener(view -> {
            addtoCart("+", "", "4");
        });


        if(productInfo.size()==1)
        {
            binding.txtPriceone.setText("" + productInfo.get(0).getProductPrice());
            binding.lvlSpineer.setVisibility(View.GONE);
            setJoinPlayrList(binding.lvlAddcart, productInfo.get(0));
        }else{
            for (int i = 0; i < productInfo.size(); i++) {
                Arealist.add(productInfo.get(i).getProductPrice());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Arealist);
            binding.spinner.setAdapter(dataAdapter);
            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positontype = position;
                    setJoinPlayrList(binding.lvlAddcart, productInfo.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        binding.rltCart.setOnClickListener(view -> {
            startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class).putExtra("type", "Medicine"));
        });
        getProductSingleDetail();
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                CartModel cartModel = gson.fromJson(result.toString(), CartModel.class);
                if (cartModel.getResult().equalsIgnoreCase("true") && cartModel.getResponseCode().equals("200")) {
                    Toast.makeText(this, cartModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    sessionManager.setIntData("cartCount", cartModel.getCartProduct().size());
                    binding.txtCountcard.setText(""+ cartModel.getCartProduct().size());
                    HomeActivity.txtCountCart.setText(""+cartModel.getCartProduct().size());
                }else if(cartModel.getResponseCode().equals("401") && cartModel.getResponseMsg().equals("Cart is Empity now..!"))
                {
                    binding.txtCountcard.setText(""+ cartModel.getCartProduct().size());
                    HomeActivity.txtCountCart.setText(""+cartModel.getCartProduct().size());
                    sessionManager.setIntData("cartCount", cartModel.getCartProduct().size());
                }
                binding.btnAddtocart.setEnabled(true);
            }else if(callNo.equalsIgnoreCase("3"))
            {
                Gson gson = new Gson();
                SingleProduct singleProduct = gson.fromJson(result.toString(), SingleProduct.class);
                if(singleProduct.getResponseCode().equals("200")){
                    isProductAdded = true;
                    qty = singleProduct.getResultData().getProductQty();
                    setJoinPlayrList(binding.lvlAddcart, productInfo.get(0));
                }else if(singleProduct.getResponseCode().equals("201"))
                {
                    isProductAdded = false;
                }
                binding.btnAddtocart.setEnabled(true);
            }else if(callNo.equalsIgnoreCase("4"))
            {
                Gson gson = new Gson();
                CartModel cartModel = gson.fromJson(result.toString(), CartModel.class);
                if (cartModel.getResult().equalsIgnoreCase("true") && cartModel.getResponseCode().equals("200")) {
                    Toast.makeText(this, cartModel.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    sessionManager.setIntData("cartCount", cartModel.getCartProduct().size());
                    binding.txtCountcard.setText(""+ cartModel.getCartProduct().size());
                    HomeActivity.txtCountCart.setText(""+cartModel.getCartProduct().size());
                    startActivity(new Intent(this, CartActivity.class).putExtra("type", "Medicine"));
                }
                binding.btnAddtocart.setEnabled(true);
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }

    }

    public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
        private Context context;
        private List<String> mBanner;

        public BannerAdapter(Context context, List<String> mBanner) {
            this.context = context;
            this.mBanner = mBanner;
        }

        @NonNull
        @Override
        public BannerAdapter.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
            return new BannerAdapter.BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            Glide.with(context).load(ApiClient.baseUrl + "/" + mBanner.get(position)).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgBanner);
        }

        @Override
        public int getItemCount() {
            return mBanner.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_banner)
            ImageView imgBanner;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    int positontype = 0;

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setJoinPlayrList(LinearLayout lnrView, ProductPrice productPrice) {
        lnrView.removeAllViews();
        final int[] count = {0};
        LayoutInflater inflater = LayoutInflater.from(ProductDetailsActivity.this);
        View view = inflater.inflate(R.layout.custome_prize, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout txtOutstock = view.findViewById(R.id.txt_outstock);
        LinearLayout lvl_addremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvl_addcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout img_mins = view.findViewById(R.id.img_mins);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);

        if (productPrice.getProductDiscount().equals("0.00")) {
            binding.lvlOffer.setVisibility(View.GONE);
            binding.txtPrice.setText(sessionManager.getStringData("currency") + productPrice.getProductPrice());
            binding.txtItemOffer.setVisibility(View.GONE);
        } else {
            binding.lvlOffer.setVisibility(View.VISIBLE);
            binding.txtItemOffer.setVisibility(View.VISIBLE);
            DecimalFormat format = new DecimalFormat("0.#");
            binding.txtOffer.setText(productPrice.getProductDiscount() + "% OFF");
            double res = (Double.parseDouble(productPrice.getProductPrice()) / 100.0f) * Double.parseDouble(productPrice.getProductDiscount());
            res = Double.parseDouble(productPrice.getProductPrice()) - res;
            binding.txtPrice.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
            binding.txtItemOffer.setText(sessionManager.getStringData("currency") + productPrice.getProductPrice());
            binding.txtItemOffer.setPaintFlags(binding.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        if(isProductAdded)
        {
            lvl_addcart.setVisibility(View.GONE);
            lvl_addremove.setVisibility(View.VISIBLE);
            txtcount.setText(qty);

        }else if(!isProductAdded)
        {
            lvl_addcart.setVisibility(View.VISIBLE);
            lvl_addremove.setVisibility(View.GONE);
        }

        img_mins.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                txtcount.setText("" + count[0]);
                lvl_addremove.setVisibility(View.GONE);
                lvl_addcart.setVisibility(View.VISIBLE);
                addtoCart("", "0", "1");
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                addtoCart("-", "", "1");
            }
        });
        img_plus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            txtcount.setText("" + count[0]);
            addtoCart("+", "", "1");
        });
        lvl_addcart.setOnClickListener(v -> {
            lvl_addcart.setVisibility(View.GONE);
            lvl_addremove.setVisibility(View.VISIBLE);
            String text = txtcount.getText().toString();
            count[0] = Integer.parseInt(text);
            count[0] = count[0] + 1;
            txtcount.setText("" + count[0]);
            addtoCart("+", "", "1");

        });
        lnrView.addView(view);
    }

    public void addtoCart(String operation, String qty, String callno)
    {
        binding.btnAddtocart.setEnabled(false);
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("type", "Medicine");
            jsonObject.put("product_id", medicine.getId());
            jsonObject.put("prodcuct_qty", qty);
            jsonObject.put("qty", operation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().addtoCart(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, callno, this);
    }

    public void getProductSingleDetail()
    {
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("type", "Medicine");
            jsonObject.put("product_id", medicine.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getSingleProductDetail(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3", this);
    }
}