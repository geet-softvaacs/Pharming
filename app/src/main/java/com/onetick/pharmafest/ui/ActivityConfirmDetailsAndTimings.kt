package com.onetick.pharmafest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.onetick.pharmafest.R
import com.onetick.pharmafest.adapter.AdapterDaySlot
import com.onetick.pharmafest.adapter.AdapterTimeSlot
import com.onetick.pharmafest.model.*
import com.onetick.pharmafest.utils.Constant
import com.onetick.pharmafest.utils.SessionManager
import com.onetick.pharmafest.utils.Utility
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_confirm_details_and_timings.*
import kotlinx.android.synthetic.main.row_slot_time.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityConfirmDetailsAndTimings : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var imgBack: ImageView
    private lateinit var sessionManger: SessionManager
    private var daySlot: ArrayList<ModelDaySlot> = ArrayList()
    private var timeSlot: ArrayList<ModelTimeSlot> = ArrayList()
    private lateinit var adapterDay: AdapterDaySlot
    private lateinit var adapterTime: AdapterTimeSlot

    private lateinit var patientDetails: PatientDetails
    private var medicine: Medicine? = null
    private var subTotal: String? = ""
    private var total: String? = ""
    private var note: String? = ""
    private var labInfo: LabInfo? = null
    private var workingDays = arrayListOf<Int>()

    private var selectedDate = ""
    private var selectedDateTemp = ""

    private var selectedSlotObject: ModelTimeSlot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_details_and_timings)

        if (intent != null) {

            patientDetails = intent.getParcelableExtra(Constant.PATIENT_DETAILS)!!
            medicine = intent.getParcelableExtra("MyClass")
            subTotal = intent.getStringExtra(Constant.SUB_TOTAL)
            total = intent.getStringExtra(Constant.TOTAL)
            note = intent.getStringExtra(Constant.NOTE)
        }

