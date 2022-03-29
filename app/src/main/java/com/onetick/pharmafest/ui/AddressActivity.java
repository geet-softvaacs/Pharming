package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityAddressBinding;
import com.onetick.pharmafest.locationpick.LocationGetActivity;
import com.onetick.pharmafest.locationpick.MapUtility;
import com.onetick.pharmafest.model.Address;
import com.onetick.pharmafest.model.AddressList;
import com.onetick.pharmafest.model.DeleteAddress;
import com.onetick.pharmafest.model.RestResponse;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Utility;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddressActivity extends AppCompatActivity implements GetResult.MyListener{
    ActivityAddressBinding binding;
    LinearLayout currentLocation;
    LinearLayout lvlMyAddress;
    SessionManager sessionManager;
    User user;
    ImageView imgBack;
    TextView txtActionTitle;
    AppBarLayout appBarLayout;
    CustPrograssbar custPrograssbar;
    String userid = "0";
    String country;
    String pincode;
    String city;
    String streetAddress, houseno, landmark;
    String state, type;
    double lat, lon;
    String addressname1;
    TextView noAddress;
    Toolbar toolbar;
    RecyclerView myRecyclerView;
    LinearLayoutManager layoutManager;
    public static boolean changeAddress = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayAdapter<String> adapter;
    String url;
    List<String>localityList = new ArrayList<>();
    Spinner localitySpinner;
    String locality;
    AdepterAddress adepterAddress;
    String landmark1= "", comeFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        custPrograssbar = new CustPrograssbar();
        myRecyclerView = binding.myRecyclerView;
        lvlMyAddress =binding.lvlMyaddress;
        noAddress = binding.tvNoAddress;
        currentLocation = binding.lvlClocation;
        sessionManager = new SessionManager(AddressActivity.this);
        user = sessionManager.getUserDetails("");
        layoutManager = new LinearLayoutManager(AddressActivity.this, LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(layoutManager);

        getAddress();
        binding.manualAddress.setOnClickListener(v ->
        {
            setManualAddress(AddressActivity.this);
        });

        if(getIntent().getStringExtra("type")!=null)
        {
            type = getIntent().getStringExtra("type");
        }

        currentLocation.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(AddressActivity.this)) {
                Toast.makeText(AddressActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
                Utility.enableLoc(AddressActivity.this);
            }
            if(isLocationEnabled(AddressActivity.this)){
                if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(AddressActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }else{
            }
        });

        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        comeFrom = getIntent().getStringExtra("comeFrom");

    }

    private void getAddress() {
        custPrograssbar.prograssCreate(AddressActivity.this);
        JSONObject jsonObject = new JSONObject();
        userid = sessionManager.getUserDetails("").getId();
        try {
            jsonObject.put("uid", sessionManager.getUserDetails("").getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().getAddress(bodyRequest);
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
                Address address = gson.fromJson(result.toString(), Address.class);
                if (address.getResult().equalsIgnoreCase("true")) {
                    lvlMyAddress.setVisibility(View.VISIBLE);
                    if (address.getAddressList().size() > 0) {
                        adepterAddress = new AdepterAddress(AddressActivity.this, address.getAddressList());
                        myRecyclerView.setAdapter(adepterAddress);
                        noAddress.setVisibility(View.GONE);
                    } else {
                        noAddress.setVisibility(View.VISIBLE);
                    }
                } else {
                    lvlMyAddress.setVisibility(View.GONE);
                    noAddress.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(AddressActivity.this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddressActivity.this, AddressActivity.class).putExtra("comeFrom", comeFrom));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(callNo.equalsIgnoreCase("3"))
        {
            custPrograssbar.closePrograssBar();
            Gson gson = new Gson();
            DeleteAddress response = gson.fromJson(result.toString(), DeleteAddress.class);
            Toast.makeText(AddressActivity.this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
            sessionManager.setStringData("pincoded","");
            sessionManager.setStringData("pincode", "");
            finish();
        }
    }


    private void setManualAddress(Context c) {
            LayoutInflater factory = LayoutInflater.from(this);

    //text_entry is an Layout XML file containing two text field to display in alert dialog

        final View textEntryView = factory.inflate(R.layout.layout_pincode, null);
        final EditText input1 = (EditText) textEntryView.findViewById(R.id.ed_country);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.ed_pincode);
        final EditText input3 = (EditText) textEntryView.findViewById(R.id.ed_city);
        final EditText input4 = (EditText) textEntryView.findViewById(R.id.ed_street_address);
        final EditText input5 = (EditText) textEntryView.findViewById(R.id.ed_state);
        final EditText input6 = (EditText) textEntryView.findViewById(R.id.ed_landmark);
        final Spinner typeSpinner = textEntryView.findViewById(R.id.type_spinner);
        localitySpinner = textEntryView.findViewById(R.id.locality_spinner);
        final EditText input7 = (EditText) textEntryView.findViewById(R.id.ed_houseno);
        final EditText addressname = (EditText)textEntryView.findViewById(R.id.ed_name);
        setSpinnerItems(typeSpinner);




        input2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pincode = input2.getText().toString();

                if(pincode.length()==6)
                {
                    url = "https://api.postalpincode.in/pincode/"+input2.getText().toString();
                    getLocality("manual");
                }
            }
        });



        input1.setText("India", TextView.BufferType.NORMAL);
        input2.setText("", TextView.BufferType.EDITABLE);
        input3.setText("", TextView.BufferType.EDITABLE);
        input4.setText("", TextView.BufferType.EDITABLE);
        addressname.setText("", TextView.BufferType.EDITABLE);

        final AlertDialog dialog = new AlertDialog.Builder(AddressActivity.this)
                .setTitle("Please fill the details:")
                .setPositiveButton("Save", null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .setView(textEntryView,50 ,30, 50 , 0)
                .create();

        dialog.setOnShowListener(dialog1 -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                country = String.valueOf(input1.getText());
                pincode = String.valueOf(input2.getText());
                city = String.valueOf(input3.getText());
                streetAddress = String.valueOf(input4.getText());
                houseno = String.valueOf(input7.getText());
                state = String.valueOf(input5.getText());
                landmark = String.valueOf(input6.getText());
                addressname1 = String.valueOf(addressname.getText().toString());
                if (localitySpinner.isSelected()) {
                    locality = localitySpinner.getSelectedItem().toString();
                }
                String type = typeSpinner.getSelectedItem().toString();
                if (addressname1.equals("")) {
                    Toast.makeText(AddressActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (houseno.equals("")) {
                    Toast.makeText(AddressActivity.this, "Please enter house no.", Toast.LENGTH_SHORT).show();
                } else if (streetAddress.equals("")) {
                    Toast.makeText(AddressActivity.this, "Please enter street address", Toast.LENGTH_SHORT).show();
                } else if (pincode.equals("")) {
                    Toast.makeText(AddressActivity.this, "Please enter pincode.", Toast.LENGTH_SHORT).show();
                } else if (localitySpinner.getSelectedItem().toString().equals("Select Postal Address")) {
                    Toast.makeText(c, "Please select postal address", Toast.LENGTH_SHORT).show();
                }else if (city.equals("")) {
                    Toast.makeText(c, "Please enter city", Toast.LENGTH_SHORT).show();
                }else if(state.equals("")) {
                    Toast.makeText(c, "Please enter state", Toast.LENGTH_SHORT).show();
                } else {
                    addAddress(type, 0, 0, "0");
                }
            });

        });
        dialog.show();
    }

    private void addAddress(String type, double lat, double lon, String aid) {
        JSONObject jsonObject = new JSONObject();
        custPrograssbar.prograssCreate(this);
        try {
            jsonObject.put("uid", sessionManager.getUserDetails("").getId());
            jsonObject.put("address", streetAddress);
            jsonObject.put("pincode", pincode);
            jsonObject.put("houseno", houseno);
            jsonObject.put("landmark", landmark);
            jsonObject.put("state", state);
            jsonObject.put("type", type);
            jsonObject.put("lat_map", lat);
            jsonObject.put("long_map", lon);
            jsonObject.put("aid", aid);
            jsonObject.put("city", city);
            jsonObject.put("postal_name", locality);
            jsonObject.put("name", addressname1);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().setAddress(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2", this);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class AdepterAddress extends RecyclerView.Adapter<AdepterAddress.BannerHolder> {
        private Context context;
        private List<AddressList> mBanner;

        public AdepterAddress(Context context, List<AddressList> mBanner) {
            this.context = context;
            this.mBanner = mBanner;
        }

        @NonNull
        @Override
        public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_addresss_item, parent, false);
            return new BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            holder.txtType.setText("" + mBanner.get(position).getType());
            if(!mBanner.get(position).getLandmark().equals(""))
            {
                landmark1 = mBanner.get(position).getLandmark()+"," ;
            }
            holder.txtHomeaddress.setText(mBanner.get(position).getHno() + ","+ mBanner.get(position).getAddress()+landmark1+mBanner.get(position).getPostalname()+","+mBanner.get(position).getCity()+","+mBanner.get(position).getState());
            if(mBanner.get(position).getName()!=null) {
                holder.txtAddressanme.setText(mBanner.get(position).getName());
            }
            Glide.with(context).load(ApiClient.baseUrl + "/" + mBanner.get(position).getAddressImage()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgBanner);
            holder.txtHomeaddress.setOnClickListener(v -> {
                if(comeFrom.equals("settings"))
                {
                    LayoutInflater factory = LayoutInflater.from(AddressActivity.this);

                    //text_entry is an Layout XML file containing two text field to display in alert dialog

                    final View textEntryView = factory.inflate(R.layout.layout_pincode, null);

                    final EditText input1 = (EditText) textEntryView.findViewById(R.id.ed_country);
                    final EditText nameEt = (EditText) textEntryView.findViewById(R.id.ed_name);
                    final EditText input2 = (EditText) textEntryView.findViewById(R.id.ed_pincode);
                    final EditText input3 = (EditText) textEntryView.findViewById(R.id.ed_city);
                    final EditText input4 = (EditText) textEntryView.findViewById(R.id.ed_street_address);
                    final EditText input5 = (EditText) textEntryView.findViewById(R.id.ed_state);
                    final EditText input6 = (EditText) textEntryView.findViewById(R.id.ed_landmark);
                    final Spinner typeSpinner = textEntryView.findViewById(R.id.type_spinner);
                    localitySpinner = textEntryView.findViewById(R.id.locality_spinner);
                    final EditText input7 = (EditText) textEntryView.findViewById(R.id.ed_houseno);
                    final EditText addressanme = (EditText)textEntryView.findViewById(R.id.ed_name);
                    locality = mBanner.get(position).getPostalname();
                    lat = mBanner.get(position).getLatMap();
                    lon = mBanner.get(position).getLongMap();
                    pincode = mBanner.get(position).getPincode();
                    url = "https://api.postalpincode.in/pincode/"+pincode;
                    getLocality("edit");


                    setSpinnerItems(typeSpinner);


                    input1.setText("India", TextView.BufferType.NORMAL);
                    nameEt.setText(mBanner.get(position).getName(), TextView.BufferType.EDITABLE);
                    input2.setText(mBanner.get(position).getPincode(), TextView.BufferType.EDITABLE);
                    input3.setText(mBanner.get(position).getCity(), TextView.BufferType.EDITABLE);
                    input7.setText(mBanner.get(position).getHno(), TextView.BufferType.EDITABLE);
                    input6.setText(mBanner.get(position).getLandmark(), TextView.BufferType.EDITABLE);
                    input4.setText(mBanner.get(position).getAddress(), TextView.BufferType.EDITABLE);
                    input5.setText(mBanner.get(position).getState(), TextView.BufferType.EDITABLE);

                    if(mBanner.get(position).getName()!=null){
                        addressanme.setText(mBanner.get(position).getName());
                    }
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddressActivity.this, R.array.address_type, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(adapter);
                    if (mBanner.get(position).getType() != null) {
                        int spinnerPosition = adapter.getPosition(mBanner.get(position).getType());
                        typeSpinner.setSelection(spinnerPosition);
                    }

                    input2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if(!b)
                            {
                                url = "https://api.postalpincode.in/pincode/"+input2.getText().toString();
                                getLocality("");
                            }
                        }
                    });


                    final AlertDialog.Builder alert = new AlertDialog.Builder(AddressActivity.this);
                    alert.setTitle("Please fill the details:").setView(textEntryView,50 ,30, 50 , 0).setPositiveButton("Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    country = String.valueOf(input1.getText());
                                    pincode=String.valueOf(input2.getText());
                                    city=String.valueOf(input3.getText());
                                    streetAddress=String.valueOf(input4.getText());
                                    houseno = String.valueOf(input7.getText());
                                    state = String.valueOf(input5.getText());
                                    landmark = String.valueOf(input6.getText());
                                    addressname1= String.valueOf(addressanme.getText());
                                    locality = localitySpinner.getSelectedItem().toString();
                                    String type = typeSpinner.getSelectedItem().toString();
                                    if(type.equals("Select Type"))
                                    {
                                        Toast.makeText(AddressActivity.this, "Please select type", Toast.LENGTH_SHORT).show();
                                    }else {
                                        addAddress(type, lat, lon, mBanner.get(position).getId() );

                                    }
                                    /* User clicked OK so do some stuff */
                                }
                            }).setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                }else{
                    changeAddress = true;
                    sessionManager.setIntData("position", position);
                    sessionManager.setStringData("pincode", mBanner.get(position).getPincode());
                    sessionManager.setStringData("delivery_charge", mBanner.get(position).getDeliveryCharge());
                    sessionManager.setStringData("pincoded", mBanner.get(position).getHno()+","+mBanner.get(position).getAddress()+","+landmark1+mBanner.get(position).getCity()+"  "+ mBanner.get(position).getState());
                    finish();
                }

            });
            holder.imgMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, holder.imgMenu);
                if(comeFrom.equals("settings"))
                {
                    popup.inflate(R.menu.address_menu_2);
                }else{
                    popup.inflate(R.menu.address_menu);
                }
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_select:
                            changeAddress = true;
                            sessionManager.setIntData("position", position);
                            sessionManager.setStringData("pincode", mBanner.get(position).getPincode());
                            sessionManager.setStringData("delivery_charge", mBanner.get(position).getDeliveryCharge());
                            sessionManager.setStringData("pincoded", mBanner.get(position).getHno()+","+mBanner.get(position).getAddress()+","+mBanner.get(position).getLandmark()+ ","+mBanner.get(position).getCity()+"  "+ mBanner.get(position).getState());
                            Toast.makeText(context, "Address Selected Successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case R.id.menu_edit:
                            LayoutInflater factory = LayoutInflater.from(AddressActivity.this);

                            //text_entry is an Layout XML file containing two text field to display in alert dialog

                            final View textEntryView = factory.inflate(R.layout.layout_pincode, null);

                            final EditText input1 = (EditText) textEntryView.findViewById(R.id.ed_country);
                            final EditText nameEt = (EditText) textEntryView.findViewById(R.id.ed_name);
                            final EditText input2 = (EditText) textEntryView.findViewById(R.id.ed_pincode);
                            final EditText input3 = (EditText) textEntryView.findViewById(R.id.ed_city);
                            final EditText input4 = (EditText) textEntryView.findViewById(R.id.ed_street_address);
                            final EditText input5 = (EditText) textEntryView.findViewById(R.id.ed_state);
                            final EditText input6 = (EditText) textEntryView.findViewById(R.id.ed_landmark);
                            final Spinner typeSpinner = textEntryView.findViewById(R.id.type_spinner);
                            localitySpinner = textEntryView.findViewById(R.id.locality_spinner);
                            final EditText input7 = (EditText) textEntryView.findViewById(R.id.ed_houseno);
                            final EditText addressanme = (EditText)textEntryView.findViewById(R.id.ed_name);
                            locality = mBanner.get(position).getPostalname();
                            lat = mBanner.get(position).getLatMap();
                            lon = mBanner.get(position).getLongMap();
                            pincode = mBanner.get(position).getPincode();
                            url = "https://api.postalpincode.in/pincode/"+pincode;
                            getLocality("edit");


                            setSpinnerItems(typeSpinner);


                            input1.setText("India", TextView.BufferType.NORMAL);
                            nameEt.setText(mBanner.get(position).getName(), TextView.BufferType.EDITABLE);
                            input2.setText(mBanner.get(position).getPincode(), TextView.BufferType.EDITABLE);
                            input3.setText(mBanner.get(position).getCity(), TextView.BufferType.EDITABLE);
                            input7.setText(mBanner.get(position).getHno(), TextView.BufferType.EDITABLE);
                            input6.setText(mBanner.get(position).getLandmark(), TextView.BufferType.EDITABLE);
                            input4.setText(mBanner.get(position).getAddress(), TextView.BufferType.EDITABLE);
                            input5.setText(mBanner.get(position).getState(), TextView.BufferType.EDITABLE);

                            if(mBanner.get(position).getName()!=null){
                                addressanme.setText(mBanner.get(position).getName());
                            }
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddressActivity.this, R.array.address_type, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            typeSpinner.setAdapter(adapter);
                            if (mBanner.get(position).getType() != null) {
                                int spinnerPosition = adapter.getPosition(mBanner.get(position).getType());
                                typeSpinner.setSelection(spinnerPosition);
                            }

                            input2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    if(!b)
                                    {
                                        url = "https://api.postalpincode.in/pincode/"+input2.getText().toString();
                                        getLocality("");
                                    }
                                }
                            });


                            final AlertDialog.Builder alert = new AlertDialog.Builder(AddressActivity.this);
                            alert.setTitle("Please fill the details:").setView(textEntryView,50 ,30, 50 , 0).setPositiveButton("Save",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            country = String.valueOf(input1.getText());
                                            pincode=String.valueOf(input2.getText());
                                            city=String.valueOf(input3.getText());
                                            streetAddress=String.valueOf(input4.getText());
                                            houseno = String.valueOf(input7.getText());
                                            state = String.valueOf(input5.getText());
                                            landmark = String.valueOf(input6.getText());
                                            addressname1= String.valueOf(addressanme.getText());
                                            locality = localitySpinner.getSelectedItem().toString();
                                            String type = typeSpinner.getSelectedItem().toString();
                                            if(type.equals("Select Type"))
                                            {
                                                Toast.makeText(AddressActivity.this, "Please select type", Toast.LENGTH_SHORT).show();
                                            }else {
                                                addAddress(type, lat, lon, mBanner.get(position).getId() );
                                                
                                            }
                                            /* User clicked OK so do some stuff */
                                        }
                                    }).setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            dialog.dismiss();
                                        }
                                    });
                            alert.show();

                            break;
                        case R.id.menu_delete:
                            deleteAddress(mBanner.get(position).getId());
                            break;
                        default:
                            break;
                    }
                    return false;
                });
                popup.show();
            });

        }

        @Override
        public int getItemCount() {
            return mBanner.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_banner)
            ImageView imgBanner;
            @BindView(R.id.img_menu)
            ImageView imgMenu;
            @BindView(R.id.txt_homeaddress)
            TextView txtHomeaddress;
            @BindView(R.id.txt_tital)
            TextView txtType;
            @BindView(R.id.lvl_home)
            LinearLayout lvlHome;
            @BindView((R.id.txt_addressname))
            TextView txtAddressanme;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
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
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
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
                if(location!=null){
                    Geocoder geocoder = new Geocoder(AddressActivity.this, Locale.getDefault());
                    try {
                        List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        city = addresses.get(0).getLocality();
                        country = addresses.get(0).getCountryName();
                        pincode = addresses.get(0).getPostalCode();
                        streetAddress = addresses.get(0).getFeatureName();
                        lat = addresses.get(0).getLatitude();
                        lon = addresses.get(0).getLongitude();
                        state = addresses.get(0).getAdminArea();
                        url= "https://api.postalpincode.in/pincode/"+pincode;

                        LayoutInflater factory = LayoutInflater.from(AddressActivity.this);

                        //text_entry is an Layout XML file containing two text field to display in alert dialog

                        final View textEntryView = factory.inflate(R.layout.layout_pincode, null);

                        final EditText input1 = (EditText) textEntryView.findViewById(R.id.ed_country);
                        final EditText input2 = (EditText) textEntryView.findViewById(R.id.ed_pincode);
                        final EditText input3 = (EditText) textEntryView.findViewById(R.id.ed_city);
                        final EditText input4 = (EditText) textEntryView.findViewById(R.id.ed_street_address);
                        final EditText input5 = (EditText) textEntryView.findViewById(R.id.ed_state);
                        final EditText input6 = (EditText) textEntryView.findViewById(R.id.ed_landmark);
                        final Spinner typeSpinner = textEntryView.findViewById(R.id.type_spinner);
                        localitySpinner = textEntryView.findViewById(R.id.locality_spinner);


                        final EditText input7 = (EditText) textEntryView.findViewById(R.id.ed_houseno);
                        final EditText addressname = (EditText) textEntryView.findViewById(R.id.ed_name);
                        setSpinnerItems(typeSpinner);
                        getLocality("location");

                        input1.setText(country, TextView.BufferType.NORMAL);
                        input2.setText(pincode, TextView.BufferType.EDITABLE);
                        input3.setText(city, TextView.BufferType.EDITABLE);
                        input4.setText(streetAddress, TextView.BufferType.EDITABLE);
                        input5.setText(state, TextView.BufferType.EDITABLE);


                        input2.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String pincode = input2.getText().toString();
                                if(pincode.length()==6)
                                {
                                    url = "https://api.postalpincode.in/pincode/"+input2.getText().toString();
                                    getLocality("location");
                                }

                            }
                        });

                        final AlertDialog dialog = new AlertDialog.Builder(AddressActivity.this)
                                .setTitle("Please fill the details:")
                                .setPositiveButton("Save", null) //Set to null. We override the onclick
                                .setNegativeButton(android.R.string.cancel, null)
                                .setView(textEntryView,50 ,30, 50 , 0)
                                .create();

                        dialog.setOnShowListener(dialog1 -> {
                            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            button.setOnClickListener(v -> {
                                country = String.valueOf(input1.getText());
                                pincode = String.valueOf(input2.getText());
                                city = String.valueOf(input3.getText());
                                streetAddress = String.valueOf(input4.getText());
                                houseno = String.valueOf(input7.getText());
                                state = String.valueOf(input5.getText());
                                landmark = String.valueOf(input6.getText());
                                addressname1 = String.valueOf(addressname.getText().toString());
                                if (localitySpinner.isSelected()) {
                                    locality = localitySpinner.getSelectedItem().toString();
                                }

                                String type = typeSpinner.getSelectedItem().toString();

                                if (addressname1.equals("")) {
                                    Toast.makeText(AddressActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                                } else if (houseno.equals("")) {
                                    Toast.makeText(AddressActivity.this, "Please enter house no.", Toast.LENGTH_SHORT).show();
                                } else if (streetAddress.equals("")) {
                                    Toast.makeText(AddressActivity.this, "Please enter street address", Toast.LENGTH_SHORT).show();
                                } else if (pincode.equals("")) {
                                    Toast.makeText(AddressActivity.this, "Please enter pincode.", Toast.LENGTH_SHORT).show();
                                } else if (localitySpinner.getSelectedItem().toString().equals("Select Postal Address")) {
                                    Toast.makeText(AddressActivity.this, "Please select postal address", Toast.LENGTH_SHORT).show();
                                }else if (city.equals("")) {
                                    Toast.makeText(AddressActivity.this, "Please enter city", Toast.LENGTH_SHORT).show();
                                }else if(state.equals("")) {
                                    Toast.makeText(AddressActivity.this, "Please enter state", Toast.LENGTH_SHORT).show();
                                } else {
                                    addAddress(type, 0, 0, "0");
                                }
                            });

                        });
                        dialog.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setSpinnerItems(Spinner spinner)
    {
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.address_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void deleteAddress(String addressid)
    {
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JSONObject jsonObject = new JSONObject();
        userid = sessionManager.getUserDetails("").getId();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("address_id", addressid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = ApiClient.getInterface().deleteAddress(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3", this);
    }


    public void getLocality(String comeFrom)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        custPrograssbar.prograssCreate(this);
        custPrograssbar.CancelTouchOutside();
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("localityarray", response.toString());
                custPrograssbar.closePrograssBar();
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    String Message = jsonObject.getString("Message");
                    String Status = jsonObject.getString("Status");
                    JSONArray postOfficeArray = jsonObject.getJSONArray("PostOffice");
                    localityList.clear();
                    localityList.add(0, "Select Postal Address");
                    for(int i =0; i<postOfficeArray.length(); i++)
                    {
                        localityList.add(postOfficeArray.getJSONObject(i).getString("Name"));
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddressActivity.this, R.layout.custom_spinner, localityList);
                    localitySpinner.setAdapter(arrayAdapter);
                    if(comeFrom.equals("edit"))
                    {
                        int spinnerpos = localityList.indexOf(locality);
                        localitySpinner.setSelection(spinnerpos);
                    }
                    Log.d("message", ""+localityList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                custPrograssbar.closePrograssBar();
            }
        });
        queue.add(request);
    }


}