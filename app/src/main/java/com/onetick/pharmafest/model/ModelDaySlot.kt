package com.onetick.pharmafest.model

import com.google.gson.annotations.SerializedName

class ModelDaySlot(@SerializedName("day")
                   var day: String? = null,

                   var isClicked: Boolean = false)