package com.onetick.pharmafest.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityNotificationBinding;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.CustPrograssbar;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmingo.model.Noti;
import com.onetick.pharmingo.model.NotificationDatum;
import com.onetick.retrofit.ApiClient;
import com.onetick.retrofit.GetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class NotificationActivity extends AppCompatActivity implements GetResult.MyListener {
    ActivityNotificationBinding binding;
    @BindView(R.id.lvl_myorder)
    LinearLayout lvlMyOrder;
    @BindView(R.id.txt_notfound)
    TextView txtNotFound;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotFound;
    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;
    int pageno=1;
    int totalPages;
    ArrayList<NotificationDatum> notificationListNew = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(NotificationActivity.this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        getNotification(pageno);


        binding.nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(pageno!=totalPages)
                    {
                        pageno++;
                        getNotification(pageno);
                    }
                }
            }
        });




    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void setNotiList(LinearLayout lnrView, List<NotificationDatum> list) {
        lnrView.removeAllViews();


        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);

            View view = inflater.inflate(R.layout.custome_noti, null);

            TextView txt_titel = view.findViewById(R.id.txt_titel);
            TextView txt_datetime = view.findViewById(R.id.txt_datetime);
            TextView txt_descption = view.findViewById(R.id.txt_descption);
            String date = parseDateToddMMyyyy(list.get(i).getDatetime());

            txt_titel.setText("  " + list.get(i).getTitle() + "  ");
            txt_datetime.setText("" + date);
            txt_descption.setText("" + list.get(i).getDescription());
            lnrView.addView(view);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void getNotification(int pageno) {
        custPrograssbar.prograssCreate(NotificationActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("page_no",pageno);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = ApiClient.getInterface().getNote(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Noti notification = gson.fromJson(result.toString(), Noti.class);
                totalPages = notification.getTotalPages();

                ArrayList<NotificationDatum> notificationList = new ArrayList<>();
                notificationListNew.addAll(notificationList);
                if (notification.getResult().equalsIgnoreCase("true")) {
                    notificationList.addAll(notification.getNotificationData());
                    /*notificationList.sort(Comparator.comparing(o -> o.getDatetime()));
                    Collections.sort(notificationList, new Comparator<NotificationDatum>() {
                        DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        @Override
                        public int compare(NotificationDatum lhs, NotificationDatum rhs) {
                            try {
                                return f.parse(lhs.getDatetime()).compareTo(f.parse(rhs.getDatetime()));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });*/

                    setNotiList(lvlMyOrder, notificationListNew);
                } else {
                    lvlNotFound.setVisibility(View.VISIBLE);
                    txtNotFound.setText("" + notification.getResponseMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}