package com.onetick.utils;

import java.util.regex.Pattern;

public class Patterns {

    public static final Pattern PERSONNAME = Pattern.compile("^[\\p{L} .'-]+$");
    public static final Pattern PHONEUMBER = Pattern.compile("[6-9]{1}[0-9]{9}");
    public static final Pattern EMAIL_ADDRESS = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    public static final Pattern GST_NUMBER = Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$");
    public static  final Pattern PAN_NUMBER = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    public static final Pattern ACCOUNT_NUMBER = Pattern.compile("[0-9]{9,18}");
    public static  final Pattern IFSC_CODE = Pattern.compile("^[A-Za-z]{4}0[A-Z0-9a-z]{6}$");
    public static final Pattern ADHAR_NUMBER = Pattern.compile("[0-9]{12}");
}
