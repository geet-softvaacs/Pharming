package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.CartModel;
import com.onetick.pharmafest.model.CartProduct;
import com.onetick.pharmafest.model.CheckSlot;
import com.onetick.pharmafest.model.GetPincode;
import com.onetick.pharmafest.model.Home;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Singleton;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.onetick.pharmafest.ui.AddressActivity.changeAddress;

public class CartActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActionTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.img_coopncode)
    ImageView imgCoupnCode;
    @BindView(R.id.ed_customnot)
    EditText edCustom;
    //    @BindView(R.id.txt_address)
//    TextView txtAddress;
//    @BindView(R.id.txt_changeadress)
//    TextView tatChangeless;
    @BindView(R.id.btn_proceed)
    TextView btnProceed;
    StaggeredGridLayoutManager gridLayoutManager;
    ArrayList<CartProduct> cartProduct = new ArrayList<>();
    String pincode, pincodeEt;


    public static  TextView txtItemTotal;
    @BindView(R.id.txt_dcharge)
    TextView txtDCharge;
    @BindView(R.id.txt_Discount)
    TextView txtDiscount;
    @BindView(R.id.txt_topay)
    TextView txtToPay;
    //    @BindView(R.id.txt_atype)
//    TextView txtAType;
    String task;
    SessionManager sessionManager;
    User user;
    double total = 0;
    double subtotal;
    @BindView(R.id.lvl_main)
    RelativeLayout lvlMain;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotFound;
    CustPrograssbar custPrograssbar;
     String type = "";
    private GetPincode getPincode;
    ArrayList<String> productName;
    ArrayList<GetPincode> pincodeProductList = new ArrayList<>();
    ArrayList<String> names = new ArrayList<String>();
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText taskEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        txtItemTotal = findViewById(R.id.txt_itemtotal);

        ButterKnife.bind(this);
        sessionManager = new SessionManager(CartActivity.this);
        sessionManager.setIntData("coupon", 0);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        custPrograssbar = new CustPrograssbar();
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        user = sessionManager.getUserDetails("");
        type = getIntent().getStringExtra("type");
        viewCart();

        imgCoupnCode.setOnClickListener(view -> {
            if (sessionManager.getIntData("coupon") != 0) {
                imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
                sessionManager.setIntData("coupon", 0);
                updateItem();
            } else {
                int temp = (int) Math.round(total);
                startActivity(new Intent(CartActivity.this, CouponActivity.class).putExtra("amount", temp).putExtra("type", type));
            }
        });

        btnProceed.setOnClickListener(view -> {
            if(type.equals("Lab"))
            {
                if(!sessionManager.getStringData("labPincode").equals(""))
                {
                    sendPincode(jsonArray);
                }else if(sessionManager.getStringData("labPincode").equals(""))
                {
                    showAddItemDialog(CartActivity.this);
                }
            }else{
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                intent.putExtra("subtotal", String.valueOf(subtotal));
                intent.putExtra("total", String.valueOf(total));
                intent.putExtra("pincode", String.valueOf(task));
                intent.putParcelableArrayListExtra("cartProduct", cartProduct);
                Log.e("pincode",String.valueOf(task));
                intent.putExtra("note", edCustom.getText().toString());
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(view -> {
            finish();
        });


        if(isLocationEnabled(CartActivity.this)){
            if (ActivityCompat.checkSelfPermission(CartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
               getLocation();
            } else {
                ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

    }

    private void sendPincode(JSONArray jsonArray2) {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("pincode", sessionManager.getStringData("labPincode"));
            jsonObject.put("ProductData", jsonArray2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(type.equals("Lab"))
        {
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().getTestAvailable(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4", this);
        }else if(type.equals("Medicine"))
        {
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().checkAvailability(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3", this);
        }
    }


    private void viewCart() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("uid", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().viewCart(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1", this);
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.closePrograssBar();

        if(callNo.equals("1"))
        {
            Gson gson = new Gson();
            CartModel cartModel = gson.fromJson(result.toString(), CartModel.class);
            cartProduct.clear();
            if(cartModel.getResponseCode().equals("200") && cartModel.getResult().equals("true"))
            {
                lvlNotFound.setVisibility(View.GONE);
                lvlMain.setVisibility(View.VISIBLE);
                cartProduct= (ArrayList<CartProduct>) cartModel.getCartProduct();
                String cartTtl = cartModel.getTotal().replace(",", "");
                subtotal = Double.parseDouble(cartTtl);
                if(type.equals("Lab"))
                {
                    Singleton.getConstant().setLabCartData(cartProduct);
                    sessionManager.setIntData("labCartCount", cartModel.getCardItemCount());
                    HomeActivity.txtCountCart.setText(""+cartModel.getCardItemCount());
                }else if(type.equals("Medicine"))
                {
                    Singleton.getConstant().setCartData(cartProduct);
                    sessionManager.setIntData("cartCount", cartModel.getCardItemCount());
//                    HomeActivity.txtCountCart.setText(""+cartModel.getCardItemCount());
                }
//                HomeActivity.txtCountCart.setText(""+cartProduct.size());

                CartAdapter cartAdapter = new CartAdapter(cartProduct, this);
                myRecyclerView.setAdapter(cartAdapter);
                custPrograssbar.closePrograssBar();
                createJsonArray(cartProduct);
            }else if(cartModel.getResponseCode().equals("401") &&cartModel.getResponseMsg().equals("cart item Not Found!!"))
            {
                lvlNotFound.setVisibility(View.VISIBLE);
                lvlMain.setVisibility(View.GONE);
                custPrograssbar.closePrograssBar();
                HomeActivity.txtCountCart.setText(""+cartModel.getCardItemCount());
            }
        }else if(callNo.equals("2"))
        {
            custPrograssbar.prograssCreate(this);
            viewCart();
        }else if(callNo.equals("3"))
        {
            Gson gson = new Gson();
            GetPincode getPincode = gson.fromJson(result.toString(), GetPincode.class);
            if (getPincode.getUnAvailable().size() != 0) {

                names.clear();
                for (int i = 0; i < getPincode.getUnAvailable().size(); i++) {
                    if (getPincode.getUnAvailable().get(i).getStatus().equalsIgnoreCase("0")) {
                        String data = getPincode.getUnAvailable().get(i).getTitle();
                        names.add(data);
                        productName = names;
                    }
                }
                if (!names.isEmpty()) {
                    sessionManager.setStringData("isAdmin", "1");

                }
            } else {
                sessionManager.setStringData("isAdmin", "0");

                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                intent.putExtra("subtotal", String.valueOf(subtotal));
                intent.putExtra("total", String.valueOf(total));
                intent.putExtra("pincode", task);
                intent.putExtra("note", edCustom.getText().toString());
                startActivity(intent);
            }
        }else if(callNo.equals("4"))
        {
            Gson gson = new Gson();
            CheckSlot getPincode = gson.fromJson(result.toString(), CheckSlot.class);
            if(getPincode.getStatus0().size()!=0)
            {
                names.clear();
                for (int i = 0; i < getPincode.getStatus0().size(); i++) {

                        if (getPincode.getStatus0().get(0).getSlotStatus()==0) {
                        String  data = getPincode.getStatus0().get(i).getTitle();
                        names.add(data);
                        productName = names;
                    }
                }
                if (!names.isEmpty()) {
                    ProductAvailability(CartActivity.this);
                }else{
                    Intent intent = new Intent(CartActivity.this, ActivitySelectPatient.class);
                    intent.putExtra("subtotal", String.valueOf(subtotal));
                    intent.putExtra("total", String.valueOf(total));
                    intent.putExtra("pincode", sessionManager.getStringData("labPincode"));
                    Log.e("pincode",String.valueOf(task));
                    intent.putExtra("note", edCustom.getText().toString());
                    startActivity(intent);
                }
            }else{
                Intent intent = new Intent(CartActivity.this, ActivitySelectPatient.class);
                intent.putExtra("subtotal", String.valueOf(subtotal));
                intent.putExtra("total", String.valueOf(total));
                intent.putExtra("pincode", sessionManager.getStringData("labPincode"));
                Log.e("pincode",String.valueOf(task));
                intent.putExtra("note", edCustom.getText().toString());
                startActivity(intent);
            }
        }
    }

    private void showAddItemDialog(Context c) {
        taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        taskEditText.setText(pincode);
        pincodeEt = taskEditText.getText().toString();
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setMessage("Please Enter Your Pincode")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            Boolean isError = false;
                            task = String.valueOf(taskEditText.getText());
                            if (task.isEmpty()) {
                                isError = true;
                                Toast.makeText(getApplicationContext(), "Pincode cannot be empty", Toast.LENGTH_SHORT).show();
                                taskEditText.setError("Pincode cannot be empty");
                            }
                            if (!task.isEmpty()) {
                                sendPincode(jsonArray);
                                isError = false;
                            }
                            if (!isError)
                                dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        final int[] count = {0};
        double[] totalAmount = {0};
        private List<CartProduct> mData;
        private LayoutInflater mInflater;
        Context mContext;
        SessionManager sessionManager;
        double ress = 0;
        double totalItem = 0;




        public CartAdapter(List<CartProduct> mData, Context mContext) {
            this.mInflater = LayoutInflater.from(mContext);

            this.mData = mData;
            this.mContext = mContext;
            sessionManager = new SessionManager(mContext);

        }

        @NonNull
        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_mycard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

            if(type.equals("Lab"))
            {

                if(cartProduct.get(position).getSlot()!=null)
                {
                    holder.slotDate.setText(cartProduct.get(position).getSlot().getDate());
                    holder.slotDay.setText(cartProduct.get(position).getSlot().getSlot());
                    holder.slotDetailLayout.setVisibility(View.VISIBLE);
                }

            }else{
                holder.slotDetailLayout.setVisibility(View.GONE);
            }
            String discount = mData.get(position).getDiscount();
            String qty = mData.get(position).getQty();
            double discountint =0;
            if(discount!=null)
            {
                discountint= Double.parseDouble(discount);
            }
            if(discountint!=0)
            {
                double res = (Double.parseDouble(mData.get(position).getCost()) * Double.parseDouble(mData.get(position).getDiscount()) / 100);
                res = Double.parseDouble(mData.get(position).getCost()) - res;
                holder.txtPrice.setText(sessionManager.getStringData("currency") + mData.get(position).getCost());
                holder.txtDscount.setPaintFlags(holder.txtDscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txtDscount.setText( sessionManager.getStringData("currency") + mData.get(position).getMrp());
                holder.lvlOffer.setVisibility(View.VISIBLE);
                holder.txtOffer.setText(discount + "% OFF ");
            }else {
                holder.txtPrice.setText(sessionManager.getStringData("currency") + mData.get(position).getCost());
                holder.lvlOffer.setVisibility(View.GONE);
                holder.txtDscount.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(ApiClient.baseUrl + "/" + mData.get(position).getImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgIcon);
            holder.txtTitle.setText("" + mData.get(position).getTitle());
            holder.txtcount.setText(mData.get(position).getQty());
            holder.txt_prototal.setText(mData.get(position).getProductAmt());


            if (discountint != 0) {
                ress = (Double.parseDouble(mData.get(position).getCost()) * discountint) / 100;
                ress = Float.parseFloat(mData.get(position).getCost()) - ress;
            } else {
                ress = Float.parseFloat(mData.get(position).getCost());
            }

            float temp = Integer.parseInt(mData.get(position).getQty()) * Float.parseFloat(mData.get(position).getCost());
            totalAmount[0] = totalAmount[0] + temp;


            sessionManager.setFloatData("subtotal",(float)subtotal);
            total = subtotal - sessionManager.getIntData("coupon");
            totalItem = totalItem + Double.parseDouble(mData.get(position).getCost());
            txtItemTotal.setText(sessionManager.getStringData("currency")+ new DecimalFormat("#,###.00").format(subtotal));
            txtToPay.setText(sessionManager.getStringData("currency") + new DecimalFormat("#,###.00").format(total));
            txtDiscount.setText(sessionManager.getStringData("currency") + sessionManager.getIntData("coupon"));

            holder.imgDelete.setOnClickListener(view -> {
                sessionManager.setIntData("coupon", 0);

                AlertDialog myDelete = new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Delete", (dialog, whichButton) -> {
                            Log.d("sdj", "" + whichButton);
                            dialog.dismiss();
                            updateCart("", mData.get(position).getProductId(), "0", mData.get(position).getType());
                        })
                        .setNegativeButton("cancel", (dialog, which) -> {
                            Log.d("sdj", "" + which);
                            dialog.dismiss();
                        })
                        .create();
                myDelete.show();
            });

            holder.imgMins.setOnClickListener(view -> {

                sessionManager.setIntData("coupon", 0);
                count[0] = Integer.parseInt(holder.txtcount.getText().toString());
                if (count[0] <= 0) {
                    holder.txtcount.setVisibility(View.INVISIBLE);
                    holder.imgMins.setVisibility(View.INVISIBLE);
                    holder.txtcount.setText("" + count[0]);
                    updateCart("", mData.get(position).getProductId(), "0", mData.get(position).getType());
                    Toast.makeText(CartActivity.this, "" + mData.get(position).getTitle() + "  is Remove", Toast.LENGTH_LONG).show();
                }else{
                    updateCart("-", mData.get(position).getProductId(), "1", mData.get(position).getType());
                }
            });

            holder.imgPlus.setOnClickListener(view -> {
                sessionManager.setIntData("coupon", 0);

//                holder.txtcount.setVisibility(View.VISIBLE);
//                holder.imgMins.setVisibility(View.VISIBLE);
//                count[0] = Integer.parseInt(holder.txtcount.getText().toString());
//                count[0] = count[0] + 1;
//                holder.txtcount.setText("" + count[0]);
                updateCart("+", mData.get(position).getProductId(), "1", mData.get(position).getType());
            });


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
            @BindView(R.id.txt_dscount)
            TextView txtDscount;
            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.img_delete)
            ImageView imgDelete;
            @BindView(R.id.img_mins)
            LinearLayout imgMins;
            @BindView(R.id.txtcount)
            TextView txtcount;
            @BindView(R.id.img_plus)
            LinearLayout imgPlus;
            @BindView(R.id.lvl_addremove)
            LinearLayout lvlAddremove;
            @BindView(R.id.txt_offer)
            TextView txtOffer;
            @BindView(R.id.lvl_offer)
            LinearLayout lvlOffer;
            @BindView(R.id.txt_prototal)
            TextView txt_prototal;
            @BindView(R.id.SlotDetailLayout)
            LinearLayout slotDetailLayout;
            @BindView(R.id.txt_slotDate)
            TextView slotDate;
            @BindView(R.id.txt_slotDay)
            TextView slotDay;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null) {
            if (changeAddress) {
                changeAddress = false;
            } else {
                updateItem();
            }
        }
    }


    public void updateItem()
    {
        subtotal = sessionManager.getFloatData("subtotal");
        total = subtotal + sessionManager.getFloatData("dcharge");
        txtDCharge.setText(sessionManager.getStringData("currency") + sessionManager.getFloatData("dcharge"));

        txtItemTotal.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(subtotal));
        if (sessionManager.getIntData("coupon") != 0) {
            imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
        } else {
            imgCoupnCode.setImageResource(R.drawable.ic_apply_coupon);

        }
        txtDiscount.setText(sessionManager.getStringData("currency") + sessionManager.getIntData("coupon"));
        total = total - sessionManager.getIntData("coupon");
        txtToPay.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(total));
    }

    public void updateCart(String operation, String pro_id, String qty, String type)
    {
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("type", type);
            jsonObject.put("product_id", pro_id);
            jsonObject.put("prodcuct_qty", qty);
            jsonObject.put("qty", operation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().addtoCart(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2", this);
    }

    private void ProductAvailability(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Product Not Available")
                .setMessage(Html.fromHtml(" <font color='"
                        + getResources().getColor(R.color.app_orange) + "'>" + productName
                        + "</font>" + "<font color='#FF7F27'> Selected Slot is not available. Please choose another slot and try again.</font>"))
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {

                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void createJsonArray(List<CartProduct>cartProducts)
    {
         jsonArray= new JSONArray();

        for(int i =0; i<cartProducts.size(); i++)
        {
            jsonObject = new JSONObject();
            if(type.equals("Lab"))
            {
                try {
                    jsonObject.put("title", cartProducts.get(i).getTitle());
                    jsonObject.put("product_id", cartProducts.get(i).getProductId());
                    jsonObject.put("slot_id", cartProducts.get(i).getSlot_id());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(type.equals("Medicine"))
            {
                try {
                    jsonObject.put("title", cartProducts.get(i).getTitle());
                    jsonObject.put("med_id", cartProducts.get(i).getProductId());
                    jsonObject.put("qty", cartProducts.size());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//                jsonObject.put("type", "medicine");
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try{
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            }catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(CartActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String city = addresses.get(0).getLocality();
                        pincode = addresses.get(0).getPostalCode();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}