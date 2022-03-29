package com.onetick.pharmafest.model

data class SearchResultItem(val id: String = "",
                            val test_title: String = "",
                            val test_name: String = "",
                            val test_image:ArrayList<String>,
                            val price: String = "",
                            val discount: String = "",
                            val short_desc: String = "",
                            val preparation: String = "",
                            val report: String = "",
                            val test_cat_id: String = "")