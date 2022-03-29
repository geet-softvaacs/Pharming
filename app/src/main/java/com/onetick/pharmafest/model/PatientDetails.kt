package com.onetick.pharmafest.model

import android.os.Parcel
import android.os.Parcelable

data class PatientDetails(

        var name: String? = null,

        var mobile: String? = null,

        var email: String? = null,

        var age: String? = null,

        var address: String? = null,

        var gender: String? = null,

        var date: String? = null,

        var time: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(mobile)
        parcel.writeString(email)
        parcel.writeString(age)
        parcel.writeString(address)
        parcel.writeString(gender)
        parcel.writeString(date)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PatientDetails> {
        override fun createFromParcel(parcel: Parcel): PatientDetails {
            return PatientDetails(parcel)
        }

        override fun newArray(size: Int): Array<PatientDetails?> {
            return arrayOfNulls(size)
        }
    }
}