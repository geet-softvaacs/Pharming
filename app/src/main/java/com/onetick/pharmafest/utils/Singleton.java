package com.onetick.pharmafest.utils;

import com.onetick.pharmafest.model.CartProduct;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton constant = new Singleton();
    public static Singleton getConstant() {
        return constant;
    }
    List<CartProduct> CartData = new ArrayList<>();
    List<CartProduct> LabCartData = new ArrayList<>();

    public List<CartProduct> getCartData() {
        return CartData;
    }

    public void setCartData(List<CartProduct> cartId) {
        CartData = cartId;
    }

    public List<CartProduct> getLabCartData() {
        return LabCartData;
    }

    public void setLabCartData(List<CartProduct> labCartData) {
        LabCartData = labCartData;
    }
}
