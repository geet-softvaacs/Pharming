package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.CartModel;
import com.onetick.pharmafest.model.CartProduct;
import com.onetick.pharmafest.model.GetPincode;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Singleton;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LabCartActivity extends AppCompatActivity implements GetResult.MyListener {
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
    List<CartProduct> cartProduct = new ArrayList<>();


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
    private final String type = "";
    private GetPincode getPincode;
    ArrayList<String> productName;
    ArrayList<GetPincode> pincodeProductList = new ArrayList<>();
    ArrayList<String> names = new ArrayList<String>();
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_cart);
        txtItemTotal = findViewById(R.id.txt_itemtotal);

        ButterKnife.bind(this);
        sessionManager = new SessionManager(LabCartActivity.this);
        sessionManager.setIntData("coupon", 0);
        custPrograssbar = new CustPrograssbar();
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        user = sessionManager.getUserDetails("");
        viewCart();

        imgCoupnCode.setOnClickListener(view -> {
            if (sessionManager.getIntData("coupon") != 0) {
                imgCoupnCode.setImageResource(R.drawable.ic_cancel_coupon);
                sessionManager.setIntData("coupon", 0);
                updateItem();
            } else {
                int temp = (int) Math.round(total);
                startActivity(new Intent(LabCartActivity.this, CouponActivity.class).putExtra("amount", temp));
            }
        });

        btnProceed.setOnClickListener(view -> {
            showAddItemDialog(LabCartActivity.this);
        });

        imgBack.setOnClickListener(view -> {
            finish();
        });

    }

    private void viewCart() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "");
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
            if(cartModel.getResponseCode().equals("200") && cartModel.getResult().equals("true"))
            {
                lvlNotFound.setVisibility(View.GONE);
                lvlMain.setVisibility(View.VISIBLE);
                for(int i =0; i<cartModel.getCartProduct().size(); i++)
                {
                    String type = cartModel.getCartProduct().get(i).getType();
                    if(type.equals("Lab"))
                    {
                        cartProduct.add(cartModel.getCartProduct().get(i));
                    }
                }
                Singleton.getConstant().setCartData(cartProduct);
                HomeActivity.txtCountCart.setText(""+cartProduct.size());


                CartAdapter cartAdapter = new CartAdapter(cartProduct, this);
                myRecyclerView.setAdapter(cartAdapter);
                custPrograssbar.closePrograssBar();
                createJsonArray(cartProduct);
            }else if(cartModel.getResponseCode().equals("401") &&cartModel.getResponseMsg().equals("cart item Not Found!!"))
            {
                lvlNotFound.setVisibility(View.VISIBLE);
                lvlMain.setVisibility(View.GONE);
                custPrograssbar.closePrograssBar();
            }
        }
    }



    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        final int[] count = {0};
        double[] totalAmount = {0};
        private List<CartProduct> mData;
        private LayoutInflater mInflater;
        Context mContext;
        SessionManager sessionManager;
        float ress = 0;
        float subtotal = 0;
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
            return new CartAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
            String discount = mData.get(position).getDiscount();
            String qty = mData.get(position).getQty();
            int discountint =0;
            if(discount!=null)
            {
                discountint= Integer.parseInt(discount);
            }
            if(discountint!=0)
            {
                double res = (Double.parseDouble(mData.get(position).getCost()) * Double.parseDouble(mData.get(position).getDiscount()) / 100);
                res = Double.parseDouble(mData.get(position).getCost()) - res;
                holder.txtPrice.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(res));
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


            if (discountint != 0) {
                ress = (Float.parseFloat(mData.get(position).getCost()) * discountint) / 100;
                ress = Float.parseFloat(mData.get(position).getCost()) - ress;
            } else {
                ress = Float.parseFloat(mData.get(position).getCost());
            }

            float temp = Integer.parseInt(mData.get(position).getQty()) * ress;
            totalAmount[0] = totalAmount[0] + temp;

            subtotal = subtotal +temp;
            sessionManager.setFloatData("subtotal", subtotal);
            total = subtotal - sessionManager.getIntData("coupon");
            totalItem = totalItem + Double.parseDouble(mData.get(position).getCost());
            txtItemTotal.setText(sessionManager.getStringData("currency")+ new DecimalFormat("##.##").format(subtotal));
            txtToPay.setText(sessionManager.getStringData("currency") + new DecimalFormat("##.##").format(total));
            txtDiscount.setText(sessionManager.getStringData("currency") + sessionManager.getIntData("coupon"));

            holder.imgDelete.setOnClickListener(view -> {
                sessionManager.setIntData("coupon", 0);

                AlertDialog myDelete = new AlertDialog.Builder(LabCartActivity.this)
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
                    Toast.makeText(LabCartActivity.this, "" + mData.get(position).getTitle() + "  is Remove", Toast.LENGTH_LONG).show();
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
            @BindView(R.id.txt_ptype)
            TextView txtPtype;
            @BindView(R.id.lvl_offer)
            LinearLayout lvlOffer;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public void updateCart(String operation, String pro_id, String qty, String type)
    {
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

    public void createJsonArray(List<CartProduct>cartProducts)
    {
        for(int i =0; i<cartProducts.size(); i++)
        {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("title", cartProducts.get(i).getTitle());
                jsonObject.put("med_id", cartProducts.get(i).getProductId());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//                jsonObject.put("type", "medicine");
        }
    }

    private void ProductAvailability(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Product Not Available")
                .setMessage(Html.fromHtml(" <font color='"
                        + getResources().getColor(R.color.app_orange) + "'>" + productName
                        + "</font>" + "<font color='#FF7F27'>is not available. Currently we do not service on this pincode.Please delete items from your cart, change the pincode and select items again.</font>"))
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

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
//                                taskEditText.setError(null);
                            }
                            if (!isError)
                                dialog.dismiss();
                            // Otherwise the dialog will stay open.
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void sendPincode(JSONArray jsonArray2) {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pincode", task.toString());
            jsonObject.put("ProductData", jsonArray2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().checkAvailability(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3", this);
    }


}