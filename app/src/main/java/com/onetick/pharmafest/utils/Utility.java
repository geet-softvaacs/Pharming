package com.onetick.pharmafest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.onetick.retrofit.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utility {
    public static String paymentID = "0";
    public static int paymentsucsses = 0;
    public static String tragectionID = "0";
    public static int isvarification = -1;

    public static GoogleApiClient googleApiClient;

    public static void enableLoc(Activity context) {

        if (googleApiClient == null) {
                    googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })

                    .addOnConnectionFailedListener(connectionResult -> Log.d("Location error", "Location error " + connectionResult.getErrorCode())).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {// Show the dialog by calling startResolutionForResult(),
                try {
                    status.startResolutionForResult(context, 210);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public static boolean internetChack(Context context) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }

    public static String getUrl(String url) {

        if (url.contains("http") || url.contains("https")) {
            return url;
        } else {
            return ApiClient.baseUrl + "/" + url;
        }
    }

    public static void toastShort(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static Date getDate(String originalDate, String sourceFormat) throws ParseException {
        SimpleDateFormat sourceSimpleDateFormat = new SimpleDateFormat(sourceFormat, Locale.getDefault());
        return sourceSimpleDateFormat.parse(originalDate);
    }

    public static String getCurrentDate(String pattern) {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat(pattern/*,Locale.UK*/);
        return sdf.format(date);
    }

    public static String getFormattedDate(Date date, String targetFormat) {
        SimpleDateFormat requiredSimpleDateFormat = new SimpleDateFormat(targetFormat, Locale.getDefault());
        return requiredSimpleDateFormat.format(date);
    }
}
