package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentItem : Serializable {
    @SerializedName("id")
    private var mId: String? = null

    @SerializedName("img")
    private var mImg: String? = null

    @SerializedName("status")
    private var mStatus: String? = null

    @SerializedName("title")
    private var mTitle: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("attributes")
    private var mAttributes: String? = null
    fun getmId(): String? {
        return mId
    }

    fun setmId(mId: String?) {
        this.mId = mId
    }

    fun getmImg(): String? {
        return mImg
    }

    fun setmImg(mImg: String?) {
        this.mImg = mImg
    }

    fun getmStatus(): String? {
        return mStatus
    }

    fun setmStatus(mStatus: String?) {
        this.mStatus = mStatus
    }

    fun getmTitle(): String? {
        return mTitle
    }

    fun setmTitle(mTitle: String?) {
        this.mTitle = mTitle
    }

    fun getmAttributes(): String? {
        return mAttributes
    }

    fun setmAttributes(mAttributes: String?) {
        this.mAttributes = mAttributes
    }
}