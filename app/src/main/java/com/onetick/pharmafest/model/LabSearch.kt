package com.onetick.pharmafest.model

data class LabSearch(val ResponseCode: String = "",
                     val Result: String = "",
                     val ResponseMsg: String = "",
                     val ResultData: List<SearchResultItem>?
)