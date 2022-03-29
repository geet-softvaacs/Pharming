package com.onetick.pharmafest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartModel {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("cartProduct")
    @Expose
    private List<CartProduct> cartProduct = null;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("card_item_count")
    @Expose
    private Integer cardItemCount;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public List<CartProduct> getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(List<CartProduct> cartProduct) {
        this.cartProduct = cartProduct;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    public Integer getCardItemCount() {
        return cardItemCount;
    }

    public void setCardItemCount(Integer cardItemCount) {
        this.cardItemCount = cardItemCount;
    }
}
