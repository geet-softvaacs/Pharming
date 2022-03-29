package com.onetick.pharmafest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LabCategoryProduct {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @SerializedName("category_img")
    @Expose
    var categoryImg: String? = null

    @SerializedName("testlist")
    @Expose
    var testlist: List<TestlistItem>? = null
}