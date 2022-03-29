package com.onetick.pharmafest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLogin {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fname")
    @Expose
    private Object fname;
    @SerializedName("lname")
    @Expose
    private Object lname;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("google_id")
    @Expose
    private Object googleId;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ccode")
    @Expose
    private String ccode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dstatus")
    @Expose
    private String dstatus;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("rdate")
    @Expose
    private String rdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getFname() {
        return fname;
    }

    public void setFname(Object fname) {
        this.fname = fname;
    }

    public Object getLname() {
        return lname;
    }

    public void setLname(Object lname) {
        this.lname = lname;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getGoogleId() {
        return googleId;
    }

    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }
}
