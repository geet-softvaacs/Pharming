package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.onetick.pharmafest.MyApplication;
import com.onetick.pharmafest.R;
import com.onetick.pharmafest.model.Medicine;
import com.onetick.pharmafest.model.PatientDetails;
import com.onetick.pharmafest.model.User;
import com.onetick.pharmafest.utils.Constant;
import com.onetick.pharmafest.utils.SessionManager;
import com.onetick.pharmafest.utils.Utility;
import com.onetick.utils.Patterns;

public class ActivitySelectPatient extends AppCompatActivity {
    SessionManager sessionManager;
    Toolbar toolbar;
    ImageView imgBack;
    Medicine medicine;
    String subTotal,total,note;
    RadioButton rbMale, rbFemale,rbOthers, selectAddress;
    SwitchCompat sc_Self;
    EditText et_name,etphone_number,et_EmailId, etAge;
    TextView etAddressDetail;
    RadioGroup rgRadio;
    TextView selectSlot;
    String phonenumber;
    String phonecode;
    String subtotal;
    String pincode2;
    String country;
    String pincode;
    String city;
    String getType;
    String isChecked;
    String getHno;
    String getLandmark;
    String getCurrentAddress;
    String streetAddress;
    String mAddress;
    String cAddress;
    String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patient);
        sessionManager = new SessionManager(this);
        if(getIntent()!=null)
        {
            medicine =getIntent().getParcelableExtra("MyClass");
            subTotal =String.valueOf(getIntent().getDoubleExtra(Constant.SUB_TOTAL, 0.0));
            total =String.valueOf(getIntent().getDoubleExtra(Constant.TOTAL, 0.0));
            note =getIntent().getStringExtra(Constant.NOTE);
        }

        toolbar =findViewById(R.id.toolbar);
        imgBack = findViewById(R.id.img_back);
        rbFemale = findViewById(R.id.rb_female);
        rbMale = findViewById(R.id.rb_male);
        sc_Self = findViewById(R.id.sc_self);
        rbOthers = findViewById(R.id.rb_others);
        et_name = findViewById(R.id.et_name);
        etphone_number = findViewById(R.id.et_phone_number);
        etAge = findViewById(R.id.et_age);
        etAddressDetail = findViewById(R.id.et_address_detail);
        rgRadio = findViewById(R.id.rg_gender);
        selectSlot = findViewById(R.id.tv_select_slot);
        selectAddress = findViewById(R.id.getCurrentLocationp);
        imgBack.setOnClickListener(view -> {
            finish();
        });

        rbMale.setChecked(true);


        sc_Self.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b)
                {
                    setData();
                }else{
                    clearData();
                }
            }
        });

        selectSlot.setOnClickListener(view -> {
            getData();
        });


        selectAddress.setOnClickListener(view -> {
            Intent intent = new Intent(ActivitySelectPatient.this, AddressActivity.class);
            intent.putExtra("comeFrom", "patient");
            startActivity(intent);
        });

        Intent intent = getIntent();

        subtotal = intent.getStringExtra("subtotal");
        total = intent.getStringExtra("total");
        note = intent.getStringExtra("note");
        pincode2 = intent.getStringExtra("pincode");

        etAddressDetail.setOnClickListener(v -> {
            Intent i = new Intent(ActivitySelectPatient.this, AddressActivity.class);
            i.putExtra("comeFrom", "patient");
            startActivity(i);
        });




    }

    public void setData()
    {
        String fname, lname, fullname, mobile, email;
        User user =sessionManager.getUserDetails("user");
        if(user.getFname()==null)
        {
            fname = "";
        }else{
            fname =user.getFname();
        }

        if(user.getLname()==null)
        {
            lname = "";
        }else{
            lname = user.getLname();
        }

        fullname = fname+lname;
        if(user.getMobile()==null)
        {
            mobile ="";
        }else{
            mobile = user.getMobile();
        }
        et_name.setText(fullname);
        etphone_number.setText(mobile);
    }

    public void clearData()
    {
        et_name.setText("");
        etphone_number.setText("");
        etAge.setText("");
        etAddressDetail.setText("");

    }

    public void getData()
    {
        String name =et_name.getText().toString();
        String number =etphone_number.getText().toString();
        String age =etAge.getText().toString();
        String address = etAddressDetail.getText().toString();
        int selectid = rgRadio.getCheckedRadioButtonId();
        RadioButton rb =findViewById(selectid);
        String gender =rb.getText().toString();
        if(gender.equals("Female"))
        {
            gender = "F";
        }else if(gender.equals("Male"))
        {
            gender = "M";
        }


        if(name.isEmpty() || name.length()<3)
        {
            Utility.toastShort(ActivitySelectPatient.this, "Please enter a name more than 2 character");
        }else if(number.isEmpty() || number.length()<=9)
        {
            Utility.toastShort(ActivitySelectPatient.this, "Please enter a valid mobile number");
        }else if(age.isEmpty()|| age==null)
        {
            Utility.toastShort(ActivitySelectPatient.this, "Please enter Age");
        }else if(address.isEmpty()|| address==null)
        {
            Utility.toastShort(ActivitySelectPatient.this, "Please enter Address");
        }else if(gender.isEmpty() || gender==null)
        {
            Utility.toastShort(ActivitySelectPatient.this, "Please select gender");
        }else if(etAddressDetail.equals(""))
        {
            Toast.makeText(this, "Please select address", Toast.LENGTH_SHORT).show();
        }
        else{
            PatientDetails patientDetails = new PatientDetails();
            patientDetails.setName(name);
            patientDetails.setMobile(number);
            patientDetails.setAge(age) ;
            patientDetails.setAddress(address);
            patientDetails.setGender(gender);

            User user =new User();
            user.setFname(name);
            user.setMobile(number);

            startActivity(new Intent(ActivitySelectPatient.this, PaymentOptionActivity.class)
                    .putExtra("subtotal", subtotal)
                    .putExtra("total", total)
                    .putExtra(Constant.NOTE, note)
                    .putExtra("MyClass", medicine)
                    .putExtra("type", true)
                    .putExtra("type1", "Lab")
                    .putExtra("pincode",pincode2)

                    .putExtra(Constant.PATIENT_DETAILS, patientDetails));

//            if(MyApplication.Companion.getInstance().getTimeSlot()!=null)
//            {
//                startActivity(new Intent(ActivitySelectPatient.this, ActivityConfirmDetailsAndTimings.class).putExtra(Constant.SUB_TOTAL, subTotal)
//                        .putExtra(Constant.TOTAL, total)
//                        .putExtra(Constant.NOTE, note)
//                        .putExtra("MyClass", medicine)
//                        .putExtra("type", true)
//                        .putExtra(Constant.PATIENT_DETAILS, patientDetails));
//            }else{
//
//            }
            finish();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        etAddressDetail.setText("" + sessionManager.getStringData("pincoded"));
    }
}