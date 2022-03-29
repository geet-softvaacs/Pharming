package com.onetick.pharmafest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityUploadPrescriptionBinding;
import com.onetick.pharmafest.imagepicker.ImageCompressionListener;
import com.onetick.pharmafest.imagepicker.ImagePicker;
import com.onetick.pharmafest.model.Address;
import com.onetick.pharmafest.model.AddressList;
import com.onetick.pharmafest.model.RestResponse;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.FileUtils;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.onetick.pharmafest.utils.FileUtils.isLocal;

public class UploadPrescriptionActivity extends AppCompatActivity implements GetResult.MyListener{
    ActivityUploadPrescriptionBinding binding;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActiontitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.txt_prescription_valid)
    TextView txtPrescriptionValid;
    @BindView(R.id.btn_upload)
    TextView btnUpload;
    @BindView(R.id.lvl_empty)
    LinearLayout lvlEmpty;
    @BindView(R.id.lvl_pic)
    LinearLayout lvlPic;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_atype)
    TextView txtAtype;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_changeadress)
    TextView txtChangeadress;
    @BindView(R.id.btn_ather)
    TextView btnAther;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.addressRow)
    LinearLayout addressRow;
    boolean isSelectAddress = false;

    private ImagePicker imagePicker;
    ArrayList<String> arrayListImage = new ArrayList<>();
    CustPrograssbar custPrograssbar;
    User user;
    SessionManager sessionManager;
    String pincode1;
    String finaladdress;
    static UploadPrescriptionActivity uploadPrescriptionActivity;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_prescription);
        binding.imgBack.setOnClickListener(view -> {
            finish();
        });

        ButterKnife.bind(this);
        uploadPrescriptionActivity=this;
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(UploadPrescriptionActivity.this);
//        user = sessionManager.getUserDetails("");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imagePicker = new ImagePicker();
        if (checkPermission()) {
            //start image picker

        } else {
            //ask permission
            requestStoragePermission();
        }
        getAddress();
    }
    public static UploadPrescriptionActivity getInstance(){
        return   uploadPrescriptionActivity;
    }
    private void uploadMultiFile(ArrayList<String> filePaths) {
        custPrograssbar.prograssCreate(UploadPrescriptionActivity.this);
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (filePaths != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < filePaths.size(); i++) {
                parts.add(prepareFilePart("image" + i, filePaths.get(i)));
            }
        }
// create a map of data to pass along

        RequestBody uid = createPartFromString(sessionManager.getUserDetails("").getId());
        RequestBody address_id = createPartFromString(finaladdress);
        RequestBody pincode = createPartFromString(pincode1);
        RequestBody size = createPartFromString("" + parts.size());

