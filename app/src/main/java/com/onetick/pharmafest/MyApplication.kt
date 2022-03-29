package com.onetick.pharmafest

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.onetick.pharmafest.model.LabInfo


class MyApplication : Application() {

    var timeSlot: LabInfo? = null

    override fun onCreate() {
        super.onCreate()
        mContext = this

        mInstance = this

        FirebaseApp.initializeApp(this)
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(NotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
    }

    inner class NotificationOpenedHandler : OneSignal.NotificationOpenedHandler {
        override fun notificationOpened(result: OSNotificationOpenResult) {
            val data = result.notification.payload.additionalData
            val activityToBeOpened: String?
            if (data != null) {
                activityToBeOpened = data.optString("type", null)
                if (activityToBeOpened != null && activityToBeOpened == "normal") {
                    Log.i("OneSignal", "customkey set with value: $activityToBeOpened")
//                    val intent = Intent(mContext, OrderDetailsActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
//                    intent.putExtra("oid", data.optString("order_id", null))
//                    startActivity(intent)
                } else if (activityToBeOpened != null && activityToBeOpened == "prescription") {
//                    val intent = Intent(mContext, PrescriptionOrderDetailsActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
//                    intent.putExtra("oid", data.optString("order_id", null))
//                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        @JvmField
        var mContext: Context? = null

        var mInstance: MyApplication? = null

        @Synchronized
        fun getInstance(): MyApplication? {
            return mInstance
        }
    }
}