//        labInfo = MyApplication.getInstance()?.timeSlot

        toolbar = findViewById(R.id.toolbar)
        imgBack = findViewById(R.id.img_back)
        imgBack.setOnClickListener { finish() }

        sessionManger = SessionManager(this@ActivityConfirmDetailsAndTimings)

        workingDays.clear()

        val days = labInfo!!.days.split(",").toTypedArray()

        days.forEach { dayObject ->
            when {
                dayObject.equals("7", true) -> {
                    workingDays.add(Calendar.SUNDAY)
                }
                dayObject.equals("1", true) -> {
                    workingDays.add(Calendar.MONDAY)
                }
                dayObject.equals("2", true) -> {
                    workingDays.add(Calendar.TUESDAY)
                }
                dayObject.equals("3", true) -> {
                    workingDays.add(Calendar.WEDNESDAY)
                }
                dayObject.equals("4", true) -> {
                    workingDays.add(Calendar.THURSDAY)
                }
                dayObject.equals("5", true) -> {
                    workingDays.add(Calendar.FRIDAY)
                }
                dayObject.equals("6", true) -> {
                    workingDays.add(Calendar.SATURDAY)
                }
            }
        }

        selectedDate = Utility.getCurrentDate("yyyy-MM-dd")
        selectedDateTemp = Utility.getCurrentDate("dd-MM-yyyy")

        labBookSlotDateTextView.text = Utility.getCurrentDate("dd MMMM, yyyy")

        setData()

        labBookSlotDateTextView.setOnClickListener {

            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val now = Calendar.getInstance()
        try {
            val setDate = Utility.getDate(selectedDate, "yyyy-MM-dd")
            now.time = setDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val dpd = DatePickerDialog.newInstance(
                { _, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    val selectedDate = Utility.getDate("$dayOfMonth-$month-$year", "d-M-yyyy")
                    this.selectedDate = Utility.getFormattedDate(selectedDate, "yyyy-MM-dd")
                    this.selectedDateTemp = Utility.getFormattedDate(selectedDate, "dd-MM-yyyy")
                    labBookSlotDateTextView.text = Utility.getFormattedDate(selectedDate, "dd MMMM, yyyy")
                    setData()
                    selectedSlotObject = null

                },
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        )

        val maxCalendar = Calendar.getInstance()
        maxCalendar.add(Calendar.DAY_OF_MONTH, 30)

        dpd.firstDayOfWeek = Calendar.MONDAY
        dpd.minDate = Calendar.getInstance()
        dpd.maxDate = maxCalendar
        if (workingDays.size < 7) {
            getDisableDays(dpd)
        }
        dpd.show(supportFragmentManager, "Datepickerdialog")
        dpd.version = DatePickerDialog.Version.VERSION_2
    }

    private fun getDisableDays(dpd: DatePickerDialog) {
        val disableDays = arrayListOf<Calendar>()
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 1)
        var loopdate: Calendar = startDate
        while (loopdate.before(endDate)) {
            val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
            if (!workingDays.contains(dayOfWeek)) {
                dpd.disabledDays = arrayOf(loopdate)
                disableDays.add(loopdate)
            }
            startDate.add(Calendar.DATE, 1)
            loopdate = startDate
        }
    }

    private fun setData() {

        toolbar.title = "Confirm"
        val mobile = if (!sessionManger.getUserDetails("").mobile.isNullOrEmpty()) sessionManger.getUserDetails("").mobile + " | " else ""

        val fullName = sessionManger.getUserDetails("").fname + " " + sessionManger.getUserDetails("").lname
        tv_patient_name.text = fullName
        tv_patient_details.text = mobile + sessionManger.getUserDetails("").email

        daySlot.add(ModelDaySlot("12\nMon"))


        val date = Utility.getCurrentDate("dd MMMM, yyyy")
        val time = getCurrentTime()



        Log.e("DateTime", "Date $date $time")

        // if (labBookSlotDateTextView.text.equals(date) && time)

        timeSlot.clear()
        timeSlot.add(ModelTimeSlot("07:00 AM", isClicked = false, is_available = true, enabled = true, saloonSlotId = 0))
        timeSlot.add(ModelTimeSlot("10:00 AM", isClicked = false, is_available = true, enabled = true, saloonSlotId = 1))
        timeSlot.add(ModelTimeSlot("01:00 PM", isClicked = false, is_available = true, enabled = true, saloonSlotId = 2))
        timeSlot.add(ModelTimeSlot("04:00 PM", isClicked = false, is_available = true, enabled = true, saloonSlotId = 3))
        timeSlot.add(ModelTimeSlot("07:00 PM", isClicked = false, is_available = true, enabled = true, saloonSlotId = 4))

        rv_day_slot.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_time_slot.layoutManager = GridLayoutManager(this, 2)

//        adapterDay = AdapterDaySlot(this, daySlot) { modelDaySlot, _ ->
//
//            daySlot.forEachIndexed { index, it ->
//
//                daySlot[index].isClicked = modelDaySlot.day.equals(it.day)
//
//                Log.e("Confirm", "Day Result - ${modelDaySlot.day.equals(it.day)}")
//
//                adapterDay.notifyItemChanged(index)
//            }
//        }
//
//        rv_day_slot.adapter = adapterDay
//
//        adapterTime = AdapterTimeSlot(this, timeSlot) { ModelTimeSlot, _ ->
//
//            timeSlot.forEachIndexed { index, it ->
//
//                timeSlot[index].isClicked = ModelTimeSlot.from.equals(it.from)
//
//                Log.e("Confirm", "Time Result - ${ModelTimeSlot.from.equals(it.from)}")
//
//                adapterTime.notifyItemChanged(index)
//            }
//        }

        rv_time_slot.adapter = adapterTime

        tv_book_now_test.setOnClickListener {

            if (selectedSlotObject == null) {
                Toast.makeText(this@ActivityConfirmDetailsAndTimings, "Please select a slot", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {

                patientDetails.date = selectedDate
                patientDetails.time = selectedSlotObject?.from

                val intent = Intent(this@ActivityConfirmDetailsAndTimings, PaymentOptionActivity::class.java)
                intent.putExtra(Constant.SUB_TOTAL, subTotal)
                intent.putExtra(Constant.TOTAL, total)
                intent.putExtra(Constant.NOTE, note)
                intent.putExtra("MyClass", medicine)
                intent.putExtra("type", true)
                intent.putExtra(Constant.PATIENT_DETAILS, patientDetails)
                startActivity(intent)
            }
        }

        labBookSlotTimeFlexBoxLayout.removeAllViews()
        inflateSlotFlowLayout()
        // salonBookSlotContentLayout.visibility = View.VISIBLE
    }

    private fun inflateSlotFlowLayout() {
        labBookSlotTimeFlexBoxLayout.removeAllViews()

        timeSlot.forEach { slot ->
            val slotDateTime = Utility.getDate(selectedDate + " " + slot.from,
                    "yyyy-MM-dd hh:mm a")
            if (slotDateTime.before(Date())) {
                slot.enabled = false
                slot.is_available = false
            } else {
                slot.enabled = true
            }
            slot.isClicked = slot.saloonSlotId == selectedSlotObject?.saloonSlotId
            labBookSlotTimeFlexBoxLayout.addView(inflateSlotData(slot))
        }
    }

    private fun inflateSlotData(slotObject: ModelTimeSlot): View {
        val rootView = LayoutInflater.from(this).inflate(R.layout.row_slot_time, null)

        rootView.rowSlotTimeTextView.text = slotObject.from
        rootView.rowSlotTimeTextView.setTextColor(ContextCompat.getColor(this, R.color.app_text_secondary))
        rootView.rowSlotTimeTextView.setBackgroundResource(getBackground(slotObject.is_available))

        if (!slotObject.enabled) {
            rootView.rowSlotTimeTextView.setTextColor(ContextCompat.getColor(this, R.color.app_text_secondary_alpha))
            rootView.rowSlotTimeTextView.setBackgroundResource(R.drawable.bg_rounded_stroked_grey_25)
        }

        if (slotObject.isClicked) {
            rootView.rowSlotTimeTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
            rootView.rowSlotTimeTextView.setBackgroundResource(R.drawable.bg_rounded_black_25)
        }

        rootView.rowSlotTimeTextView.setOnClickListener {
            if (slotObject.is_available) {
                if (!slotObject.isClicked) {
                    selectedSlotObject = slotObject
                    inflateSlotFlowLayout()
                }
            }
        }
        return rootView
    }

    private fun getBackground(isAvailable: Boolean): Int {
        return if (isAvailable) {
            R.drawable.bg_rounded_stroked_green_25
        } else {
            R.drawable.bg_rounded_stroked_red_25
        }
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("hh:mm a")
        return mdformat.format(calendar.time)
    }

    private fun getCurrentDate(): String {
        val mdformat = SimpleDateFormat("yyyy-MM-dd")
        return mdformat.format(Date())
    }
}