// finally, execute the request
        Call<JsonObject> call = ApiClient.getInterface().uploadMultiFile(uid, address_id,pincode, size, parts);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(response.body(), RestResponse.class);
                Toast.makeText(UploadPrescriptionActivity.this, restResponse.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    lvlEmpty.setVisibility(VISIBLE);
                    lvlPic.setVisibility(GONE);
                    arrayListImage.clear();
                    startActivity(new Intent(UploadPrescriptionActivity.this, PrescriptionOrderActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                custPrograssbar.closePrograssBar();
            }
        });

    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = getFile(fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static File getFile(String path) {
        if (path == null) {
            return null;
        }

        if (isLocal(path)) {
            return new File(path);
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == RESULT_OK) {

            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onCompressed(String filePath) {
                    if (filePath != null) {
                        //return filepath
                        arrayListImage.add(filePath);
                        postImage(arrayListImage);
                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {
                //return filepath
                arrayListImage.add(filePath);
                postImage(arrayListImage);
            }

        }
    }

    public void postImage(ArrayList<String> urilist) {
        if (urilist.size() != 0) {
            lvlEmpty.setVisibility(GONE);
            lvlPic.setVisibility(VISIBLE);
            if(sessionManager.getStringData("pincoded").equals(""))
            {
                addressRow.setVisibility(GONE);
                isSelectAddress = false;
            }else{
                isSelectAddress = true;
                addressRow.setVisibility(VISIBLE);
                txtAddress.setText("" + sessionManager.getStringData("pincoded"));
            }
        }
        ImageAdp imageAdp = new ImageAdp(UploadPrescriptionActivity.this, urilist);
        recyclerView.setAdapter(imageAdp);

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        Gson gson = new Gson();
        Address address = gson.fromJson(result.toString(), Address.class);
        if (address.getResult().equalsIgnoreCase("true")) {
            int position = sessionManager.getIntData("position");
            if(address.getAddressList()!=null)
            {
                if(address.getAddressList().size()==0)
                {
                    Toast.makeText(UploadPrescriptionActivity.this, "", Toast.LENGTH_SHORT).show();
                }else{
                    AddressList address1 = address.getAddressList().get(position);
                    String landmark = address1.getLandmark();
                    if(landmark.equals("0"))
                    {
                        landmark= "";
                    }
                    pincode1 = address1.getPincode();
                    finaladdress = address1.getHno() + landmark + address1.getAddress() + pincode1;

                    if (address.getAddressList().size() != 0) {
                        txtAtype.setText("" + address1.getType());
                        addressRow.setVisibility(VISIBLE);
                        txtAddress.setText("" + address1.getHno()+ landmark+ address1.getAddress()+ " "+ pincode1);
                        sessionManager.setStringData(SessionManager.address, address.getAddressList().get(sessionManager.getIntData("position")).getAddress());
                    }
                }
            }



        } else {
            finish();
        }
    }

    public class ImageAdp extends RecyclerView.Adapter<ImageAdp.MyViewHolder> {
        private ArrayList<String> arrayList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView remove;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);

                thumbnail = view.findViewById(R.id.image_pic);
                remove = view.findViewById(R.id.image_remove);
            }
        }

        public ImageAdp(Context mContext, ArrayList<String> arrayList) {
            this.arrayList = arrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            Glide.with(UploadPrescriptionActivity.this)
                    .load(arrayList.get(position))
                    .into(holder.thumbnail);
            holder.remove.setOnClickListener(v -> {
                arrayList.remove(position);
                if (arrayList.size() != 0) {
                    notifyDataSetChanged();
                } else {
                    lvlEmpty.setVisibility(VISIBLE);
                    lvlPic.setVisibility(GONE);
                }

            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1234);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1234) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&  grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.e("OOOn", "Done");
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    public void bottonChoseoption() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);

        View sheetView = getLayoutInflater().inflate(R.layout.layout_image_select, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView textViewCamera = sheetView.findViewById(R.id.textViewCamera);
        TextView textViewGallery = sheetView.findViewById(R.id.textViewGallery);
        TextView textViewCancel = sheetView.findViewById(R.id.textViewCancel);


        mBottomSheetDialog.show();

        textViewCamera.setOnClickListener(v -> {

            mBottomSheetDialog.cancel();
            imagePicker.withActivity(UploadPrescriptionActivity.this).chooseFromGallery(false).chooseFromCamera(true).withCompression(true).start();


        });
        textViewGallery.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            imagePicker.withActivity(UploadPrescriptionActivity.this).chooseFromGallery(true).chooseFromCamera(false).withCompression(true).start();

        });
        textViewCancel.setOnClickListener(v -> mBottomSheetDialog.cancel());
    }

    public void bottonVelidation() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_vallid_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    private void getAddress() {
        JSONObject jsonObject = new JSONObject();
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

    @OnClick({R.id.img_back, R.id.txt_prescription_valid, R.id.btn_upload, R.id.btn_ather, R.id.btn_submit, R.id.txt_changeadress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_ather:
                bottonChoseoption();
                break;
            case R.id.btn_submit:
                if(!isSelectAddress)
                {
                    Intent i = new Intent(UploadPrescriptionActivity.this, AddressActivity.class);
                    i.putExtra("comeFrom", "upload");
                    startActivity(i);
                }else{
                    if (arrayListImage.size() != 0) {
                        uploadMultiFile(arrayListImage);
                    }
                }
                break;
            case R.id.txt_changeadress:
                Intent intent = new Intent(this, AddressActivity.class);
                intent.putExtra("comeFrom", "upload");
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.txt_prescription_valid:
                bottonVelidation();
                break;
            case R.id.btn_upload:
                bottonChoseoption();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtAddress.setText("" + sessionManager.getStringData("pincoded"));
        finaladdress = sessionManager.getStringData("pincoded");
        pincode1 = sessionManager.getStringData("pincode");
        if(sessionManager.getStringData("pincoded").equals(""))
        {
            isSelectAddress = false;
            addressRow.setVisibility(GONE);
        }else{
            addressRow.setVisibility(VISIBLE);
            isSelectAddress = true;
        }


    }
}