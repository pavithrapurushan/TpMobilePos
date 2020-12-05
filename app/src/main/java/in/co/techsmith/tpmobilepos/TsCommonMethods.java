package in.co.techsmith.tpmobilepos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;



//Modified by Pavithra on 14-09-2020

public class TsCommonMethods {

    Context CurrentContext;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;

    public TsCommonMethods(Context cntxt) {
        CurrentContext = cntxt;
    }

    public boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) CurrentContext.getSystemService(CurrentContext.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    public void allowPermissionsDynamically() {
        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) CurrentContext, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    //This method added by Pavithra on 23-07-2020
    public String GetDeviceUniqueId() {
        String DeviceUniqueId = "";
        try {

//            String DeviceUniqueId = "";
            String StoreIdentifier = "", WSUrl = "";

            TelephonyManager TelephoneManager1;
            TelephoneManager1 = (TelephonyManager) CurrentContext.getSystemService(CurrentContext.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) CurrentContext, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

        }

            DeviceUniqueId = TelephoneManager1.getDeviceId();  // this line makes app crashes installing first time reason at debug getDeviceId: Neither user 10185 nor current process has android.permission.READ_PHONE_STATE.

            if (DeviceUniqueId == null || DeviceUniqueId.length() == 0) {

                SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(CurrentContext);
                DeviceUniqueId = sharedPrefs1.getString("DeviceId", "");

                Log.d("MSAPP", "Taking From SharedPreferences");
            }

            if (DeviceUniqueId == null || DeviceUniqueId.length() == 0) {
                String strUUID = String.valueOf(UUID.randomUUID());
                strUUID = strUUID.replace("-", "");
                DeviceUniqueId = strUUID.substring(0, 15).toUpperCase();

                //Added by Pavithra on 23-07-2020

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CurrentContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("DeviceId", DeviceUniqueId);
                editor.commit();

            }

            Log.d("POSAPP", "DeviceUniqueId : " + DeviceUniqueId);

            return DeviceUniqueId;
        }catch (Exception ex){
            Log.d("TCM",""+ex);
            return DeviceUniqueId;
        }
    }
}











//Added by 1165 on 19-03-2020

//public class TsCommonMethods {
//
//    Context CurrentContext;
//    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
//
//    public TsCommonMethods(Context cntxt) {
//        CurrentContext = cntxt;
//    }
//
//    public boolean isNetworkConnected() {
//
//        ConnectivityManager cm = (ConnectivityManager) CurrentContext.getSystemService(CurrentContext.CONNECTIVITY_SERVICE);
//
//        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (wifiNetwork != null && wifiNetwork.isConnected()) {
//            return true;
//        }
//
//        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (mobileNetwork != null && mobileNetwork.isConnected()) {
//            return true;
//        }
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        if (activeNetwork != null && activeNetwork.isConnected()) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public void allowPermissionsDynamically(){
//        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) CurrentContext, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//
//        }
////        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////
////            ActivityCompat.requestPermissions((Activity) CurrentContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
////
////        }
//    }
//
//    //This method added by Pavithra on 23-07-2020
//    public String GetDeviceUniqueId() {
//
//        String DeviceUniqueId = "";
//        String StoreIdentifier = "", WSUrl = "";
//
//        TelephonyManager TelephoneManager1;
//        TelephoneManager1 = (TelephonyManager) CurrentContext.getSystemService(CurrentContext.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions((Activity)CurrentContext, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
////            return TODO;
//        }
//
////        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////
////            ActivityCompat.requestPermissions((Activity) CurrentContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
////
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
//////            return TODO;
////        }
////
////        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////
////            ActivityCompat.requestPermissions((Activity)CurrentContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
////
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
//////            return TODO;
////        }
//
//
//        DeviceUniqueId = TelephoneManager1.getDeviceId();  // this line makes app crashes installing first time reason at debug getDeviceId: Neither user 10185 nor current process has android.permission.READ_PHONE_STATE.
//
//        if(DeviceUniqueId == null || DeviceUniqueId.length() == 0 ) {
//
//            SharedPreferences sharedPrefs1 = PreferenceManager.getDefaultSharedPreferences(CurrentContext);
////            DeviceUniqueId = sharedPrefs1.getString("StoredDevId","");
//            DeviceUniqueId = sharedPrefs1.getString("DeviceId","");
////            StoreIdentifier = sharedPrefs1.getString("StoreIdf","");
////            WSUrl = sharedPrefs1.getString("WSUrl","");
//
//            Log.d("MSAPP", "Taking From SharedPreferences");
//            //Toast.makeText(CurrentContext,"Taking From SharedPreferences", Toast.LENGTH_SHORT).show();
//        }
//
//        if(DeviceUniqueId == null || DeviceUniqueId.length() == 0 ) {
//            String strUUID  = String.valueOf( UUID.randomUUID());
//            strUUID = strUUID.replace("-", "");
//            DeviceUniqueId = strUUID.substring(0,15).toUpperCase();
//
//            //Added by Pavithra on 23-07-2020
//
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CurrentContext);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("DeviceId",DeviceUniqueId);
//            editor.commit();
//
//        }
//
//        Log.d("POSAPP", "DeviceUniqueId : " + DeviceUniqueId);
//
//        return DeviceUniqueId;
//    }
//
//}

