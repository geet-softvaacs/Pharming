package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
        @SerializedName("ccode")
        var ccode: String? = null,

        @SerializedName("email")
        var email: String? = null,

        @SerializedName("fname")
        var fname: String? = null,

        @SerializedName("id")
        var id: String? = null,

        @SerializedName("lname")
        var lname: String? = null,

        @SerializedName("mobile")
        var mobile: String? = null,

        @SerializedName("password")
        var password: String? = null,

        @SerializedName("rdate")
        var rdate: String? = null,

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("device_id")
        var device_id: String? = null,

        @SerializedName("google_id")
        var google_id: String? = null
) : Serializable