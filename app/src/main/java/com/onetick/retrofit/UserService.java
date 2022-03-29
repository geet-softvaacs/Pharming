package com.onetick.retrofit;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {
    @POST("papi/p_country_code.php")
    Call<JsonObject> getCountry();

    @POST("papi/register_user.php")
    Call<JsonObject> getCheckMobile(@Body @Nullable RequestBody var1);

    @POST("papi/p_home_data.php")
    Call<JsonObject> getHome(@Body @Nullable RequestBody requestBody);

    @POST("papi/p_cat_list.php")
    Call<JsonObject>getCatList(@Body @Nullable RequestBody requestBody);

    @POST("lapi/lab_home.php")
    @Nullable
    Call<JsonObject> getLab(@Body @Nullable RequestBody var1);

    @POST("papi/p_brand_product_web.php")
    @Nullable
    Call<JsonObject> getBrandProduct(@Body @Nullable RequestBody var1);

    @POST("papi/p_cat_product.php")
    @Nullable
    Call<JsonObject> getCatProduct(@Body @Nullable RequestBody var1);

    @POST("papi/p_brand_list.php")
    @Nullable
    Call<JsonObject> getBrand(@Body @Nullable RequestBody var1);

    @POST("papi/p_rand_product.php")
    @Nullable
    Call<JsonObject> getRandomProduct(@Body @Nullable RequestBody var1);


    @POST("lapi/lab_cat_tests.php")
    @Nullable
    Call<JsonObject> getlabProduct(@Body @Nullable RequestBody var1);

    @POST("papi/p_product_search.php")
    @Nullable
    Call<JsonObject> getSearch(@Body @Nullable RequestBody var1);

    @POST("papi/p_mobile_login.php")
    Call<JsonObject> MobileLogin(@Body RequestBody requestBody);

    @POST("papi/p_order_cancel.php")
    Call<JsonObject> getOrderCancel(@Body RequestBody requestBody);

    @POST("papi/p_profile.php")
    Call<JsonObject> getUpdate(@Body RequestBody requestBody);

    @POST("papi/p_address_list.php")
    Call<JsonObject> getAddress(@Body RequestBody requestBody);

    @POST("papi/p_address_user.php")
    Call<JsonObject> setAddress(@Body RequestBody requestBody);

    @POST("papi/add_item_to_cart.php")
    Call<JsonObject> addtoCart(@Body RequestBody requestBody);

    @POST("papi/p_order_prescription.php")
    @Multipart
    @Nullable
    Call<JsonObject> uploadMultiFile(@Part("uid") @Nullable RequestBody var1, @Part("Full_Address") @Nullable RequestBody var2, @Part("pincode") @Nullable RequestBody var3, @Part("size") @Nullable RequestBody var4, @Part @Nullable List<MultipartBody.Part> var5);

    @POST("papi/p_prescription_history.php")
    @Nullable
    Call<JsonObject> getPredationOrder(@Body @Nullable RequestBody var1);


    @POST("papi/p_prescription_cancle.php")
    @Nullable
    Call<JsonObject> getPresOrderCancel(@Body @Nullable RequestBody var1);

    @POST("papi/p_prescription_order_product_list.php")
    @Nullable
    Call<JsonObject> getPrescriptionOrderHistry(@Body @Nullable RequestBody var1);

    @POST("papi/check_availability.php")
    @Nullable
    Call<JsonObject> checkAvailability(@Body @Nullable RequestBody var1);

    @POST("papi/cart_product.php")
    Call<JsonObject> viewCart(@Body RequestBody requestBody);

    @POST("papi/p_couponlist.php")
    @Nullable
    Call<JsonObject>getCouponList(@Body @Nullable RequestBody var1);

    @POST("papi/p_check_coupon.php")
    @Nullable
    Call<JsonObject> checkCoupon(@Body @Nullable RequestBody var1);

    @POST("papi/p_order_now.php")
    @Nullable
    Call<JsonObject>getOrderNow(@Body @Nullable RequestBody var1);


    @POST("lapi/order_tests.php")
    @Nullable
    Call<JsonObject>getLabORderNow(@Body @Nullable RequestBody body);

    @POST("papi/cart_single_Product_Qty.php")
    @Nullable
    Call<JsonObject>getSingleProductDetail(@Body @Nullable RequestBody requestBody);

    @POST("papi/p_order_history.php")
    @Nullable
    Call<JsonObject>getOrder(@Body @Nullable RequestBody requestBody);

    @POST("papi/p_order_product_list.php")
    Call<JsonObject>getOrderHistory(@Body RequestBody requestBody);


    @POST("papi/p_address_delete.php")
    Call<JsonObject>deleteAddress(@Body RequestBody requestBody);

    @POST("papi/p_notification_list.php")
    Call<JsonObject>getNote(@Body RequestBody requestBody);


    @POST("lapi/lab_test_availability_check.php")
    Call<JsonObject>getTestAvailable(@Body RequestBody requestBody);

    @POST("lapi/search_test.php")
    Call<JsonObject>getLabSearch(@Body RequestBody requestBody);


    @POST("lapi/lab_rand_product.php")
    Call<JsonObject>getLabTests(@Body RequestBody requestBody);
}
